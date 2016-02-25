package cl.a2r.secados;

import java.util.ArrayList;
import java.util.List;

import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.EstadoLeche;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Medicamento;
import cl.a2r.sip.wsservice.WSSecadosCliente;
import cl.ar2.sqlite.servicio.SecadosServicio;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

public class GetDataWS extends AsyncTask<Void, Void, Void>{
	
	private Activity act;
	private String title, msg;
	private List<Ganado> ganList;
	private ProgressBar loading;
	
	public GetDataWS(Activity act){
		this.act = act;
		loading = (ProgressBar)act.findViewById(R.id.loading);
	}
	
	protected void onPreExecute(){
		loading.setVisibility(View.VISIBLE);
		title = "";
		msg = "";
	}
	
	protected Void doInBackground(Void... arg0) {
		try {
			ganList = WSSecadosCliente.traeAllDiio();
			System.out.println("ganList: "+ganList.size());
			SecadosServicio.deleteSincronizados();
			List<Ganado> ganExistentes = SecadosServicio.traeDiios();
			List<Ganado> toAdd = new ArrayList<Ganado>();
			for (Ganado g1 : ganList){
				boolean exists = false;
				for (Ganado g2 : ganExistentes){
					if (g1.getId().intValue() == g2.getId().intValue()){
						exists = true;
						break;
					}
				}
				if (!exists){
					toAdd.add(g1);
				}
			}
			
			SecadosServicio.insertaDiio(toAdd);
			System.out.println("toAdd Size: " + toAdd.size());

			
		} catch (AppException e) {
			title = "Error";
			msg = e.getMessage();
		}
		return null;
	}
	
	protected void onPostExecute(Void result){
		loading.setVisibility(View.INVISIBLE);
//		if (title.equals("Error")){
//			ShowAlert.showAlert(title, msg, Secados.this);
//		} else {
//			ArrayAdapter<Medicamento> mAdapter1 = new ArrayAdapter<Medicamento>(Secados.this, android.R.layout.simple_list_item_1, medList);
//			spMedicamento.setAdapter(mAdapter1);
//			
//			ArrayAdapter<EstadoLeche> mAdapter2 = new ArrayAdapter<EstadoLeche>(Secados.this, android.R.layout.simple_list_item_1, estList);
//			spEstado.setAdapter(mAdapter2);
//		}
	}
}
