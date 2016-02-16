package cl.a2r.salvatajes;

import java.util.List;

import cl.a2r.animales.Login;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Salvataje;
import cl.ar2.sqlite.servicio.SalvatajesServicio;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SalvatajesDiio extends Activity implements View.OnClickListener{

	private RelativeLayout rlCalculadora;
	private TextView tvDiio, tvGrupoActual, tvTotalAnimales;
	private ImageButton goBack, logs, confirmarAnimal;
	private Ganado gan;
	private Integer grupoId;
	private EditText obs;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_salvatajes_diio);
		
		Calculadora.isSalvataje = true;
		cargarInterfaz();
		gan = new Ganado();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    grupoId = extras.getInt("grupoId");
		    String nombreGrupo = extras.getString("nombreGrupo");
		    tvGrupoActual.setText("Grupo Actual: " + nombreGrupo);
		}
	}
	
	private void cargarInterfaz(){
		rlCalculadora = (RelativeLayout)findViewById(R.id.rlCalculadora);
		rlCalculadora.setOnClickListener(this);
		tvDiio = (TextView)findViewById(R.id.tvDiio);
		tvDiio.setOnClickListener(this);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		logs = (ImageButton)findViewById(R.id.logs);
		logs.setOnClickListener(this);
		confirmarAnimal = (ImageButton)findViewById(R.id.confirmarAnimal);
		confirmarAnimal.setOnClickListener(this);
		tvGrupoActual = (TextView)findViewById(R.id.tvGrupoActual);
		tvTotalAnimales = (TextView)findViewById(R.id.tvTotalAnimales);
		obs = (EditText)findViewById(R.id.etNota);

	}
	
	public void onClick(View v) {
		int id = v.getId();
		switch (id){
		case R.id.goBack:
			finish();
			break;
		case R.id.logs:
			Intent i = new Intent(this, Logs.class);
			i.putExtra("grupoId", grupoId);
			startActivity(i);
			break;
		case R.id.tvDiio:
		case R.id.rlCalculadora:
			startActivity(new Intent(this, Calculadora.class));
			break;
		case R.id.confirmarAnimal:
			agregarAnimal();
			break;
		}
	}
	
	private void mostrarCandidatos(){
		try {
			List<Ganado> ganList = SalvatajesServicio.traeDiios(grupoId);
			if (ganList.size() > 0){
				tvTotalAnimales.setText(Integer.toString(ganList.size()));
			} else {
				tvTotalAnimales.setText("");
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	private void agregarAnimal(){
		Salvataje s = new Salvataje();
		s.setGrupoId(grupoId);
		gan.setObservacion(obs.getText().toString());
		s.getGanado().add(gan);
		try {
			SalvatajesServicio.insertaDiio(s);
			clearScreen();
			mostrarCandidatos();
			updateStatus();
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	private void updateStatus(){
		if (gan.getEid() != null){
			tvDiio.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
			tvDiio.setText(gan.getEid());
			RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			tvDiio.setLayoutParams(lp);
			confirmarAnimal.setEnabled(true);
		} else {
			tvDiio.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
			tvDiio.setText("DIIO:");
			RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			lp.setMargins(10, 0, 0, 0);
			tvDiio.setLayoutParams(lp);
			confirmarAnimal.setEnabled(false);
		}
	}
	
	private void checkDiioStatus(Ganado g){
		if (g != null){
			try {
				boolean exists = SalvatajesServicio.existsGanado(g.getEid(), grupoId);
				if (!exists){
					gan.setEid(g.getEid());
				} else {
					ShowAlert.showAlert("Error", "Animal ya existe", this);	
				}
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this);
			}
		}
		updateStatus();
	}
	
	private void clearScreen(){
		gan = new Ganado();
		obs.setText("");
		resetCalculadora();
	}
	
	private void resetCalculadora(){
		Calculadora.gan = null;
	}
	
	protected  void onDestroy(){
		super.onStart();
		
		Calculadora.isSalvataje = false;
		resetCalculadora();
	}
	
	protected void onStart(){
		super.onStart();
		
		if (Login.user == 0){
			finish();
		}
		
		ConnectThread.setHandler(mHandler);

		checkDiioStatus(Calculadora.gan);
		mostrarCandidatos();
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
    			Ganado g = new Ganado();
    			g.setEid(EID.trim());
    			checkDiioStatus(g);
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", SalvatajesDiio.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };

}
