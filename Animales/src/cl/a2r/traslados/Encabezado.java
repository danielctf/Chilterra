package cl.a2r.traslados;

import cl.a2r.animales.R;
import cl.a2r.sip.wsservice.WSAutorizacionCliente;
import cl.a2r.sip.wsservice.WSTrasladosCliente;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class Encabezado extends Fragment implements View.OnClickListener{

	private Spinner spinnerOrigen, spinnerDestino, spinnerTipoTransporte, spinnerTransportista, spinnerChofer, spinnerCamion, spinnerAcoplado, spinnerArriero;
	private TextView tvChofer, tvCamion, tvAcoplado, tvTransportista, tvArriero;
	private View v;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	v = inflater.inflate(R.layout.fragment_encabezado, container, false);

    	cargarInterfaz();
    	traeDatos();
    	
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
		spinnerArriero = (Spinner)v.findViewById(R.id.spinnerArriero);
		
		tvTransportista = (TextView)v.findViewById(R.id.tvTransportista);
		tvChofer = (TextView)v.findViewById(R.id.tvChofer);
		tvCamion = (TextView)v.findViewById(R.id.tvCamion);
		tvAcoplado = (TextView)v.findViewById(R.id.tvAcoplado);
		tvArriero = (TextView)v.findViewById(R.id.tvArriero);
		
	}
	
	private void traeDatos(){

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}
