package cl.a2r.custom;

import java.util.List;

import cl.a2r.animales.PredioLibreDiio;
import cl.a2r.animales.PredioLibreLobby;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.sip.model.InyeccionTB;
import cl.a2r.sip.wsservice.WSPredioLibreCliente;
import cl.ar2.sqlite.servicio.PredioLibreServicio;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class AsyncPredioLibre extends AsyncTask<List<InyeccionTB>, Void, Void>{

	private Activity act;
	private ProgressBar loading;
	private ImageButton sync;
	private String title, msg;
	
	public AsyncPredioLibre(Activity act){
		this.act = act;
		loading = (ProgressBar) this.act.findViewById(R.id.loading);
		sync = (ImageButton) this.act.findViewById(R.id.sync);
		title = "";
		msg = "";
	}
	
	protected void onPreExecute(){
		loading.setVisibility(View.VISIBLE);
		sync.setVisibility(View.INVISIBLE);
	}
	
	protected Void doInBackground(List<InyeccionTB>... params) {
		try {
			WSPredioLibreCliente.insertaGanadoTuberculina(params[0]);
			PredioLibreServicio.deletePL();
			title = "Sincronización";
			msg = "Sincronozación Completa";
		} catch (AppException e) {
			title = "Error";
			msg = e.getMessage();
		}
		return null;
	}
	
	protected void onPostExecute(Void result){
		ShowAlert.showAlert(title, msg, act);
		loading.setVisibility(View.INVISIBLE);
		((PredioLibreLobby) act).syncPendientes();
		sync.setVisibility(View.VISIBLE);
	}



}
