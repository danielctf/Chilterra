package com.example.bt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;
import com.weyr_associates.eidclient.R;

public class EIDService extends Service {
	
	TextView label;

	private BluetoothAdapter mBluetoothAdapter = null;
	private BTConnectThread mBTConnectThread;
    private BTConnectedThread mBTConnectedThread;
    private int mBTState;
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    private Boolean BTShouldAutoSwitch = false;
	private Boolean BTwasDisabled = false;

	private static boolean isRunning = false;
	private Timer timer = new Timer();
	private String logmsgs = "";
	private int DisplayMsgType = 0;
	ArrayList<Messenger> mClients = new ArrayList<Messenger>(); // Keeps track of all current registered clients.
	int mValue = 0; // Holds last value set by a client.
	public static final int MSG_THREAD_SUICIDE = 0;
	public static final int MSG_REGISTER_CLIENT = 1;
	static final int MSG_UNREGISTER_CLIENT = 2;
	public static final int MSG_UPDATE_STATUS = 3;
	static final int MSG_UPDATE_BYTESIN = 4;
//	static final int MSG_SHOW_PROGRESSBAR = 5;
	public static final int MSG_UPDATE_LOG_APPEND = 6;
	static final int MSG_UPDATE_LOG_FULL = 7;
	static final int MSG_TOGGLE_LOG_TYPE = 8;
//	static final int MSG_ADD_NOTE_TO_EID_Data = 9;
	static final int MSG_RELOAD_PREFERENCES = 10;
	static final int MSG_TIMER_TICK = 100;
	static final int MSG_BT_LOG_MESSAGE = 200;
	static final int MSG_BT_GOT_DATA = 201;
	static final int MSG_BT_FINISHED = 299;

	final Messenger mMessenger = new Messenger(new IncomingHandler()); //Target we publish for clients to send messages to IncomingHandler.

	private String MACAddress = "00:00:00:00:00:00";
	private Boolean UseHTCConnectionWorkaround = false;
	private byte[] BinaryDataToSave = new byte[4096];
	private String EIDDataToSave = "";
	private int BinaryDataToSaveIndex = 0;
	private String EID = "";
	private String LastEID = "zzz";
	private boolean completeline;
	private int TicksSinceLastStatusSent = 0;
	
	String newMACAddress;
	boolean readInitial = true;
	String tempDatoBaston;
	public static boolean canRead = true;

	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}
	
	public class IncomingHandler extends Handler { // Handler of incoming messages from clients.
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_REGISTER_CLIENT:
				mClients.add(msg.replyTo);
				break;
			case MSG_UNREGISTER_CLIENT:
				mClients.remove(msg.replyTo);
				break;
			case MSG_UPDATE_STATUS:
				sendStatusMessageToUI(); // Client requested a status update
				break;
			case MSG_RELOAD_PREFERENCES:
				LoadPreferences(true); // Client requested that the service reload the shared preferences
				break;
			case MSG_UPDATE_LOG_FULL:
				sendAllLogMessagesToUI(); // Client requested all of the log messages.
				if (!isRunning) {
					InformActivityOfThreadSuicide();
				}
				break;
			case MSG_TOGGLE_LOG_TYPE:
				if (DisplayMsgType == 0) {
					SetDisplayMsgType(1);
				} else {
					SetDisplayMsgType(0);
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}
	private void InformActivityOfThreadSuicide() {
		for (int i = mClients.size() - 1; i >= 0; i--) {
			try {
				mClients.get(i).send(Message.obtain(null, MSG_THREAD_SUICIDE, 0, 0));
				//Log.i("MyService", "Service informed Activity of Suicide. +");
			} catch (RemoteException e) {
				// The client is dead. Remove it from the list; we are going
				// through the list from back to front so this is safe to do
				// inside the loop.
				mClients.remove(i);
				//Log.i("MyService", "Service informed Activity of Suicide. -");
			}
		}
		//Log.i("MyService", "Service informed Activity of Suicide.");
	}
	
	private void LogMessage(String m) {
		// Check if log is too long, shorten if necessary.
		if (logmsgs.length() > 1000) {
			int tempi = logmsgs.length();
			tempi = logmsgs.indexOf("\n", tempi - 500);
			logmsgs = logmsgs.substring(tempi + 1);
		}

		// Append new message to the log.
		logmsgs += "\n" + TheDateTimeIs() + m;

		if (DisplayMsgType == 0) {
			// Build bundle
			Bundle b = new Bundle();
			b.putString("logappend", TheDateTimeIs() + m);
			for (int i = mClients.size() - 1; i >= 0; i--) {
				try {
					Message msg = Message.obtain(null, MSG_UPDATE_LOG_APPEND);
					msg.setData(b);
					mClients.get(i).send(msg);
				} catch (RemoteException e) {
					// The client is dead. Remove it from the list; we are going
					// through the list from back to front so this is safe to do
					// inside the loop.
					mClients.remove(i);
				}
			}
		}
	}

	private void sendAllLogMessagesToUI() {
		Bundle b = new Bundle();
			b.putString("logfull", logmsgs);
		
		for (int i = mClients.size() - 1; i >= 0; i--) {
			try {
				Message msg = Message.obtain(null, MSG_UPDATE_LOG_FULL);
				msg.setData(b);
				mClients.get(i).send(msg);
			} catch (RemoteException e) {
				// The client is dead. Remove it from the list; we are going
				// through the list from back to front so this is safe to do
				// inside the loop.
				mClients.remove(i);
			}
		}
	}
	private void SetDisplayMsgType(int MsgType) {
			MsgType = 0;

		if (DisplayMsgType != MsgType) { //Type changed. Need to re-send everything
			DisplayMsgType = MsgType;
			sendAllLogMessagesToUI();
		}
	}
	private void sendStatusMessageToUI() {
		// Build bundle
		String Stat;
			Stat = "Scanning";

		Bundle b = new Bundle();
		b.putString("stat", Stat);
		b.putString("info1", LastEID);
		b.putString("info2", TheTimeIs());
		
		for (int i = mClients.size() - 1; i >= 0; i--) {
			try {
				Message msg = Message.obtain(null, MSG_UPDATE_STATUS);
				msg.setData(b);
				mClients.get(i).send(msg);
			} catch (RemoteException e) {
				// The client is dead. Remove it from the list; we are going
				// through the list from back to front so this is safe to do
				// inside the loop.
				mClients.remove(i);
			}
		}
		TicksSinceLastStatusSent = 0; // Reset to zero
	}
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		//Log.i("MyService", "Service Started.");
		logmsgs = TheDateTimeIs() + "Service Started";

		isRunning = true;
		LoadPreferences(false);
		Notification lNotification = new Notification(R.drawable.icon,getText(R.string.service_started), System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
		lNotification.setLatestEventInfo(this, getText(R.string.service_label), getText(R.string.service_started), contentIntent);
		startForeground(5213583, lNotification);
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		timer.scheduleAtFixedRate(new TimerTask(){ public void run()
		{onTimerTick_TimerThread();}}, 0, 1000L);
	

	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Log.i("MyService", "Received start id " + startId + ": " + intent);
	    return START_STICKY; // run until explicitly stopped.
	}
	
	private void LoadPreferences(Boolean NotifyOfChanges) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		try {

			LogMessage(MainActivity.currentMAC);
			newMACAddress = MainActivity.currentMAC;
			
			BTShouldAutoSwitch = preferences.getBoolean("autoswitchbluetooth", false);
			UseHTCConnectionWorkaround = preferences.getBoolean("htcconnectworkaround", false);
	
			
			//newMACAddress = preferences.getString("bluetooth_mac", "00:00:00:00:00:00");
			if (newMACAddress != MACAddress) {
				if (!MACAddress.equals("00:00:00:00:00:00")) {
					LogMessage("BT: Target Device Changed. You will need to Disconnet/Reconnect.");
				}
				MACAddress = newMACAddress;
				BTstop();
			}
			
		} catch (NumberFormatException nfe) {}
		
		
	}
	public boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public static boolean isRunning() {
		return isRunning;
	}
	private String TheDateTimeIs() {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		int seconds = calendar.get(Calendar.SECOND);
		return Make2Digits(month + 1) + "/" +  Make2Digits(day) + "/" + year + ":" + Make2Digits(hours) + ":" + Make2Digits(minutes) + ":"
				+ Make2Digits(seconds) + " ";
	}
	private String TheTimeIs() {
		Calendar calendar = Calendar.getInstance();
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		int seconds = calendar.get(Calendar.SECOND);
		return Make2Digits(hours) + ":" + Make2Digits(minutes) + ":"
				+ Make2Digits(seconds) + " ";
	}
	private String Make2Digits(int i) {
		if (i < 10) {
			return "0" + i;
		} else {
			return Integer.toString(i);
		}
	}
	private void onTimerTick_TimerThread() {
		// This is running on a separate thread. Cannot do UI stuff from here.
		// Send a message to the handler to do that stuff on the main thread.
		handler.sendMessage(handler.obtainMessage(MSG_TIMER_TICK));
	}
	private void onTimerTick() { // Back on the main thread.
		TicksSinceLastStatusSent++;
		if (TicksSinceLastStatusSent > 0) {
			sendStatusMessageToUI();
		}

		if (getBTState() == STATE_NONE) { //We're not connected, try to start.
			if (mBluetoothAdapter == null) { //No adapter. Fail
				//Log.e("Bluetooth", "getDefaultAdapter returned null");
				Toast.makeText(this, "This device does not support Bluetooth.", Toast.LENGTH_SHORT).show();
				LogMessage("Bluetooth is NOT supported.");
				isRunning = false;
				InformActivityOfThreadSuicide();
				if (timer != null) {timer.cancel();}
				this.stopSelf();
			} else {
				if (!mBluetoothAdapter.isEnabled()) { //Bluetooth disabled
					if (BTShouldAutoSwitch) { //At least auto-switch is enabled
						if (!BTwasDisabled) { //We need to turn on Bluetooth
							//Log.i("Bluetooth", "Turning on");
							mBluetoothAdapter.enable();
							BTwasDisabled = true;
							LogMessage("Waiting for Bluetooth...");
						} else { //We need to wait until bluetooth comes online
							//Log.i("Bluetooth", "Waiting for for Bluetooth to turn on");
						}
					} else { //and auto-switch is disabled. Fail
						//Log.e("Bluetooth", "Bluetooth is Disabled and we can't autoswitch it on");
						Toast.makeText(this, "Bluetooth is Disabled", Toast.LENGTH_SHORT).show();
						LogMessage("Bluetooth is Disabled");
						isRunning = false;
						InformActivityOfThreadSuicide();
						if (timer != null) {timer.cancel();}
						this.stopSelf();
					}
				} else {
					BTstart();
				}
			}
		}
	}

	// Bluetooth Reader Stuff
	
	private synchronized void setBTState(int state) {
        //Log.i(BTAG, "setBTState() " + mBTState + " -> " + state);
        mBTState = state;
    }
	public synchronized int getBTState() {
	        return mBTState;
	    }
	
	public synchronized void BTstart() {
		SetDisplayMsgType(0);
		//Log.i(BTAG, "BTstart");
        // Cancel any thread attempting to make a connection
        if (mBTConnectThread != null) {mBTConnectThread.cancel(); mBTConnectThread = null;}
        // Cancel any thread currently running a connection
        if (mBTConnectedThread != null) {mBTConnectedThread.cancel(); mBTConnectedThread = null;}
        
        if (!BluetoothAdapter.checkBluetoothAddress(MACAddress)) {
        	LogMessage("Invalid Bluetooth MAC Address: \"" + MACAddress + "\"");
        	InformActivityOfThreadSuicide();
        } else if (MACAddress.equals("00:00:00:00:00:00")) {
        	LogMessage("Error: No Bluetooth device has been selected.");
        	isRunning = false;
			InformActivityOfThreadSuicide();
			if (timer != null) {timer.cancel();}
			this.stopSelf();
        } else {
        	setBTState(STATE_LISTEN);
            BluetoothDevice btdevice = mBluetoothAdapter.getRemoteDevice(MACAddress);
            BTconnect(btdevice);	
        }
    }
	public synchronized void BTconnect(BluetoothDevice device) {
        LogMessage("Device: " + device.getName());
		// Cancel any thread attempting to make a connection
        if (mBTState == STATE_CONNECTING) {
            if (mBTConnectThread != null) {mBTConnectThread.cancel(); mBTConnectThread = null;}
        }
        // Cancel any thread currently running a connection
        if (mBTConnectedThread != null) {mBTConnectedThread.cancel(); mBTConnectedThread = null;}

        // Start the thread to connect with the given device
        mBTConnectThread = new BTConnectThread(device, UseHTCConnectionWorkaround);
        mBTConnectThread.start();
        setBTState(STATE_CONNECTING);
    }
	private class BTConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        
        public BTConnectThread(BluetoothDevice device, boolean IsAnHTCDevice) {
        	mmDevice = device;
            BluetoothSocket tmp = null;
        
            // Get a BluetoothSocket for a connection with the given BluetoothDevice
            if (IsAnHTCDevice) {
				try {
					Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
					tmp = (BluetoothSocket) m.invoke(device, Integer.valueOf(1));
				} catch (Exception e) {
					//Log.e(BTAG, "Error at HTC/createRfcommSocket: " + e);
					e.printStackTrace();
					handler.sendMessage(handler.obtainMessage(MSG_BT_LOG_MESSAGE, "Exception creating socket: " + e));
				}
			} else {
				try {
				UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
				} catch (Exception e) {
					//Log.e(BTAG, "Error at createRfcommSocketToServiceRecord: " + e);
					e.printStackTrace();
					handler.sendMessage(handler.obtainMessage(MSG_BT_LOG_MESSAGE, "Exception creating socket: " + e));
				}
			}
            
            mmSocket = tmp;
        }
        public void run() {
            //Log.i(BTAG, "BEGIN BTConnectThread");
            handler.sendMessage(handler.obtainMessage(MSG_BT_LOG_MESSAGE, "Trying to Connect..."));
           
            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    //Log.e(BTAG, "unable to close() socket during connection failure", e2);
                }
                //Log.e(BTAG, "unable to connect() socket. Error: ", e);
                handler.sendMessage(handler.obtainMessage(MSG_BT_LOG_MESSAGE, "Failed to Connect: " + e));
                handler.sendMessage(handler.obtainMessage(MSG_BT_FINISHED));
                //insertar mensaje de error de conexion
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (EIDService.this) {
            	mBTConnectThread = null;
            }

            // Start the connected thread
            BTconnected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                //Log.e(BTAG, "close() of connect socket failed", e);
            }
        }
    }
	
	public synchronized void BTconnected(BluetoothSocket socket, BluetoothDevice device) {
        //Log.i(BTAG, "Connected");
        // Cancel the thread that completed the connection
        if (mBTConnectThread != null) {mBTConnectThread.cancel(); mBTConnectThread = null;}
        // Cancel any thread currently running a connection
        if (mBTConnectedThread != null) {mBTConnectedThread.cancel(); mBTConnectedThread = null;}

        // Start the thread to manage the connection and perform transmissions
        mBTConnectedThread = new BTConnectedThread(socket);
        mBTConnectedThread.start();

        setBTState(STATE_CONNECTED);
    }
	private class BTConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        
        public BTConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            
            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                //Log.e(BTAG, "temp sockets not created", e);
                handler.sendMessage(handler.obtainMessage(MSG_BT_LOG_MESSAGE, "Could not create Streams"));
                handler.sendMessage(handler.obtainMessage(MSG_BT_FINISHED));
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            //Log.i(BTAG, "BEGIN BTConnectedThread");
            handler.sendMessage(handler.obtainMessage(MSG_BT_LOG_MESSAGE, "Bluetooth Device Connected"));

            
            byte[] buffer = new byte[1024];
            int bytesread;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
	            	bytesread = mmInStream.read(buffer); //This is a blocking call
	                byte[] tempdata = new byte[bytesread];
	                System.arraycopy(buffer, 0, tempdata, 0, bytesread);
	                if (canRead){
		            	handler.sendMessage(handler.obtainMessage(MSG_BT_GOT_DATA, tempdata));
                	}
                    //mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    //LogMessage(buffer);
                } catch (IOException e) {
                    //Log.e(BTAG, "ConnectionLost. Error: " + e);
                    handler.sendMessage(handler.obtainMessage(MSG_BT_FINISHED));
                    break;
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                //Log.e(BTAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                //Log.e(BTAG, "close() of connected socket failed", e);
            }
        }
    }
	public synchronized void BTstop() {
        //Log.i(BTAG, "BTstop");
        if (mBTConnectThread != null) {mBTConnectThread.cancel(); mBTConnectThread = null;}
        if (mBTConnectedThread != null) {mBTConnectedThread.cancel(); mBTConnectedThread = null;}
        setBTState(STATE_NONE);
    }
	public void SendDataToBluetooth(byte[] buffer) { // You run this from the main thread.
		// Create temporary object
		BTConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mBTState != STATE_CONNECTED) return;
            r = mBTConnectedThread;
            //Log.d(BTAG, "Sent Data");
        }
        // Perform the write unsynchronized
        r.write(buffer);
	}
	private void ParseBTDataStream(byte[] buffer) {
//		LogMessage("bytes from bt:" + buffer.length + ", " + new String(buffer));
		ParseEIDStream(new String(buffer));
// Save all data to file
//			SaveRawDataToFile(buffer); // helpful for debugging 
	}
	
private void ParseEIDStream(String newdata) {
	
		EID += newdata;
		String[] lines = EID.split("\\r?\\n");
		if (lines.length > 0) {
			for (int i = 0; i < lines.length; i++) {
				completeline = false; // Reset this
//				 LogMessage("EID1" + " " + (lines.length - 1) + " " + i + " " + EID); //For testing only
				if (lines.length > 0) { // There is some data here
//					 LogMessage("ParseEID2"); //For testing only
					if (lines[i].lastIndexOf("\r") + 1 == lines[i].length()) { // Line ends with a \r 
						completeline = true;
						LastEID = EID;
						EID = ""; // Clear out
//						LogMessage("EID Cleared");
						if (lines[i].length() > 10) { // There are still at least 6 characters
//							LogMessage("Check CountryCode");
								if (lines[i].substring(0, 3).equals("840")) {
										LogMessage("USA");
								} else if (lines[i].substring(0, 3).equals("940")) {
										LogMessage("ShearWell");
								} else 
										LogMessage("Other");
								
							SaveEIDLineToFile(lines[i]);
						}
					}													
				}
			}

			if (!completeline) { // Last line wasn't complete, put last incomplete line back
				if (lines[lines.length - 1].length() < 1000) { // Only if less than 1000 characters long.
					EID = lines[lines.length - 1];
				}
			}
		}
	}
	
	private void SaveEIDLineToFile(String line) {
		EIDDataToSave += TheDateTimeIs() + line;
		if (EIDDataToSave.length() > 4096) { // There is at least 4KB to save.
			SaveEIDChunk();
		}
	
	}
	private void SaveEIDChunk() {
		if (EIDDataToSave.length() > 0) {
				try {
				String state = Environment.getExternalStorageState();
				if (Environment.MEDIA_MOUNTED.equals(state)) { // We can read and write the media
					File sdcard = Environment.getExternalStorageDirectory();
					File dir = new File(sdcard.getAbsolutePath() + "/EID-TAGS");
					dir.mkdirs();

					Calendar calendar = Calendar.getInstance();
					int year = calendar.get(Calendar.YEAR);
					int month = calendar.get(Calendar.MONTH) + 1;
					int day = calendar.get(Calendar.DAY_OF_MONTH);
					String filename = year + "-" + Make2Digits(month) + "-" + Make2Digits(day) + ".txt";

					File file = new File(dir, filename);
					FileWriter writer = new FileWriter(file, true);
					writer.append(EIDDataToSave);
					writer.flush();
					writer.close();
					EIDDataToSave = "";
				}
			} catch (Exception e) {
				//Log.d("SaveEIDChunk", e.getMessage());
			}
		}
	}
	
	private void SaveRawDataToFile(byte[] buffer) {
		int copystart = 0;
		int inlen = buffer.length;
		//Log.d("RAWData", "Received " + inlen + " bytes. BinaryDataToSaveIndex is currently " + BinaryDataToSaveIndex);
		while (true) {
			if (BinaryDataToSaveIndex + (inlen - copystart) < 4096) { //Easy, won't fill buffer
//				LogMessage("Appending " + (inlen - copystart) + " bytes. BinaryDataToSaveIndex=" + BinaryDataToSaveIndex);
				System.arraycopy(buffer, copystart, BinaryDataToSave, BinaryDataToSaveIndex, inlen - copystart);
				BinaryDataToSaveIndex += inlen - copystart;
				//Log.d("RAWData", "Append complete");
				break;

			} else { //Buffer will get full, need to write data to file
				int copylength = 4096 - BinaryDataToSaveIndex;
//				LogMessage("Writing out " + BinaryDataToSaveIndex + "+" + copylength + " bytes.");
				System.arraycopy(buffer, copystart, BinaryDataToSave, BinaryDataToSaveIndex, copylength);
				BinaryDataToSaveIndex += copylength;
				copystart += copylength;
				SaveRawDataChunk();
				//Log.d("RAWData", "Write complete");
			}
		}
	}
	private void SaveRawDataChunk() {
		if (BinaryDataToSaveIndex > 0) {
			try {
				String state = Environment.getExternalStorageState();
				if (Environment.MEDIA_MOUNTED.equals(state)) { // We can read and write the media
					File sdcard = Environment.getExternalStorageDirectory();
					File dir = new File(sdcard.getAbsolutePath() + "/EID-RAW/");
					dir.mkdirs();

					Calendar calendar = Calendar.getInstance();
					int year = calendar.get(Calendar.YEAR);
					int month = calendar.get(Calendar.MONTH) + 1;
					int day = calendar.get(Calendar.DAY_OF_MONTH);
					String filename = year + "-" + Make2Digits(month) + "-" + Make2Digits(day) + ".txt";
					File file = new File(dir, filename);

					FileOutputStream f = new FileOutputStream(file, true);
					f.write(BinaryDataToSave, 0, BinaryDataToSaveIndex);
					f.flush();
					f.close();

					//Log.d("SaveRawDataChunk", "Saved data to file: " + filename);
				}
			} catch (Exception e) {
				//Log.d("SaveRawDataChunk", e.getMessage());
			}
			BinaryDataToSaveIndex = 0;
		}	
	}
	
	
	
	public Handler handler = new Handler() { // Handler for data coming from bluetooth sockets
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_TIMER_TICK:
				onTimerTick();
				break;
			case MSG_BT_GOT_DATA:
				// Log.i("handleMessage", "MSG_BT_GOT_DATA");
				byte[] buffer2 = (byte[]) msg.obj;
				String temp = null;
				try {
					temp = new String(buffer2, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ParseBTDataStream(buffer2);
				break;
			case MSG_BT_LOG_MESSAGE:
				LogMessage((String) msg.obj);
				break;
			case MSG_BT_FINISHED:
				//Log.i("handleMessage", "MSG_BT_FINISHED");
				BTstop();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};
	
	@Override
	public void onDestroy() {
		super.onDestroy();

		SaveRawDataChunk(); //Write data to file
		SaveEIDChunk();
		
		// Kill threads
		if (timer != null) {timer.cancel();}

		BTstop();

		if (BTShouldAutoSwitch && BTwasDisabled) { // Turn BT off
			//Log.i(BTAG, "ON DESTROY: Turning BT back off");
			mBluetoothAdapter.disable();
			Toast.makeText(this, "Disabling Bluetooth...", Toast.LENGTH_SHORT).show();
		}

		stopForeground(true);
		//Log.i("MyService", "Service Stopped.");
		isRunning = false;
	}
}