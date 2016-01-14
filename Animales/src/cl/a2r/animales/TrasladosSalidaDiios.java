package cl.a2r.animales;

import java.util.List;

import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.wsservice.WSGanadoCliente;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TrasladosSalidaDiios extends Activity implements View.OnClickListener{

	private TextView despliegaDiio, textViewDiio, tvApp, deshacer;
	private TextView tvTotalAnimales, tvMangada, tvAnimalesMangada;
	private ImageButton goBack, undo, logs, confirmarAnimal, cerrarMangada;
	private Integer ganadoId, diio, predio, tipoGanado;
	private RelativeLayout layoutCalculadora;
	public static int mangadaActual = 0;
	public static boolean isMangadaLocked = true;
	private int inputText;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_traslados_salida_diios);
		
		cargarInterfaz();
		updateStatus();
	}
	
	private void cargarInterfaz(){
		tvTotalAnimales = (TextView)findViewById(R.id.tvTotalAnimales);
		tvMangada = (TextView)findViewById(R.id.tvMangada);
		tvAnimalesMangada = (TextView)findViewById(R.id.tvAnimalesMangada);
		despliegaDiio = (TextView)findViewById(R.id.despliegaDiio);
		despliegaDiio.setOnClickListener(this);
		textViewDiio = (TextView)findViewById(R.id.textViewDiio);
		textViewDiio.setOnClickListener(this);
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
		cerrarMangada = (ImageButton)findViewById(R.id.cerrarMangada);
		cerrarMangada.setOnClickListener(this);
		layoutCalculadora = (RelativeLayout)findViewById(R.id.layoutCalculadora);
		layoutCalculadora.setOnClickListener(this);
		
		ganadoId = 0;
		diio = 0;
		predio = 0;
		tipoGanado = 0;
	}
	
	private void updateStatus(){
		//Ver si hay animales en la mangada para habilitar el boton
		int size = TrasladosSalida.trasladoSalida.getGanado().size();
		if (isMangadaLocked){
			cerrarMangada.setEnabled(false);
		} else {
			cerrarMangada.setEnabled(true);
		}
		
		//Actualizar numeros (total animales, mangada actual, animales en mangada)
		tvTotalAnimales.setText(Integer.toString(size));
		tvMangada.setText(Integer.toString(mangadaActual));
		int animalesMangada = 0;
		for (int i = 0; i < size; i++){
			if (TrasladosSalida.trasladoSalida.getGanado().get(i).getMangada() == mangadaActual){
				animalesMangada++;
			}
		}
		tvAnimalesMangada.setText(Integer.toString(animalesMangada));
		
		//Actualizar interfaz
		if (ganadoId != 0 && diio != 0 && predio != 0 && tipoGanado != 0){
			goBack.setVisibility(View.INVISIBLE);
			tvApp.setVisibility(View.INVISIBLE);
			undo.setVisibility(View.VISIBLE);
			deshacer.setVisibility(View.VISIBLE);
			logs.setVisibility(View.INVISIBLE);
			despliegaDiio.setText(Integer.toString(diio));
			textViewDiio.setText("");
			confirmarAnimal.setEnabled(true);
		} else if (ganadoId == 0 && diio == 0 && predio == 0 && tipoGanado == 0){
			goBack.setVisibility(View.VISIBLE);
			tvApp.setVisibility(View.VISIBLE);
			undo.setVisibility(View.INVISIBLE);
			deshacer.setVisibility(View.INVISIBLE);
			logs.setVisibility(View.VISIBLE);
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
			despliegaDiio.setText("");
			textViewDiio.setText("DIIO:");
			confirmarAnimal.setEnabled(false);
		}

	}
	
	private void clearScreen(){
		ganadoId = 0;
		diio = 0;
		predio = 0;
		tipoGanado = 0;
		resetCalculadora();
	}
	
	private void agregarAnimal(){
		if (isMangadaLocked){
			mangadaActual++;
			isMangadaLocked = false;
		}
		Ganado g = new Ganado();
		g.setId(ganadoId);
		g.setDiio(diio);
		g.setPredio(predio);
		g.setMangada(mangadaActual);
		g.setTipoGanadoId(tipoGanado);
		TrasladosSalida.trasladoSalida.getGanado().add(g);
		Toast.makeText(this, "Animal Insertado", Toast.LENGTH_LONG).show();
		clearScreen();
	}
	
	private void cerrarMangada(){
		isMangadaLocked = true;
		Toast.makeText(this, "Mangada Cerrada", Toast.LENGTH_LONG).show();
		clearScreen();
		updateStatus();
	}
	
	public void onClick(View arg0) {
		if (isOnline() == false){
			return;
		}
		int id = arg0.getId();
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
		case R.id.cerrarMangada:
			verificarCierreMangada();
			break;
		case R.id.textViewDiio:
		case R.id.despliegaDiio:
		case R.id.layoutCalculadora:
			i = new Intent(this, Calculadora.class);
			startActivity(i);
			break;
		case R.id.logs:
			i = new Intent(this, TrasladosLogs.class);
			startActivity(i);
			break;
		}
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
		    	try{
			        inputText = Integer.parseInt(input.getText().toString());
			        int totalMangada = 0;
			        for (Ganado g : TrasladosSalida.trasladoSalida.getGanado()){
			        	if (g.getMangada().intValue() == mangadaActual){
			        		totalMangada++;
			        	}
			        }
			        if (inputText == totalMangada){
			        	cerrarMangada();
			        } else {
			        	ShowAlert.showAlert("Error", "El número de animales ingresado no coincide con la mangada actual", TrasladosSalidaDiios.this);
			        }
		    	} catch (NumberFormatException e){
		    		ShowAlert.showAlert("Error", "Debe ingresar un numero", TrasladosSalidaDiios.this);
		    	}
		    }
		});
	
		builder.show();
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
		for (Ganado g : TrasladosSalida.trasladoSalida.getGanado()){
			if (g.getId().intValue() == ganadoId){
				Toast.makeText(this, "Animal ya existe", Toast.LENGTH_LONG).show();
				return;
			}
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
						ShowAlert.showAlert("Error", "DIIO no existe", TrasladosSalidaDiios.this);
						return;
					}
					for (Ganado g: list){
						checkDiioStatus(g.getDiio(), g.getId(), g.getActiva(), g.getPredio(), g.getTipoGanadoId());
					}
				} catch (AppException ex) {
					ShowAlert.showAlert("Error", ex.getMessage(), TrasladosSalidaDiios.this);
				}
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", TrasladosSalidaDiios.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };

}
