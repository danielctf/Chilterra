package cl.a2r.animales;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cl.a2r.animales.R;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.object.BastonObject;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class Baston extends Activity implements View.OnClickListener, ListView.OnItemClickListener{

	private ListView lvConectados, lvVinculados, lvEncontrados;
	private Button btnBuscar;
	private ImageButton goBack;
	private boolean waitForDeviceToPair = false;
	
	private BluetoothAdapter mBluetoothAdapter;
	private Set<BluetoothDevice> pairedDevices;
	private List<BastonObject> listaVinculados, listaEncontrados;
	public static List<ConnectedThread> listaConectados = new ArrayList<ConnectedThread>();

	
	private static final int SUCCESS_CONNECT = 0;
	private static final int MESSAGE_READ = 1;
	private static final int CONNECTION_INTERRUPTED = 2;
	private static final int CONNECTION_FAILED = 3;
	private static final int RETRY_CONNECTION = 4;
	
    private Handler mHandler = new Handler(){
    	@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		switch(msg.what){
    		case SUCCESS_CONNECT:
    			BluetoothSocket mmSocket = (BluetoothSocket) ((List<Object>) msg.obj).get(0);
    			BluetoothDevice mmDevice = (BluetoothDevice) ((List<Object>) msg.obj).get(1);
    			lvVinculados.setEnabled(true);
    			//Libera el bloqueo
    			ConnectThread.blockRetries = false;
    	        ConnectedThread connectedThread = new ConnectedThread(mmSocket, mmDevice);
    	        listaConectados.add(connectedThread);
    	        connectedThread.start();
    	        updateDevices();
    			break;
    		case MESSAGE_READ:
    			String EID = (String) msg.obj;
    			System.out.println(EID);
    			break;
    		case CONNECTION_INTERRUPTED:
    			//Dialogos son asincronos, x lo q le pregunta al dialogo si quiere volver a conectar.
    			//La razon por la que pregunta y no simplemente intenta reconectar es debido a que
    			//el usuario puede apagar el baston manualmente y la app incorrectamente 
    			//intentaria buscar indefinidamente al baston siendo que no es la intencion del usuario.
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", Baston.this, (BluetoothDevice) msg.obj);
    			updateDevices();
    			break;
    		case RETRY_CONNECTION:
    			//Intenta reconectar indefinidamente si el usuario puso "SI" en el dialogo
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		case CONNECTION_FAILED:
    			//Primer intento de conectar al baston, si no conecta, simplemente no sigue intentando
    			lvVinculados.setEnabled(true);
    			ShowAlert.showAlert("Error", "No se pudo conectar con el bastón especificado", Baston.this);
    			break;
  
    		}
    	}
    };
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_baston);
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		ConnectThread.setHandler(mHandler);
		
		cargarInterfaz();
		enableBluetooth();
		cargarListeners();
		
	}
	
	private void cargarInterfaz(){
		btnBuscar = (Button)findViewById(R.id.btnBuscar);
		btnBuscar.setOnClickListener(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);
		
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		lvConectados = (ListView)findViewById(R.id.lvConectados);
		lvVinculados = (ListView)findViewById(R.id.lvVinculados);
		lvEncontrados = (ListView)findViewById(R.id.lvEncontrados);
		listaVinculados = new ArrayList<BastonObject>();
		listaEncontrados = new ArrayList<BastonObject>();
		
	}
	
	private void cargarListeners(){
		lvConectados.setOnItemClickListener(this);
		lvVinculados.setOnItemClickListener(this);
		lvEncontrados.setOnItemClickListener(this);
	}
	
	private void updateDevices(){
		//CONECTADOS
		for (int i = 0; i < listaConectados.size(); i++){
			if (!(listaConectados.get(i).isAlive())){
				listaConectados.remove(i);
			}
		}
		ArrayAdapter<ConnectedThread> mAdapter = new ArrayAdapter<ConnectedThread>(this, android.R.layout.simple_list_item_1, listaConectados);
		lvConectados.setAdapter(mAdapter);
		
		//VINCULADOS
		pairedDevices = mBluetoothAdapter.getBondedDevices();
		listaVinculados.clear();
		for (BluetoothDevice dev : pairedDevices) {
			BastonObject baston = new BastonObject();
			baston.setName(dev.getName());
			baston.setMacAdress(dev.getAddress());
			listaVinculados.add(baston);
		}
		ArrayAdapter<BastonObject> mAdapter2 = new ArrayAdapter<BastonObject>(this, android.R.layout.simple_list_item_1, listaVinculados);
		lvVinculados.setAdapter(mAdapter2);
		
		//ENCONTRADOS
		ArrayAdapter<BastonObject> mAdapter3 = new ArrayAdapter<BastonObject>(this, android.R.layout.simple_list_item_1, listaEncontrados);
		lvEncontrados.setAdapter(mAdapter3);
	}

	public void enableBluetooth(){
		
		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
			return;
		}
		
		if (!mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, 1);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == 1) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	        	updateDevices();
	        }
	    }
	}
	
	public void onClick(View v) {
		if (isOnline() == false){
			return;
		}
		int id = v.getId();
		switch (id){
		case R.id.btnBuscar:
			if (mBluetoothAdapter.isEnabled()){
				listaEncontrados.clear();
				updateDevices();
				mBluetoothAdapter.cancelDiscovery();
				mBluetoothAdapter.startDiscovery();
			}else{
				enableBluetooth();
			}
			break;
		case R.id.goBack:
			finish();
			break;
		}
	}
	
	final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            // Get the BluetoothDevice object from the Intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Add the name and address to an array adapter to show in a ListView
	            BastonObject baston = new BastonObject();
	            baston.setName(device.getName());
	            baston.setMacAdress(device.getAddress());
	            listaEncontrados.add(baston);
	            updateDevices();
	        }
	    }
	};
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (isOnline() == false){
			return;
		}
		int id = arg0.getId();
		BluetoothDevice device;
		switch (id){
		case R.id.lvConectados:
			//Desconectar el bastón actual
			if (((ConnectedThread) arg0.getItemAtPosition(arg2)).cancel()){
				updateDevices();
			}
			break;
		case R.id.lvVinculados:
			lvVinculados.setEnabled(false);
			String macAddress = ((BastonObject) arg0.getItemAtPosition(arg2)).getMacAdress();
			device = mBluetoothAdapter.getRemoteDevice(macAddress);
			//Antes de conectar, elimina todos los intentos reconexión automática...
			//Pasa a veces el baston no logra reconectar solo y hay q forzar la conexion manual.
			ConnectThread.blockRetries = true;
			ConnectThread connectThread = new ConnectThread(device, false);
			connectThread.start();
			break;
		case R.id.lvEncontrados:
			device = mBluetoothAdapter.getRemoteDevice(((BastonObject) arg0.getItemAtPosition(arg2)).getMacAdress());
			try {
				Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
				Method createBondMethod = class1.getMethod("createBond");
				Boolean returnValue = (Boolean) createBondMethod.invoke(device); 
				if (returnValue) {
					waitForDeviceToPair = true;
				}else{
					ShowAlert.showAlert("Pareado", "Dispositivo ya se encuentra pareado", this);
				}
			} catch (ClassNotFoundException e) {} 
			catch (NoSuchMethodException e) {} 
			catch (IllegalAccessException e) {} 
			catch (IllegalArgumentException e) {} 
			catch (InvocationTargetException e) {
			}
			break;
		}
	}
	
	public void onResume() {
	    super.onResume();
	    
	    if (waitForDeviceToPair){
		    try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		    waitForDeviceToPair = false;
		    updateDevices();
	    }
	}
	
	protected  void onStart(){
		super.onStart();
		updateDevices();
	}
	
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
	
	public void onBackPressed(){
		if (isOnline()){
			finish();
		}
	}
	
	private boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if ((netInfo != null && netInfo.isConnectedOrConnecting()) == false){
	    	ShowAlert.showAlert("Error", "No hay conexión a Internet", this);
	    }
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	}

}
