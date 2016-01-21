package cl.a2r.custom;

import java.util.List;

import cl.a2r.animales.PredioLibreDiio;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.sip.model.Brucelosis;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.InyeccionTB;
import cl.a2r.sip.wsservice.WSPredioLibreCliente;
import cl.ar2.sqlite.servicio.PredioLibreServicio;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

public class AsyncPLDiio extends AsyncTask<Void, Void, Void>{

	private Activity act;
	private ProgressBar loading;
	private ListView lvPredioLibre;
	private String title, msg;
	private Integer instancia;
	
	public AsyncPLDiio(Activity act, Integer instancia){
		this.act = act;
		this.instancia = instancia;
		loading = (ProgressBar) this.act.findViewById(R.id.loading);
		lvPredioLibre = (ListView)this.act.findViewById(R.id.lvPredioLibre);
		title = "";
		msg = "";
	}
	
	protected void onPreExecute(){
		loading.setVisibility(View.VISIBLE);
		lvPredioLibre.setEnabled(false);
	}
	
	protected Void doInBackground(Void... params) {
		try {
			List<InyeccionTB> listEncontrados = WSPredioLibreCliente.traeGanadoTuberculina(instancia);
			for (InyeccionTB tb : listEncontrados){
				boolean exists = PredioLibreServicio.existsGanadoPL(tb.getGanadoID());
				if (!exists){
					PredioLibreServicio.insertaGanadoPL(tb);
				}
			}
			List<Brucelosis> listEncontradosBrucelosis = WSPredioLibreCliente.traeGanadoBrucelosis(instancia);
			for (Brucelosis b : listEncontradosBrucelosis){
				boolean exists = PredioLibreServicio.existsGanadoPLBrucelosis(b.getGanado().getId());
				if (!exists){
					PredioLibreServicio.insertaGanadoPLBrucelosis(b);
				}
			}
			List<Ganado> list = WSPredioLibreCliente.traeAllDiio();
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
		loading.setVisibility(View.INVISIBLE);
		lvPredioLibre.setEnabled(true);
		if (!title.equals("Error")){
			Intent i = new Intent (act, PredioLibreDiio.class);
			i.putExtra("instancia", instancia);
			act.startActivity(i);
		} else if (title.equals("Error")){
			ShowAlert.showAlert(title, msg, act);
		}
	}

}
