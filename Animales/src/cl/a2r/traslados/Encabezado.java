package cl.a2r.traslados;

import java.util.ArrayList;
import java.util.List;

import cl.a2r.animales.Aplicaciones;
import cl.a2r.animales.R;
import cl.a2r.auditoria.GruposAuditoria;
import cl.a2r.common.AppException;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Camion;
import cl.a2r.sip.model.Chofer;
import cl.a2r.sip.model.Predio;
import cl.a2r.sip.model.Transportista;
import cl.ar2.sqlite.servicio.AuditoriasServicio;
import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Encabezado extends Fragment{

	private Spinner spinnerOrigen, spinnerDestino, spinnerTipoTransporte, spinnerTransportista, spinnerChofer, spinnerCamion, spinnerAcoplado;
	private TextView tvChofer, tvCamion, tvAcoplado, tvTransportista;
	private View v;
	private Activity act;
	
	public Encabezado(Activity act){
		this.act = act;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	v = inflater.inflate(R.layout.fragment_encabezado, container, false);

    	cargarInterfaz();
    	cargarListeners();
    	cargarDatos();
    	
    	return v;
	}
	
	private void cargarInterfaz(){
		spinnerChofer = (Spinner)v.findViewById(R.id.spinnerChofer);
		spinnerCamion = (Spinner)v.findViewById(R.id.spinnerCamion);
		spinnerAcoplado = (Spinner)v.findViewById(R.id.spinnerAcoplado);
		spinnerTransportista = (Spinner)v.findViewById(R.id.spinnerTransportista);
		spinnerTipoTransporte = (Spinner)v.findViewById(R.id.spinnerTipoTransporte);
		spinnerOrigen = (Spinner)v.findViewById(R.id.spinnerOrigen);
		spinnerDestino = (Spinner)v.findViewById(R.id.spinnerDestino);
		
		tvTransportista = (TextView)v.findViewById(R.id.tvTransportista);
		tvChofer = (TextView)v.findViewById(R.id.tvChofer);
		tvCamion = (TextView)v.findViewById(R.id.tvCamion);
		tvAcoplado = (TextView)v.findViewById(R.id.tvAcoplado);
	}
	
	private void cargarListeners(){
		spinnerTipoTransporte.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 0){
					spinnerTransportista.setVisibility(View.VISIBLE);
					tvTransportista.setVisibility(View.VISIBLE);
					spinnerChofer.setVisibility(View.VISIBLE);
					tvChofer.setVisibility(View.VISIBLE);
					spinnerCamion.setVisibility(View.VISIBLE);
					tvCamion.setVisibility(View.VISIBLE);
					spinnerAcoplado.setVisibility(View.VISIBLE);
					tvAcoplado.setVisibility(View.VISIBLE);
				} else {
					spinnerTransportista.setVisibility(View.GONE);
					tvTransportista.setVisibility(View.GONE);
					spinnerChofer.setVisibility(View.GONE);
					tvChofer.setVisibility(View.GONE);
					spinnerCamion.setVisibility(View.GONE);
					tvCamion.setVisibility(View.GONE);
					spinnerAcoplado.setVisibility(View.GONE);
					tvAcoplado.setVisibility(View.GONE);
				}
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		
		spinnerTransportista.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Transportista t = (Transportista) arg0.getItemAtPosition(arg2);
				cargarTransportista(t);
				TrasladosV2.traslado.setTransportistaId(t.getId());
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		
		spinnerDestino.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Predio destino = (Predio) arg0.getItemAtPosition(arg2);
				TrasladosV2.traslado.setDestino(destino);
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		
		spinnerChofer.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Integer choferId = ((Chofer) arg0.getItemAtPosition(arg2)).getId();
				TrasladosV2.traslado.setChoferId(choferId);
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		
		spinnerCamion.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Integer camionId = ((Camion) arg0.getItemAtPosition(arg2)).getId();
				TrasladosV2.traslado.setCamionId(camionId);
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		
		spinnerAcoplado.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Integer acopladoId = ((Camion) arg0.getItemAtPosition(arg2)).getId();
				TrasladosV2.traslado.setAcopladoId(acopladoId);
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
	}
	
	private void cargarDatos(){
		TrasladosV2.traslado.setOrigen(Aplicaciones.predioWS);
		List<Predio> list = new ArrayList<Predio>();
		list.add(Aplicaciones.predioWS);
		ArrayAdapter<Predio> mAdapter = new ArrayAdapter<Predio>(act, android.R.layout.simple_list_item_1, list);
		spinnerOrigen.setAdapter(mAdapter);
		spinnerOrigen.setEnabled(false);
		
		ArrayAdapter<Predio> mAdapter0 = new ArrayAdapter<Predio>(act, android.R.layout.simple_list_item_1, Menu.predios);
		spinnerDestino.setAdapter(mAdapter0);
		
		String[] tipoTrans = {"Con Camión", "Sin Camión"};
		ArrayAdapter<String> mAdapter1 = new ArrayAdapter<String>(act, android.R.layout.simple_list_item_1, tipoTrans);
		spinnerTipoTransporte.setAdapter(mAdapter1);
		
		ArrayAdapter<Transportista> mAdapter2 = new ArrayAdapter<Transportista>(act, android.R.layout.simple_list_item_1, Menu.transportistas); 
		spinnerTransportista.setAdapter(mAdapter2);

	}
	
	private void cargarTransportista(Transportista t){
		List<Chofer> choferes = new ArrayList<Chofer>();
		for (Chofer c : Menu.chofer){
			if (t.getId().intValue() == c.getTransportistaId().intValue()){
				choferes.add(c);
			}
		}
		ArrayAdapter<Chofer> mAdapter3 = new ArrayAdapter<Chofer>(act, android.R.layout.simple_list_item_1, choferes);
		spinnerChofer.setAdapter(mAdapter3);
		
		List<Camion> camiones = new ArrayList<Camion>();
		for (Camion c : Menu.camion){
			if (t.getId().intValue() == c.getTransportistaId().intValue()){
				camiones.add(c);
			}
		}
		ArrayAdapter<Camion> mAdapter4 = new ArrayAdapter<Camion>(act, android.R.layout.simple_list_item_1, camiones);
		spinnerCamion.setAdapter(mAdapter4);
		
		List<Camion> acoplados = new ArrayList<Camion>();
		for (Camion c : Menu.acoplado){
			if (t.getId().intValue() == c.getTransportistaId().intValue()){
				acoplados.add(c);
			}
		}
		Camion c = new Camion();
		acoplados.add(0, c);
		ArrayAdapter<Camion> mAdapter5 = new ArrayAdapter<Camion>(act, android.R.layout.simple_list_item_1, acoplados);
		spinnerAcoplado.setAdapter(mAdapter5);
	}
	
	public void onStart(){
		super.onStart();
		ConnectThread.setHandler(mHandler);
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
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", act, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };
	
}
