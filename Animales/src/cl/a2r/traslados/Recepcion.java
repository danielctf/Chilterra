package cl.a2r.traslados;

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
import cl.a2r.sip.model.Instancia;
import cl.a2r.sip.wsservice.WSTrasladosCliente;
import cl.ar2.sqlite.servicio.PredioLibreServicio;
import cl.ar2.sqlite.servicio.TrasladosServicio;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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

public class Recepcion extends Activity implements View.OnClickListener{
	
	private ImageButton confirmarAnimal, goBack;
	private TextView tvDiio, tvFaltantes, tvEncontrados, tvInfo;
	private LinearLayout llEncontrados, llFaltantes;
	private ProgressBar loading;
	private Ganado gan;
	private Integer superInstanciaId;
	private List<Ganado> ganList;
	private Instancia superInstancia;
	public static List<Ganado> faltantes;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_traslado_recepcion);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			superInstanciaId = extras.getInt("superInstanciaId");
		}
		
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
		tvFaltantes = (TextView)findViewById(R.id.tvFaltantes);
		tvEncontrados = (TextView)findViewById(R.id.tvEncontrados);
		llEncontrados = (LinearLayout)findViewById(R.id.llEncontrados);
		llEncontrados.setOnClickListener(this);
		llFaltantes = (LinearLayout)findViewById(R.id.llFaltantes);
		llFaltantes.setOnClickListener(this);
		loading = (ProgressBar)findViewById(R.id.loading);
		loading.setVisibility(View.INVISIBLE);
		tvInfo = (TextView)findViewById(R.id.tvInfo);
		
		superInstancia = new Instancia();
		superInstancia.setId(superInstanciaId);
		Instancia instancia = new Instancia();
		superInstancia.setInstancia(instancia);
		
		gan = new Ganado();
	}
	
	private void traeDatosWS(){
		new AsyncTask<Void, Void, Void>(){
			String title, msg;
			
			protected void onPreExecute(){
				loading.setVisibility(View.VISIBLE);
				title = "";
				msg = "";
			}
			
			protected Void doInBackground(Void... arg0) {
				try {
					Thread.sleep(100);
					
					ganList = WSTrasladosCliente.traeTraslado(superInstanciaId);
					
				} catch (AppException e) {
					title = "Error";
					msg = e.getMessage();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			
			protected void onPostExecute(Void result){
				loading.setVisibility(View.INVISIBLE);
				if (!title.equals("Error")){
					mostrarCandidatos();
				} else {
					ShowAlert.showAlert(title, msg, Recepcion.this);
				}
			}
	
		}.execute();
	}
	
	public void onClick(View v) {
		int id = v.getId();
		Intent i;
		switch (id){
		case R.id.goBack:
			finish();
			break;
		case R.id.confirmarAnimal:
			agregarAnimal();
			break;
		case R.id.tvDiio:
			startActivity(new Intent(Recepcion.this, Calculadora.class));
			break;
		case R.id.llEncontrados:
			i = new Intent(Recepcion.this, LogsRecepcion.class);
			i.putExtra("mangadaActual", 1);
			startActivity(i);
			break;
		case R.id.llFaltantes:
			i = new Intent(Recepcion.this, Candidatos.class);
			i.putExtra("stance", "trasladosFaltantes");
			startActivity(i);
			break;
		}
	}
	
	private void mostrarCandidatos(){
		try {
			List<Ganado> list = TrasladosServicio.traeGanadoTraslado();
			
			//Encontrados
			tvEncontrados.setText(Integer.toString(list.size()));
			
			//Faltantes
			faltantes = new ArrayList<Ganado>();
			for (Ganado g : ganList){
				boolean exists = false;
				for (Ganado g2 : list){
					if (g.getId().intValue() == g2.getId().intValue()){
						exists = true;
						break;
					}
				}
				if (!exists){
					faltantes.add(g);
				}
			}
			tvFaltantes.setText(Integer.toString(faltantes.size()));
			
			actualizaInfoTraslado(faltantes);
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	private void actualizaInfoTraslado(List<Ganado> list){
		int buey = 0;
		int toro = 0;
		int ternera = 0;
		int torete = 0;
		int ternero = 0;
		int vaca = 0;
		int vaquilla = 0;
		tvInfo.setText("");
		for (Ganado g : list){
			switch (g.getTipoGanadoId().intValue()){
			case 1:
				buey++;
				break;
			case 3:
				toro++;
				break;
			case 4:
				ternera++;
				break;
			case 5:
				torete++;
				break;
			case 6:
				ternero++;
				break;
			case 7:
				vaca++;
				break;
			case 8:
				vaquilla++;
				break;
			}
		}
		if (buey > 0){
			tvInfo.setText(tvInfo.getText() + "Buey: " + Integer.toString(buey) + "\n");
		}
		if (toro > 0){
			tvInfo.setText(tvInfo.getText() + "Toro: " + Integer.toString(toro) + "\n");
		}
		if (ternera > 0){
			tvInfo.setText(tvInfo.getText() + "Ternera: " + Integer.toString(ternera) + "\n");
		}
		if (torete > 0){
			tvInfo.setText(tvInfo.getText() + "Torete: " + Integer.toString(torete) + "\n");
		}
		if (ternero > 0){
			tvInfo.setText(tvInfo.getText() + "Ternero: " + Integer.toString(ternero) + "\n");
		}
		if (vaca > 0){
			tvInfo.setText(tvInfo.getText() + "Vaca: " + Integer.toString(vaca) + "\n");
		}
		if (vaquilla > 0){
			tvInfo.setText(tvInfo.getText() + "Vaquilla: " + Integer.toString(vaquilla) + "\n");
		}
	}
	
	private void agregarAnimal(){
		List<Ganado> list = new ArrayList<Ganado>();
		list.add(gan);
		superInstancia.getInstancia().setGanList(list);
		
		try {
			TrasladosServicio.insertaTraslado(superInstancia);
			Toast.makeText(Recepcion.this, "Animal Registrado", Toast.LENGTH_LONG).show();
			clearScreen();
			mostrarCandidatos();
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), Recepcion.this);
		}
	}
	
	private void updateStatus(){
		if (gan.getId() != null && gan.getDiio() != null){
			tvDiio.setText(Integer.toString(gan.getDiio()));
			tvDiio.setGravity(Gravity.CENTER_HORIZONTAL);
			confirmarAnimal.setEnabled(true);
		} else {
			tvDiio.setGravity(Gravity.LEFT);
			tvDiio.setText("DIIO:");
			confirmarAnimal.setEnabled(false);
		}
	}
	
	private void showDiio(Ganado gan){
		boolean exists = false;
		for (Ganado g : ganList){
			if (g.getId().intValue() == gan.getId().intValue()){
				exists = true;
				break;
			}
		}
		if (!exists){
			ShowAlert.showAlert("Aviso", "Este Animal no corresponde al traslado", this);
		}
		
		this.gan = gan;
		updateStatus();
	}
	
	private void clearScreen(){
		gan = new Ganado();
		resetCalculadora();
		updateStatus();
	}
	
	private void resetCalculadora(){
		Calculadora.gan = null;
	}
	
	private void checkDiioStatus(Ganado gan){
		if (gan != null){
			try {
				boolean exists = TrasladosServicio.existsGanado(gan.getId());
				clearScreen();
				if (!exists){
					showDiio(gan);
				} else {
					ShowAlert.showAlert("Animal", "Animal ya existe", Recepcion.this);
				}
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), Recepcion.this);
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
						ShowAlert.showAlert("Error", "DIIO no existe", Recepcion.this);
					}
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), Recepcion.this);
				}
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", Recepcion.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };

}
