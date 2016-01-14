package cl.a2r.animales;

import java.util.List;

import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Traslado;
import cl.a2r.sip.wsservice.WSGanadoCliente;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TrasladosConfirmacionDiios extends Activity implements View.OnClickListener{

	private TextView despliegaDiio, textViewDiio, tvApp, deshacer, tvEncontrados, tvFaltantes;
	private ImageButton goBack, undo, logs, confirmarAnimal, cows_left;
	private RelativeLayout layoutCalculadora;
	private Integer ganadoId, diio, predio, tipoGanado;
	private boolean isReubicacion;
	
	public static Traslado reubicacion = new Traslado();
	public static Traslado faltantes = new Traslado();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_traslados_confirmacion_diios);
		
		cargarInterfaz();
	}
	
	private void cargarInterfaz(){
		despliegaDiio = (TextView)findViewById(R.id.despliegaDiio);
		textViewDiio = (TextView)findViewById(R.id.textViewDiio);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		undo = (ImageButton)findViewById(R.id.undo);
		undo.setOnClickListener(this);
		tvApp = (TextView)findViewById(R.id.app);
		deshacer = (TextView)findViewById(R.id.deshacer);
		deshacer.setOnClickListener(this);
		logs = (ImageButton)findViewById(R.id.logs);
		logs.setOnClickListener(this);
		confirmarAnimal = (ImageButton)findViewById(R.id.confirmarAnimal);
		confirmarAnimal.setOnClickListener(this);
		layoutCalculadora = (RelativeLayout)findViewById(R.id.layoutCalculadora);
		layoutCalculadora.setOnClickListener(this);
		cows_left = (ImageButton)findViewById(R.id.cows_left);
		cows_left.setOnClickListener(this);
		tvEncontrados = (TextView)findViewById(R.id.textViewEncontrados);
		tvFaltantes = (TextView)findViewById(R.id.textViewFaltantes);
		
		
		isReubicacion = false;
		
		ganadoId = 0;
		diio = 0;
		predio = 0;
		tipoGanado = 0;
	}
	
	private void updateStatus(){
		getCandidatos();
		//Actualizar interfaz
		if (ganadoId != 0 && diio != 0 && predio != 0 && tipoGanado != 0){
			goBack.setVisibility(View.INVISIBLE);
			tvApp.setVisibility(View.INVISIBLE);
			undo.setVisibility(View.VISIBLE);
			deshacer.setVisibility(View.VISIBLE);
			logs.setVisibility(View.INVISIBLE);
			tvFaltantes.setVisibility(View.INVISIBLE);
			tvEncontrados.setVisibility(View.INVISIBLE);
			cows_left.setVisibility(View.INVISIBLE);
			despliegaDiio.setText(Integer.toString(diio));
			textViewDiio.setText("");
			confirmarAnimal.setEnabled(true);
		} else if (ganadoId == 0 && diio == 0 && predio == 0 && tipoGanado == 0){
			goBack.setVisibility(View.VISIBLE);
			tvApp.setVisibility(View.VISIBLE);
			undo.setVisibility(View.INVISIBLE);
			deshacer.setVisibility(View.INVISIBLE);
			logs.setVisibility(View.VISIBLE);
			tvFaltantes.setVisibility(View.VISIBLE);
			tvEncontrados.setVisibility(View.VISIBLE);
			cows_left.setVisibility(View.VISIBLE);
			despliegaDiio.setText("");
			textViewDiio.setText("DIIO:");
			confirmarAnimal.setEnabled(false);
		} else {
			ShowAlert.showAlert("Error", "ERROR: GANADO\nContactarse con oficina\n", this);
			goBack.setVisibility(View.VISIBLE);
			tvApp.setVisibility(View.VISIBLE);
			undo.setVisibility(View.INVISIBLE);
			deshacer.setVisibility(View.INVISIBLE);
			logs.setVisibility(View.VISIBLE);
			tvFaltantes.setVisibility(View.VISIBLE);
			tvEncontrados.setVisibility(View.VISIBLE);
			cows_left.setVisibility(View.VISIBLE);
			despliegaDiio.setText("");
			textViewDiio.setText("DIIO:");
			confirmarAnimal.setEnabled(false);
		}

	}
	
	private void getCandidatos(){
		tvFaltantes.setText(Integer.toString(faltantes.getGanado().size()));
		tvEncontrados.setText(Integer.toString(TrasladosConfirmacion.confirmacion.getGanado().size()));
	}

	private void clearScreen(){
		ganadoId = 0;
		diio = 0;
		predio = 0;
		tipoGanado = 0;
		isReubicacion = false;
		resetCalculadora();
	}
	
	private void agregarAnimal(){
		if (isReubicacion){
			Ganado g = new Ganado();
			g.setId(ganadoId);
			g.setDiio(diio);
			g.setPredio(predio);
			g.setTipoGanadoId(tipoGanado);
			reubicacion.getGanado().add(g);
		} else {
			//Animal normal; viene de una salida
			for (Ganado g : TrasladosConfirmacion.salida.getGanado()){
				if (g.getId().intValue() == ganadoId.intValue()){
					TrasladosConfirmacion.confirmacion.getGanado().add(g);
					faltantes.getGanado().remove(g);
				}
			}
		}
		
		Toast.makeText(this, "Animal confirmado", Toast.LENGTH_LONG).show();
		clearScreen();
	}
	
	public void onClick(View v) {
		if (isOnline() == false){
			return;
		}
		int id = v.getId();
		Intent i;
		switch (id){
		case R.id.goBack:
			finish();
			break;
		case R.id.deshacer:
		case R.id.undo:
			clearScreen();
			break;
		case R.id.confirmarAnimal:
			agregarAnimal();
			break;
		case R.id.textViewDiio:
		case R.id.despliegaDiio:
		case R.id.layoutCalculadora:
			 i = new Intent(this, Calculadora.class);
			 startActivity(i);
			 break;
		case R.id.logs:
			 i = new Intent(this, Candidatos.class);
			 i.putExtra("stance","movimientosEncontrados");
			 startActivity(i);
			break;
		case R.id.cows_left:
			 i = new Intent(this, Candidatos.class);
			 i.putExtra("stance","movimientosFaltantes");
			 startActivity(i);
			break;
		}
		updateStatus();
		
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
		
		checkDiioStatus(Calculadora.diio, Calculadora.ganadoId, Calculadora.activa, Calculadora.predio, Calculadora.tipoGanado);
	}
	
	public void onBackPressed(){
		if (isOnline()){
			finish();
		}
	}
	
	protected  void onDestroy(){
		super.onStart();
		
		resetCalculadora();
	}
	
	private void resetCalculadora(){
		Calculadora.ganadoId = 0;
		Calculadora.diio = 0;
		Calculadora.predio = 0;
		Calculadora.activa = "";
		Calculadora.sexo = "";
		Calculadora.tipoGanado = 0;
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
	
	private void checkDiioStatus(int diio, int ganadoId, String activa, int predio, int tipoGanado){
		if (activa.equals("N")){
			ShowAlert.showAlert("Error", "DIIO se encuentra dado de baja", this);
			return;
		}
		
		if (diio != 0 && (diio == this.diio)){
			return;
		}
		
		//Verificar que el animal no exista en la lista
		for (Ganado g : TrasladosConfirmacion.confirmacion.getGanado()){
			if (g.getId().intValue() == ganadoId){
				Toast.makeText(this, "Animal ya existe", Toast.LENGTH_LONG).show();
				return;
			}
		}
		
		//Ni en la lista de reubicacion
		for (Ganado g : reubicacion.getGanado()){
			if (g.getId().intValue() == ganadoId){
				Toast.makeText(this, "Animal ya existe", Toast.LENGTH_LONG).show();
				return;
			}
		}
		
		//Verificar que el animal corresponde al traslado
		boolean perteneceAlTraslado = false;
		for (Ganado g : TrasladosConfirmacion.salida.getGanado()){
			if (g.getId().intValue() == ganadoId){
				perteneceAlTraslado = true;
			}
		}
		
		isReubicacion = false;
		if (diio != 0 && perteneceAlTraslado == false){
			isReubicacion = true;
			ShowAlert.showAlert("Aviso", "Animal no corresponde a éste Traslado", this);
		}
		
		this.ganadoId = ganadoId;
		this.diio = diio;
		this.predio = predio;
		this.tipoGanado = tipoGanado;
		
		updateStatus();
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
    			if (isOnline() == false){
    				return;
    			}
    			String EID = (String) msg.obj;
    			try {
					List<Ganado> list = WSGanadoCliente.traeGanadoBaston(EID);
					if (list.size() == 0){
						ShowAlert.showAlert("Error", "DIIO no existe", TrasladosConfirmacionDiios.this);
						return;
					}
					for (Ganado g: list){
						checkDiioStatus(g.getDiio(), g.getId(), g.getActiva(), g.getPredio(), g.getTipoGanadoId());
					}
				} catch (AppException ex) {
					ShowAlert.showAlert("Error", ex.getMessage(), TrasladosConfirmacionDiios.this);
				}
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", TrasladosConfirmacionDiios.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };

}
