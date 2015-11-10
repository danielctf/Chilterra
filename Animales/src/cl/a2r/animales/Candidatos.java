package cl.a2r.animales;

import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.login.R;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.wsservice.WSPartosCliente;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class Candidatos extends Activity implements View.OnClickListener{

	private ListView lvCandidatos;
	private TextView tvApp;
	private ImageButton goBack;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_candidatos);
		
		Bundle extras = getIntent().getExtras();
		String stance = null;
		if (extras != null) {
		    stance = extras.getString("stance");
		}
		
		cargarInterfaz();
		getCandidatosWS(stance);
	}
	
	private void cargarInterfaz(){
		lvCandidatos = (ListView)findViewById(R.id.lvCandidatos);
		tvApp = (TextView)findViewById(R.id.app);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
	}
	
	@SuppressWarnings("unchecked")
	private void getCandidatosWS(String stance){
		if (stance.equals("encontrados")){
			tvApp.setText("Candidatos Encontrados");
			try {
				List<Ganado> list = WSPartosCliente.traeCandidatosEncontrados(Aplicaciones.predioWS.getId());
				ArrayAdapter<Ganado> mAdapter = new ArrayAdapter<Ganado>(this, android.R.layout.simple_list_item_1, list);
				lvCandidatos.setAdapter(mAdapter);
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this);
			}
		} else if (stance.equals("faltantes")){
			tvApp.setText("Candidatos Faltantes");
			try {
				List<Ganado> list = WSPartosCliente.traeCandidatosFaltantes(Aplicaciones.predioWS.getId());
				ArrayAdapter<Ganado> mAdapter = new ArrayAdapter<Ganado>(this, android.R.layout.simple_list_item_1, list);
				lvCandidatos.setAdapter(mAdapter);
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this);
			}
			
		}
	}
	
	public void onClick(View arg0) {
		if (isOnline() == false){
			return;
		}
		int id = arg0.getId();
		switch (id){
		case R.id.goBack:
			finish();
			break;
		}
	}
	
	protected void onStart(){
		super.onStart();
		ConnectThread.setHandler(mHandler);
		
		if (isOnline() == false){
			return;
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
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", Candidatos.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };
	
}
