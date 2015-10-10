package com.example.btbaston;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
//import android.util.Log;
import com.example.btbaston.R;

public class MainActivity extends Activity {
	private Boolean KeepScreenOn = false;
	DecimalFormat df = new DecimalFormat();

	
	Messenger mService = null;
	boolean mIsBound;

	}
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = new Messenger(service);
			try {
				//Register client with service
				Message msg = Message.obtain(null, EIDService.MSG_REGISTER_CLIENT);
				msg.replyTo = mMessenger;
				mService.send(msg);

				//Request a status update.
				msg = Message.obtain(null, EIDService.MSG_UPDATE_STATUS, 0, 0);
				mService.send(msg);
				
				//Request full log from service.
				msg = Message.obtain(null, EIDService.MSG_UPDATE_LOG_FULL, 0, 0);
				mService.send(msg);
				
			} catch (RemoteException e) {
				// In this case the service has crashed before we could even do anything with it
			}
		}
		public void onServiceDisconnected(ComponentName className) {
			// This is called when the connection with the service has been unexpectedly disconnected - process crashed.
			mService = null;
		}
	};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTitle(R.string.app_name_long);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		
		restoreMe(savedInstanceState);
		CheckIfServiceIsRunning();
	}
	
	private String SetDefaultStatusText() {
		String t = "Contact: oogiem@desertweyr.com"; 
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			return "Version: " + packageInfo.versionName + "\n" + t;
		} catch (PackageManager.NameNotFoundException e) {
			return t;	
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("textxxx", textStat.getText().toString());
		outState.putString("textinfo1", textInfo1.getText().toString());
		outState.putString("textinfo2", textInfo2.getText().toString());
		outState.putString("connectbuttontext", btnService.getText().toString());
		outState.putString("textlog", textLog.getText().toString());
		outState.putString("textbytes", textBytes.getText().toString());
	}
	private void restoreMe(Bundle state) {
		if (state!=null) {
			textStat.setText(state.getString("textxxx"));
			textInfo1.setText(state.getString("textinfo1"));
			textInfo2.setText(state.getString("textinfo2"));
			btnService.setText(state.getString("connectbuttontext"));
			textLog.setText(state.getString("textlog"));
			textBytes.setText(state.getString("textbytes"));
			svLog.post(new Runnable() {
			    public void run() {
			    	svLog.fullScroll(ScrollView.FOCUS_DOWN);
			    }
			});
		}
	}

	private void CheckIfServiceIsRunning() {
		//If the service is running when the activity starts, we want to automatically bind to it.
		if (EIDService.isRunning()) {
			doBindService();
			mLogoImage.setVisibility(View.GONE);
		} else {
			btnService.setText("Connect");
			if (textLog.length() > 60) { //More text here than the default start-up amount
				mLogoImage.setVisibility(View.GONE);
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//startActivity(new Intent(this, EditPreferences.class));
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Settings").setIcon(R.drawable.settings).setAlphabeticShortcut('s');
		return true;
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		//startActivity(new Intent(this, EditPreferences.class));
		super.onCreateOptionsMenu(menu);
		
		menu.removeItem(1);

		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case 0: //Settings
			startActivity(new Intent(this, EditPreferences.class));
			return true;
//		case 9: //Record Note Here
//			AskUserAboutNote();
//			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
//	private void AskUserAboutNote() {
//		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
//		final EditText input = new EditText(this);
//		alert.setView(input);
//		alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//				Bundle b = new Bundle();
//				b.putString("note", input.getText().toString().trim());
//				if (mService != null) {
//					try {
//						Message msg = Message.obtain(null, EIDService.MSG_ADD_NOTE_TO_NMEA);
//						msg.replyTo = mMessenger;
//						msg.setData(b);
//						mService.send(msg);
//					} catch (RemoteException e) {}
//				}
//				Toast.makeText(getApplicationContext(), "Note Sent to Service", Toast.LENGTH_SHORT).show();
//			}
//		});
//		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//				dialog.cancel();
//			}
//		});
//		alert.show();
//	}
	
	private OnClickListener ListenerBtnService = new OnClickListener() {
		public void onClick(View v){
			mLogoImage.setVisibility(View.GONE);

			if(btnService.getText() == "Connect"){
				LogMessage("Starting Service");
				startService(new Intent(MainActivity.this, EIDService.class));
				doBindService();
//				if (KeepScreenOn) {
//					getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//				}
			} else {
				doUnbindService();
				stopService(new Intent(MainActivity.this, EIDService.class));
				LogMessage("Service Stopped");
//				if (KeepScreenOn) {
//					getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//				}
			}
		}
	};
	private OnClickListener ListenerToggleDisplayMsgType = new OnClickListener() {
		public void onClick(View v){
			if(btnService.getText() != "Connect"){
				if (mService != null) {
					try {
						//Request change of display message type
						Message msg = Message.obtain(null, EIDService.MSG_TOGGLE_LOG_TYPE, 0, 0);
						msg.replyTo = mMessenger;
						mService.send(msg);
					} catch (RemoteException e) {}
				}
			}
		}
	};

	private void LogMessage(String m) {
		//Check if log is too long, shorten if necessary.
		if (textLog.getText().toString().length() > 4000) {
			String templog = textLog.getText().toString();
			int tempi = templog.length();
			tempi = templog.indexOf("\n", tempi-1000);
			textLog.setText(templog.substring(tempi+1));
		}
		
		textLog.append("\n" + m);
		svLog.post(new Runnable() { 
		    public void run() { 
		    	svLog.fullScroll(ScrollView.FOCUS_DOWN); 
		    } 
		}); 
	}

	void doBindService() {
		// Establish a connection with the service.  We use an explicit
		// class name because there is no reason to be able to let other
		// applications replace our component.
		bindService(new Intent(this, EIDService.class), mConnection, Context.BIND_AUTO_CREATE);
		textStat.setText("Connecting...");
		btnService.setText("Disconnect");
		mIsBound = true;
		if (mService != null) {
			try {
				//Request status update
				Message msg = Message.obtain(null, EIDService.MSG_UPDATE_STATUS, 0, 0);
				msg.replyTo = mMessenger;
				mService.send(msg);

				//Request full log from service.
				msg = Message.obtain(null, EIDService.MSG_UPDATE_LOG_FULL, 0, 0);
				mService.send(msg);
			} catch (RemoteException e) {}
		}
	}
	void doUnbindService() {
		if (mIsBound) {
			// If we have received the service, and hence registered with it, then now is the time to unregister.
			if (mService != null) {
				try {
					Message msg = Message.obtain(null, EIDService.MSG_UNREGISTER_CLIENT);
					msg.replyTo = mMessenger;
					mService.send(msg);
				} catch (RemoteException e) {
					// There is nothing special we need to do if the service has crashed.
				}
			}
			// Detach our existing connection.
			unbindService(mConnection);
			mIsBound = false;
		}
		textStat.setText("Disconnected");
		textInfo1.setText("");
		textInfo2.setText("");
		ProgressBar.setVisibility(View.INVISIBLE);
		textBytes.setText("");
		btnService.setText("Connect");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (KeepScreenOn) {
			getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		
		try {
			doUnbindService();
		} catch (Throwable t) {
			//Log.e("MainActivity", "Failed to unbind from the service", t);
		}
	}
}