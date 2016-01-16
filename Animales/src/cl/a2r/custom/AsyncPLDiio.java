package cl.a2r.custom;

import java.util.List;

import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.wsservice.WSPredioLibreCliente;
import cl.ar2.sqlite.servicio.PredioLibreServicio;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

public class AsyncPLDiio extends AsyncTask<Void, Void, Void>{

	private Activity act;
	private ProgressBar loading;
	private String title, msg;
	
	public AsyncPLDiio(Activity act){
		this.act = act;
		loading = (ProgressBar) this.act.findViewById(R.id.loading);
		title = "";
		msg = "";
	}
	
	protected void onPreExecute(){
		loading.setVisibility(View.VISIBLE);
	}
	
	protected Void doInBackground(Void... params) {
		List<Ganado> list = null;
		try {
			list = WSPredioLibreCliente.traeAllDiio();
			PredioLibreServicio.deleteDiio();
			PredioLibreServicio.insertaDiio(list);
			title = "Sincronización";
			msg = "Sincronización Completa";
		} catch (AppException e) {
			title = "Error";
			msg = e.getMessage();
		}
		return null;
	}
	
	protected void onPostExecute(Void result){
		ShowAlert.showAlert(title, msg, act);
		loading.setVisibility(View.INVISIBLE);
	}

}
