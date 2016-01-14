package cl.a2r.custom;

import java.util.List;

import cl.a2r.animales.Login;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.wsservice.WSGanadoCliente;
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
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Calculadora extends Activity implements View.OnClickListener {
	
	private ImageButton goBack, borrar;
	private TextView tvdiio;
	private Button uno, dos, tres, cuatro, cinco, seis, siete, ocho, nueve, cero, aceptar;
	private boolean diioInverso;
	private String diioActual, diioActualInverso;
	
	public static int ganadoId, diio, predio, tipoGanado;
	public static String activa ="", sexo = "";
	public static boolean isAreteo = false, isSalvataje = false;
	private String errMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_calculadora);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
		
		cargarInterfaz();
	}

	private void cargarInterfaz(){
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		borrar = (ImageButton)findViewById(R.id.borrar);
		borrar.setOnClickListener(this);
		uno = (Button)findViewById(R.id.uno);
		uno.setOnClickListener(this);
		dos = (Button)findViewById(R.id.dos);
		dos.setOnClickListener(this);
		tres = (Button)findViewById(R.id.tres);
		tres.setOnClickListener(this);
		cuatro = (Button)findViewById(R.id.cuatro);
		cuatro.setOnClickListener(this);
		cinco = (Button)findViewById(R.id.cinco);
		cinco.setOnClickListener(this);
		seis = (Button)findViewById(R.id.seis);
		seis.setOnClickListener(this);
		siete = (Button)findViewById(R.id.siete);
		siete.setOnClickListener(this);
		ocho = (Button)findViewById(R.id.ocho);
		ocho.setOnClickListener(this);
		nueve = (Button)findViewById(R.id.nueve);
		nueve.setOnClickListener(this);
		cero = (Button)findViewById(R.id.cero);
		cero.setOnClickListener(this);
		aceptar = (Button)findViewById(R.id.aceptar);
		aceptar.setOnClickListener(this);
		tvdiio = (TextView)findViewById(R.id.diio);
		
		diioInverso = false;
		
	}
	
	public void onClick(View v) {
		if (isOnline() == false){
			return;
		}
		int id = v.getId();
		switch (id){
		case R.id.uno:
			tvdiio.setText(tvdiio.getText() + "1");
			break;
		case R.id.dos:
			tvdiio.setText(tvdiio.getText() + "2");
			break;
		case R.id.tres:
			tvdiio.setText(tvdiio.getText() + "3");
			break;
		case R.id.cuatro:
			tvdiio.setText(tvdiio.getText() + "4");
			break;
		case R.id.cinco:
			tvdiio.setText(tvdiio.getText() + "5");
			break;
		case R.id.seis:
			tvdiio.setText(tvdiio.getText() + "6");
			break;
		case R.id.siete:
			tvdiio.setText(tvdiio.getText() + "7");
			break;
		case R.id.ocho:
			tvdiio.setText(tvdiio.getText() + "8");
			break;
		case R.id.nueve:
			tvdiio.setText(tvdiio.getText() + "9");
			break;
		case R.id.cero:
			tvdiio.setText(tvdiio.getText() + "0");
			break;
		case R.id.aceptar:
			if (diioInverso == false && tvdiio.length() >= 1){
				ShowAlert.showAlert("Diio", "Ingrese diio al revés.", this);
				diioInverso = true;
				diioActual = tvdiio.getText().toString();
				tvdiio.setText("");
			}else{
				if (diioInverso && tvdiio.length() >= 1){
					diioActualInverso = tvdiio.getText().toString();
					if (validarDiioInverso()){
						if (getGanadoWS(Integer.parseInt(diioActual))){
							finish();
						}else{
							ShowAlert.showAlert("Error", errMsg, this);
							diioInverso = false;
							tvdiio.setText("");
						}
					}else{
						ShowAlert.showAlert("Error", "El DIIO ingresado no coincide con el original\nIntente ingresar el diio al revés nuevamente", this);
					}
				}else{
					ShowAlert.showAlert("Error", "DIIO no válido.\nNo escribió ningún DIIO", this);
				}
			}
			break;
		case R.id.goBack:
			finish();
			break;
		case R.id.borrar:
			if (tvdiio.getText().length() > 0){
				tvdiio.setText(tvdiio.getText().toString().substring(0, tvdiio.getText().length() - 1));
			}
			break;
		
		}
	}
	
	@SuppressWarnings({ "unchecked", "static-access" })
	private boolean getGanadoWS(int diio){
		if (isSalvataje){
			this.diio = diio;
			return true;
		}
		
		List<Ganado> list = null;
		if (isAreteo == false){
			try {
				list = WSGanadoCliente.traeGanado(diio);
				if (list.size() == 0){
					errMsg = "DIIO no existe";
					return false;
				}
				for (Ganado g : list){
					ganadoId = g.getId();
					this.diio = g.getDiio();
					activa = g.getActiva();
					predio = g.getPredio();
					sexo = g.getSexo();
					tipoGanado = g.getTipoGanadoId();
				}
			} catch (AppException ex) {
				errMsg = ex.getMessage();
				return false;
			}
		} else if (isAreteo) {
			try {
				this.diio = WSGanadoCliente.traeDiio(diio);
			} catch (AppException e) {
				errMsg = e.getMessage();
				return false;
			}
		}
		return true;
	}
	
	private boolean validarDiioInverso(){
		if (diioActual.length() != diioActualInverso.length()){
			return false;
		}
		
		for (int i = 0; i < diioActual.length(); i++){
			if (diioActual.charAt(i) != diioActualInverso.charAt(diioActualInverso.length() - 1 - i)){
				return false;
			}
		}
		return true;
	}
	
	protected  void onStart(){
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
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", Calculadora.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };

}
