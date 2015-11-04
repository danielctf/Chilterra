package cl.a2r.animales;


import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ShowAlert;
import cl.a2r.login.R;
import cl.a2r.sip.model.CausaBaja;
import cl.a2r.sip.model.MotivoBaja;
import cl.a2r.sip.model.RegistroParto;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Partos extends Activity implements View.OnClickListener{
	
	private Fragment registroTab = new PartosRegistro();
	private Fragment confirmacionTab = new PartosConfirmacion();
	
	private static ImageButton undo, goBack, logs;
	private static TextView despliegaDiio, textViewDiio, tvApp, deshacer;
	private Button btnRegistro, btnConfirmacion;
	private RelativeLayout calculadora;
	private static int diio;
	
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
			btnRegistro.setBackgroundResource(R.drawable.tab_state_activated);
			btnConfirmacion.setBackgroundResource(R.drawable.tab_state_deactivated);
			clearScreen();
			break;
		case R.id.btnConfirmacion:
			FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
			transaction2.replace(R.id.fragmentContainer, confirmacionTab);
			transaction2.addToBackStack(null);
			transaction2.commit();
			btnRegistro.setBackgroundResource(R.drawable.tab_state_deactivated);
			btnConfirmacion.setBackgroundResource(R.drawable.tab_state_activated);
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
		}
		
	}
	
	private void clearScreen(){
		Calculadora.ganadoId = 0;
		Calculadora.diio = 0;
		PartosRegistro.registroWS.setGanadoId(0);
		PartosRegistro.registroWS.setTipoPartoId(0);
		PartosRegistro.registroWS.setSubTipoParto(0);
		PartosRegistro.registroWS.setSexo("");
		PartosRegistro.registroWS.setCollarId(0);
		PartosRegistro.spinnerTipoParto.setSelection(0);
		PartosRegistro.spinnerSubTipoParto.setSelection(0);
		PartosRegistro.spinnerSexo.setSelection(0);
		PartosRegistro.spinnerCollar.setSelection(0);
		updateStatus();
	}
	
	public static void updateStatus(){
		//Si escribió un DIIO, el texto 'DIIO:' desaparece
		if (PartosRegistro.registroWS.getGanadoId() != 0){
			textViewDiio.setText("");
			despliegaDiio.setText(Integer.toString(diio));
		}else{
			textViewDiio.setText("DIIO:");
			despliegaDiio.setText("");
		}
		
		//Si llenó todos los campos, el boton de confirmacion aparece
		if (PartosRegistro.registroWS.getGanadoId() != 0 && 
				PartosRegistro.registroWS.getTipoPartoId() != 0 && 
				(PartosRegistro.registroWS.getSexo() != "" || PartosRegistro.isMuerto) &&
				(PartosRegistro.registroWS.getCollarId() != 0 || PartosRegistro.isMuerto) &&
				PartosRegistro.registroWS.getSubTipoParto() != 0){
			
			goBack.setVisibility(View.INVISIBLE);
			tvApp.setVisibility(View.INVISIBLE);
			undo.setVisibility(View.VISIBLE);
			deshacer.setVisibility(View.VISIBLE);
			logs.setVisibility(View.INVISIBLE);
			PartosRegistro.confirmarRegistro.setVisibility(View.VISIBLE);
		}else{
			if (PartosRegistro.registroWS.getGanadoId() == 0 && 
					PartosRegistro.registroWS.getTipoPartoId() == 0 && 
					(PartosRegistro.registroWS.getSexo() == "" || PartosRegistro.isMuerto) &&
					(PartosRegistro.registroWS.getCollarId() == 0 || PartosRegistro.isMuerto) &&
					PartosRegistro.registroWS.getSubTipoParto() == 0){
				
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
	
	private void checkDiioStatus(int diio, int ganadoId, String activa, int predio){
		Partos.diio = diio;
		
		if (activa.equals("N")){
			ShowAlert.showAlert("Error", "DIIO no existe", this);
			return;
		}
		
		PartosRegistro.registroWS.setGanadoId(ganadoId);
		updateStatus();
	}
	
	protected  void onStart(){
		super.onStart();
	
		if (isOnline() == false){
			return;
		}
		
		checkDiioStatus(Calculadora.diio, Calculadora.ganadoId, Calculadora.activa, Calculadora.predio);
	}
	
	protected  void onDestroy(){
		super.onStart();
		
		Calculadora.ganadoId = 0;
		Calculadora.diio = 0;
		Calculadora.predio = 0;
		Calculadora.activa = "";
		
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

}
