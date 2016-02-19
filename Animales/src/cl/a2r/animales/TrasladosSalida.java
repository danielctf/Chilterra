package cl.a2r.animales;

import java.util.ArrayList;
import java.util.List;

import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Camion;
import cl.a2r.sip.model.Chofer;
import cl.a2r.sip.model.DctoAdem;
import cl.a2r.sip.model.FMA;
import cl.a2r.sip.model.Persona;
import cl.a2r.sip.model.Predio;
import cl.a2r.sip.model.TipoGanado;
import cl.a2r.sip.model.Transportista;
import cl.a2r.sip.model.Traslado;
import cl.a2r.sip.wsservice.WSTrasladosCliente;
import cl.a2r.traslados.AsyncGetData;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TrasladosSalida extends Activity implements View.OnClickListener{

	private Spinner spinnerOrigen, spinnerDestino, spinnerTipoTransporte, spinnerTransportista, spinnerChofer, spinnerCamion, spinnerAcoplado;
	private TextView despliegaGD, tvApp, deshacer;
	private TextView tvVacas, tvVaquillas, tvTerneras, tvToros, tvToretes, tvTerneros, tvAnimales, tvBueyes;
	private TextView tvChofer, tvCamion, tvAcoplado, tvTransportista;
	private LinearLayout layoutAnimales;
	private ImageButton goBack, undo, confirmarMovimiento;
	private ProgressBar loading;
	public List<TipoGanado> listaTipoGanado;
	public List<Transportista> transportistas;
	public List<Persona> arrieros;
	private static int contVacas, contVaquillas, contTerneras, contToros, contToretes, contTerneros, contBueyes;
	private boolean hayTransportista;
	private static String strGD;
	private static boolean finished = false;
	
	public static Traslado trasladoSalida = new Traslado(); 
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_traslados_salida);
		
		listaTipoGanado = new ArrayList<TipoGanado>();
		transportistas = new ArrayList<Transportista>();
		arrieros = new ArrayList<Persona>();
		
		cargarInterfaz();
		cargarListeners();
		new AsyncGetData(this).execute();
		getFundoOrigen();
		getTipoTransporte();

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
		tvTransportista = (TextView)findViewById(R.id.tvTransportista);
		tvChofer = (TextView)findViewById(R.id.tvChofer);
		tvCamion = (TextView)findViewById(R.id.tvCamion);
		tvAcoplado = (TextView)findViewById(R.id.tvAcoplado);
		
		spinnerChofer = (Spinner)findViewById(R.id.spinnerChofer);
		spinnerCamion = (Spinner)findViewById(R.id.spinnerCamion);
		spinnerAcoplado = (Spinner)findViewById(R.id.spinnerAcoplado);
		spinnerTransportista = (Spinner)findViewById(R.id.spinnerTransportista);
		spinnerTipoTransporte = (Spinner)findViewById(R.id.spinnerTipoTransporte);
		spinnerOrigen = (Spinner)findViewById(R.id.spinnerOrigen);
		spinnerDestino = (Spinner)findViewById(R.id.spinnerDestino);
		despliegaGD = (TextView)findViewById(R.id.despliegaGD);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		undo = (ImageButton)findViewById(R.id.undo);
		undo.setOnClickListener(this);
		tvApp = (TextView)findViewById(R.id.app);
		deshacer = (TextView)findViewById(R.id.deshacer);
		deshacer.setOnClickListener(this);
		confirmarMovimiento = (ImageButton)findViewById(R.id.confirmarMovimiento);
		confirmarMovimiento.setOnClickListener(this);
		loading = (ProgressBar)findViewById(R.id.loading);
		
		resetContadores();

	}
	
	private void cargarListeners(){
		spinnerOrigen.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				trasladoSalida.setFundoOrigenId(((Predio) arg0.getItemAtPosition(arg2)).getId());
				updateStatus();
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		
		spinnerDestino.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				trasladoSalida.setFundoDestinoId(((Predio) arg0.getItemAtPosition(arg2)).getId());
				updateStatus();
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		
		spinnerTipoTransporte.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				switch (arg2){
				case 0:
					//Con Camión
					tvChofer.setVisibility(View.VISIBLE);
					spinnerChofer.setVisibility(View.VISIBLE);
					tvCamion.setVisibility(View.VISIBLE);
					spinnerCamion.setVisibility(View.VISIBLE);
					tvAcoplado.setVisibility(View.VISIBLE);
					spinnerAcoplado.setVisibility(View.VISIBLE);
					tvTransportista.setText("Transportista");
					
					ArrayAdapter<Transportista> mAdapter = new ArrayAdapter<Transportista>(TrasladosSalida.this, android.R.layout.simple_list_item_1, transportistas);
					spinnerTransportista.setAdapter(mAdapter);
					
					trasladoSalida.setArrieroId(null);
					hayTransportista = true;
					break;
				case 1:
					//Sin Camión
					tvChofer.setVisibility(View.INVISIBLE);
					spinnerChofer.setVisibility(View.INVISIBLE);
					tvCamion.setVisibility(View.INVISIBLE);
					spinnerCamion.setVisibility(View.INVISIBLE);
					tvAcoplado.setVisibility(View.INVISIBLE);
					spinnerAcoplado.setVisibility(View.INVISIBLE);
					tvTransportista.setText("Arriero");
					
					ArrayAdapter<Persona> mAdapter2 = new ArrayAdapter<Persona>(TrasladosSalida.this, android.R.layout.simple_list_item_1, arrieros);
					spinnerTransportista.setAdapter(mAdapter2);
					
					trasladoSalida.setTransportistaId(null);
					trasladoSalida.setChoferId(null);
					trasladoSalida.setCamionId(null);
					trasladoSalida.setAcopladoId(null);
					hayTransportista = false;
					break;
				}
				updateStatus();
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		
		spinnerTransportista.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (hayTransportista){
					Integer id = ((Transportista) arg0.getItemAtPosition(arg2)).getId();
					trasladoSalida.setTransportistaId(id);
					getInfoTransportista(id);
				} else {
					Integer id = ((Persona) arg0.getItemAtPosition(arg2)).getId();
					trasladoSalida.setArrieroId(id);
				}
				updateStatus();
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		
		spinnerChofer.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				trasladoSalida.setChoferId(((Chofer) arg0.getItemAtPosition(arg2)).getId());
				updateStatus();
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		
		spinnerCamion.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				trasladoSalida.setCamionId(((Camion) arg0.getItemAtPosition(arg2)).getId());
				updateStatus();
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		
		spinnerAcoplado.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				trasladoSalida.setAcopladoId(((Camion) arg0.getItemAtPosition(arg2)).getId());
				updateStatus();
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
	}

	private void getFundoOrigen(){
		List<Predio> list = new ArrayList<Predio>();
		list.add(Aplicaciones.predioWS);
		ArrayAdapter<Predio> mAdapter = new ArrayAdapter<Predio>(this, android.R.layout.simple_list_item_1, list);
		spinnerOrigen.setAdapter(mAdapter);
		spinnerOrigen.setEnabled(false);
	}
	
	private void getTipoTransporte(){
		String[] tipoTrans = {"Con Camión", "Sin Camión"};
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tipoTrans);
		spinnerTipoTransporte.setAdapter(mAdapter);
	}
	
	private void getInfoTransportista(final Integer transportistaId){
		new AsyncTask<Void, Void, Void>(){
			
			List<Chofer> chofer;
			List<Camion> camion;
			List<Camion> acoplado;
			String title, msg;
			
			protected void onPreExecute(){
				loading.setVisibility(View.VISIBLE);
				title = "";
				msg = "";
			}
			
			protected Void doInBackground(Void... arg0) {
				try {
					chofer = WSTrasladosCliente.traeChofer(transportistaId);
					camion = WSTrasladosCliente.traeCamion(transportistaId);
					acoplado = WSTrasladosCliente.traeAcoplado(transportistaId);
				} catch (AppException e) {
					title = "Error";
					msg = e.getMessage();
				}
				return null;
			}
			
			protected void onPostExecute(Void result){
				loading.setVisibility(View.INVISIBLE);
				if (title.equals("Error")){
					ShowAlert.showAlert(title, msg, TrasladosSalida.this);
				} else {
					ArrayAdapter<Chofer> mAdapter = new ArrayAdapter<Chofer>(TrasladosSalida.this, android.R.layout.simple_list_item_1, chofer);
					spinnerChofer.setAdapter(mAdapter);
					
					ArrayAdapter<Camion> mAdapter2 = new ArrayAdapter<Camion>(TrasladosSalida.this, android.R.layout.simple_list_item_1, camion);
					spinnerCamion.setAdapter(mAdapter2);
					
					Camion c = new Camion();
					acoplado.add(0, c);
					ArrayAdapter<Camion> mAdapter3 = new ArrayAdapter<Camion>(TrasladosSalida.this, android.R.layout.simple_list_item_1, acoplado);
					spinnerAcoplado.setAdapter(mAdapter3);
				}
			}
		}.execute();
	}
	
	private void clearScreen(){
		trasladoSalida = new Traslado();
		//spinnerDestino.setSelection(0);
		//spinnerTipoTransporte.setSelection(0);
		//spinnerTransportista.setSelection(0);
		//spinnerChofer.setSelection(0);
		//spinnerCamion.setSelection(0);
		//spinnerAcoplado.setSelection(0);
		resetContadores();
		trasladoSalida.getGanado().clear();
		TrasladosSalidaDiios.mangadaActual = 0;
		TrasladosSalidaDiios.isMangadaLocked = true;
		finished = false;
		strGD = "";
		updateStatus();
	}
	
	private void resetContadores(){
		contVacas = 0;
		contVaquillas = 0;
		contTerneras = 0;
		contToros = 0;
		contToretes = 0;
		contTerneros = 0;
		contBueyes = 0;
	}
	
	private boolean isMangadaCerrada(){
		int size = trasladoSalida.getGanado().size();
		if (size > 0 && TrasladosSalidaDiios.isMangadaLocked){
			return true;
		}
		return false;
	}
	
	private void updateStatus(){
		despliegaGD.setText(strGD);
		//Actualizar numeros (vacas, toros, vaq, toretes, etc)
		resetContadores();
		int size = trasladoSalida.getGanado().size();
		for (int i = 0; i < size; i++){
			for (int j = 0; j < listaTipoGanado.size(); j++){
				if (trasladoSalida.getGanado().get(i).getTipoGanadoId().intValue() == listaTipoGanado.get(j).getId().intValue()){
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
		
		if (contVacas > 0){
			tvVacas.setText("V: " + Integer.toString(contVacas));
		} else {
			tvVacas.setText("V:");
		}
		
		if (contVaquillas > 0){
			tvVaquillas.setText("Vq: " + Integer.toString(contVaquillas));
		} else {
			tvVaquillas.setText("Vq:");
		}
		
		if (contTerneras > 0){
			tvTerneras.setText("Ta: " + Integer.toString(contTerneras));
		} else {
			tvTerneras.setText("Ta:");
		}
		
		if (contToros > 0){
			tvToros.setText("T: " + Integer.toString(contToros));
		} else {
			tvToros.setText("T:");
		}
		
		if (contToretes > 0){
			tvToretes.setText("Te: " + Integer.toString(contToretes));
		} else {
			tvToretes.setText("Te:");
		}
		
		if (contTerneros > 0){
			tvTerneros.setText("To: " + Integer.toString(contTerneros));
		} else {
			tvTerneros.setText("To:");
		}
		
		if (contBueyes > 0){
			tvBueyes.setText("B: " + Integer.toString(contBueyes));
		} else {
			tvBueyes.setText("B:");
		}
		
		//Actualizar interfaz
		if ((trasladoSalida.getArrieroId() != null || hayTransportista) &&
				isMangadaCerrada() &&
				finished == false){
			
			goBack.setVisibility(View.INVISIBLE);
			tvApp.setVisibility(View.INVISIBLE);
			undo.setVisibility(View.VISIBLE);
			deshacer.setVisibility(View.VISIBLE);
			confirmarMovimiento.setVisibility(View.VISIBLE);
		} else if ((trasladoSalida.getArrieroId() == null || hayTransportista) &&
				trasladoSalida.getGanado().size() == 0 &&
				finished == false){
			
			goBack.setVisibility(View.VISIBLE);
			tvApp.setVisibility(View.VISIBLE);
			undo.setVisibility(View.INVISIBLE);
			deshacer.setVisibility(View.INVISIBLE);
			confirmarMovimiento.setVisibility(View.INVISIBLE);
		} else {
			goBack.setVisibility(View.INVISIBLE);
			tvApp.setVisibility(View.INVISIBLE);
			undo.setVisibility(View.VISIBLE);
			deshacer.setVisibility(View.VISIBLE);
			confirmarMovimiento.setVisibility(View.INVISIBLE);
		}
		
	}

	public void onClick(View v) {
		if (isOnline() == false){
			return;
		}
		int id = v.getId();
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
			if (finished){
				return;
			}
			Intent i = new Intent(this, TrasladosSalidaDiios.class);
			startActivity(i);
			break;
		case R.id.deshacer:
		case R.id.undo:
			clearScreen();
			break;
		case R.id.confirmarMovimiento:
			confirmarMovimiento();
			break;
		}
	}
	
	private void confirmarMovimiento(){
		if (hayTransportista){
			trasladoSalida.setTransportistaId(((Transportista) spinnerTransportista.getSelectedItem()).getId());
			trasladoSalida.setFundoDestinoId(((Predio) spinnerDestino.getSelectedItem()).getId());
			trasladoSalida.setChoferId(((Chofer) spinnerChofer.getSelectedItem()).getId());
			trasladoSalida.setCamionId(((Camion) spinnerCamion.getSelectedItem()).getId());
			trasladoSalida.setAcopladoId(((Camion) spinnerAcoplado.getSelectedItem()).getId());
			trasladoSalida.setArrieroId(null);
		} else {
			trasladoSalida.setTransportistaId(null);
			trasladoSalida.setFundoDestinoId(((Predio) spinnerDestino.getSelectedItem()).getId());
			trasladoSalida.setChoferId(null);
			trasladoSalida.setCamionId(null);
			trasladoSalida.setAcopladoId(null);
			trasladoSalida.setArrieroId(((Persona) spinnerTransportista.getSelectedItem()).getId());
		}
    	new AsyncTask<Void, Void, Void>(){
    		String title, msg;
    		protected void onPreExecute(){
    			loading.setVisibility(View.VISIBLE);
    			confirmarMovimiento.setVisibility(View.INVISIBLE);
    			title = "";
    			msg = "";
    		}
    		
			protected Void doInBackground(Void... params) {
	    		try {
	    			trasladoSalida.setUsuarioId(Login.user);
	    			trasladoSalida.setFundoOrigenId(Aplicaciones.predioWS.getId());
	    			trasladoSalida.setDescripcion("TRASLADO");
	    			DctoAdem d = WSTrasladosCliente.insertaMovimiento(trasladoSalida);
	    			generarFMA(Integer.parseInt(d.getNrodocto()));
	    			finished = true;
	    			strGD = "GD: " + d.getNrodocto();
	    		} catch (AppException e) {
	    			title = "Error";
	    			msg = e.getMessage();
	    		}
				return null;
			}
			
			protected void onPostExecute(Void result){
				loading.setVisibility(View.INVISIBLE);
				confirmarMovimiento.setVisibility(View.VISIBLE);
				if (!title.equals("Error")){
	    			despliegaGD.setText(strGD);
	    			Toast.makeText(TrasladosSalida.this, "Salida Generada", Toast.LENGTH_LONG).show();
				} else {
					ShowAlert.showAlert(title, msg, TrasladosSalida.this);
				}
				updateStatus();
			}
    	}.execute();
    	
	}
	
	private void generarFMA(Integer nro_documento){
		FMA fma = new FMA();
		fma.setUsuarioId(Login.user);
		//en realidad al FMA le pasa el numero de guia de despacho, no el mov_id...
		fma.setG_movimiento_id(nro_documento);
		fma.setFundoOrigenId(Aplicaciones.predioWS.getId());
		fma.setFundoDestinoId(trasladoSalida.getFundoDestinoId());
		
		try {
			WSTrasladosCliente.generaXMLTraslado(fma);
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
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
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", TrasladosSalida.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };
	
}
