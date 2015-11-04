package cl.a2r.animales;

import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.custom.AppLauncher;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.login.R;
import cl.a2r.sip.model.Baja;
import cl.a2r.sip.model.CausaBaja;
import cl.a2r.sip.model.MotivoBaja;
import cl.a2r.sip.wsservice.WSBajasCliente;
import cl.a2r.wssipmodel.BajaGanadoWS;
import cl.a2r.wssipmodel.GetBajaWS;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class BajaGanadoEdicion extends Activity implements View.OnClickListener {

	private TextView despliegaDiio, app, deshacer;
	private Spinner motivoSpinner, causaSpinner;
	private ImageButton goBack, confirmarBaja, undo;
	private boolean isEditing;
	private List<MotivoBaja> listaMotivos;
	private List<CausaBaja> listaCausas;
	
	public static Baja bajaEdicionWS = new Baja();
	private Handler mHandler = new Handler();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_baja_ganado_edicion);
		
		bajaEdicionWS.setUserId(Login.user);
		bajaEdicionWS.setPredioId(Aplicaciones.predioWS.getId());
		
		cargarInterfaz();
		cargarListeners();
		getMotivosBajaWS();
		getCausasBajaWS();
		getBajaWS();
	}

	private void cargarInterfaz(){
		despliegaDiio = (TextView)findViewById(R.id.despliegaDiio);
		//despliegaDiio.setOnClickListener(this);
		motivoSpinner = (Spinner)findViewById(R.id.motivoBaja);
		causaSpinner = (Spinner)findViewById(R.id.causaBaja);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		confirmarBaja = (ImageButton)findViewById(R.id.confirmarBaja);
		confirmarBaja.setOnClickListener(this);
		deshacer = (TextView)findViewById(R.id.deshacer);
		deshacer.setOnClickListener(this);
		app = (TextView)findViewById(R.id.app);
		undo = (ImageButton)findViewById(R.id.undo);
		undo.setOnClickListener(this);

		motivoSpinner.setEnabled(false);
		causaSpinner.setEnabled(false);
		isEditing = false;
		
	}
	
	@SuppressWarnings("unchecked")
	private void getMotivosBajaWS(){
        try {
        	listaMotivos = WSBajasCliente.traeMotivos();
        } catch (AppException ex) {
            ShowAlert.showAlert("Error", ex.getMessage(), this);
        }
		
		ArrayAdapter<MotivoBaja> mAdapter = new ArrayAdapter<MotivoBaja>(this, android.R.layout.simple_list_item_1, listaMotivos);
		motivoSpinner.setAdapter(mAdapter);
	}
	
	@SuppressWarnings("unchecked")
	private void getCausasBajaWS(){
		
        try {
        	listaCausas = WSBajasCliente.traeCausas();
        } catch (AppException ex) {
            ShowAlert.showAlert("Error", ex.getMessage(), this);
        }
		
		ArrayAdapter<CausaBaja> mAdapter2 = new ArrayAdapter<CausaBaja>(this, android.R.layout.simple_list_item_1, listaCausas);
		causaSpinner.setAdapter(mAdapter2);
		
	}
	
	private void getBajaWS(){
		//entrega el diio y éste entrega motivo y causa.
		//.getBaja(bajaEdicionWS.getDiio())
		GetBajaWS gb1 = new GetBajaWS();
		gb1.setMotivo(2);
		gb1.setCausa(3);
		
		List<GetBajaWS> list = new ArrayList<GetBajaWS>();
		list.add(gb1);
		
		despliegaDiio.setText(Integer.toString(Logs.diio));
		for (int i = 0; i < listaMotivos.size(); i++){
			if (list.get(0).getMotivo() == listaMotivos.get(i).getId()){
				motivoSpinner.setSelection(i);
			}
		}
		for (int i = 0; i < listaCausas.size(); i++){
			if (list.get(0).getCausa() == listaCausas.get(i).getId()){
				causaSpinner.setSelection(i);
			}
		}
		
	}
	
	public void onClick(View v) {
		if (isOnline() == false){
			return;
		}
		int id = v.getId();
		switch (id){
		case R.id.goBack:
			finish();
			break;
		case R.id.confirmarBaja:
			if (isEditing){
				System.out.println("User: " + bajaEdicionWS.getUserId());
				System.out.println("Predio: " + bajaEdicionWS.getPredioId());
				System.out.println("Diio: " + Logs.diio);
				System.out.println("Sr_Ganado: " + bajaEdicionWS.getGanadoId());
				System.out.println("Motivo Baja: " + bajaEdicionWS.getMotivoId());
				System.out.println("Causa Baja: " + bajaEdicionWS.getCausaId());
				//Intent i = new Intent(this, Aplicaciones.class);
				//i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				//startActivity(i);
				finish();
				Toast.makeText(getApplicationContext(), "Cambio guardado exitosamente", Toast.LENGTH_LONG).show();
			}else{
				if (AppLauncher.getHasLogAccess()){
					isEditing = true;
					updateStatus();
				}
			}
			break;
		case R.id.undo:
			isEditing = false;
			getBajaWS();
			updateStatus();
			break;
		case R.id.deshacer:
			isEditing = false;
			getBajaWS();
			updateStatus();
			break;
		}
	}
	
	private void cargarListeners(){
		motivoSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				bajaEdicionWS.setMotivoId(((MotivoBaja) arg0.getSelectedItem()).getId());
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
		
		causaSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				bajaEdicionWS.setCausaId(((CausaBaja) arg0.getSelectedItem()).getId());
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
	}
	
	private void updateStatus(){
		if (isEditing){
			confirmarBaja.setImageResource(R.drawable.ic_action_done);
			confirmarBaja.setBackgroundResource(R.drawable.circlebutton_green);
			app.setVisibility(View.INVISIBLE);
			goBack.setVisibility(View.INVISIBLE);
			undo.setVisibility(View.VISIBLE);
			deshacer.setVisibility(View.VISIBLE);
			motivoSpinner.setEnabled(true);
			causaSpinner.setEnabled(true);
		}else{
			confirmarBaja.setImageResource(R.drawable.ic_create_white_36dp);
			confirmarBaja.setBackgroundResource(R.drawable.circlebutton_rojo);
			app.setVisibility(View.VISIBLE);
			goBack.setVisibility(View.VISIBLE);
			undo.setVisibility(View.INVISIBLE);
			deshacer.setVisibility(View.INVISIBLE);
			motivoSpinner.setEnabled(false);
			causaSpinner.setEnabled(false);
		}
	}
	
	protected  void onStart(){
		super.onStart();
		ConnectThread.setHandler(mHandler);
		
		if (isOnline() == false){
			return;
		}
		
		bajaEdicionWS.setGanadoId(Logs.sr_ganado);
		updateStatus();
	}
	
	protected  void onDestroy(){
		super.onStart();
		
		bajaEdicionWS.setUserId(0);
		bajaEdicionWS.setPredioId(0);
		bajaEdicionWS.setGanadoId(0);
		bajaEdicionWS.setMotivoId(0);
		bajaEdicionWS.setCausaId(0);
		
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
