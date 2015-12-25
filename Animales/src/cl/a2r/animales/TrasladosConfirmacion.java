package cl.a2r.animales;

import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.login.R;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.TipoGanado;
import cl.a2r.sip.model.Traslado;
import cl.a2r.sip.wsservice.WSAreteosCliente;
import cl.a2r.sip.wsservice.WSTrasladosCliente;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TrasladosConfirmacion extends Activity implements View.OnClickListener{
	
	private Spinner spinnerOrigen, spinnerDestino, spinnerTransportista, spinnerChofer, spinnerCamion, spinnerAcoplado;
	private static TextView tvVacas, tvVaquillas, tvTerneras, tvToros, tvToretes, tvTerneros, tvAnimales, tvBueyes;
	private static TextView despliegaGD, tvApp, deshacer;
	private static int contVacas, contVaquillas, contTerneras, contToros, contToretes, contTerneros, contBueyes;
	private static ImageButton goBack, undo, guias, confirmarMovimiento;
	private LinearLayout layoutAnimales;
	private static List<TipoGanado> listaTipoGanado;
	private static boolean finished = false;
	
	public static Traslado salida = new Traslado();
	public static Traslado confirmacion = new Traslado();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_traslados_confirmacion);
		
		cargarInterfaz();
		getTipoGanadoWS();
	}
	
	private void cargarInterfaz(){
		tvVacas = (TextView)findViewById(R.id.tvVacas);
		tvVaquillas = (TextView)findViewById(R.id.tvVaquillas);
		tvTerneras = (TextView)findViewById(R.id.tvTerneras);
		tvToros = (TextView)findViewById(R.id.tvToros);
		tvToretes = (TextView)findViewById(R.id.tvToretes);
		tvTerneros = (TextView)findViewById(R.id.tvTerneros);
		tvAnimales = (TextView)findViewById(R.id.tvAnimales);
		tvBueyes = (TextView)findViewById(R.id.tvBueyes);
		tvVacas.setOnClickListener(this);
		tvVaquillas.setOnClickListener(this);
		tvTerneras.setOnClickListener(this);
		tvToros.setOnClickListener(this);
		tvToretes.setOnClickListener(this);
		tvTerneros.setOnClickListener(this);
		tvAnimales.setOnClickListener(this);
		layoutAnimales = (LinearLayout)findViewById(R.id.layoutAnimales);
		layoutAnimales.setOnClickListener(this);
		
		spinnerChofer = (Spinner)findViewById(R.id.spinnerChofer);
		spinnerCamion = (Spinner)findViewById(R.id.spinnerCamion);
		spinnerAcoplado = (Spinner)findViewById(R.id.spinnerAcoplado);
		spinnerTransportista = (Spinner)findViewById(R.id.spinnerTransportista);
		spinnerOrigen = (Spinner)findViewById(R.id.spinnerOrigen);
		spinnerDestino = (Spinner)findViewById(R.id.spinnerDestino);
		spinnerChofer.setEnabled(false);
		spinnerCamion.setEnabled(false);
		spinnerAcoplado.setEnabled(false);
		spinnerTransportista.setEnabled(false);
		spinnerOrigen.setEnabled(false);
		spinnerDestino.setEnabled(false);
		
		despliegaGD = (TextView)findViewById(R.id.despliegaGD);
		guias = (ImageButton)findViewById(R.id.guias);
		guias.setOnClickListener(this);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		undo = (ImageButton)findViewById(R.id.undo);
		undo.setOnClickListener(this);
		tvApp = (TextView)findViewById(R.id.app);
		deshacer = (TextView)findViewById(R.id.deshacer);
		deshacer.setOnClickListener(this);
		confirmarMovimiento = (ImageButton)findViewById(R.id.confirmarMovimiento);
		confirmarMovimiento.setOnClickListener(this);
		
		resetContadores();
	}
	
	@SuppressWarnings("unchecked")
	private void getTipoGanadoWS(){
		try {
			listaTipoGanado = WSAreteosCliente.traeTipoGanado();
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	public static void getTraslado(Integer nro_documento){
		try {
			salida = WSTrasladosCliente.traeMovimiento(nro_documento);
			
			confirmacion.setG_movimiento_id(salida.getG_movimiento_id());
			confirmacion.setM_movement_id(salida.getM_movement_id());
			confirmacion.setNro_documento(salida.getNro_documento());
			
			confirmacion.setFundoDestinoId(salida.getFundoDestinoId());
			
			for (Ganado g : salida.getGanado()){
				TrasladosConfirmacionDiios.faltantes.getGanado().add(g);
			}
			updateStatus();
		} catch (AppException e) {
			e.printStackTrace();
		}
	}
	
	private static void updateStatus(){
		//Actualizar GD
		int size = salida.getGanado().size();
		if (size > 0){
			despliegaGD.setText("GD: " + Integer.toString(salida.getNro_documento()));
		}
		
		//Actualizar numeros (vacas, toros, vaq, toretes, etc)
		resetContadores();
		for (int i = 0; i < size; i++){
			for (int j = 0; j < listaTipoGanado.size(); j++){
				if (salida.getGanado().get(i).getTipoGanadoId().intValue() == listaTipoGanado.get(j).getId().intValue()){
					String nombre = listaTipoGanado.get(j).getNombre();
					switch (nombre){
					case "Vaca":
						contVacas++;
						break;
					case "Vaquilla":
						contVaquillas++;
						break;
					case "Ternera":
						contTerneras++;
						break;
					case "Toro":
						contToros++;
						break;
					case "Torete":
						contToretes++;
						break;
					case "Ternero":
						contTerneros++;
						break;
					case "Buey":
						contBueyes++;
						break;
					}
				}
			}
		}
		
		tvVacas.setText("V: " + Integer.toString(contVacas));
		tvVaquillas.setText("Vq: " + Integer.toString(contVaquillas));
		tvTerneras.setText("Ta: " + Integer.toString(contTerneras));
		tvToros.setText("T: " + Integer.toString(contToros));
		tvToretes.setText("Te: " + Integer.toString(contToretes));
		tvTerneros.setText("To: " + Integer.toString(contTerneros));
		tvBueyes.setText("B: " + Integer.toString(contBueyes));
		
		if (size > 0){
			goBack.setVisibility(View.INVISIBLE);
			tvApp.setVisibility(View.INVISIBLE);
			undo.setVisibility(View.VISIBLE);
			deshacer.setVisibility(View.VISIBLE);
			guias.setVisibility(View.INVISIBLE);
		} else {
			goBack.setVisibility(View.VISIBLE);
			tvApp.setVisibility(View.VISIBLE);
			undo.setVisibility(View.INVISIBLE);
			deshacer.setVisibility(View.INVISIBLE);
			guias.setVisibility(View.VISIBLE);
		}
		
		int sizeConf = confirmacion.getGanado().size();
		if (sizeConf > 0 && finished == false){
			confirmarMovimiento.setVisibility(View.VISIBLE);
		} else {
			confirmarMovimiento.setVisibility(View.INVISIBLE);
		}

	}
	
	private static void resetContadores(){
		contVacas = 0;
		contVaquillas = 0;
		contTerneras = 0;
		contToros = 0;
		contToretes = 0;
		contTerneros = 0;
		contBueyes = 0;
	}
	
	private void clearScreen(){
		salida = new Traslado();
		confirmacion = new Traslado();
		TrasladosConfirmacionDiios.reubicacion = new Traslado();
		TrasladosConfirmacionDiios.faltantes = new Traslado();
		despliegaGD.setText("GD:");
		finished = false;
		updateStatus();
	}
	
	public void onClick(View v) {
		if (isOnline() == false){
			return;
		}
		int id = v.getId();
		Intent i;
		switch (id){
			case R.id.goBack:
				finish();
				break;
			case R.id.tvAnimales:
			case R.id.tvVacas:
			case R.id.tvVaquillas:
			case R.id.tvTerneras:
			case R.id.tvToros:
			case R.id.tvToretes:
			case R.id.tvTerneros:
			case R.id.layoutAnimales:
				if (confirmacion.getG_movimiento_id() != null && finished == false){
					i = new Intent(this, TrasladosConfirmacionDiios.class);
					startActivity(i);
				}
				break;
			case R.id.deshacer:
			case R.id.undo:
				clearScreen();
				break;
			case R.id.layoutCalculadora:
			case R.id.despliegaGD:
				break;
			case R.id.guias:
				i = new Intent(this, Candidatos.class);
				i.putExtra("stance","trasladosGuiasDespacho");
				startActivity(i);
				break;
			case R.id.confirmarMovimiento:
				confirmacion();
				break;
		}
		
	}
	
    Handler hand = new Handler(); 
    Runnable run = new Runnable() { 
        public void run() {
    		try {
    			confirmacion.setUsuarioId(Login.user);
    			WSTrasladosCliente.insertaMovtoConfirm(confirmacion);
    			verReubicacion();
    			Toast.makeText(TrasladosConfirmacion.this, "Traslado confirmado", Toast.LENGTH_LONG).show();
    			deshacer.setText("Limpiar");
    			finished = true;
    		} catch (AppException e) {
    			ShowAlert.showAlert("Error", e.getMessage(), TrasladosConfirmacion.this);
    			confirmarMovimiento.setVisibility(View.VISIBLE);
    		}
        }
    }; 
	
	private void confirmacion(){
		confirmarMovimiento.setVisibility(View.INVISIBLE);
		hand.postDelayed(run, 100);
	}
	
	private void verReubicacion(){
		if (TrasladosConfirmacionDiios.reubicacion.getGanado().size() > 0){
			TrasladosConfirmacionDiios.reubicacion.setUsuarioId(Login.user);
			//TrasladosConfirmacionDiios.reubicacion.setNro_documento(confirmacion.getNro_documento());
			TrasladosConfirmacionDiios.reubicacion.setG_movimiento_id(confirmacion.getG_movimiento_id());
			TrasladosConfirmacionDiios.reubicacion.setFundoDestinoId(confirmacion.getFundoDestinoId());
			try {
				WSTrasladosCliente.insertaMovtoReubicacion(TrasladosConfirmacionDiios.reubicacion);
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this);
			}
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
		
		updateStatus();
		
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
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", TrasladosConfirmacion.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };

}
