package cl.a2r.ecografias;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Ecografia;
import cl.a2r.sip.model.Inseminacion;
import cl.a2r.sip.model.Traslado;
import cl.a2r.sip.wsservice.WSEcografiasCliente;
import cl.a2r.sip.wsservice.WSGanadoCliente;
import cl.ar2.sqlite.servicio.EcografiasServicio;
import cl.ar2.sqlite.servicio.TrasladosServicio;

public class Sincronizacion extends AsyncTask<Void, Void, Void>{

	private Activity act;
	private ProgressBar loading;
	private String title, msg;
	private Integer usuarioId;
	private ImageButton sync;
	
	public Sincronizacion(Activity act, Integer usuarioId){
		this.act = act;
		this.usuarioId = usuarioId;
		loading = (ProgressBar) this.act.findViewById(R.id.loading);
		sync = (ImageButton)this.act.findViewById(R.id.sync);
		title = "";
		msg = "";
	}
	
	protected void onPreExecute(){
		loading.setVisibility(View.VISIBLE);
		sync.setVisibility(View.INVISIBLE);
	}
	
	@SuppressWarnings("unchecked")
	protected Void doInBackground(Void... params) {
		try {
			List<Ecografia> ecoList = EcografiasServicio.traeEcografias();
			WSEcografiasCliente.insertaEcografia(ecoList, usuarioId);
			EcografiasServicio.deleteEcografias();
			EcografiasServicio.deleteInseminaciones();
			
			List<Inseminacion> insList = WSEcografiasCliente.traeInseminaciones();
			EcografiasServicio.deleteInseminacionesSincronizadas();
			EcografiasServicio.insertaInseminacion(insList);
			
			List<Ecografia> ecoNewList = WSEcografiasCliente.traeEcografias();
			EcografiasServicio.deleteEcografiasSincronizadas();
			EcografiasServicio.insertaEcografia(ecoNewList);
			
			List<Traslado> trasList = TrasladosServicio.traeReubicaciones();
			for (Traslado t : trasList){
				t.setUsuarioId(usuarioId);
				t.setDescripcion("REUBICACION POR BASTONEO");
			}
			WSGanadoCliente.reajustaGanado(trasList);
			TrasladosServicio.deleteReubicaciones();
			
			title = "Sincronización";
			msg = "Sincronización Completa";
		} catch (AppException e) {
			title = "Error";
			msg = e.getMessage();
		}
		return null;
	}
	
	protected void onPostExecute(Void result){
		loading.setVisibility(View.INVISIBLE);
		sync.setVisibility(View.VISIBLE);
		ShowAlert.showAlert(title, msg, act);
		((Ecografias) act).onStart();
	}
	
}
