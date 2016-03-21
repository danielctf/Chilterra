package cl.a2r.secados;

import java.util.List;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Secado;
import cl.a2r.sip.model.Traslado;
import cl.a2r.sip.wsservice.WSGanadoCliente;
import cl.a2r.sip.wsservice.WSSecadosCliente;
import cl.ar2.sqlite.servicio.SecadosServicio;
import cl.ar2.sqlite.servicio.TrasladosServicio;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

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
		title = "";
		msg = "";
	}
	
	protected Void doInBackground(Void... arg0) {
		try {
			List<Secado> secList = SecadosServicio.traeGanadoASincronizar();
			WSSecadosCliente.insertaEstadoLeche(secList, usuarioId);
			SecadosServicio.deleteAllSecado();
			
			List<Traslado> trasList = TrasladosServicio.traeReubicaciones();
			for (Traslado t : trasList){
				t.setUsuarioId(usuarioId);
				t.setDescripcion("REUBICACION POR BASTONEO");
			}
			WSGanadoCliente.reajustaGanado(trasList);
			TrasladosServicio.deleteReubicaciones();
			
			List<Ganado> ganList = WSSecadosCliente.traeAllDiio();
			SecadosServicio.deleteAllDiio();
			SecadosServicio.insertaDiio(ganList);
			
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
		((Secados) act).onStart();
	}

}
