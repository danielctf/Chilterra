package cl.a2r.traslados;

import java.util.List;

import cl.a2r.animales.Login;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Instancia;
import cl.a2r.sip.model.TrasladoV2;
import cl.a2r.sip.wsservice.WSTrasladosCliente;
import cl.ar2.sqlite.servicio.TrasladosServicio;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

public class TrasladosV2 extends Activity implements View.OnClickListener{

	private Fragment frEncabezado, frAnimales;
	private Button btnEncabezado, btnAnimales;
	private ImageButton confirmarMovimiento, goBack;
	private ProgressBar loading;
	private Integer superInstanciaId;
	
	public static Instancia superInstancia;
	public static TrasladoV2 traslado;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_traslado_salida_v2);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			superInstanciaId = extras.getInt("superInstanciaId");
		}
		
		cargarInterfaz();
	}

	private void cargarInterfaz(){
		frEncabezado = new Encabezado(this);
		frAnimales = new Animales(this);
		
		confirmarMovimiento = (ImageButton)findViewById(R.id.confirmarMovimiento);
		confirmarMovimiento.setOnClickListener(this);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		btnEncabezado = (Button)findViewById(R.id.btnEncabezado);
		btnEncabezado.setOnClickListener(this);
		btnAnimales = (Button)findViewById(R.id.btnAnimales);
		btnAnimales.setOnClickListener(this);
		loading = (ProgressBar)findViewById(R.id.loading);
		loading.setVisibility(View.INVISIBLE);
		
		traslado = new TrasladoV2();
		superInstancia = new Instancia();
		superInstancia.setId(superInstanciaId);
		Instancia instancia = new Instancia();
		superInstancia.setInstancia(instancia);
		instancia.setTraslado(traslado);
		
		btnEncabezado.performClick();
	}

	public void onClick(View v) {
		int id = v.getId();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		switch (id){
		case R.id.goBack:
			finish();
			break;
		case R.id.btnEncabezado:
			transaction.replace(R.id.container, frEncabezado);
			btnEncabezado.setBackgroundResource(R.drawable.tab_state_activated);
			btnAnimales.setBackgroundResource(R.drawable.tab_state_deactivated);
			transaction.commit();
			getFragmentManager().executePendingTransactions();
			break;
		case R.id.btnAnimales:
			transaction.replace(R.id.container, frAnimales);
			btnEncabezado.setBackgroundResource(R.drawable.tab_state_deactivated);
			btnAnimales.setBackgroundResource(R.drawable.tab_state_activated);
			transaction.commit();
			getFragmentManager().executePendingTransactions();
			break;
		case R.id.confirmarMovimiento:
			List<Ganado> list = null;
			try {
				list = TrasladosServicio.traeGanadoTraslado();
			} catch (AppException e) {}
			if (list.size() == 0){
				ShowAlert.showAlert("Error", "No ha ingresado ningún Animal", this);
				return;
			}
			
			if (Encabezado.hayTransportista){
				if (traslado.getDestino().getId() == null || traslado.getTransportistaId() == null || traslado.getChoferId() == null || traslado.getCamionId() == null){
					ShowAlert.showAlert("Error", "Debe completar el encabezado del traslado", this);
					return;
				}
			} else {
				if (traslado.getDestino().getId() == null){
					ShowAlert.showAlert("Error", "Debe completar el encabezado del traslado", this);
					return;
				} else {
					traslado.setTransportistaId(null);
					traslado.setChoferId(null);
					traslado.setCamionId(null);
					traslado.setAcopladoId(null);
				}
			}
			
			ShowAlert.askYesNo("Completar Traslado", "¿Seguro que desea completar el traslado?", this, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if (which == -2){
						confirmarMovimiento();
					}
				}
			});
			break;
		}
	}
	
	private void confirmarMovimiento(){
		new AsyncTask<Void, Void, Void>(){
			
			String title, msg;
			
			protected void onPreExecute(){
				loading.setVisibility(View.VISIBLE);
				confirmarMovimiento.setVisibility(View.INVISIBLE);
				title = "";
				msg = "";
			}
			
			protected Void doInBackground(Void... arg0) {
				try {
					List<Ganado> list = TrasladosServicio.traeGanadoTraslado();
					superInstancia.getInstancia().setGanList(list);
					superInstancia.setUsuarioId(Login.user);
					superInstancia.getInstancia().getTraslado().setDescription("TRASLADO");
					
					WSTrasladosCliente.insertaTraslado(superInstancia);
					
					TrasladosServicio.deleteGanado();
				} catch (AppException e) {
					title = "Error";
					msg = e.getMessage();
				}
				return null;
			}
			
			protected void onPostExecute(Void result){
				loading.setVisibility(View.INVISIBLE);
				confirmarMovimiento.setVisibility(View.VISIBLE);
				if (!title.equals("Error")){
					Toast.makeText(TrasladosV2.this, "Traslado Generado", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    finish();
				} else {
					ShowAlert.showAlert(title, msg, TrasladosV2.this);
				}
			}
			
		}.execute();
	}
	
}
