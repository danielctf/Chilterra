package cl.a2r.auditoria;

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
import cl.a2r.sip.model.Auditoria;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Instancia;
import cl.a2r.sip.model.Traslado;
import cl.a2r.sip.wsservice.WSAuditoriaCliente;
import cl.a2r.sip.wsservice.WSGanadoCliente;
import cl.a2r.sip.wsservice.WSTrasladosCliente;
import cl.ar2.sqlite.servicio.AuditoriasServicio;
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
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Auditorias extends Activity implements View.OnClickListener{

	private ImageButton goBack, confirmarAnimal, sync, cerrarMangada;
	private TextView tvSync, tvDiio, tvTotalAnimales, tvMangada, tvAnimalesMangada, tvFaltantes, tvEncontrados;
	private ProgressBar loading;
	private LinearLayout llEncontrados, llFaltantes;
	private Integer mangadaActual, instancia;
	private boolean isMangadaCerrada;
	private Ganado gan;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_auditorias);
		
		cargarInterfaz();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    instancia = extras.getInt("instancia");
		}
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
		loading = (ProgressBar)findViewById(R.id.loading);
		loading.setVisibility(View.INVISIBLE);
		tvFaltantes = (TextView)findViewById(R.id.tvFaltantes);
		tvEncontrados = (TextView)findViewById(R.id.tvEncontrados);
		llEncontrados = (LinearLayout)findViewById(R.id.llEncontrados);
		llEncontrados.setOnClickListener(this);
		llFaltantes = (LinearLayout)findViewById(R.id.llFaltantes);
		llFaltantes.setOnClickListener(this);
		
		isMangadaCerrada = false;
		gan = new Ganado();
	}
	
	private void mostrarCandidatos(){
		try {
			//Faltantes
			Auditoria faltantes = AuditoriasServicio.traeCandidatosFaltantes(Aplicaciones.predioWS.getId(), instancia);
			if (faltantes.getGanList().size() > 0 ){
				tvFaltantes.setText(Integer.toString(faltantes.getGanList().size()));
			} else {
				tvFaltantes.setText("");
			}
			
			//Encontrados
			Auditoria encontrados = AuditoriasServicio.traeInstanciaGanado(instancia);
			if (encontrados.getGanList().size() > 0){
				tvEncontrados.setText(Integer.toString(encontrados.getGanList().size()));
			} else {
				tvEncontrados.setText("");
			}
			
			//Contadores mangada
			Auditoria a = AuditoriasServicio.traeInstanciaGanadoNoSync(instancia);
			tvTotalAnimales.setText(Integer.toString(a.getGanList().size()));
			tvMangada.setText(Integer.toString(mangadaActual));
			int animalesMangada = 0;
			for (Ganado g : a.getGanList()){
				if (g.getMangada().intValue() == mangadaActual.intValue()){
					animalesMangada++;
				}
			}
			tvAnimalesMangada.setText(Integer.toString(animalesMangada));

		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	private void syncPendientes(){
		try {
			List<Auditoria> list = AuditoriasServicio.traeGanadoNoSync();
			int pendientes = 0;
			for (Auditoria a : list){
				for (Ganado g : a.getGanList()){
					pendientes++;
				}
			}
			if (pendientes > 0){
				tvSync.setText(Integer.toString(pendientes));
			} else {
				tvSync.setText("");
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	private void agregarAnimal(){
		if (isMangadaCerrada){
			isMangadaCerrada = false;
			mangadaActual++;
		}
		
		Auditoria a = new Auditoria();
		a.setId(instancia);
		gan.setMangada(mangadaActual);
		gan.setFecha(new Date());
		gan.setSincronizado("N");
		a.getGanList().add(gan);
		
		try {
			AuditoriasServicio.insertaAuditoria(a);
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
		case R.id.llEncontrados:
			i = new Intent(this, Logs.class);
			i.putExtra("mangadaActual", mangadaActual);
			i.putExtra("instancia", instancia);
			startActivity(i);
			break;
		case R.id.llFaltantes:
			i = new Intent(this, Candidatos.class);
			i.putExtra("stance", "auditoriaFaltantes");
			i.putExtra("instancia", instancia);
			startActivity(i);
			break;
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
		    		Auditoria a = AuditoriasServicio.traeInstanciaGanadoNoSync(instancia);
					int totalesManga = 0;
					for (Ganado g : a.getGanList()){
						if (g.getMangada().intValue() == mangadaActual.intValue()){
							totalesManga++;
						}
					}
					if (totalesManga == ingresados){
						cerrarMangada(true);
					} else {
						ShowAlert.showAlert("Error", "El número de animales ingresado no coincide con la mangada actual", Auditorias.this);
					}
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), Auditorias.this);
				} catch (NumberFormatException e){
					ShowAlert.showAlert("Error", "Debe ingresar un numero", Auditorias.this);
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
					List<Instancia> instList = TrasladosServicio.traeReubicaciones();
					WSTrasladosCliente.insertaReubicacion(instList);
					TrasladosServicio.deleteReubicaciones();
					
					List<Auditoria> auList = AuditoriasServicio.traeGanadoNoSync();
					WSAuditoriaCliente.insertaGanado(auList, Login.user);
					AuditoriasServicio.deleteGanado();
					
					Auditoria a = WSAuditoriaCliente.traeGanado(instancia);
					AuditoriasServicio.deleteGanadoSynced();
					AuditoriasServicio.insertaAuditoria(a);
					
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
				ShowAlert.showAlert(title, msg, Auditorias.this);
				try {
					mangadaActual = AuditoriasServicio.traeMangadaActual(instancia);
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
			confirmarAnimal.setEnabled(true);
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
							Instancia instancia = new Instancia();
							instancia.setFundoId(Aplicaciones.predioWS.getId());
							List<Ganado> ganList = new ArrayList<Ganado>();
							ganList.add(gan);
							instancia.setGanList(ganList);
							TrasladosServicio.insertaReubicacion(instancia);
							TrasladosServicio.updateGanadoFundo(Aplicaciones.predioWS.getId(), gan.getId());
							gan.setPredio(Aplicaciones.predioWS.getId());
							showDiio(gan);
						} catch (AppException e) {
							ShowAlert.showAlert("Error", e.getMessage(), Auditorias.this);
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
				boolean exists = AuditoriasServicio.existsGanado(gan.getId(), instancia);
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
			mangadaActual = AuditoriasServicio.traeMangadaActual(instancia);
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
						ShowAlert.showAlert("Error", "DIIO no existe", Auditorias.this);
					}
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), Auditorias.this);
				}
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", Auditorias.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };

}
