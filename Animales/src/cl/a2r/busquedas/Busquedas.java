package cl.a2r.busquedas;

import java.util.ArrayList;
import java.util.List;

import cl.a2r.animales.Candidatos;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.GanadoBusqueda;
import cl.a2r.sip.model.MedicamentoControl;
import cl.a2r.sip.wsservice.WSGanadoCliente;
import cl.a2r.sip.wsservice.WSPredioLibreCliente;
import cl.ar2.sqlite.servicio.BusquedasServicio;
import cl.ar2.sqlite.servicio.PredioLibreServicio;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Busquedas extends Activity implements View.OnClickListener{

	private ImageButton goBack, confirmarAnimal;
	private TextView tvDiio, tvFaltantes, tvEncontrados, tvCandidato;
	private LinearLayout llEncontrados, llFaltantes;
	private ProgressBar loading;
	private Ganado gan;
	public static List<Ganado> faltantes;
	private boolean esCandidato;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_busquedas);
		
		cargarInterfaz();
		traeDatosWS();
	}
	
	private void cargarInterfaz(){
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		confirmarAnimal = (ImageButton)findViewById(R.id.confirmarAnimal);
		confirmarAnimal.setOnClickListener(this);
		tvDiio = (TextView)findViewById(R.id.tvDiio);
		tvDiio.setOnClickListener(this);
		loading = (ProgressBar)findViewById(R.id.loading);
		loading.setVisibility(View.INVISIBLE);
		tvFaltantes = (TextView)findViewById(R.id.tvFaltantes);
		tvEncontrados = (TextView)findViewById(R.id.tvEncontrados);
		llEncontrados = (LinearLayout)findViewById(R.id.llEncontrados);
		llEncontrados.setOnClickListener(this);
		llFaltantes = (LinearLayout)findViewById(R.id.llFaltantes);
		llFaltantes.setOnClickListener(this);
		tvCandidato = (TextView)findViewById(R.id.tvCandidato);
		
		esCandidato = false;
		gan = new Ganado();
	}

	private void traeDatosWS(){		
		new AsyncTask<Void, Void, Void>(){

			String title, msg;
			List<MedicamentoControl> medList;
			
			protected void onPreExecute(){
				loading.setVisibility(View.VISIBLE);
				title = "";
				msg = "";
			}
			
			protected Void doInBackground(Void... arg0) {
				try {
					//Actualiza los diios
					List<Ganado> list = WSPredioLibreCliente.traeAllDiio();
					PredioLibreServicio.deleteDiio();
					PredioLibreServicio.insertaDiio(list);
					
					faltantes = WSGanadoCliente.traeGanadoBusqueda();
				} catch (AppException e) {
					title = "Error";
					msg = e.getMessage();
				}
				return null;
			}
			
			protected void onPostExecute(Void result){
				loading.setVisibility(View.INVISIBLE);
				if (!title.equals("Error")){
					mostrarCandidatos();
				} else {
					ShowAlert.showAlert(title, msg + "\nReinicie la aplicación", Busquedas.this);
				}
			}
			
		}.execute();

	}
	
	public void onClick(View v) {
		int id = v.getId();
		Intent i;
		switch (id){
		case R.id.goBack:
			ShowAlert.askYesNo("Advertencia", "¿Seguro que desea salir de la aplicación?", this, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if (which == -2){
						finish();
					}
				}
			});
			break;
		case R.id.llFaltantes:
			i = new Intent(this, Candidatos.class);
			i.putExtra("stance", "busquedasFaltantes");
			startActivity(i);
			break;
		case R.id.llEncontrados:
			i = new Intent(this, Logs.class);
			startActivity(i);
			break;
		case R.id.confirmarAnimal:
			agregarAnimal();
			break;
		case R.id.tvDiio:
			startActivity(new Intent(this, Calculadora.class));
			break;
		}
	}
	
	private void agregarAnimal(){
		try {
			BusquedasServicio.insertaBusqueda(gan);
			Toast.makeText(this, "Animal Registrado", Toast.LENGTH_LONG).show();
			clearScreen();
			mostrarCandidatos();
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	private void mostrarCandidatos(){
		try {
			List<GanadoBusqueda> encontrados = BusquedasServicio.traeGanBusqueda();
			List<Ganado> toRemove = new ArrayList<Ganado>();
			for (Ganado g : faltantes){
				for (GanadoBusqueda gb : encontrados){
					if (g.getId().intValue() == gb.getGan().getId().intValue()){
						toRemove.add(g);
					}
				}
			}
			faltantes.removeAll(toRemove);
			tvFaltantes.setText(Integer.toString(faltantes.size()));
			tvEncontrados.setText(Integer.toString(encontrados.size()));
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	private void verSiEsCandidato(Ganado g){
		boolean esCandidato = false;
		for (Ganado gan : faltantes){
			if (gan.getId().intValue() == g.getId().intValue()){
				if (gan.getFlag().intValue() == 1 && gan.getVenta().intValue() == 1){
					tvCandidato.setText("Es Candidato!\nPomo Secado: Si\nDesecho: Si");
				} else if (gan.getFlag().intValue() == 1 && gan.getVenta().intValue() == 0){
					tvCandidato.setText("Es Candidato!\nPomo Secado: Si\nDesecho: No");
				} else if (gan.getFlag().intValue() == 0 && gan.getVenta().intValue() == 1){
					tvCandidato.setText("Es Candidato!\nPomo Secado: No\nDesecho: Si");
				} else if (gan.getFlag().intValue() == 0 && gan.getVenta().intValue() == 0){
					tvCandidato.setText("Es Candidato!\nPomo Secado: No\nDesecho: No");
				}
				this.gan = gan;
				esCandidato = true;
				this.esCandidato = true;
				break;
			}
		}
		if (!esCandidato){
			tvCandidato.setText("No es Candidato");
			this.esCandidato = false;
		}
	}
	
	private void updateStatus(){
		if (gan.getId() != null && gan.getDiio() != null){
			tvDiio.setText(Integer.toString(gan.getDiio()));
			tvDiio.setGravity(Gravity.CENTER_HORIZONTAL);
			if (esCandidato){
				confirmarAnimal.setEnabled(true);
			}
		} else {
			tvDiio.setGravity(Gravity.LEFT);
			tvDiio.setText("DIIO:");
			confirmarAnimal.setEnabled(false);
		}
	}
	
	private void clearScreen(){
		gan = new Ganado();
		tvCandidato.setText("");
		esCandidato = false;
		resetCalculadora();
		updateStatus();
	}
	
	private void resetCalculadora(){
		Calculadora.gan = null;
	}
	
	private void showDiio(Ganado gan){
		this.gan = gan;
		updateStatus();
		verSiEsCandidato(this.gan);
	}
	
	private void checkDiioStatus(Ganado gan){
		if (gan != null){
			try {
				boolean exists = BusquedasServicio.existsGanado(gan.getId());
				clearScreen();
				if (!exists){
					showDiio(gan);
				}
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this);
			}
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
		
		try{
			mostrarCandidatos();
		} catch (NullPointerException e){}
		
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
						ShowAlert.showAlert("Error", "DIIO no existe", Busquedas.this);
					}
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), Busquedas.this);
				}
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", Busquedas.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };
	
}
