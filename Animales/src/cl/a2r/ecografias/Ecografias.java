package cl.a2r.ecografias;

import java.util.List;

import cl.a2r.animales.R;
import cl.a2r.animales.R.id;
import cl.a2r.animales.R.layout;
import cl.a2r.common.AppException;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.object.LecturaTBObject;
import cl.a2r.sip.model.Ganado;
import cl.ar2.sqlite.servicio.PredioLibreServicio;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Ecografias extends Activity implements View.OnClickListener{

	private ImageButton goBack, logs, confirmarAnimal;
	private TextView tvUltAct, tvLogs, tvDiio, tvProblema;
	private Spinner spEcografista, spEstado, spProblema, spNota;
	private RadioGroup rg;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_ecografias);
		
		cargarInterfaz();
		cargarListeners();
	}
	
	private void cargarInterfaz(){
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		logs = (ImageButton)findViewById(R.id.logs);
		logs.setOnClickListener(this);
		confirmarAnimal = (ImageButton)findViewById(R.id.confirmarAnimal);
		confirmarAnimal.setOnClickListener(this);
		tvDiio = (TextView)findViewById(R.id.tvDiio);
		tvDiio.setOnClickListener(this);
		tvProblema = (TextView)findViewById(R.id.tvProblema);
		tvUltAct = (TextView)findViewById(R.id.tvUltAct);
		tvLogs = (TextView)findViewById(R.id.tvLogs);
		spEcografista = (Spinner)findViewById(R.id.spEcografista);
		spEstado = (Spinner)findViewById(R.id.spEstado);
		spProblema = (Spinner)findViewById(R.id.spProblema);
		spNota = (Spinner)findViewById(R.id.spNota);
		rg = (RadioGroup)findViewById(R.id.radioGroup1);
		
	}
	
	private void cargarListeners(){
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener(){
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	            RadioButton rb = (RadioButton)findViewById(checkedId);
	            
	        }
	    });
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id){
		case R.id.goBack:
			finish();
			break;
			
		}
	}
	
	private void updateStatus(){
		
	}
	
	private void resetCalculadora(){
		Calculadora.gan = null;
	}
	
	private void checkDiioStatus(Ganado gan){
		if (gan != null){

			tvDiio.setText(Integer.toString(0));
			tvDiio.setGravity(Gravity.CENTER_HORIZONTAL);
		}
		updateStatus();
	}
	
	public void onDestroy(){
		super.onStart();

		Calculadora.isPredioLibre = false;
		resetCalculadora();
	}
	
	public void onStart(){
		super.onStart();

		Calculadora.isPredioLibre = true;
		ConnectThread.setHandler(mHandler);
		
		checkDiioStatus(Calculadora.gan);
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
    			EID = EID.trim();
    			long temp = Long.parseLong(EID);
    			EID = Long.toString(temp);
    			try {
					Ganado g = PredioLibreServicio.traeEID(EID);
					if (g != null){
						checkDiioStatus(g);
					} else {
						ShowAlert.showAlert("Error", "DIIO no existe", Ecografias.this);
					}
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), Ecografias.this);
				}
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", Ecografias.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };

}
