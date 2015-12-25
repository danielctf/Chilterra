package cl.a2r.alimentacion;

import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sap.model.Medicion;
import cl.a2r.sap.wsservice.WSMedicionCliente;
import cl.ar2.sqlite.cobertura.MedicionServicio;
import cl.ar2.sqlite.cobertura.RegistroMedicion;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Mediciones extends Activity implements View.OnClickListener{

	private TextView tvSync;
	private Button btnResiduo, btnEntrada, btnSemanal, btnControl;
	private ImageButton goBack, sync;
	public static int tipoMuestraActual;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_medicion);
		
		cargarInterfaz();
	}
	
	private void cargarInterfaz(){
		btnResiduo = (Button)findViewById(R.id.btnResiduo);
		btnResiduo.setOnClickListener(this);
		btnEntrada = (Button)findViewById(R.id.btnEntrada);
		btnEntrada.setOnClickListener(this);
		btnSemanal = (Button)findViewById(R.id.btnSemanal);
		btnSemanal.setOnClickListener(this);
		btnControl = (Button)findViewById(R.id.btnControl);
		btnControl.setOnClickListener(this);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		sync = (ImageButton)findViewById(R.id.sync);
		sync.setOnClickListener(this);
		tvSync = (TextView)findViewById(R.id.tvSync);
		tvSync.setOnClickListener(this);
	}

	public void onClick(View v) {
		int id = v.getId();
		Intent i;
		switch (id){
		case R.id.goBack:
			finish();
			break;
		case R.id.btnResiduo:
			i = new Intent(this, MedicionResiduo.class);
			startActivity(i);
			break;
		case R.id.btnEntrada:
			i = new Intent(this, MedicionEntrada.class);
			startActivity(i);
			break;
		case R.id.btnSemanal:
			i = new Intent(this, MedicionSemanal.class);
			startActivity(i);
			break;
		case R.id.btnControl:
			i = new Intent(this, MedicionControl.class);
			startActivity(i);
			break;
		case R.id.tvSync:
		case R.id.sync:
			sincronizar();
			break;
		}
	}
	
    Handler hand = new Handler();
    Runnable run = new Runnable() {
        public void run() { 
    		try {
    			List<RegistroMedicion> list = MedicionServicio.traeMediciones();
    			if (list.size() == 0){
    				ShowAlert.showAlert("Sincronización", "Sincronización Completa", Mediciones.this);
    				return;
    			}
    			for (RegistroMedicion rm : list){
    				WSMedicionCliente.insertaMedicion(rm.getMedicion());
    				MedicionServicio.deleteMedicion(rm.getId());
    			}
    			ShowAlert.showAlert("Sincronización", "Sincronización Completa", Mediciones.this);
    		} catch (AppException e) {
    			ShowAlert.showAlert("Error", e.getMessage(), Mediciones.this);
    		} finally {
	    		tvSync.setVisibility(View.VISIBLE);
	    		sync.setVisibility(View.VISIBLE);
	    		updateStatus();
    		}
        }
    }; 
	
	private void sincronizar(){
		tvSync.setVisibility(View.INVISIBLE);
		sync.setVisibility(View.INVISIBLE);
		hand.postDelayed(run, 100);
	}
	
	private void updateStatus(){
		try {
			List<Medicion> list = MedicionServicio.traeMediciones();
			if (list.size() > 0){
				tvSync.setText(Integer.toString(list.size()));
			} else {
				tvSync.setText("");
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	protected void onStart(){
		super.onStart();
		
		updateStatus();
	}

}
