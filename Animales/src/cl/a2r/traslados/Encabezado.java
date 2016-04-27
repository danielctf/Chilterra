package cl.a2r.traslados;

import java.util.ArrayList;
import java.util.List;

import cl.a2r.animales.Aplicaciones;
import cl.a2r.animales.R;
import cl.a2r.sip.model.Camion;
import cl.a2r.sip.model.Chofer;
import cl.a2r.sip.model.Predio;
import cl.a2r.sip.model.Transportista;
import cl.a2r.sip.wsservice.WSAutorizacionCliente;
import cl.a2r.sip.wsservice.WSTrasladosCliente;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class Encabezado extends Fragment implements View.OnClickListener{

	private Spinner spinnerOrigen, spinnerDestino, spinnerTipoTransporte, spinnerTransportista, spinnerChofer, spinnerCamion, spinnerAcoplado, spinnerArriero;
	private TextView tvChofer, tvCamion, tvAcoplado, tvTransportista, tvArriero;
	private View v;
	private Activity act;
	
	public Encabezado(Activity act){
		this.act = act;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	v = inflater.inflate(R.layout.fragment_encabezado, container, false);

    	cargarInterfaz();
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
//		spinnerArriero = (Spinner)v.findViewById(R.id.spinnerArriero);
		
		tvTransportista = (TextView)v.findViewById(R.id.tvTransportista);
		tvChofer = (TextView)v.findViewById(R.id.tvChofer);
		tvCamion = (TextView)v.findViewById(R.id.tvCamion);
		tvAcoplado = (TextView)v.findViewById(R.id.tvAcoplado);
//		tvArriero = (TextView)v.findViewById(R.id.tvArriero);
	}
	
	private void cargarDatos(){
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
		
		ArrayAdapter<Chofer> mAdapter3 = new ArrayAdapter<Chofer>(act, android.R.layout.simple_list_item_1, Menu.chofer);
		spinnerChofer.setAdapter(mAdapter3);
		
		ArrayAdapter<Camion> mAdapter4 = new ArrayAdapter<Camion>(act, android.R.layout.simple_list_item_1, Menu.camion);
		spinnerCamion.setAdapter(mAdapter4);
		
		ArrayAdapter<Camion> mAdapter5 = new ArrayAdapter<Camion>(act, android.R.layout.simple_list_item_1, Menu.acoplado);
		spinnerAcoplado.setAdapter(mAdapter5);
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}
