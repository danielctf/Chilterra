package cl.a2r.traslados;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import cl.a2r.animales.R;
import cl.a2r.animales.TrasladosSalida;
import cl.a2r.common.AppException;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Persona;
import cl.a2r.sip.model.Predio;
import cl.a2r.sip.model.Transportista;
import cl.a2r.sip.wsservice.WSAreteosCliente;
import cl.a2r.sip.wsservice.WSAutorizacionCliente;
import cl.a2r.sip.wsservice.WSTrasladosCliente;

public class AsyncGetData extends AsyncTask<Void, Void, Void>{
	private Activity act;
	private ProgressBar loading;
	private String title, msg;
	private Spinner spinnerDestino, spinnerTransportista;
	
	private List<Predio> predios;
	private List<Transportista> transportistas;
	private List<Persona> arrieros;
	
	public AsyncGetData(Activity act){
		this.act = act;
		loading = (ProgressBar) this.act.findViewById(R.id.loading);
		spinnerDestino = (Spinner) this.act.findViewById(R.id.spinnerDestino);
		spinnerTransportista = (Spinner) this.act.findViewById(R.id.spinnerTransportista);
		title = "";
		msg = "";
	}
	
	protected void onPreExecute(){
		loading.setVisibility(View.VISIBLE);
	}
	
	@SuppressWarnings("unchecked")
	protected Void doInBackground(Void... params) {
		try {
			((TrasladosSalida) act).listaTipoGanado = WSAreteosCliente.traeTipoGanado();
			predios = WSAutorizacionCliente.traePredios();
			transportistas = WSTrasladosCliente.traeTransportistas();
			arrieros = WSTrasladosCliente.traeArrieros();
		} catch (AppException e) {
			title = "Error";
			msg = e.getMessage();
		}
		return null;
	}
	
	protected void onPostExecute(Void result){
		loading.setVisibility(View.INVISIBLE);
		
		if (title.equals("Error")){
			ShowAlert.showAlert(title, msg, act);
		} else {
			((TrasladosSalida) act).transportistas = transportistas;
			((TrasladosSalida) act).arrieros = arrieros;
			
			ArrayAdapter<Predio> mAdapter = new ArrayAdapter<Predio>(act, android.R.layout.simple_list_item_1, predios);
			spinnerDestino.setAdapter(mAdapter);
			
			ArrayAdapter<Transportista> mAdapter2 = new ArrayAdapter<Transportista>(act, android.R.layout.simple_list_item_1, transportistas);
			spinnerTransportista.setAdapter(mAdapter2);
		}
	}
	
}
