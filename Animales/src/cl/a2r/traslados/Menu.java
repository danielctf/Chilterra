package cl.a2r.traslados;

import java.util.Date;
import java.util.List;

import cl.a2r.animales.Aplicaciones;
import cl.a2r.animales.Login;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.ShowAlert;
import cl.a2r.custom.Utility;
import cl.a2r.sip.model.Camion;
import cl.a2r.sip.model.Chofer;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Persona;
import cl.a2r.sip.model.Predio;
import cl.a2r.sip.model.Instancia;
import cl.a2r.sip.model.Transportista;
import cl.a2r.sip.wsservice.WSAutorizacionCliente;
import cl.a2r.sip.wsservice.WSGanadoCliente;
import cl.a2r.sip.wsservice.WSInstanciasCliente;
import cl.a2r.sip.wsservice.WSTrasladosCliente;
import cl.ar2.sqlite.dao.SqLiteTrx;
import cl.ar2.sqlite.servicio.PredioLibreServicio;
import cl.ar2.sqlite.servicio.TrasladosServicio;
import android.app.Activity;
import android.content.DialogInterface;
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
import android.widget.TextView;

public class Menu extends Activity implements View.OnClickListener, ListView.OnItemClickListener, ListView.OnItemLongClickListener{

	private ImageButton goBack, addTraslado;
	private ListView lvTraslados;
	private ProgressBar loading;
	private TextView tvFundo;
	
	public static List<Chofer> chofer = null;
	public static List<Camion> camion = null;
	public static List<Camion> acoplado = null;
	public static List<Predio> predios = null;
	public static List<Transportista> transportistas = null;
	public static List<Persona> arrieros = null;
	private static boolean isInCache = false;
	private static Date cacheDate = new Date();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_traslados_v2);
		
		SqLiteTrx trx = new SqLiteTrx(false);
		trx.close();
		
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
		lvTraslados.setOnItemLongClickListener(this);
		loading = (ProgressBar)findViewById(R.id.loading);
		tvFundo = (TextView)findViewById(R.id.tvFundo);
		
		tvFundo.setText(Aplicaciones.predioWS.getCodigo());
	}
	
	private void traeDatosWS(){
		new AsyncTask<Void, Void, Void>(){
			
			String title, msg;
			List<Instancia> trasList;
			
			protected void onPreExecute(){
				loading.setVisibility(View.VISIBLE);
				addTraslado.setVisibility(View.INVISIBLE);
				title = "";
				msg = "";
			}
			
			protected Void doInBackground(Void... arg0) {
				try {
					//Si no esta en cache o ha pasado mas de 1 dia, actualiza los datos.
					if (!isInCache || cacheDate.getTime() + (24L * 60L * 60L * 1000L) < new Date().getTime()){
						System.out.println("not in cache... loading data");
						chofer = WSTrasladosCliente.traeChofer();
						camion = WSTrasladosCliente.traeCamion();
						acoplado = WSTrasladosCliente.traeAcoplado();
						predios = WSAutorizacionCliente.traePredios();
						transportistas = WSTrasladosCliente.traeTransportistas();
						
						List<Ganado> list = WSGanadoCliente.traeOfflineDiioBasico();
						PredioLibreServicio.deleteDiio();
						PredioLibreServicio.insertaDiio(list);
					}
					trasList = WSTrasladosCliente.traeTraslados(Aplicaciones.predioWS.getId());
				} catch (AppException e) {
					title = "Error";
					msg = e.getMessage();
				}
				return null;
			}
			
			protected void onPostExecute(Void result){
				loading.setVisibility(View.INVISIBLE);
				addTraslado.setVisibility(View.VISIBLE);
				if (!title.equals("Error")){
					isInCache = true;
					cacheDate = new Date();
					Adapter mAdapter = new Adapter(Menu.this, trasList);
					lvTraslados.setAdapter(mAdapter);
					Utility.setListViewHeightBasedOnChildren(lvTraslados);
				} else {
					ShowAlert.showAlert(title, msg, Menu.this);
				}
			}
			
		}.execute();
	}
	
	private void insertaInstancia(){
		new AsyncTask<Void, Void, Void>(){
			
			String title, msg;
			List<Instancia> trasList;
			
			protected void onPreExecute(){
				loading.setVisibility(View.VISIBLE);
				addTraslado.setVisibility(View.INVISIBLE);
				title = "";
				msg = "";
			}
			
			protected Void doInBackground(Void... arg0) {
				try {
					Instancia instancia = new Instancia();
					instancia.setFundoId(Aplicaciones.predioWS.getId());
					instancia.setUsuarioId(Login.user);
					WSInstanciasCliente.insertaInstancia(instancia, Aplicaciones.appId);
					trasList = WSTrasladosCliente.traeTraslados(Aplicaciones.predioWS.getId());
				} catch (AppException e) {
					title = "Error";
					msg = e.getMessage();
				}
				return null;
			}
			
			protected void onPostExecute(Void result){
				loading.setVisibility(View.INVISIBLE);
				addTraslado.setVisibility(View.VISIBLE);
				if (!title.equals("Error")){
					Adapter mAdapter = new Adapter(Menu.this, trasList);
					lvTraslados.setAdapter(mAdapter);
					Utility.setListViewHeightBasedOnChildren(lvTraslados);
				} else {
					ShowAlert.showAlert(title, msg, Menu.this);
				}
			}
			
		}.execute();
	}
	
	private void borrarTraslado(final Instancia instancia){
		new AsyncTask<Void, Void, Void>(){
			
			String title, msg;
			List<Instancia> trasList;
			
			protected void onPreExecute(){
				loading.setVisibility(View.VISIBLE);
				title = "";
				msg = "";
			}
			
			protected Void doInBackground(Void... arg0) {
				try {
					WSTrasladosCliente.borrarTraslado(instancia);
					trasList = WSTrasladosCliente.traeTraslados(Aplicaciones.predioWS.getId());
				} catch (AppException e) {
					title = "Error";
					msg = e.getMessage();
				}
				return null;
			}
			
			protected void onPostExecute(Void result){
				loading.setVisibility(View.INVISIBLE);
				if (!title.equals("Error")){
					Adapter mAdapter = new Adapter(Menu.this, trasList);
					lvTraslados.setAdapter(mAdapter);
					Utility.setListViewHeightBasedOnChildren(lvTraslados);
				} else {
					ShowAlert.showAlert(title, msg, Menu.this);
				}
			}
			
		}.execute();
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id){
		case R.id.goBack:
			finish();
			break;
		case R.id.addTraslado:
			ShowAlert.askYesNo("Crear Traslado", "¿Está seguro que desea crear un nuevo traslado?", Menu.this, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface arg0, int which) {
					if (which == -2){
						insertaInstancia();
					}
				}
			});
			break;

		}
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int id = arg0.getId();
		switch (id){
		case R.id.lvTraslados:
			Intent i;
			final Integer superInstanciaId = ((Instancia) arg0.getItemAtPosition(arg2)).getId();
			String estado = ((Instancia) arg0.getItemAtPosition(arg2)).getInstancia().getEstado();
			if (estado.equals("BO")){
				Integer usuario = ((Instancia) arg0.getItemAtPosition(arg2)).getInstancia().getUsuarioId();
				if (usuario.intValue() == Login.user){
					try {
						boolean replace = TrasladosServicio.checkInstance(((Instancia) arg0.getItemAtPosition(arg2)).getId());
						if (replace){
							ShowAlert.askYesNo("Advertencia", "Tiene datos guardados de un traslado anterior.\nSi continúa perderá éstos.\n¿Está seguro de continuar?", this, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									if (which == -2){
										try {
											TrasladosServicio.deleteGanado();
											final Intent intent = new Intent(Menu.this, TrasladosV2.class);
											intent.putExtra("superInstanciaId", superInstanciaId);
											startActivityForResult(intent, 0);
										} catch (AppException e) {
											ShowAlert.showAlert("Error", e.getMessage(), Menu.this);
										}
									}
								}
							});
						} else {
							i = new Intent(this, TrasladosV2.class);
							i.putExtra("superInstanciaId", superInstanciaId);
							startActivityForResult(i, 0);
						}
					} catch (AppException e) {
						ShowAlert.showAlert("Error", e.getMessage(), this);
					}
				} else {
					ShowAlert.showAlert("Error", "El traslado corresponde a otro usuario", this);
				}
			} else if (estado.equals("EP")){
				try {
					boolean replace = TrasladosServicio.checkInstance(((Instancia) arg0.getItemAtPosition(arg2)).getId());
					if (replace){
						ShowAlert.askYesNo("Advertencia", "Tiene datos guardados de un traslado anterior.\nSi continúa perderá éstos.\n¿Está seguro de continuar?", this, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								if (which == -2){
									try {
										TrasladosServicio.deleteGanado();
										final Intent intent = new Intent(Menu.this, Recepcion.class);
										intent.putExtra("superInstanciaId", superInstanciaId);
										startActivityForResult(intent, 0);
									} catch (AppException e) {
										ShowAlert.showAlert("Error", e.getMessage(), Menu.this);
									}
								}
							}
						});
					} else {
						final Intent intent = new Intent(Menu.this, Recepcion.class);
						intent.putExtra("superInstanciaId", superInstanciaId);
						startActivityForResult(intent, 0);
					}
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), this);
				}
			} else if (estado.equals("CO")){
				ShowAlert.showAlert("Traslado", "El traslado se encuentra cerrado", this);
			}
			break;
		}
	}

	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		
		int id = arg0.getId();
		switch (id){
		case R.id.lvTraslados:
			final Instancia instancia = new Instancia();
			instancia.setId(((Instancia) arg0.getItemAtPosition(arg2)).getId());
			instancia.setUsuarioId(((Instancia) arg0.getItemAtPosition(arg2)).getInstancia().getUsuarioId());
			ShowAlert.askYesNo("Eliminar Procedimiento", "ADVERTENCIA\n¿Seguro que desea eliminar el procedimiento seleccionado?", Menu.this, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface arg0, int which) {
					if (which == -2){
						borrarTraslado(instancia);
					}
				}
			});
			break;
		}
		return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode) {
        case 0:
            if (resultCode == RESULT_OK) {
            	traeDatosWS();
            }
            break;
        }
    }
	
}
