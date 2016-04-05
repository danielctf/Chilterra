package cl.a2r.ecografias;

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
import cl.a2r.sip.model.Ecografia;
import cl.a2r.sip.model.EcografiaEstado;
import cl.a2r.sip.model.EcografiaNota;
import cl.a2r.sip.model.EcografiaProblema;
import cl.a2r.sip.model.Ecografista;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Inseminacion;
import cl.a2r.sip.model.Traslado;
import cl.ar2.sqlite.servicio.EcografiasServicio;
import cl.ar2.sqlite.servicio.PredioLibreServicio;
import cl.ar2.sqlite.servicio.TrasladosServicio;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Ecografias extends Activity implements View.OnClickListener, View.OnKeyListener{

	private ImageButton goBack, logs, confirmarAnimal, sync, cerrarMangada;
	private TextView tvUltAct, tvUltAct2, tvUltEco, tvLogs, tvDiio, tvProblema, tvTotalAnimales, tvMangada, tvAnimalesMangada;
	private Spinner spEcografista, spEstado, spProblema, spNota;
	private RadioGroup rg;
	private RadioButton radio1, radio2, radio3;
	private ProgressBar loading;
	private EditText etDias;
	private Ecografia eco;
	private Integer mangadaActual;
	private boolean isMangadaCerrada;
	private MediaPlayer mp;
	private AudioManager audio;
	private Inseminacion maxIns;
	private Inseminacion maxIns2da;
	public static List<EcografiaEstado> menor30 = new ArrayList<EcografiaEstado>();
	public static List<EcografiaEstado> prenada = new ArrayList<EcografiaEstado>();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_ecografias);
		
		cargarInterfaz();
		new AsyncGetData(this).execute();
	}
	
	private void cargarInterfaz(){
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		logs = (ImageButton)findViewById(R.id.logs);
		logs.setOnClickListener(this);
		sync = (ImageButton)findViewById(R.id.sync);
		sync.setOnClickListener(this);
		cerrarMangada = (ImageButton)findViewById(R.id.cerrarMangada);
		cerrarMangada.setOnClickListener(this);
		confirmarAnimal = (ImageButton)findViewById(R.id.confirmarAnimal);
		confirmarAnimal.setOnClickListener(this);
		tvDiio = (TextView)findViewById(R.id.tvDiio);
		tvDiio.setOnClickListener(this);
		tvProblema = (TextView)findViewById(R.id.tvProblema);
		tvUltAct = (TextView)findViewById(R.id.tvUltAct);
		tvUltAct.setVisibility(View.GONE);
		tvUltAct2 = (TextView)findViewById(R.id.tvUltAct2);
		tvUltAct2.setVisibility(View.GONE);
		tvUltEco = (TextView)findViewById(R.id.tvUltEco);
		tvUltEco.setVisibility(View.GONE);
		tvLogs = (TextView)findViewById(R.id.tvLogs);
    	tvTotalAnimales = (TextView)findViewById(R.id.tvTotalAnimales);
    	tvMangada = (TextView)findViewById(R.id.tvMangada);
    	tvAnimalesMangada = (TextView)findViewById(R.id.tvAnimalesMangada);
		spEcografista = (Spinner)findViewById(R.id.spEcografista);
		spEstado = (Spinner)findViewById(R.id.spEstado);
		spProblema = (Spinner)findViewById(R.id.spProblema);
		spNota = (Spinner)findViewById(R.id.spNota);
		rg = (RadioGroup)findViewById(R.id.radioGroup1);
		loading = (ProgressBar)findViewById(R.id.loading);
		loading.setVisibility(View.INVISIBLE);
		radio1 = (RadioButton)findViewById(R.id.radio1);
		radio2 = (RadioButton)findViewById(R.id.radio2);
		radio2.setOnClickListener(this);
		radio3 = (RadioButton)findViewById(R.id.radio3);
		etDias = (EditText)findViewById(R.id.etDias);
		etDias.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus){
					radio2.setChecked(true);
				}
			}
		});
		audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		
		eco = new Ecografia();
		isMangadaCerrada = false;
	}
	
	public void cargarListeners(){
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener(){
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	            RadioButton rb = (RadioButton)findViewById(checkedId);
	            
	            if (rb.getId() != R.id.radio2){
		            etDias.setFocusable(false);
		            etDias.setFocusableInTouchMode(true);
		            etDias.setText("");
		            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(etDias.getWindowToken(), 0);
	            }
	            
	    		ArrayAdapter<EcografiaEstado> mAdapter = new ArrayAdapter<EcografiaEstado>(Ecografias.this, android.R.layout.simple_list_item_1);
	    		if (radio1.isChecked()){
	    			mAdapter.addAll(menor30);
	    		} else {
	    			mAdapter.addAll(prenada);
	    		}
	    		spEstado.setAdapter(mAdapter);
	        }
	    });
		
		radio2.setChecked(true);
		radio1.setChecked(true);
		
		spEstado.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (!((EcografiaEstado) spEstado.getSelectedItem()).getNombre().equals("Problema")){
					tvProblema.setVisibility(View.GONE);
					spProblema.setVisibility(View.GONE);
				} else {
					tvProblema.setVisibility(View.VISIBLE);
					spProblema.setVisibility(View.VISIBLE);
				}
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
	}
	
	public void mostrarCandidatos(){
		try {
			List<Ecografia> list = EcografiasServicio.traeEcografias();
			if (list.size() > 0){
				tvLogs.setText(Integer.toString(list.size()));
			} else {
				tvLogs.setText("");
			}
			
			tvTotalAnimales.setText(Integer.toString(list.size()));
			tvMangada.setText(Integer.toString(mangadaActual));
			int animalesMangada = 0; 
			for (Ecografia e : list){
				if (e.getMangada().intValue() == mangadaActual.intValue()){
					animalesMangada++;
				}
			}
			tvAnimalesMangada.setText(Integer.toString(animalesMangada));
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}

	public void onClick(View v) {
		int id = v.getId();
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
		case R.id.tvDiio:
			startActivity(new Intent(this, Calculadora.class));
			break;
		case R.id.confirmarAnimal:
			agregarAnimal();
			break;
		case R.id.sync:
			new Sincronizacion(this, Login.user).execute();
			break;
		case R.id.logs:
			Intent i = new Intent(this, Logs.class);
			i.putExtra("mangada", mangadaActual);
			startActivity(i);
			break;
		case R.id.cerrarMangada:
			verificarCierreMangada();
			break;
		}
	}
	
	private void updateStatus(){
		if (eco.getGan().getId() == null ||
				eco.getGan().getDiio() == null){
			
			tvDiio.setGravity(Gravity.LEFT);
			tvDiio.setText("DIIO:");
			confirmarAnimal.setEnabled(false);
		} else {
			confirmarAnimal.setEnabled(true);
		}
		
		if (isMangadaCerrada){
			cerrarMangada.setEnabled(false);
		} else {
			cerrarMangada.setEnabled(true);
		}
		
	}
	
	private void verActividad(){
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		boolean diasPrenezSegunEco = false;
		Integer dias_prenez = null;
		//Resetea las INS aqui porque podria interferir con otro bastoneo 
		maxIns = new Inseminacion();
		maxIns2da = new Inseminacion();
		maxIns.setFecha(new Date(0));
		maxIns2da.setFecha(new Date(0));
		try {
			//--------------------------REVISION ECOGRAFIAS--------------------------
			List<Ecografia> ecoList = EcografiasServicio.traeEcografiaGanado(eco.getGan().getId());
			if (ecoList.size() > 0){
				Ecografia maxEco = new Ecografia();
				maxEco.setFecha(new Date(0));
				int numEcos = 0;
				for (Ecografia e : ecoList){
					if (e.getFecha().getTime() > maxEco.getFecha().getTime()){
						maxEco = e;
					}
					numEcos++;
				}
				tvUltEco.setVisibility(View.VISIBLE);
				int maxEcoDays = (int) ((new Date().getTime() - maxEco.getFecha().getTime()) / (24 * 60 * 60 * 1000));
				tvUltEco.setText("Ecografías: " + Integer.toString(numEcos)+ "  (Última " + Integer.toString(maxEcoDays) + " Días)");
				//Verificar si tiene una ecografia hoy, de ser asi, tira un warning
				long diffDays = new Date().getTime() - maxEco.getFecha().getTime();
				int days = (int) (diffDays / (24 * 60 * 60 * 1000));
				if (days == 0){
					ShowAlert.showAlert("Advertencia", "Éste Animal ya tiene una ecografia el dia de hoy", this);
				}
				
				if (maxEco.getDias_prenez() != null && maxEco.getDias_prenez().intValue() != 0){
					System.out.println("DIAS PREÑEZ DESDE ECO");
					diasPrenezSegunEco = true;
					long diff = new Date().getTime() - maxEco.getFecha().getTime();
					dias_prenez = ((int) (diff / (24 * 60 * 60 * 1000))) + maxEco.getDias_prenez().intValue();
				}
				
			} else {
				tvUltEco.setVisibility(View.GONE);
			}
			
			//--------------------------REVISION INSEMINACIONES--------------------------
			List<Inseminacion> insList = EcografiasServicio.traeInseminacionGanado(eco.getGan().getId());
			if (insList.size() > 0){
				for (Inseminacion i : insList){
					if (i.getFecha().getTime() > maxIns.getFecha().getTime()){
						maxIns2da = maxIns;
						maxIns = i;
					} else if (i.getFecha().getTime() > maxIns2da.getFecha().getTime()){
						maxIns2da = i;
					}
				}
				tvUltAct.setVisibility(View.VISIBLE);
				int maxInsDays = (int) ((new Date().getTime() - maxIns.getFecha().getTime()) / (24 * 60 * 60 * 1000));
				tvUltAct.setText("Inseminación: " + df.format(maxIns.getFecha()) + "  (" + Integer.toString(maxInsDays) + " Días)");
				if (maxIns2da.getFecha().getTime() > 0){
					tvUltAct2.setVisibility(View.VISIBLE);
					int maxIns2daDays = (int) ((new Date().getTime() - maxIns2da.getFecha().getTime()) / (24 * 60 * 60 * 1000));
					tvUltAct2.setText("Inseminación: " + df.format(maxIns2da.getFecha()) + "  (" + Integer.toString(maxIns2daDays) + " Días)");
				} else {
					tvUltAct2.setVisibility(View.GONE);	
				}

				if (!diasPrenezSegunEco){
					System.out.println("DIAS PREÑEZ DESDE INSEM");
					long diff = new Date().getTime() - maxIns.getFecha().getTime();
					dias_prenez = (int) (diff / (24 * 60 * 60 * 1000));
				}
			} else {
				tvUltAct.setVisibility(View.GONE);
				tvUltAct2.setVisibility(View.GONE);
			}
			
			//-----------------------AJUSTAR RADIOBUTTONS SEGUN DIAS PREÑEZ-----------------
			if (dias_prenez == null){
				return;
			} else if (dias_prenez.intValue() >= 30 && dias_prenez.intValue() <= 120){
				etDias.setText(Integer.toString(dias_prenez));
				radio2.setChecked(true);
			} else if (dias_prenez.intValue() < 30){
				radio1.setChecked(true);
			} else if (dias_prenez.intValue() > 120){
				radio3.setChecked(true);
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
		
		if (radio2.isChecked() && etDias.getText().toString().equals("")){
			ShowAlert.showAlert("Error", "Debe ingresar un número", this);
			return;
		}
		
		//------------------------ASOCIAR ECOGRAFIA A INSEMINACION-----------------------
		int diasParametro = 15;
		if (radio2.isChecked() && !etDias.getText().toString().equals("")){
			
			int diasIngresados = Integer.parseInt(etDias.getText().toString());
			int diasMaxIns = (int) ((new Date().getTime() - maxIns.getFecha().getTime()) / (24 * 60 * 60 * 1000));
			int diasMaxIns2da = (int) ((new Date().getTime() - maxIns2da.getFecha().getTime()) / (24 * 60 * 60 * 1000));
			if (maxIns.getFecha().getTime() > 0 && maxIns2da.getFecha().getTime() > 0){
				
				if (diasMaxIns + diasParametro > diasIngresados && diasMaxIns - diasParametro < diasIngresados){
					if (diasMaxIns2da + diasParametro > diasIngresados && diasMaxIns2da - diasParametro < diasIngresados){
						int diffMaxIns = diasIngresados - diasMaxIns;
						int diffMaxIns2da = diasIngresados - diasMaxIns2da;
						if (Math.abs(diffMaxIns) <= Math.abs(diffMaxIns2da)){
							System.out.println("2 INS, 2 EN RANGO, maxIns");
							eco.setInseminacionId(maxIns.getId());
						} else {
							System.out.println("2 INS, 2 EN RANGO, maxIns2da");
							eco.setInseminacionId(maxIns2da.getId());
						}
					} else {
						System.out.println("2 INS, 1 EN RANGO, maxIns");
						eco.setInseminacionId(maxIns.getId());
					}
				} else {
					System.out.println("2 INS, 0 EN RANGO, null");
					eco.setInseminacionId(null);
				}
				
			} else if (maxIns.getFecha().getTime() > 0){
				if (diasMaxIns + diasParametro > diasIngresados && diasMaxIns - diasParametro < diasIngresados){
					System.out.println("1 INS, 1 EN RANGO, maxIns");
					eco.setInseminacionId(maxIns.getId());
				} else {
					System.out.println("1 INS, 0 EN RANGO, null");
					eco.setInseminacionId(null);
				}
			} else {
				System.out.println("0 INS, 0 EN RANGO, null");
				eco.setInseminacionId(null);
			}
		}
		//------------------------------------------------------------------------------
		eco.setFecha(new Date());
		if (!etDias.getText().toString().equals("")){
			eco.setDias_prenez(Integer.parseInt(etDias.getText().toString()));	
		}
		eco.setEcografistaId(((Ecografista) spEcografista.getSelectedItem()).getId());
		eco.setEstadoId(((EcografiaEstado) spEstado.getSelectedItem()).getId());
		if (spProblema.isShown()){
			eco.setProblemaId(((EcografiaProblema) spProblema.getSelectedItem()).getId());
		} else {
			eco.setProblemaId(null);
		}
		eco.setNotaId(((EcografiaNota) spNota.getSelectedItem()).getId());
		eco.setMangada(mangadaActual);
		eco.setSincronizado("N");
		eco.setFundoId(Aplicaciones.predioWS.getId());
		List<Ecografia> list = new ArrayList<Ecografia>();
		list.add(eco);
		try {
			EcografiasServicio.insertaEcografia(list);
			clearScreen();
			Toast.makeText(this, "Animal Insertado", Toast.LENGTH_LONG).show();
			mostrarCandidatos();
			radio1.setChecked(true);
			spNota.setSelection(0);
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
					List<Ecografia> list = EcografiasServicio.traeEcografias();
					int totalesManga = 0;
					for (Ecografia e : list){
						if (e.getMangada().intValue() == mangadaActual.intValue()){
							totalesManga++;
						}
					}
					if (totalesManga == ingresados){
						cerrarMangada(true);
					} else {
						ShowAlert.showAlert("Error", "El número de animales ingresado no coincide con la mangada actual", Ecografias.this);
					}
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), Ecografias.this);
				} catch (NumberFormatException e){
					ShowAlert.showAlert("Error", "Debe ingresar un numero", Ecografias.this);
				}
		    	
		    }
		});
	
		builder.show();
	}
	
	private void clearScreen(){
		eco = new Ecografia();
		tvUltAct.setText("");
		tvUltAct.setVisibility(View.GONE);
		tvUltAct2.setText("");
		tvUltAct2.setVisibility(View.GONE);
		tvUltEco.setText("");
		tvUltEco.setVisibility(View.GONE);
		etDias.setText("");
		radio1.setChecked(true);
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
							//EcografiasServicio.updateEcoFundo(Aplicaciones.predioWS.getId(), gan.getId());
							showDiio(gan);
						} catch (AppException e) {
							ShowAlert.showAlert("Error", e.getMessage(), Ecografias.this);
						}
					}
				}
			});
		} else {
			showDiio(gan);
		}
	}
	
	private void showDiio(Ganado gan){
		eco.setGanado(gan);
		tvDiio.setText(Integer.toString(gan.getDiio()));
		tvDiio.setGravity(Gravity.CENTER_HORIZONTAL);
		verActividad();
		updateStatus();
	}
	
	private void checkDiioStatus(Ganado gan){
		if (gan != null){
			clearScreen();
			verReubicacion(gan);
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
			mangadaActual = EcografiasServicio.traeMangadaActual();
			if (mangadaActual == null){
				mangadaActual = 0;
				cerrarMangada(false);
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
		
		mostrarCandidatos();
		Calculadora.isPredioLibre = true;
		ConnectThread.setHandler(mHandler);
		
		checkDiioStatus(Calculadora.gan);
	}
	
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
			audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
			audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
	                AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
		}
		return false;
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
    			mp = MediaPlayer.create(Ecografias.this, R.raw.beep2);
    			mp.setVolume(10, 10);
    			mp.start();
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
