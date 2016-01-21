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
import cl.a2r.sip.wsservice.WSPredioLibreCliente;
import cl.ar2.sqlite.servicio.PredioLibreServicio;
import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
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

public class PredioLibreBrucelosis extends Fragment implements View.OnClickListener{

    private static int SCANNER_REQUEST_CODE = 123;
	private Activity act;
	private ImageButton confirmarAnimal, btnCodBarra;
	private Spinner spinnerTB;
	private View v;
	private TextView tvDiio, tvFaltantes, tvEncontrados, tvCodBarra;
	private LinearLayout llEncontrados, llFaltantes;
	private Brucelosis bru;
	private Integer instancia;
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
    	bru = new Brucelosis();
    	mostrarCandidatos();
    	setSpinner();
    	
		Bundle extras = act.getIntent().getExtras();
		if (extras != null) {
		    instancia = extras.getInt("instancia");
		}
    
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
				if (b.getGanado().getPredio().intValue() == Aplicaciones.predioWS.getId().intValue()){
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
		
		try {
			if (bru.getGanado().getId() != null){
				boolean exists = PredioLibreServicio.existsGanadoPL(bru.getGanado().getId());
				ArrayAdapter<LecturaTBObject> mAdapter;
				if (exists){
					mAdapter = new ArrayAdapter<LecturaTBObject>(act, android.R.layout.simple_list_item_1, aplicaTB);
				} else {
					mAdapter = new ArrayAdapter<LecturaTBObject>(act, android.R.layout.simple_list_item_1, noAplicaTB);
				}
				spinnerTB.setAdapter(mAdapter);
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), act);
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
			i = new Intent(act, Candidatos.class);
			i.putExtra("stance", "predioLibreBrucelosisEncontrados");
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
			bru.setInstancia(instancia);
			bru.setSincronizado("N");
			bru.setFecha_muestra(new Date());
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
			System.out.println("fucked");
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
	
	private void checkDiioStatus(Ganado gan){
		if (gan != null){
			bru.getGanado().setId(gan.getId());
			bru.getGanado().setDiio(gan.getDiio());
			bru.getGanado().setPredio(gan.getPredio());
			tvDiio.setText(Integer.toString(bru.getGanado().getDiio()));
			tvDiio.setGravity(Gravity.CENTER_HORIZONTAL);
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
