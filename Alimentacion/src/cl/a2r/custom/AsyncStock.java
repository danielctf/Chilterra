package cl.a2r.custom;

import java.util.List;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import cl.a2r.alimentacion.R;
import cl.a2r.alimentacion.Stock;
import cl.a2r.common.AppException;
import cl.a2r.sap.model.Medicion;
import cl.a2r.sap.model.Potrero;
import cl.a2r.sap.model.TipoMedicion;
import cl.a2r.sap.wsservice.WSMedicionCliente;
import cl.ar2.sqlite.cobertura.MedicionServicio;

public class AsyncStock extends AsyncTask<Void, Void, Void>{

	private Activity targetActivity;
	private TextView tvSync;
	private ImageButton update;
	private ProgressBar loading;
	String title, msg;
	
	public AsyncStock(Activity act){
		targetActivity = act;
		tvSync = (TextView) targetActivity.findViewById(R.id.tvSync);
		update = (ImageButton) targetActivity.findViewById(R.id.update);
		loading = (ProgressBar) targetActivity.findViewById(R.id.loading3);
		title = "";
		msg = "";
	}
	
	protected void onPreExecute(){		
		tvSync.setVisibility(View.INVISIBLE);
		update.setVisibility(View.INVISIBLE);
		loading.setVisibility(View.VISIBLE);
	}
	
	protected Void doInBackground(Void... params) {
		try {
			boolean exists = MedicionServicio.existsPotrero();
			if (!exists){
				List<Potrero> list = WSMedicionCliente.traePotreros();
				MedicionServicio.insertaPotrero(list);
			}
			exists = MedicionServicio.existsTipoMedicion();
			if (!exists){
				List<TipoMedicion> tList = WSMedicionCliente.traeTipoMedicion();
				MedicionServicio.insertaTipoMedicion(tList);
			}
			List<Medicion> medList = WSMedicionCliente.traeStock();
			MedicionServicio.deleteAllMediciones();
			MedicionServicio.insertaMedicion(medList, true);
			title = "Sincronización";
			msg = "Sincronización Completa";
		} catch (AppException e) {
			title = "Error";
			msg = e.getMessage();
		}
		return null;
	}
	
	protected void onPostExecute(Void result){
		ShowAlert.showAlert(title, msg, this.targetActivity);
		tvSync.setVisibility(View.VISIBLE);
		update.setVisibility(View.VISIBLE);
		loading.setVisibility(View.INVISIBLE);
		((Stock) targetActivity).updateStatus();
		((Stock) targetActivity).getStock();
	}

}
