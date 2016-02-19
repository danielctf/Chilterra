package cl.a2r.animales;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.zxing.client.android.CaptureActivity;

import cl.a2r.common.AppException;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.object.LecturaTBObject;
import cl.a2r.sip.model.Brucelosis;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.InyeccionTB;
import cl.a2r.sip.model.PPD;
import cl.a2r.sip.model.Traslado;
import cl.a2r.sip.wsservice.WSPredioLibreCliente;
import cl.ar2.sqlite.servicio.PredioLibreServicio;
import cl.ar2.sqlite.servicio.TrasladosServicio;
import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class PredioLibreBrucelosis extends Fragment implements View.OnClickListener, View.OnKeyListener{

    private static int SCANNER_REQUEST_CODE = 123;
	private Activity act;
	private ImageButton confirmarAnimal, btnCodBarra, cerrarMangada;
	private Spinner spinnerTB;
	private View v;
	private TextView tvDiio, tvFaltantes, tvEncontrados, tvCodBarra, tvTotalAnimales, tvMangada, tvAnimalesMangada;
	private LinearLayout llEncontrados, llFaltantes;
	private Brucelosis bru;
	private Integer instancia;
	private boolean isMangadaCerrada;
	private MediaPlayer mp;
	private AudioManager audio;
	private int totalAnimales, animalesMangada, mangadaActual;
	private List<LecturaTBObject> aplicaTB, noAplicaTB;
	public static List<Brucelosis> listEncontrados;
	public static List<Ganado> listFaltantes;
	
	public PredioLibreBrucelosis(Activity act){
		this.act = act;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	v = inflater.inflate(R.layout.fragment_brucelosis, container, false);
    	tvDiio = (TextView)v.findViewById(R.id.tvDiio);
    	tvDiio.setOnClickListener(this);
    	tvFaltantes = (TextView)v.findViewById(R.id.tvFaltantes);
    	tvEncontrados = (TextView)v.findViewById(R.id.tvEncontrados);
    	tvCodBarra = (TextView)v.findViewById(R.id.tvCodBarra);
    	confirmarAnimal = (ImageButton)v.findViewById(R.id.confirmarAnimal);
    	confirmarAnimal.setOnClickListener(this);
    	btnCodBarra = (ImageButton)v.findViewById(R.id.btnCodBarra);
    	btnCodBarra.setOnClickListener(this);
    	llEncontrados = (LinearLayout)v.findViewById(R.id.llEncontrados);
    	llEncontrados.setOnClickListener(this);
    	llFaltantes = (LinearLayout)v.findViewById(R.id.llFaltantes);
    	llFaltantes.setOnClickListener(this);
    	spinnerTB = (Spinner)v.findViewById(R.id.spinnerTB);
    	cerrarMangada = (ImageButton)v.findViewById(R.id.cerrarMangada);
    	cerrarMangada.setOnClickListener(this);
    	tvTotalAnimales = (TextView)v.findViewById(R.id.tvTotalAnimales);
    	tvMangada = (TextView)v.findViewById(R.id.tvMangada);
    	tvAnimalesMangada = (TextView)v.findViewById(R.id.tvAnimalesMangada);
    	
    	audio = (AudioManager) act.getSystemService(Context.AUDIO_SERVICE);
    	bru = new Brucelosis();
    	
    	isMangadaCerrada = false;
    	totalAnimales = 0;
    	animalesMangada = 0;
    	mangadaActual = 0;
    	
		Bundle extras = act.getIntent().getExtras();
		if (extras != null) {
		    instancia = extras.getInt("instancia");
		}
		
    	setSpinner();
    
    	return v;
	}
	
	private void setSpinner(){
		aplicaTB = new ArrayList<LecturaTBObject>();
		noAplicaTB = new ArrayList<LecturaTBObject>();
		LecturaTBObject l = new LecturaTBObject();
		l.setCodigo("NE");
		l.setNombre("Negativo");
		aplicaTB.add(l);
		l = new LecturaTBObject();
		l.setCodigo("RE");
		l.setNombre("Reactante");
		aplicaTB.add(l);
		l = new LecturaTBObject();
		l.setCodigo("NA");
		l.setNombre("No Aplica");
		noAplicaTB.add(l);
		ArrayAdapter<LecturaTBObject> mAdapter = new ArrayAdapter<LecturaTBObject>(act, android.R.layout.simple_list_item_1, aplicaTB);
		spinnerTB.setAdapter(mAdapter);
	}
	
	private void mostrarCandidatos(){
		try {
			List<Brucelosis> list = PredioLibreServicio.traeGanadoPLBrucelosis();
			listEncontrados = new ArrayList<Brucelosis>();
			for (Brucelosis b : list){
				/*
				if (b.getGanado().getPredio().intValue() == Aplicaciones.predioWS.getId().intValue()){
					listEncontrados.add(b);
				}
				*/
				if (b.getInstancia().intValue() == instancia){
					listEncontrados.add(b);
				}
			}
			
			List<Ganado> listTodos = PredioLibreServicio.traeDiioFundo(Aplicaciones.predioWS.getId());
			listFaltantes = new ArrayList<Ganado>();
			for (Ganado g : listTodos){
				boolean isEncontrado = false;
				for (Brucelosis b : listEncontrados){
					if (g.getId().intValue() == b.getGanado().getId().intValue()){
						isEncontrado = true;
					}
				}
				if (!isEncontrado){
					listFaltantes.add(g);
				}
			}
			tvFaltantes.setText(Integer.toString(listFaltantes.size()));
			tvEncontrados.setText(Integer.toString(listEncontrados.size()));
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), act);
		}
	}
	
	private void updateStatus(){
		if (bru.getGanado().getId() == null ||
				bru.getGanado().getDiio() == null){
			
			tvDiio.setGravity(Gravity.LEFT);
			tvDiio.setText("DIIO:");
		}
		
		if (bru.getCodBarra() != null &&
				bru.getGanado().getId() != null &&
				bru.getGanado().getDiio() != null){
			
			confirmarAnimal.setEnabled(true);
		} else {
			confirmarAnimal.setEnabled(false);
		}
		
		//Actualizar contadores de mangada
		try {
			List<Brucelosis> list = PredioLibreServicio.traeGanadoPLBrucelosis();
			totalAnimales = list.size();
			animalesMangada = 0;
			for (Brucelosis b : list){
				if (b.getGanado().getMangada().intValue() == mangadaActual){
					animalesMangada++;
				}
			}
			tvTotalAnimales.setText(Integer.toString(totalAnimales));
			tvAnimalesMangada.setText(Integer.toString(animalesMangada));
			tvMangada.setText(Integer.toString(mangadaActual));
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), act);
		}
		
		//Actualiza boton cerrar mangada
		if (isMangadaCerrada){
			cerrarMangada.setEnabled(false);
		} else {
			cerrarMangada.setEnabled(true);
		}
		
	}
	
	public void onClick(View v) {
		int id = v.getId();
		Intent i;
		switch (id){
		case R.id.confirmarAnimal:
			agregarAnimal();
			break;
		case R.id.tvDiio:
			startActivity(new Intent(act, Calculadora.class));
			break;
		case R.id.llFaltantes:
			i = new Intent(act, Candidatos.class);
			i.putExtra("stance", "predioLibreBrucelosisFaltantes");
			startActivity(i);
			break;
		case R.id.llEncontrados:
			i = new Intent(act, PredioLibreLogsBR.class);
			i.putExtra("cantMangadas", mangadaActual);
			startActivity(i);
			break;
		case R.id.btnCodBarra:
            Intent intent = new Intent(act.getApplicationContext(), CaptureActivity.class);
            intent.setAction("com.google.zxing.client.android.SCAN");
            intent.putExtra("SAVE_HISTORY", false);
            intent.putExtra("SCAN_FORMATS", "PRODUCT_MODE, QR_CODE_MODE");
            intent.putExtra("PROMPT_MESSAGE", "Leer codigo");
            startActivityForResult(intent, SCANNER_REQUEST_CODE);
			break;
		case R.id.cerrarMangada:
			isMangadaCerrada = true;
			updateStatus();
			break;
		}
	}
	
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == SCANNER_REQUEST_CODE) {
            // Handle scan intent
            if (resultCode == Activity.RESULT_OK) {
                // Handle successful scan
                String contents = intent.getStringExtra("SCAN_RESULT");
                bru.setCodBarra(contents);
                tvCodBarra.setText(contents);
            }
        }
    }
	
	private void agregarAnimal(){
		try {
			if (isMangadaCerrada){
				mangadaActual++;
				isMangadaCerrada = false;
			}
			bru.setInstancia(instancia);
			bru.setSincronizado("N");
			bru.setFecha_muestra(new Date());
			bru.getGanado().setMangada(mangadaActual);
			boolean exists = PredioLibreServicio.existsGanadoPLBrucelosis(bru.getGanado().getId());
			boolean existsCodBarra = PredioLibreServicio.existsCodigoBarra(bru.getCodBarra());
			if (exists){
				Toast.makeText(act, "Animal ya existe", Toast.LENGTH_LONG).show();
				clearScreen();
				return;
			} else if (existsCodBarra){
				Toast.makeText(act, "Codigo de barra ya existe", Toast.LENGTH_LONG).show();
				clearScreen();
				return;
			}
			String lecturaTB = ((LecturaTBObject) spinnerTB.getSelectedItem()).getCodigo();
			PredioLibreServicio.insertaLecturaTuberculosis(lecturaTB, bru.getGanado().getId());
			PredioLibreServicio.insertaGanadoPLBrucelosis(bru);
			Toast.makeText(act, "Animal Registrado", Toast.LENGTH_LONG).show();
			mostrarCandidatos();
			clearScreen();
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), act);
		}
	}
	
	private void clearScreen(){
		tvCodBarra.setText("");
		bru = new Brucelosis();
		resetCalculadora();
		updateStatus();
	}
	
	private void resetCalculadora(){
		Calculadora.gan = null;
	}
	
	private void verReubicacion(final Ganado gan){
		if (gan.getPredio().intValue() != Aplicaciones.predioWS.getId().intValue()){
			ShowAlert.askYesNo("Predio", "El Animal figura en otro predio\n¿Esta seguro que el DIIO es correcto?", act, new DialogInterface.OnClickListener() {
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
							//PredioLibreServicio.updateGanFundo(Aplicaciones.predioWS.getId(), gan.getId());
							mostrarCandidatos();
							showDiio(gan);
						} catch (AppException e) {
							ShowAlert.showAlert("Error", e.getMessage(), act);
						}
					}
				}
			});
		} else {
			showDiio(gan);
		}
	}
	
	private void showDiio(Ganado gan){
		bru.getGanado().setId(gan.getId());
		bru.getGanado().setDiio(gan.getDiio());
		bru.getGanado().setPredio(gan.getPredio());
		tvDiio.setText(Integer.toString(bru.getGanado().getDiio()));
		tvDiio.setGravity(Gravity.CENTER_HORIZONTAL);
		
		try {
			boolean exists = PredioLibreServicio.existsGanadoPL(bru.getGanado().getId());
			ArrayAdapter<LecturaTBObject> mAdapter;
			if (exists){
				mAdapter = new ArrayAdapter<LecturaTBObject>(act, android.R.layout.simple_list_item_1, aplicaTB);
			} else {
				mAdapter = new ArrayAdapter<LecturaTBObject>(act, android.R.layout.simple_list_item_1, noAplicaTB);
			}
			spinnerTB.setAdapter(mAdapter);
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), act);
		}

	}
	
	private void checkDiioStatus(Ganado gan){
		if (gan != null){
			try{
			clearScreen();
			boolean existsGan = PredioLibreServicio.existsGanadoPLBrucelosis(gan.getId());
			if (existsGan){
				Toast.makeText(act, "Animal ya existe", Toast.LENGTH_LONG).show();
				return;
			}
			verReubicacion(gan);
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), act);
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
			Integer mang = PredioLibreServicio.traeMangadaActualBrucelosis();
			if (mang != null){
				mangadaActual = mang.intValue();
				if (mangadaActual == 0){
					isMangadaCerrada = true;
				}
			} else {
				isMangadaCerrada = true;
				mangadaActual = 0;
			}
		} catch (AppException e) {
			e.printStackTrace();
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
    			mp = MediaPlayer.create(act, R.raw.beep2);
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
						ShowAlert.showAlert("Error", "DIIO no existe", act);
					}
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), act);
				}
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", act, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };
	
}
