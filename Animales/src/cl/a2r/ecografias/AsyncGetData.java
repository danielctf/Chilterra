package cl.a2r.ecografias;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Ecografia;
import cl.a2r.sip.model.EcografiaEstado;
import cl.a2r.sip.model.EcografiaNota;
import cl.a2r.sip.model.EcografiaProblema;
import cl.a2r.sip.model.Ecografista;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Inseminacion;
import cl.a2r.sip.wsservice.WSEcografiasCliente;
import cl.a2r.sip.wsservice.WSPredioLibreCliente;
import cl.ar2.sqlite.servicio.EcografiasServicio;
import cl.ar2.sqlite.servicio.PredioLibreServicio;


public class AsyncGetData extends AsyncTask<Void, Void, Void>{
	private Activity act;
	private Spinner spEcografista, spEstado, spProblema, spNota;
	private ProgressBar loading;
	private String title, msg;
	
	private List<Ecografista> ecografistas;
	private List<EcografiaEstado> estados;
	private List<EcografiaProblema> problemas;
	private List<EcografiaNota> notas;
	
	public AsyncGetData(Activity act){
		this.act = act;
		loading = (ProgressBar) this.act.findViewById(R.id.loading);
		spEcografista = (Spinner)this.act.findViewById(R.id.spEcografista);
		spEstado = (Spinner)this.act.findViewById(R.id.spEstado);
		spProblema = (Spinner)this.act.findViewById(R.id.spProblema);
		spNota = (Spinner)this.act.findViewById(R.id.spNota);
		title = "";
		msg = "";
	}
	
	protected void onPreExecute(){
		loading.setVisibility(View.VISIBLE);
	}
	
	@SuppressWarnings("unchecked")
	protected Void doInBackground(Void... params) {
		try {
			List<Ganado> ganList = WSPredioLibreCliente.traeAllDiio();
			PredioLibreServicio.deleteDiio();
			PredioLibreServicio.insertaDiio(ganList);
			
			List<Inseminacion> insList = WSEcografiasCliente.traeInseminaciones();
			EcografiasServicio.deleteInseminacionesSincronizadas();
			EcografiasServicio.insertaInseminacion(insList);
			
			List<Ecografia> ecoList = WSEcografiasCliente.traeEcografias();
			EcografiasServicio.deleteEcografiasSincronizadas();
			EcografiasServicio.insertaEcografia(ecoList);
			
			ecografistas = WSEcografiasCliente.traeEcografistas();
			estados = WSEcografiasCliente.traeEcografiaEstado();
			Ecografias.menor30.clear();
			Ecografias.prenada.clear();
			for (EcografiaEstado e : estados){
				if (e.getNombre().equals("Preñada")){
					Ecografias.prenada.add(e);
				} else {
					Ecografias.menor30.add(e);
				}
			}
			problemas = WSEcografiasCliente.traeEcografiaProblema();
			notas = WSEcografiasCliente.traeEcografiaNota();
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
			ArrayAdapter<Ecografista> mEcografistas = new ArrayAdapter<Ecografista>(act, android.R.layout.simple_list_item_1, ecografistas);
			spEcografista.setAdapter(mEcografistas);
			
			ArrayAdapter<EcografiaProblema> mProblema = new ArrayAdapter<EcografiaProblema>(act, android.R.layout.simple_list_item_1, problemas);
			spProblema.setAdapter(mProblema);
			
			EcografiaNota e = new EcografiaNota();
			notas.add(0, e);
			ArrayAdapter<EcografiaNota> mNotas = new ArrayAdapter<EcografiaNota>(act, android.R.layout.simple_list_item_1, notas);
			spNota.setAdapter(mNotas);
			
			((Ecografias) act).cargarListeners();
		}
	}
	
}
