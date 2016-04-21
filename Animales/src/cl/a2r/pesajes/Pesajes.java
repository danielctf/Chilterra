package cl.a2r.pesajes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.a2r.animales.Aplicaciones;
import cl.a2r.animales.Login;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Pesaje;
import cl.a2r.sip.model.Traslado;
import cl.a2r.sip.wsservice.WSGanadoCliente;
import cl.a2r.sip.wsservice.WSPesajesCliente;
import cl.a2r.sip.wsservice.WSPredioLibreCliente;
import cl.ar2.sqlite.servicio.PesajesServicio;
import cl.ar2.sqlite.servicio.PredioLibreServicio;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Pesajes extends Activity implements View.OnClickListener, EditText.OnEditorActionListener{

	private ImageButton goBack, confirmarAnimal, sync, cerrarMangada, logs;
	private TextView tvSync, tvDiio, tvTotalAnimales, tvMangada, tvAnimalesMangada;
	private TextView tvUltFechaPesaje, tvUltPeso, tvPeso, tvGananciaPeso;
	private EditText etPeso;
	private ProgressBar loading;
	private Integer mangadaActual;
	private boolean isMangadaCerrada;
	private Ganado gan;
	private Pesaje p;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_pesajes);
		
		cargarInterfaz();
		traeDatosWS();
	}
	
	private void cargarInterfaz(){
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		sync = (ImageButton)findViewById(R.id.sync);
		sync.setOnClickListener(this);
		logs = (ImageButton)findViewById(R.id.logs);
		logs.setOnClickListener(this);
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
		loading = (ProgressBar)findViewById(R.id.loading);
		loading.setVisibility(View.INVISIBLE);
		
		tvUltFechaPesaje = (TextView)findViewById(R.id.tvUltFechaPesaje);
		tvUltFechaPesaje.setVisibility(View.GONE);
		tvUltPeso = (TextView)findViewById(R.id.tvUltPeso);
		tvUltPeso.setVisibility(View.GONE);
		tvPeso = (TextView)findViewById(R.id.tvPeso);
		tvPeso.setVisibility(View.GONE);
		tvGananciaPeso = (TextView)findViewById(R.id.tvGananciaPeso);
		tvGananciaPeso.setVisibility(View.GONE);
		etPeso = (EditText)findViewById(R.id.etPeso);
		etPeso.setOnEditorActionListener(this);
		etPeso.setVisibility(View.GONE);
		
		isMangadaCerrada = false;
		gan = new Ganado();
		p = null;
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
					List<Traslado> trasList = TrasladosServicio.traeReubicaciones();
					for (Traslado t : trasList){
						t.setUsuarioId(Login.user);
						t.setDescripcion("REUBICACION POR BASTONEO. PESAJE");
					}
					WSGanadoCliente.reajustaGanado(trasList);
					TrasladosServicio.deleteReubicaciones();
					
					List<Ganado> list = WSPredioLibreCliente.traeAllDiio();
					PredioLibreServicio.deleteDiio();
					PredioLibreServicio.insertaDiio(list);
					
					List<Pesaje> pesList = WSPesajesCliente.traePesaje();
					PesajesServicio.deletePesajeSynced();
					PesajesServicio.insertaPesaje(pesList);
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
					tvUltFechaPesaje.setVisibility(View.VISIBLE);
					tvUltPeso.setVisibility(View.VISIBLE);
					tvPeso.setVisibility(View.VISIBLE);
					tvGananciaPeso.setVisibility(View.VISIBLE);
					etPeso.setVisibility(View.VISIBLE);
				} else {
					ShowAlert.showAlert(title, msg, Pesajes.this);
				}
			}
			
		}.execute();
	}
	
	private void syncPendientes(){
		try {
			List<Pesaje> list = PesajesServicio.traePesajesNoSync();
			if (list.size() > 0){
				tvSync.setText(Integer.toString(list.size()));
			} else {
				tvSync.setText("");
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	private void mostrarCandidatos(){
		try {
			List<Pesaje> list = PesajesServicio.traePesajesFundoNoSync(Aplicaciones.predioWS.getId());
			tvTotalAnimales.setText(Integer.toString(list.size()));
			tvMangada.setText(Integer.toString(mangadaActual));
			int animalesMangada = 0;
			for (Pesaje p : list){
				if (p.getGan().getMangada().intValue() == mangadaActual.intValue()){
					animalesMangada++;
				}
			}
			tvAnimalesMangada.setText(Integer.toString(animalesMangada));
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	private void agregarAnimal(){
		if (isMangadaCerrada){
			isMangadaCerrada = false;
			mangadaActual++;
		}
		
		Pesaje p = new Pesaje();
		p.setFecha(new Date());
		p.setPeso(Double.parseDouble(etPeso.getText().toString()));
		if (this.p != null){
			p.setGpd(this.p.getGpd());
		}
		p.setSincronizado("N");
		gan.setMangada(mangadaActual);
		p.setGan(gan);
		
		try {
			List<Pesaje> list = new ArrayList<Pesaje>();
			list.add(p);
			PesajesServicio.insertaPesaje(list);
			Toast.makeText(this, "Animal Registrado", Toast.LENGTH_LONG).show();
			clearScreen();
			syncPendientes();
			mostrarCandidatos();
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
		case R.id.logs:
			i = new Intent(this, Logs.class);
			i.putExtra("mangadaActual", mangadaActual);
			startActivity(i);
			break;
		}
	}
	
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		int result = actionId & EditorInfo.IME_MASK_ACTION;
		switch (result){
		case EditorInfo.IME_ACTION_DONE:
			calcularGananciaPesoDiaria();
			updateStatus();
			break;
		case EditorInfo.IME_ACTION_NEXT:
            break;
		}
		return false;
	}
	
	private void calcularGananciaPesoDiaria(){
		if (p != null){
			if (!etPeso.getText().toString().equals("")){
				long diffDaysTime = new Date().getTime() - p.getFecha().getTime();
				long diffDays = diffDaysTime / (24L * 60L * 60L * 1000L);
				double diffPeso = Double.parseDouble(etPeso.getText().toString()) - p.getPeso();
				double pesoDiario = diffPeso / (double) diffDays;
				p.setGpd(roundForDisplay(pesoDiario));
				tvGananciaPeso.setText("Ganancia peso diario: " + roundForDisplay(pesoDiario) + " Kg/día");
			}
		}
	}
	
	private double roundForDisplay(double value){
		double res = 0;
		res = value * 100;
		res = Math.round(res);
		res = res / 100;
		return res;
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
		    		List<Pesaje> list = PesajesServicio.traePesajesFundoNoSync(Aplicaciones.predioWS.getId());
					int totalesManga = 0;
					for (Pesaje p : list){
						if (p.getGan().getMangada() == mangadaActual.intValue()){
							totalesManga++;
						}
					}
					if (totalesManga == ingresados){
						cerrarMangada(true);
					} else {
						ShowAlert.showAlert("Error", "El número de animales ingresado no coincide con la mangada actual", Pesajes.this);
					}
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), Pesajes.this);
				} catch (NumberFormatException e){
					ShowAlert.showAlert("Error", "Debe ingresar un numero", Pesajes.this);
				}
		    	
		    }
		});
	
		builder.show();
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
					
					List<Pesaje> list = PesajesServicio.traePesajesNoSync();
					WSPesajesCliente.insertaPesaje(list, Login.user);
					PesajesServicio.deletePesaje();
					
					List<Pesaje> pesList = WSPesajesCliente.traePesaje();
					PesajesServicio.deletePesajeSynced();
					PesajesServicio.insertaPesaje(pesList);
					
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
				ShowAlert.showAlert(title, msg, Pesajes.this);
				try {
					mangadaActual = PesajesServicio.traeMangadaActual();
					if (mangadaActual == null){
						mangadaActual = 0;
						cerrarMangada(false);
					}
				} catch (AppException e) {}
				syncPendientes();
				mostrarCandidatos();
			}
			
		}.execute();
	}
	
	private void cerrarMangada(boolean showMsg){
		if (showMsg){
			Toast.makeText(this, "Mangada Cerrada", Toast.LENGTH_LONG).show();
		}
		isMangadaCerrada = true;
		updateStatus();
	}
	
	private void updateStatus(){
		if (gan.getId() != null && gan.getDiio() != null){
			tvDiio.setText(Integer.toString(gan.getDiio()));
			tvDiio.setGravity(Gravity.CENTER_HORIZONTAL);
			if (!etPeso.getText().toString().equals("")){
				confirmarAnimal.setEnabled(true);
			} else {
				confirmarAnimal.setEnabled(false);
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
	}
	
	private void clearScreen(){
		gan = new Ganado();
		p = null;
		tvUltFechaPesaje.setText("Último pesaje:");
		tvUltPeso.setText("Último peso:");
		etPeso.setText("");
		tvGananciaPeso.setText("Ganancia peso diario:");
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
							ShowAlert.showAlert("Error", e.getMessage(), Pesajes.this);
						}
					}
				}
			});
		} else {
			showDiio(gan);
		}
	}
	
	private void verSiTienePesaje(Ganado gan){
		try {
			Pesaje p = PesajesServicio.traeGanPesaje(gan.getId());
			if (p != null){
				this.p = p;
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				tvUltFechaPesaje.setText("Último pesaje: " + df.format(p.getFecha()));
				tvUltPeso.setText("Último peso: " + p.getPeso() + " Kg");
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	private void showDiio(Ganado gan){
		this.gan = gan;
		updateStatus();
		verSiTienePesaje(gan);
	}
	
	private void checkDiioStatus(Ganado gan){
		if (gan != null){
			try {
				boolean exists = PesajesServicio.existsGanado(gan.getId());
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
			mangadaActual = PesajesServicio.traeMangadaActual();
			if (mangadaActual == null){
				mangadaActual = 0;
				cerrarMangada(false);
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
		mostrarCandidatos();
		syncPendientes();
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
						ShowAlert.showAlert("Error", "DIIO no existe", Pesajes.this);
					}
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), Pesajes.this);
				}
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", Pesajes.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };
    
}
