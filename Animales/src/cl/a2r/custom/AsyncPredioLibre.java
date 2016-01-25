package cl.a2r.custom;

import java.util.List;

import cl.a2r.animales.PredioLibreLobby;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.sip.model.Brucelosis;
import cl.a2r.sip.model.InyeccionTB;
import cl.a2r.sip.wsservice.WSPredioLibreCliente;
import cl.ar2.sqlite.servicio.PredioLibreServicio;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class AsyncPredioLibre extends AsyncTask<Void, Void, Void>{

	private Activity act;
	private ProgressBar loading;
	private ImageButton sync;
	private String title, msg;
	private Integer instancia;
	private boolean showMsg;
	private List<InyeccionTB> syncPendientes, syncLecturaTB;
	private List<Brucelosis> syncBrucelosis;
	
	public AsyncPredioLibre(Activity act, List<InyeccionTB> syncPendientes, List<InyeccionTB> syncLecturaTB, List<Brucelosis> syncBrucelosis, boolean showMsg, Integer instancia){
		this.act = act;
		this.syncPendientes = syncPendientes;
		this.syncLecturaTB = syncLecturaTB;
		this.syncBrucelosis = syncBrucelosis;
		this.showMsg = showMsg;
		this.instancia = instancia;
		loading = (ProgressBar) this.act.findViewById(R.id.loading);
		sync = (ImageButton) this.act.findViewById(R.id.sync);
		title = "";
		msg = "";
	}
	
	protected void onPreExecute(){
		loading.setVisibility(View.VISIBLE);
		sync.setVisibility(View.INVISIBLE);
	}
	
	protected Void doInBackground(Void... params) {
		try {
			WSPredioLibreCliente.insertaGanadoTuberculina(syncPendientes);
			WSPredioLibreCliente.updateLecturaTB(syncLecturaTB);
			WSPredioLibreCliente.insertaGanadoBrucelosis(syncBrucelosis);
			PredioLibreServicio.deletePL();
			PredioLibreServicio.deletePLBrucelosis();
			title = "Sincronización";
			msg = "Sincronización Completa";
		} catch (AppException e) {
			title = "Error";
			msg = e.getMessage();
		}
		return null;
	}
	
	protected void onPostExecute(Void result){
		if (showMsg){
			ShowAlert.showAlert(title, msg, act);
			loading.setVisibility(View.INVISIBLE);
			((PredioLibreLobby) act).syncPendientes();
		} else {
			new AsyncPLDiio(act, instancia).execute();
		}
		sync.setVisibility(View.VISIBLE);
	}

}
