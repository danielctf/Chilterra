package cl.a2r.animales;


import java.util.List;

import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Parto;
import cl.a2r.sip.model.Traslado;
import cl.a2r.sip.wsservice.WSGanadoCliente;
import cl.a2r.sip.wsservice.WSPartosCliente;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Partos extends Activity implements View.OnClickListener, DialogInterface.OnClickListener{
	
	private Fragment registroTab = new PartosRegistro();
	private Fragment confirmacionTab = new PartosConfirmacion();
	
	private static ImageButton undo, goBack, logs, cows_left;
	private static TextView despliegaDiio, textViewDiio, tvApp, deshacer;
	public static TextView tvFaltantes, tvEncontrados;
	private Button btnRegistro, btnConfirmacion;
	private RelativeLayout calculadora;
	private static int diio, tempDiio, tempGanadoId, tempPredio;
	private String stance, tempSexo;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_partos);
		
		cargarInterfaz();
	}
	
	private void cargarInterfaz(){
		despliegaDiio = (TextView)findViewById(R.id.despliegaDiio);
		despliegaDiio.setOnClickListener(this);
		textViewDiio = (TextView)findViewById(R.id.textViewDiio);
		textViewDiio.setOnClickListener(this);
		tvApp = (TextView)findViewById(R.id.app);
		calculadora = (RelativeLayout)findViewById(R.id.layoutCalculadora);
		calculadora.setOnClickListener(this);
		btnRegistro = (Button)findViewById(R.id.btnRegistro);
		btnRegistro.setOnClickListener(this);
		btnConfirmacion = (Button)findViewById(R.id.btnConfirmacion);
		btnConfirmacion.setOnClickListener(this);
		undo = (ImageButton)findViewById(R.id.undo);
		undo.setOnClickListener(this);
		deshacer = (TextView)findViewById(R.id.deshacer);
		deshacer.setOnClickListener(this);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		logs = (ImageButton)findViewById(R.id.logs);
		logs.setOnClickListener(this);
		cows_left = (ImageButton)findViewById(R.id.cows_left);
		cows_left.setOnClickListener(this);
		tvFaltantes = (TextView)findViewById(R.id.textViewFaltantes);
		tvEncontrados = (TextView)findViewById(R.id.textViewEncontrados);
		
		stance = "registro";
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.fragmentContainer, registroTab);
		btnRegistro.setBackgroundResource(R.drawable.tab_state_activated);
		btnConfirmacion.setBackgroundResource(R.drawable.tab_state_deactivated);
		transaction.addToBackStack(null);
		transaction.commit();
		
	}
	
	public void onClick(View v) {
		if (isOnline() == false){
			return;
		}
		int id = v.getId();
		switch (id){
		case R.id.btnRegistro:
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.fragmentContainer, registroTab);
			transaction.addToBackStack(null);
			transaction.commit();
			getFragmentManager().executePendingTransactions();
			btnRegistro.setBackgroundResource(R.drawable.tab_state_activated);
			btnConfirmacion.setBackgroundResource(R.drawable.tab_state_deactivated);
			stance = "registro";
			clearScreen();
			break;
		case R.id.btnConfirmacion:
			FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
			transaction2.replace(R.id.fragmentContainer, confirmacionTab);
			transaction2.addToBackStack(null);
			transaction2.commit();
			getFragmentManager().executePendingTransactions();
			btnRegistro.setBackgroundResource(R.drawable.tab_state_deactivated);
			btnConfirmacion.setBackgroundResource(R.drawable.tab_state_activated);
			stance = "confirmacion";
			clearScreen();
			break;
		case R.id.layoutCalculadora:
		case R.id.textViewDiio:
		case R.id.despliegaDiio:
			Intent i3 = new Intent(this, Calculadora.class);
			startActivity(i3);
			break;
		case R.id.goBack:
			finish();
			break;
		case R.id.confirmarRegistro:
			break;
		case R.id.undo:
		case R.id.deshacer:
			clearScreen();
			break;
		case R.id.logs:
			if (stance.equals("registro")){
				Intent i4 = new Intent(this, Logs.class);
				startActivity(i4);
			} else if (stance.equals("confirmacion")){
				getCandidatosEncontrados();
			}
			break;
		case R.id.cows_left:
			getCandidatosFaltantes();
			break;
		}
	}
	
	@SuppressWarnings("static-access")
	public void onClick(DialogInterface dialog, int which) {
		if (which == -2){
			this.diio = tempDiio;
			
			Traslado t = new Traslado();
			t.setUsuarioId(Login.user);
			t.setFundoOrigenId(tempPredio);
			t.setFundoDestinoId(Aplicaciones.predioWS.getId());
			Ganado g = new Ganado();
			g.setId(tempGanadoId);
			t.getGanado().add(g);
			
			try {
				WSGanadoCliente.reajustaGanado(t);
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this);
			}
			
			verParto(tempGanadoId, tempSexo);
		} else {
			Calculadora.ganadoId = 0;
			Calculadora.diio = 0;
			Calculadora.predio = 0;
			Calculadora.activa = "";
			Calculadora.sexo = "";
		}
	}
	
	private void getCandidatosEncontrados(){
		Intent i = new Intent(getApplicationContext(), Candidatos.class);
		i.putExtra("stance","partosEncontrados");
		startActivity(i);
	}
	
	private void getCandidatosFaltantes(){
		Intent i = new Intent(getApplicationContext(), Candidatos.class);
		i.putExtra("stance","partosFaltantes");
		startActivity(i);
	}
	
	private void clearScreen(){
		Calculadora.ganadoId = 0;
		Calculadora.diio = 0;
		Calculadora.predio = 0;
		Calculadora.activa = "";
		Calculadora.sexo = "";
		diio = 0;
		PartosConfirmacion.ganadoId = 0;
		PartosRegistro.partoWS.setGanadoId(0);
		PartosRegistro.partoWS.setTipoPartoId(0);
		PartosRegistro.partoWS.setSubTipoParto(0);
		PartosRegistro.partoWS.setSexo("");
		PartosRegistro.partoWS.setCollarId(0);
		PartosRegistro.spinnerTipoParto.setSelection(0);
		PartosRegistro.spinnerSubTipoParto.setSelection(0);
		PartosRegistro.spinnerSexo.setSelection(0);
		PartosRegistro.spinnerCollar.setSelection(0);
		
		if (stance.equals("registro")){
			updateRegistro();
		}else if (stance.equals("confirmacion")){
			updateConfirmacion();
		}
	}
	
	public static void updateRegistro(){
		cows_left.setVisibility(View.INVISIBLE);
		tvFaltantes.setVisibility(View.INVISIBLE);
		tvEncontrados.setVisibility(View.INVISIBLE);
		//Si escribió un DIIO, el texto 'DIIO:' desaparece
		if (PartosRegistro.partoWS.getGanadoId() != 0){
			textViewDiio.setText("");
			despliegaDiio.setText(Integer.toString(diio));
		}else{
			textViewDiio.setText("DIIO:");
			despliegaDiio.setText("");
		}
		
		//Si llenó todos los campos, el boton de confirmacion aparece
		if (PartosRegistro.partoWS.getGanadoId() != 0 && 
				PartosRegistro.partoWS.getTipoPartoId() != 0 && 
				(PartosRegistro.partoWS.getSexo() != "" || PartosRegistro.isMuerto) &&
				(PartosRegistro.partoWS.getCollarId() != 0 || PartosRegistro.isMuerto) &&
				(PartosRegistro.partoWS.getSubTipoParto() != 0 || PartosRegistro.isNotPartoNatural)){
			
			goBack.setVisibility(View.INVISIBLE);
			tvApp.setVisibility(View.INVISIBLE);
			undo.setVisibility(View.VISIBLE);
			deshacer.setVisibility(View.VISIBLE);
			logs.setVisibility(View.INVISIBLE);
			PartosRegistro.confirmarRegistro.setVisibility(View.VISIBLE);
		}else{
			if (PartosRegistro.partoWS.getGanadoId() == 0 && 
					PartosRegistro.partoWS.getTipoPartoId() == 0 && 
					(PartosRegistro.partoWS.getSexo() == "" || PartosRegistro.isMuerto) &&
					(PartosRegistro.partoWS.getCollarId() == 0 || PartosRegistro.isMuerto) &&
					(PartosRegistro.partoWS.getSubTipoParto() == 0 || PartosRegistro.isNotPartoNatural)){
				
				goBack.setVisibility(View.VISIBLE);
				tvApp.setVisibility(View.VISIBLE);
				undo.setVisibility(View.INVISIBLE);
				deshacer.setVisibility(View.INVISIBLE);
				logs.setVisibility(View.VISIBLE);
				PartosRegistro.confirmarRegistro.setVisibility(View.INVISIBLE);
			}
			else{
				goBack.setVisibility(View.INVISIBLE);
				tvApp.setVisibility(View.INVISIBLE);
				undo.setVisibility(View.VISIBLE);
				deshacer.setVisibility(View.VISIBLE);
				logs.setVisibility(View.INVISIBLE);
				PartosRegistro.confirmarRegistro.setVisibility(View.INVISIBLE);
			}
		}
			
	}
	
	public static void updateConfirmacion(){
		cows_left.setVisibility(View.VISIBLE);
		tvFaltantes.setVisibility(View.VISIBLE);
		tvEncontrados.setVisibility(View.VISIBLE);
		
		if (PartosConfirmacion.ganadoId == 0){
			goBack.setVisibility(View.VISIBLE);
			logs.setVisibility(View.VISIBLE);
			tvApp.setVisibility(View.VISIBLE);
			undo.setVisibility(View.INVISIBLE);
			deshacer.setVisibility(View.INVISIBLE);
			PartosConfirmacion.confirmarConfirmacion.setEnabled(false);
			textViewDiio.setText("DIIO:");
			despliegaDiio.setText("");
		}else{
			goBack.setVisibility(View.INVISIBLE);
			tvApp.setVisibility(View.INVISIBLE);
			//logs.setVisibility(View.INVISIBLE);
			undo.setVisibility(View.VISIBLE);
			deshacer.setVisibility(View.VISIBLE);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void verParto(int ganadoId, String sexo){
		if (!sexo.equals("H") && !sexo.equals("")){
			ShowAlert.showAlert("Aviso", "AVISO: El animal figura con sexo distinto a Hembra", Partos.this);
		}
		
		if (stance.equals("registro")){
			PartosRegistro.partoWS.setGanadoId(ganadoId);
			try {
				List<Parto> list = WSPartosCliente.traePartoAnterior(ganadoId);
				if (list.size() > 0){
					ShowAlert.showAlert("Registro", "AVISO\nEste Animal ya tiene una cria asociada", this);
				}
				updateRegistro();
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this);
				return;
			}
		} else if (stance.equals("confirmacion")){
			try {
				List<Parto> list = WSPartosCliente.traePartoPorConfirmar(ganadoId);
				boolean confirmacionPendiente = false;
				for (Parto p : list){
					if (p.getEstadoParto().equals("R")){
						confirmacionPendiente = true;
					}
				}
				if (confirmacionPendiente){
					PartosConfirmacion.ganadoId = ganadoId;
					textViewDiio.setText("");
					despliegaDiio.setText(Integer.toString(diio));
					PartosConfirmacion.confirmarConfirmacion.setEnabled(true);
				} else if (ganadoId != 0){ 
					ShowAlert.showAlert("Animal", "Este Animal no tiene una confirmación de parto pendiente", this);
					return;
				}
				updateConfirmacion();
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this);
				return;
			}
		}
	}
	
	@SuppressWarnings("static-access")
	private void checkDiioStatus(int diio, int ganadoId, String activa, int predio, String sexo){
		if (activa.equals("N")){
			ShowAlert.showAlert("Error", "DIIO se encuentra dado de baja", this);
			return;
		}
		
		if (diio != 0 && (diio == this.diio)){
			return;
		}
		
		if (ganadoId != 0){
			if (Aplicaciones.predioWS.getId() != predio){
				tempGanadoId = ganadoId;
				tempDiio = diio;
				tempSexo = sexo;
				tempPredio = predio;
				ShowAlert.askYesNo("Predio", "El Animal figura en otro predio\n¿Esta seguro que el DIIO es correcto?", this, this);
				return;
			}
		}
		
		this.diio = diio;
		verParto(ganadoId, sexo);
		
	}
	
	protected  void onStart(){
		super.onStart();
		
		if (Login.user == 0){
			finish();
		}
		
		ConnectThread.setHandler(mHandler);
		
		if (isOnline() == false){
			return;
		}
		
		checkDiioStatus(Calculadora.diio, Calculadora.ganadoId, Calculadora.activa, Calculadora.predio, Calculadora.sexo);
		
	}
	
	protected  void onDestroy(){
		super.onStart();
		
		Calculadora.ganadoId = 0;
		Calculadora.diio = 0;
		Calculadora.predio = 0;
		Calculadora.activa = "";
		Calculadora.sexo = "";
		Calculadora.tipoGanado = 0;
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
    			if (isOnline() == false){
    				return;
    			}
    			String EID = (String) msg.obj;
    			try {
					List<Ganado> list = WSGanadoCliente.traeGanadoBaston(EID);
					if (list.size() == 0){
						ShowAlert.showAlert("Error", "DIIO no existe", Partos.this);
						return;
					}
					for (Ganado g: list){
						checkDiioStatus(g.getDiio(), g.getId(), g.getActiva(), g.getPredio(), g.getSexo());
					}
				} catch (AppException ex) {
					ShowAlert.showAlert("Error", ex.getMessage(), Partos.this);
				}
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", Partos.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };

}
