package cl.a2r.auditoria;

import java.util.List;

import cl.a2r.animales.Aplicaciones;
import cl.a2r.animales.Login;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.custom.Signature;
import cl.a2r.custom.Utility;
import cl.a2r.sip.model.Auditoria;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Traslado;
import cl.a2r.sip.wsservice.WSAuditoriaCliente;
import cl.a2r.sip.wsservice.WSGanadoCliente;
import cl.a2r.sip.wsservice.WSPredioLibreCliente;
import cl.ar2.sqlite.dao.SqLiteTrx;
import cl.ar2.sqlite.servicio.AuditoriasServicio;
import cl.ar2.sqlite.servicio.PredioLibreServicio;
import cl.ar2.sqlite.servicio.TrasladosServicio;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GruposAuditoria extends Activity implements View.OnClickListener, ListView.OnItemClickListener, ListView.OnItemLongClickListener{

	private TextView tvFundo;
	private ImageButton goBack, addAuditoria;
	private ProgressBar loading;
	private ListView lvAuditoria;
	private List<Auditoria> auList;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_grupos_auditoria);
		
		cargarInterfaz();
		SqLiteTrx trx = new SqLiteTrx(false);
		trx.close();
		traeDatosWS();
	}
	
	private void cargarInterfaz(){
		tvFundo = (TextView)findViewById(R.id.tvFundo);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		addAuditoria = (ImageButton)findViewById(R.id.addAuditoria);
		addAuditoria.setOnClickListener(this);
		loading = (ProgressBar)findViewById(R.id.loading);
		loading.setVisibility(View.INVISIBLE);
		lvAuditoria = (ListView)findViewById(R.id.lvAuditoria);
		lvAuditoria.setOnItemClickListener(this);
		lvAuditoria.setOnItemLongClickListener(this);
		
		tvFundo.setText(Aplicaciones.predioWS.getCodigo());
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
				try {
					auList = WSAuditoriaCliente.traeAuditoria(Aplicaciones.predioWS.getId());
				} catch (AppException e) {
					title = "Error";
					msg = e.getMessage();
				}
				return null;
			}
			
			protected void onPostExecute(Void result){
				loading.setVisibility(View.INVISIBLE);
				if (!title.equals("Error")){
					Adapter mAdapter = new Adapter(GruposAuditoria.this, auList);
					lvAuditoria.setAdapter(mAdapter);
					Utility.setListViewHeightBasedOnChildren(lvAuditoria);
				} else {
					ShowAlert.showAlert(title, msg, GruposAuditoria.this);
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
		case R.id.addAuditoria:
			ShowAlert.askYesNo("Crear Procedimiento", "¿Está seguro que desea crear un nuevo procedimiento?", GruposAuditoria.this, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface arg0, int which) {
					if (which == -2){
						agregarAuditoria();
					}
				}
			});
			break;
		}
	}
	
	private void agregarAuditoria(){
		new AsyncTask<Void, Void, Void>(){
			
			String title, msg;
			List<Auditoria> auList;
			
			protected void onPreExecute(){
				loading.setVisibility(View.VISIBLE);
				addAuditoria.setVisibility(View.INVISIBLE);
				title = "";
				msg = "";
			}
			
			protected Void doInBackground(Void... arg0) {
				try {
					Auditoria auditoria = new Auditoria();
					auditoria.setFundoId(Aplicaciones.predioWS.getId());
					WSAuditoriaCliente.insertaAuditoria(auditoria, Login.user);
					auList = WSAuditoriaCliente.traeAuditoria(Aplicaciones.predioWS.getId());
				} catch (AppException e) {
					title = "Error";
					msg = e.getMessage();
				}
				return null;
			}
			
			protected void onPostExecute(Void result){
				loading.setVisibility(View.INVISIBLE);
				addAuditoria.setVisibility(View.VISIBLE);
				if (!title.equals("Error")){
					Adapter mAdapter = new Adapter(GruposAuditoria.this, auList);
					lvAuditoria.setAdapter(mAdapter);
					Utility.setListViewHeightBasedOnChildren(lvAuditoria);
				} else {
					ShowAlert.showAlert(title, msg, GruposAuditoria.this);
				}
			}
			
		}.execute();
	}
	
	private void borrarAuditoria(final Integer instancia){
		new AsyncTask<Void, Void, Void>(){
			
			String title, msg;
			List<Auditoria> auList;
			
			protected void onPreExecute(){
				loading.setVisibility(View.VISIBLE);
				title = "";
				msg = "";
			}
			
			protected Void doInBackground(Void... arg0) {
				try {
					Auditoria auditoria = new Auditoria();
					auditoria.setId(instancia);
					WSAuditoriaCliente.borrarAuditoria(auditoria, Login.user);
					auList = WSAuditoriaCliente.traeAuditoria(Aplicaciones.predioWS.getId());
				} catch (AppException e) {
					title = "Error";
					msg = e.getMessage();
				}
				return null;
			}
			
			protected void onPostExecute(Void result){
				loading.setVisibility(View.INVISIBLE);
				if (!title.equals("Error")){
					Adapter mAdapter = new Adapter(GruposAuditoria.this, auList);
					lvAuditoria.setAdapter(mAdapter);
					Utility.setListViewHeightBasedOnChildren(lvAuditoria);
				} else {
					ShowAlert.showAlert(title, msg, GruposAuditoria.this);
				}
			}
			
		}.execute();
	}
	
	private void cargarProcedimiento(final Integer instancia){
		new AsyncTask<Void, Void, Void>(){
			
			String title, msg;
			
			protected void onPreExecute(){
				loading.setVisibility(View.VISIBLE);
				title = "";
				msg = "";
			}
			
			protected Void doInBackground(Void... arg0) {
				try {
					List<Traslado> trasList = TrasladosServicio.traeReubicaciones();
					for (Traslado t : trasList){
						t.setUsuarioId(Login.user);
						t.setDescripcion("REUBICACION POR BASTONEO");
					}
					WSGanadoCliente.reajustaGanado(trasList);
					TrasladosServicio.deleteReubicaciones();
					
					List<Ganado> list = WSPredioLibreCliente.traeAllDiio();
					PredioLibreServicio.deleteDiio();
					PredioLibreServicio.insertaDiio(list);
					
					Auditoria a = WSAuditoriaCliente.traeGanado(instancia);
					AuditoriasServicio.deleteGanadoSynced();
					AuditoriasServicio.insertaAuditoria(a);
					
				} catch (AppException e) {
					title = "Error";
					msg = e.getMessage();
				}
				return null;
			}
			
			protected void onPostExecute(Void result){
				loading.setVisibility(View.INVISIBLE);
				if (!title.equals("Error")){
					launchActivity(instancia);
				} else {
					ShowAlert.showAlert(title, msg, GruposAuditoria.this);
				}
			}
			
		}.execute();
	}
	
	private void launchActivity(Integer instancia){
		Intent i = new Intent(this, Auditorias.class);
		i.putExtra("instancia", instancia);
		startActivity(i);
	}
	
	public void solicitarFirma(Integer position){
		Intent intent = new Intent(this, Signature.class);
		intent.putExtra("position", position);
		startActivityForResult(intent, 1);
	}
	
	private void completarProcedimiento(String strFirma, final Integer position){
		System.out.println("str: "+strFirma);
		System.out.println("pos:"+position);
		new AsyncTask<Void, Void, Void>(){
			
			String title, msg;
			
			protected void onPreExecute(){
				loading.setVisibility(View.VISIBLE);
				title = "";
				msg = "";
			}
			
			protected Void doInBackground(Void... arg0) {
				try {
					Integer id = auList.get(position).getId();
					Auditoria auditoria = new Auditoria();
					auditoria.setId(id);
					WSAuditoriaCliente.cerrarAuditoria(auditoria, Login.user);
					auList = WSAuditoriaCliente.traeAuditoria(Aplicaciones.predioWS.getId());
				} catch (AppException e) {
					title = "Error";
					msg = e.getMessage();
				}
				return null;
			}
			
			protected void onPostExecute(Void result){
				loading.setVisibility(View.INVISIBLE);
				if (!title.equals("Error")){
					Adapter mAdapter = new Adapter(GruposAuditoria.this, auList);
					lvAuditoria.setAdapter(mAdapter);
					Utility.setListViewHeightBasedOnChildren(lvAuditoria);
				} else {
					ShowAlert.showAlert(title, msg, GruposAuditoria.this);
				}
			}
			
		}.execute();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode) {
        case 1:
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String strFirma = bundle.getString("strFirma");
                Integer position = bundle.getInt("position");
                completarProcedimiento(strFirma, position);
            }
            break;
        }
    }
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int id = arg0.getId();
		switch (id){
		case R.id.lvAuditoria:
			String estado = ((Auditoria) arg0.getItemAtPosition(arg2)).getEstado();
			if (estado.equals("CO")){
				ShowAlert.showAlert("Procedimiento Cerrado", "El procedimiento ya se encuentra cerrado", GruposAuditoria.this);
				return;
			}
			Integer instancia = ((Auditoria) arg0.getItemAtPosition(arg2)).getId();
			cargarProcedimiento(instancia);
			break;
		}
	}

	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		
		int id = arg0.getId();
		switch (id){
		case R.id.lvAuditoria:
			final Integer instancia = ((Auditoria) arg0.getItemAtPosition(arg2)).getId();
			ShowAlert.askYesNo("Eliminar Procedimiento", "ADVERTENCIA\n¿Seguro que desea eliminar el procedimiento seleccionado?", GruposAuditoria.this, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface arg0, int which) {
					if (which == -2){
						borrarAuditoria(instancia);
					}
				}
			});
			break;
		}
		return true;
	}
	
	//---------------------------------------------------------------------------
	//------------------------DATOS ENVIADOS DESDE BASTÓN------------------------
	//---------------------------------------------------------------------------
	
    private Handler mHandler = new Handler(){
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
    			System.out.println(EID);
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", GruposAuditoria.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };

}
