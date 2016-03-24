package cl.a2r.rb51;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.a2r.animales.Aplicaciones;
import cl.a2r.animales.Candidatos;
import cl.a2r.animales.Login;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Bang;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.MedicamentoControl;
import cl.a2r.sip.model.Traslado;
import cl.a2r.sip.model.VRB51;
import cl.a2r.sip.wsservice.WSGanadoCliente;
import cl.a2r.sip.wsservice.WSPredioLibreCliente;
import cl.a2r.sip.wsservice.WSRB51Cliente;
import cl.ar2.sqlite.servicio.PredioLibreServicio;
import cl.ar2.sqlite.servicio.RB51Servicio;
import cl.ar2.sqlite.servicio.TrasladosServicio;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RB51 extends Activity implements View.OnClickListener{

	private ImageButton goBack, confirmarAnimal, sync, cerrarMangada, btnDelete;
	private TextView tvSync, tvDiio, tvTotalAnimales, tvMangada, tvAnimalesMangada, tvPrimeraV, tvBangActual, tvFaltantes, tvEncontrados;
	private Spinner spMedicamento, spBang;
	private ProgressBar loading;
	private LinearLayout llEncontrados, llFaltantes;
	private CheckBox checkBoxBang;
	private Integer mangadaActual, numeroVacuna;
	private List<VRB51> rbGanList;
	private List<Ganado> faltantesWS;
	private boolean isMangadaCerrada, logAccessed, hayBangsDisponibles;
	private Ganado gan;
	public static List<Ganado> faltantesFiltrado = new ArrayList<Ganado>();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_rb51);
		
		cargarInterfaz();
		syncPendientes();
		traeDatosWS();
	}
	
	private void cargarInterfaz(){
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		sync = (ImageButton)findViewById(R.id.sync);
		sync.setOnClickListener(this);
		cerrarMangada = (ImageButton)findViewById(R.id.cerrarMangada);
		cerrarMangada.setOnClickListener(this);
		confirmarAnimal = (ImageButton)findViewById(R.id.confirmarAnimal);
		confirmarAnimal.setOnClickListener(this);
		tvDiio = (TextView)findViewById(R.id.tvDiio);
		tvDiio.setOnClickListener(this);
		tvSync = (TextView)findViewById(R.id.tvSync);
    	tvTotalAnimales = (TextView)findViewById(R.id.tvTotalAnimales);
    	tvMangada = (TextView)findViewById(R.id.tvMangada);
    	tvAnimalesMangada = (TextView)findViewById(R.id.tvAnimalesMangada);
    	spMedicamento = (Spinner)findViewById(R.id.spMedicamento);
    	spBang = (Spinner)findViewById(R.id.spBang);
		loading = (ProgressBar)findViewById(R.id.loading);
		loading.setVisibility(View.INVISIBLE);
		btnDelete = (ImageButton)findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(this);
		tvPrimeraV = (TextView)findViewById(R.id.tvPrimeraV);
		tvPrimeraV.setVisibility(View.GONE);
		tvBangActual = (TextView)findViewById(R.id.tvBangActual);
		tvBangActual.setVisibility(View.GONE);
		checkBoxBang = (CheckBox)findViewById(R.id.checkBox1);
		checkBoxBang.setVisibility(View.GONE);
		tvFaltantes = (TextView)findViewById(R.id.tvFaltantes);
		tvEncontrados = (TextView)findViewById(R.id.tvEncontrados);
		llEncontrados = (LinearLayout)findViewById(R.id.llEncontrados);
		llEncontrados.setOnClickListener(this);
		llFaltantes = (LinearLayout)findViewById(R.id.llFaltantes);
		llFaltantes.setOnClickListener(this);
		
		hayBangsDisponibles = true;
		logAccessed = false;
		isMangadaCerrada = false;
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
					
					medList = WSRB51Cliente.traeMedicamentos(Aplicaciones.appId);
					numeroVacuna = WSRB51Cliente.traeNumeroVacuna();
					rbGanList = WSRB51Cliente.traeGanadoRB51();
					faltantesWS = WSRB51Cliente.traeCandidatos(Aplicaciones.predioWS.getId());
					
					//Sincroniza bangs
					List<Bang> bangsABorrar = RB51Servicio.traeNoSyncBang();
					WSRB51Cliente.deleteBang(bangsABorrar, Login.user);
					RB51Servicio.deleteAllBang();
					List<Bang> bangList = WSRB51Cliente.traeBang();
					RB51Servicio.insertaBang(bangList);
					
					//Actualiza ganados con rb51
					RB51Servicio.deleteSyncedRB51();
					RB51Servicio.insertaRB51(rbGanList);
				} catch (AppException e) {
					title = "Error";
					msg = e.getMessage();
				}
				return null;
			}
			
			protected void onPostExecute(Void result){
				loading.setVisibility(View.INVISIBLE);
				if (!title.equals("Error")){
					ArrayAdapter<MedicamentoControl> mAdapter = new ArrayAdapter<MedicamentoControl>(RB51.this, android.R.layout.simple_list_item_1, medList);
					spMedicamento.setAdapter(mAdapter);
					traeBangs();
					mostrarCandidatos();
				} else {
					ShowAlert.showAlert(title, msg, RB51.this);
				}
			}
			
		}.execute();
	}
	
	private void traeBangs(){
		try {
			List<Bang> bangs = RB51Servicio.traeBang();
			if (bangs.size() == 0){
				hayBangsDisponibles = false;
			} else {
				hayBangsDisponibles = true;
			}
			ArrayAdapter<Bang> mAdapter2 = new ArrayAdapter<Bang>(RB51.this, android.R.layout.simple_list_item_1, bangs);
			spBang.setAdapter(mAdapter2);
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	private void mostrarCandidatos(){
		if (numeroVacuna.intValue() == 1){
			//Primera Vacuna
			try {
				List<VRB51> encontradosList = RB51Servicio.traeCandidatosEncontrados(Aplicaciones.predioWS.getId());
				if (encontradosList.size() > 0){
					tvEncontrados.setText(Integer.toString(encontradosList.size()));
				} else {
					tvEncontrados.setText("");
				}
				
				faltantesFiltrado = new ArrayList<Ganado>();
				for (Ganado g : faltantesWS){
					boolean exists = false;
					for (VRB51 rb : encontradosList){
						if (g.getId().intValue() == rb.getGan().getId().intValue()){
							exists = true;
							break;
						}
					}
					if (!exists){
						faltantesFiltrado.add(g);
					}
				}
				if (faltantesFiltrado.size() > 0){
					tvFaltantes.setText(Integer.toString(faltantesFiltrado.size()));
				} else {
					tvFaltantes.setText("");
				}
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this);
			}
		} else if (numeroVacuna.intValue() == 2){
			//Segunda Vacuna
		}
		
	}
	
	private void syncPendientes(){
		try {
			List<VRB51> list = RB51Servicio.traeNoSyncRB51();
			if (list.size() > 0){
				tvSync.setText(Integer.toString(list.size()));
			} else {
				tvSync.setText("");
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
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
		case R.id.confirmarAnimal:
			agregarAnimal();
			break;
		case R.id.tvDiio:
			startActivity(new Intent(this, Calculadora.class));
			break;
		case R.id.cerrarMangada:
			verificarCierreMangada();
			break;
		case R.id.sync:
			sincronizar();
			break;
		case R.id.btnDelete:
			ShowAlert.askYesNo("Advertencia", "¿Seguro que desea eliminar el bang seleccionado?", this, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if (which == -2){
						try {
							if (hayBangsDisponibles){
								Integer bangId = ((Bang) spBang.getSelectedItem()).getId();
								RB51Servicio.borrarBang(bangId);
								traeBangs();
							}
						} catch (AppException e) {
							ShowAlert.showAlert("Erro", e.getMessage(), RB51.this);
						}
					}
				}
			});
			break;
		case R.id.llEncontrados:
			logAccessed = true;
			i = new Intent(this, Logs.class);
			i.putExtra("mangadaActual", mangadaActual);
			startActivity(i);
			break;
		case R.id.llFaltantes:
			i = new Intent(this, Candidatos.class);
			i.putExtra("stance", "rb51Faltantes");
			startActivity(i);
			break;
		}
	}
	
	private void sincronizar(){
		new AsyncTask<Void, Void, Void>(){
			
			String title, msg;
			
			protected void onPreExecute(){
				loading.setVisibility(View.VISIBLE);
				sync.setVisibility(View.INVISIBLE);
				title = "";
				msg = "";
			}
			
			protected Void doInBackground(Void... arg0) {
				try {
					List<Traslado> trasList = TrasladosServicio.traeReubicaciones();
					for (Traslado t : trasList){
						t.setUsuarioId(Login.user);
						t.setDescripcion("REUBICACION POR BASTONEO");
					}
					WSGanadoCliente.reajustaGanado(trasList);
					TrasladosServicio.deleteReubicaciones();
					
					List<Bang> bangsABorrar = RB51Servicio.traeNoSyncBang();
					WSRB51Cliente.deleteBang(bangsABorrar, Login.user);
					RB51Servicio.deleteAllBang();
					List<Bang> bangList = WSRB51Cliente.traeBang();
					RB51Servicio.insertaBang(bangList);
					
					List<VRB51> rbList = RB51Servicio.traeNoSyncRB51();
					WSRB51Cliente.insertaRB51(rbList, Login.user);
					RB51Servicio.deleteRB51();
					List<VRB51> rbGanList = WSRB51Cliente.traeGanadoRB51();
					RB51Servicio.insertaRB51(rbGanList);
					
					title = "Sincronización";
					msg = "Sincronización Completa";
				} catch (AppException e) {
					title = "Error";
					msg = e.getMessage();
				}
				return null;
			}
			
			protected void onPostExecute(Void result){
				loading.setVisibility(View.INVISIBLE);
				sync.setVisibility(View.VISIBLE);
				ShowAlert.showAlert(title, msg, RB51.this);
				syncPendientes();
				traeBangs();
				mostrarCandidatos();
				try {
					mangadaActual = RB51Servicio.mangadaActual(Aplicaciones.predioWS.getId());
					if (mangadaActual == null){
						mangadaActual = 0;
						cerrarMangada(false);
					}
				} catch (AppException e) {}
			}
			
		}.execute();
	}
	
	private void agregarAnimal(){
		if (isMangadaCerrada){
			isMangadaCerrada = false;
			mangadaActual++;
		}
		
		VRB51 rb = new VRB51();
		MedicamentoControl m = (MedicamentoControl) spMedicamento.getSelectedItem();
		Bang b = (Bang) spBang.getSelectedItem();
		gan.setMangada(mangadaActual);
		rb.setFecha(new Date());
		rb.setSincronizado("N");
		rb.setMed(m);
		rb.setBang(b);
		rb.setGan(gan);
		
		List<VRB51> list = new ArrayList<VRB51>();
		list.add(rb);
		try {
			RB51Servicio.insertaRB51(list);
			Toast.makeText(this, "Animal Registrado", Toast.LENGTH_LONG).show();
			clearScreen();
			traeBangs();
			syncPendientes();
			mostrarCandidatos();
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	private void verificarCierreMangada(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Title");
	
		// Set up the input
		final EditText input = new EditText(this);
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		builder.setView(input);
		// Set up the buttons
		builder.setTitle("Animales");
		builder.setMessage("Ingrese numero de Animales en Mangada");
		builder.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	dialog.cancel();
		    }
		});
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	try {
		    		int ingresados = Integer.parseInt(input.getText().toString());
					List<VRB51> list = RB51Servicio.traeNoSyncRB51();
					int totalesManga = 0;
					for (VRB51 rb : list){
						if (rb.getGan().getMangada().intValue() == mangadaActual.intValue()){
							totalesManga++;
						}
					}
					if (totalesManga == ingresados){
						cerrarMangada(true);
					} else {
						ShowAlert.showAlert("Error", "El número de animales ingresado no coincide con la mangada actual", RB51.this);
					}
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), RB51.this);
				} catch (NumberFormatException e){
					ShowAlert.showAlert("Error", "Debe ingresar un numero", RB51.this);
				}
		    	
		    }
		});
	
		builder.show();
	}
	
	private void updateStatus(){
		if (gan.getId() != null && gan.getDiio() != null){
			tvDiio.setText(Integer.toString(gan.getDiio()));
			tvDiio.setGravity(Gravity.CENTER_HORIZONTAL);
			if (hayBangsDisponibles){
				confirmarAnimal.setEnabled(true);
			}
		} else {
			tvDiio.setGravity(Gravity.LEFT);
			tvDiio.setText("DIIO:");
			confirmarAnimal.setEnabled(false);
		}
		
		if (isMangadaCerrada){
			cerrarMangada.setEnabled(false);
		} else {
			cerrarMangada.setEnabled(true);
		}
		
		try {
			List<VRB51> list = RB51Servicio.traeNoSyncRB51();
			List<VRB51> listaFiltrada = new ArrayList<VRB51>();
			for (VRB51 rb : list){
				if (rb.getGan().getPredio().intValue() == Aplicaciones.predioWS.getId().intValue()){
					listaFiltrada.add(rb);
				}
			}
			tvTotalAnimales.setText(Integer.toString(listaFiltrada.size()));
			tvMangada.setText(Integer.toString(mangadaActual));
			int animalesMangada = 0;
			for (VRB51 rb : listaFiltrada){
				if (rb.getGan().getMangada().intValue() == mangadaActual.intValue()){
					animalesMangada++;
				}
			}
			tvAnimalesMangada.setText(Integer.toString(animalesMangada));
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	private void cerrarMangada(boolean showMsg){
		if (showMsg){
			Toast.makeText(this, "Mangada Cerrada", Toast.LENGTH_LONG).show();
		}
		isMangadaCerrada = true;
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
	
	private void verReubicacion(final Ganado gan){
		if (gan.getPredio().intValue() != Aplicaciones.predioWS.getId().intValue()){
			ShowAlert.askYesNo("Predio", "El Animal figura en otro predio\n¿Esta seguro que el DIIO es correcto?", this, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					if (arg1 == -2){
						try {
							Traslado t = new Traslado();
							t.setFundoOrigenId(gan.getPredio());
							t.setFundoDestinoId(Aplicaciones.predioWS.getId());
							t.getGanado().add(gan);
							TrasladosServicio.insertaReubicacion(t);
							TrasladosServicio.updateGanadoFundo(Aplicaciones.predioWS.getId(), gan.getId());
							gan.setPredio(Aplicaciones.predioWS.getId());
							showDiio(gan);
						} catch (AppException e) {
							ShowAlert.showAlert("Error", e.getMessage(), RB51.this);
						}
					}
				}
			});
		} else {
			showDiio(gan);
		}
	}
	
	private void showDiio(Ganado gan){
		this.gan = gan;
		updateStatus();
	}
	
	private void checkDiioStatus(Ganado gan){
		if (gan != null){
			try {
				boolean exists = RB51Servicio.existsGanado(gan.getId(), numeroVacuna);
				clearScreen();
				if (!exists){
					verReubicacion(gan);
				} else {
					ShowAlert.showAlert("Animal", "Animal ya existe", this);
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
		
		try {
			mangadaActual = RB51Servicio.mangadaActual(Aplicaciones.predioWS.getId());
			if (mangadaActual == null){
				mangadaActual = 0;
				cerrarMangada(false);
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
		
		try {
			mostrarCandidatos();
		} catch (NullPointerException e){}
		
		if (logAccessed){
			traeBangs();
			syncPendientes();
			logAccessed = false;
		}
		
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
						ShowAlert.showAlert("Error", "DIIO no existe", RB51.this);
					}
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), RB51.this);
				}
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", RB51.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };

}
