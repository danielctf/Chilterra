package cl.a2r.traslados;

import java.util.List;

import cl.a2r.animales.Aplicaciones;
import cl.a2r.animales.Login;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Camion;
import cl.a2r.sip.model.Chofer;
import cl.a2r.sip.model.Persona;
import cl.a2r.sip.model.Predio;
import cl.a2r.sip.model.Instancia;
import cl.a2r.sip.model.Transportista;
import cl.a2r.sip.wsservice.WSAutorizacionCliente;
import cl.a2r.sip.wsservice.WSInstanciasCliente;
import cl.a2r.sip.wsservice.WSTrasladosCliente;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

public class Menu extends Activity implements View.OnClickListener, ListView.OnItemClickListener{

	private ImageButton goBack, addTraslado;
	private ListView lvTraslados;
	private ProgressBar loading;
	
	public static List<Chofer> chofer = null;
	public static List<Camion> camion = null;
	public static List<Camion> acoplado = null;
	public static List<Predio> predios = null;
	public static List<Transportista> transportistas = null;
	public static List<Persona> arrieros = null;
	private static boolean updated = false;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_traslados_v2);
		
		cargarInterfaz();
		traeDatosWS();
	}

	private void cargarInterfaz(){
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		addTraslado = (ImageButton)findViewById(R.id.addTraslado);
		addTraslado.setOnClickListener(this);
		lvTraslados = (ListView)findViewById(R.id.lvTraslados);
		lvTraslados.setOnItemClickListener(this);
		loading = (ProgressBar)findViewById(R.id.loading);
	}
	
	private void traeDatosWS(){
		new AsyncTask<Void, Void, Void>(){
			
			String title, msg;
			
			protected void onPreExecute(){
				loading.setVisibility(View.VISIBLE);
				title = "";
				msg = "";
			}
			
			protected Void doInBackground(Void... arg0) {
//				try {
//					
//				} catch (AppException e) {
//					title = "Error";
//					msg = e.getMessage();
//				}
				return null;
			}
			
			protected void onPostExecute(Void result){
				loading.setVisibility(View.INVISIBLE);
				if (!title.equals("Error")){

				} else {
					ShowAlert.showAlert(title, msg, Menu.this);
				}
			}
			
		}.execute();
	}
	
	private void traeDatosTraslado(){
		Intent i = new Intent(this, TrasladosV2.class);
		if (updated){
			
		}
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id){
		case R.id.goBack:
			finish();
			break;
		case R.id.addTraslado:
			Instancia instancia = new Instancia();
			instancia.setFundoId(Aplicaciones.predioWS.getId());
			instancia.setUsuarioId(Login.user);
			try {
				WSInstanciasCliente.insertaInstancia(instancia, Aplicaciones.appId);
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this);
			}
			break;

		}
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int id = arg0.getId();
		switch (id){
		case R.id.lvTraslados:
			//ver si esta en bo, co o ep
			
			traeDatosTraslado();
			break;
		}
	}
	
}
