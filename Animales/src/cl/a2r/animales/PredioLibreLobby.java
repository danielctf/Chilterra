package cl.a2r.animales;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.AsyncPLDiio;
import cl.a2r.custom.AsyncPredioLibre;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.PredioLibreAdapter;
import cl.a2r.custom.ShowAlert;
import cl.a2r.custom.Utility;
import cl.a2r.sip.model.Brucelosis;
import cl.a2r.sip.model.InyeccionTB;
import cl.a2r.sip.model.PredioLibre;
import cl.a2r.sip.wsservice.WSPredioLibreCliente;
import cl.ar2.sqlite.dao.SqLiteTrx;
import cl.ar2.sqlite.servicio.PredioLibreServicio;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PredioLibreLobby extends Activity implements View.OnClickListener, ListView.OnItemClickListener{

	private TextView tvSync, tvUpdate, tvFundo;
	private ImageButton goBack, sync, addPredioLibre;
	private ProgressBar loading;
	private ListView lvPredioLibre;
	private List<InyeccionTB> syncPendientes, syncLecturaTB;
	private List<Brucelosis> syncBrucelosis;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_predio_libre_lobby);
		
		cargarInterfaz();
		getPrediosLibreWS();
	}
	
	private void cargarInterfaz(){
		tvSync = (TextView)findViewById(R.id.tvSync);
		//tvUpdate = (TextView)findViewById(R.id.tvUpdate);
		tvFundo = (TextView)findViewById(R.id.tvFundo);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		sync = (ImageButton)findViewById(R.id.sync);
		sync.setOnClickListener(this);
		addPredioLibre = (ImageButton)findViewById(R.id.addPredioLibre);
		addPredioLibre.setOnClickListener(this);
		loading = (ProgressBar)findViewById(R.id.loading);
		loading.setVisibility(View.INVISIBLE);
		lvPredioLibre = (ListView)findViewById(R.id.lvPredioLibre);
		lvPredioLibre.setOnItemClickListener(this);
		
		tvFundo.setText(Aplicaciones.predioWS.getCodigo());
	}
	
	public void getPrediosLibreWS(){
		new AsyncTask<Void, Void, Void>(){
			List<PredioLibre> list;
			String title, msg;
			
			protected void onPreExecute(){
				loading.setVisibility(View.VISIBLE);
				title = "";
				msg = "";
			}

			protected Void doInBackground(Void... arg0) {
				try {
					list = WSPredioLibreCliente.traePredioLibre(Aplicaciones.predioWS.getId());
				} catch (AppException e) {
					title = "Error";
					msg = e.getMessage();
				}
				return null;
			}
			
			protected void onPostExecute(Void result){
				loading.setVisibility(View.INVISIBLE);
				if (title.equals("Error")){
					ShowAlert.showAlert(title, msg, PredioLibreLobby.this);
				} else {
					PredioLibreAdapter mAdapter = new PredioLibreAdapter(PredioLibreLobby.this, list);
					lvPredioLibre.setAdapter(mAdapter);
					Utility.setListViewHeightBasedOnChildren(lvPredioLibre);
				}
			}
			
		}.execute();
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int id = arg0.getId();
		switch (id){
		case R.id.lvPredioLibre:
			Integer instancia = ((PredioLibre) arg0.getItemAtPosition(arg2)).getId();
			try {
				boolean isTheSame = PredioLibreServicio.checkIfSameInstance(instancia);
				if (!isTheSame){
					ShowAlert.showAlert("Sincronización", "Debe sincronizar antes de ingresar a éste Predio Libre", this);
				} else {
					new AsyncPLDiio(this, instancia).execute();
				}
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this);
			}
			break;
		}
	}

	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id){
		case R.id.goBack:
			finish();
			break;
		case R.id.sync:
			new AsyncPredioLibre(this, syncPendientes, syncLecturaTB, syncBrucelosis, Login.user).execute();
			break;
		case R.id.addPredioLibre:
			addPredioLibre.setVisibility(View.INVISIBLE);
			hand.postDelayed(run, 100);
			break;
		}
	}
	
    Handler hand = new Handler();
    Runnable run = new Runnable() {
        public void run() {
			try {
				WSPredioLibreCliente.insertaPredioLibre(Login.user, Aplicaciones.predioWS.getId());
				getPrediosLibreWS();
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), PredioLibreLobby.this);
			} finally {
				addPredioLibre.setVisibility(View.VISIBLE);
			}
        }
    };
	
	public void syncPendientes(){
		//Inyeccion TB
		try {
			int syncs = 0;
			List<InyeccionTB> list = PredioLibreServicio.traeGanadoPL();
			syncPendientes = new ArrayList<InyeccionTB>();
			for (InyeccionTB tb : list){
				if (tb.getSincronizado().equals("N")){
					tb.setUsuarioId(Login.user);
					tb.setFecha_dosis(new Date());
					syncPendientes.add(tb);
					syncs++;
				}
			}
			
			//Lectura TB
			syncLecturaTB = new ArrayList<InyeccionTB>();
			for (InyeccionTB tb : list){
				if (tb.getLecturaTB() != null){
					tb.setUsuarioId(Login.user);
					tb.setFecha_lectura(new Date());
					syncLecturaTB.add(tb);
					//syncs++;
				}
			}
			
			List<Brucelosis> list2 = PredioLibreServicio.traeGanadoPLBrucelosis();
			syncBrucelosis = new ArrayList<Brucelosis>();
			for (Brucelosis b : list2){
				if (b.getSincronizado().equals("N")){
					b.setUsuarioId(Login.user);
					b.setFecha_muestra(new Date());
					syncBrucelosis.add(b);
					syncs++;
				}
			}
			
			if (syncs > 0){
				tvSync.setText(Integer.toString(syncs));
			} else {
				tvSync.setText("");
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
	}
	
	protected  void onStart(){
		super.onStart();
		
		if (Login.user == 0){
			finish();
		}
		
		syncPendientes();
		
		ConnectThread.setHandler(mHandler);
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
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", PredioLibreLobby.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };
	
}
