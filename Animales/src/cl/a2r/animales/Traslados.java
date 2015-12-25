package cl.a2r.animales;

import java.util.List;

import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.login.R;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;


public class Traslados extends Activity implements View.OnClickListener{

	private Button btnSalida, btnRecepcion;
	private ImageButton goBack;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_traslados);
		
		cargarInterfaz();
	}
	
	private void cargarInterfaz(){
		btnSalida = (Button)findViewById(R.id.btnSalida);
		btnSalida.setOnClickListener(this);
		btnRecepcion = (Button)findViewById(R.id.btnRecepcion);
		btnRecepcion.setOnClickListener(this);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (isOnline() == false){
			return;
		}
		int id = v.getId();
		Intent i;
		switch (id){
		case R.id.btnSalida:
			i = new Intent(this, TrasladosSalida.class);
			startActivity(i);
			break;
		case R.id.btnRecepcion:
			i = new Intent(this, TrasladosConfirmacion.class);
			startActivity(i);
			break;
		case R.id.goBack:
			finish();
			break;
		}
	}
	
	protected void onStart(){
		super.onStart();
		
		if (Login.user == 0){
			finish();
		}
		
		ConnectThread.setHandler(mHandler);
		
		if (isOnline() == false){
			return;
		}
		
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
	
	//---------------------------------------------------------------------------
	//------------------------DATOS ENVIADOS DESDE BASTÓN------------------------
	//---------------------------------------------------------------------------
	
    private Handler mHandler = new Handler(){
    	@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		switch(msg.what){
    		case ConnectThread.SUCCESS_CONNECT:
    			BluetoothSocket mmSocket = (BluetoothSocket) ((List<Object>) msg.obj).get(0);
    			BluetoothDevice mmDevice = (BluetoothDevice) ((List<Object>) msg.obj).get(1);
    	        ConnectedThread connectedThread = new ConnectedThread(mmSocket, mmDevice);
    	        connectedThread.start();
    			break;
    		case ConnectedThread.MESSAGE_READ:
    			String EID = (String) msg.obj;
    			System.out.println(EID);
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", Traslados.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };
	
}
