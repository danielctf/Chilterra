package cl.a2r.custom;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import cl.a2r.alimentacion.Login;
import cl.a2r.alimentacion.R;
import cl.a2r.alimentacion.Stock;
import cl.a2r.common.AppException;
import cl.a2r.sap.model.Calificacion;
import cl.a2r.sap.model.Medicion;
import cl.a2r.sap.wsservice.WSMedicionCliente;
import cl.ar2.sqlite.cobertura.MedicionServicio;
import cl.ar2.sqlite.cobertura.RegistroMedicion;

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
		loading = (ProgressBar) targetActivity.findViewById(R.id.loading);
		title = "";
		msg = "";
	}
	
	protected void onPreExecute(){		
		tvSync.setVisibility(View.INVISIBLE);
		update.setVisibility(View.INVISIBLE);
		loading.setVisibility(View.VISIBLE);
	}
	
	protected Void doInBackground(Void... params) {
		sync();
		if (title.equals("")){
			getStockWS();
		}
		if (title.equals("")){
			getCalificacionWS();
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
	
    private void sync(){
		try {
			List<RegistroMedicion> list = MedicionServicio.traeMediciones();
			if (list.size() == 0){
				return;
			}
			List<Medicion> medList = new ArrayList<Medicion>();
			for (RegistroMedicion rm : list){
				medList.add(rm.getMedicion());
			}
			WSMedicionCliente.insertaMedicion(medList, Login.mail);
			for (RegistroMedicion rm : list){
				MedicionServicio.deleteMedicion(rm.getId());
			}
		} catch (AppException e) {
			title = "Error";
			msg = e.getMessage();
		}
    }

    private void getStockWS(){
    	List<Medicion> list = null;
		try {
			list = WSMedicionCliente.traeStock();
			MedicionServicio.deleteStock();
			MedicionServicio.insertaStock(list);
		} catch (AppException e) {
			title = "Error";
			msg = e.getMessage();
		}
    }
    
    private void getCalificacionWS(){
    	try {
			List<Calificacion> list = MedicionServicio.traeCalificacion();
			List<Calificacion> noSincronizados = new ArrayList<Calificacion>();
			for (Calificacion cal : list){
				if (cal.getSincronizado().equals("N")){
					noSincronizados.add(cal);
				}
			}
			
			if (noSincronizados.size() > 0){
				WSMedicionCliente.insertaCalificacion(noSincronizados, Login.mail);
			}
			
			MedicionServicio.deleteCalificacion();
			List<Calificacion> calList = WSMedicionCliente.traeCalificacion();
			MedicionServicio.insertaCalificacion(calList);
			
			title = "Sincronización";
			msg = "Sincronización Completa";
		} catch (AppException e) {
			title = "Error";
			msg = e.getMessage();
		}
    }

}
