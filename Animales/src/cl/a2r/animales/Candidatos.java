package cl.a2r.animales;

import java.util.List;

import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Areteo;
import cl.a2r.sip.model.CollarParto;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.InyeccionTB;
import cl.a2r.sip.model.Movimiento;
import cl.a2r.sip.wsservice.WSAreteosCliente;
import cl.a2r.sip.wsservice.WSPartosCliente;
import cl.a2r.sip.wsservice.WSTrasladosCliente;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class Candidatos extends Activity implements View.OnClickListener, ListView.OnItemClickListener{

	private ListView lvCandidatos;
	private TextView tvApp;
	private ImageButton goBack;
	private String clickStance;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_candidatos);
		
		Bundle extras = getIntent().getExtras();
		String stance = null;
		if (extras != null) {
		    stance = extras.getString("stance");
		}
		
		cargarInterfaz();
		getCandidatosWS(stance);
	}
	
	private void cargarInterfaz(){
		lvCandidatos = (ListView)findViewById(R.id.lvCandidatos);
		lvCandidatos.setOnItemClickListener(this);
		tvApp = (TextView)findViewById(R.id.app);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		clickStance = "";
	}
	
	@SuppressWarnings("unchecked")
	private void getCandidatosWS(String stance){
		switch (stance){
		case "partosEncontrados":
			tvApp.setText("Candidatos Encontrados");
			try {
				List<Ganado> list = WSPartosCliente.traeCandidatosEncontrados(Aplicaciones.predioWS.getId());
				ArrayAdapter<Ganado> mAdapter = new ArrayAdapter<Ganado>(this, android.R.layout.simple_list_item_1, list);
				lvCandidatos.setAdapter(mAdapter);
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this);
			}
			break;
		case "partosFaltantes":
			tvApp.setText("Candidatos Faltantes");
			try {
				List<Ganado> list = WSPartosCliente.traeCandidatosFaltantes(Aplicaciones.predioWS.getId());
				ArrayAdapter<Ganado> mAdapter = new ArrayAdapter<Ganado>(this, android.R.layout.simple_list_item_1, list);
				lvCandidatos.setAdapter(mAdapter);
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this);
			}
			break;
		case "areteosEncontrados":
			tvApp.setText("Candidatos Encontrados");
			try {
				List<Areteo> list = WSAreteosCliente.traeAreteosEncontrados(Aplicaciones.predioWS.getId());
				ArrayAdapter<Areteo> mAdapter = new ArrayAdapter<Areteo>(this, android.R.layout.simple_list_item_1, list);
				lvCandidatos.setAdapter(mAdapter);
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this);
			}
			break;
		case "areteosFaltantes":
			tvApp.setText("Candidatos Faltantes");
			try {
				List<CollarParto> list = WSAreteosCliente.traeAreteosFaltantes(Aplicaciones.predioWS.getId());
				ArrayAdapter<CollarParto> mAdapter = new ArrayAdapter<CollarParto>(this, android.R.layout.simple_list_item_1, list);
				lvCandidatos.setAdapter(mAdapter);
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this);
			}
			break;
		case "trasladosGuiasDespacho":
			clickStance = "trasladosGuiasDespacho";
			tvApp.setText("Guias Despacho Pendientes");
			try {
				List<Movimiento> list = WSTrasladosCliente.traeMovimientosEP();
				ArrayAdapter<Movimiento> mAdapter = new ArrayAdapter<Movimiento>(this, android.R.layout.simple_list_item_1, list);
				lvCandidatos.setAdapter(mAdapter);
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this);
			}
			break;
		case "movimientosFaltantes":
			tvApp.setText("Candidatos Faltantes");
			List<Ganado> list = TrasladosConfirmacionDiios.faltantes.getGanado();
			ArrayAdapter<Ganado> mAdapter = new ArrayAdapter<Ganado>(this, android.R.layout.simple_list_item_1, list);
			lvCandidatos.setAdapter(mAdapter);
			break;
		case "movimientosEncontrados":
			tvApp.setText("Candidatos Encontrados");
			List<Ganado> list2 = TrasladosConfirmacion.confirmacion.getGanado();
			ArrayAdapter<Ganado> mAdapter2 = new ArrayAdapter<Ganado>(this, android.R.layout.simple_list_item_1, list2);
			lvCandidatos.setAdapter(mAdapter2);
			break;
		case "predioLibreEncontrados":
			tvApp.setText("Candidatos Encontrados");
			ArrayAdapter<InyeccionTB> mAdapter3 = new ArrayAdapter<InyeccionTB>(this, android.R.layout.simple_list_item_1, PredioLibreInyeccionTB.listEncontrados);
			lvCandidatos.setAdapter(mAdapter3);
			break;
		case "predioLibreFaltantes":
			tvApp.setText("Candidatos Faltantes");
			ArrayAdapter<Ganado> mAdapter4 = new ArrayAdapter<Ganado>(this, android.R.layout.simple_list_item_1, PredioLibreInyeccionTB.listFaltantes);
			lvCandidatos.setAdapter(mAdapter4);
			break;
		}
		
	}
	
	public void onClick(View arg0) {
		if (isOnline() == false){
			return;
		}
		int id = arg0.getId();
		switch (id){
		case R.id.goBack:
			finish();
			break;
		}
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (clickStance){
		case "trasladosGuiasDespacho":
			Integer nro_documento = ((Movimiento) arg0.getItemAtPosition(arg2)).getNro_documento();
			TrasladosConfirmacion.getTraslado(nro_documento);
			this.finish();
			break;
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
    	@SuppressWarnings("unchecked")
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
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", Candidatos.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };
	
}
