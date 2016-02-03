package cl.a2r.custom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cl.a2r.alimentacion.Aplicaciones;
import cl.a2r.alimentacion.R;
import cl.a2r.alimentacion.Stock;
import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.Util;
import cl.a2r.sap.model.Medicion;
import cl.a2r.sap.model.Potrero;
import cl.a2r.sap.model.TipoMedicion;
import cl.a2r.sap.wsservice.WSMedicionCliente;
import cl.ar2.sqlite.cobertura.MedicionServicio;

public class AsyncGetStock extends AsyncTask<Void, Void, Void>{

	private Activity act;
	private TextView tvFundo, tvUpdate, tvCrecimiento, tvCobertura, tvClick;
	private ListView lvStock;
	private ProgressBar loading;
	private List<Medicion> medList;
	private double clickCobertura;
	private double crecimiento;
	private String fechaActStr;
	private String title, msg;
	
	public AsyncGetStock(Activity act){
		this.act = act;
		
		tvFundo = (TextView)act.findViewById(R.id.tvFundo);
		tvUpdate = (TextView)act.findViewById(R.id.tvUpdate);
		tvCrecimiento = (TextView)act.findViewById(R.id.tvCrecimiento);
		tvCobertura = (TextView)act.findViewById(R.id.tvCobertura);
		tvClick = (TextView)act.findViewById(R.id.tvClick);
		lvStock = (ListView)act.findViewById(R.id.lvStock);
		loading = (ProgressBar)act.findViewById(R.id.loading3);
		
		title = "";
		msg = "";
		clickCobertura = 0;
		crecimiento = 0;
		fechaActStr = "";
	}
	
	protected void onPreExecute(){		
		loading.setVisibility(View.VISIBLE);
	}
	
	protected Void doInBackground(Void... params) {
		try {
			medList = MedicionServicio.traeMedicionFundo(Aplicaciones.predioWS.getId());
			
			calcularCobertura();
			calcularCrecimiento();
			traeUltimaActualizacion();
			
			/*
			for (int i = 0; i < filterChecked.length; i++){
				filterChecked[i] = true;
			}
			*/
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), act);
		}
		return null;
	}
	
	protected void onPostExecute(Void result){
		tvFundo.setText(Aplicaciones.predioWS.getCodigo());
		StockAdapter mAdapter = new StockAdapter(act, medList);
		lvStock.setAdapter(mAdapter);
		Utility.setListViewHeightBasedOnChildren(lvStock);
		
		if (Math.round(clickCobertura) != 0){
			tvCobertura.setText(Integer.toString(Formulas.calculaMS(clickCobertura)) + " KgMs/Ha");
			tvClick.setText(Double.toString(Formulas.roundForDisplay(clickCobertura)) + " Click");
		}
		
		if (crecimiento != 0){
			tvCrecimiento.setText(Double.toString(crecimiento) + " KgMs/Ha/día");
		}
		
		tvUpdate.setText(fechaActStr);
		
		loading.setVisibility(View.INVISIBLE);
	}
	
	private void traeUltimaActualizacion(){
		try {
			Date fechaAct = MedicionServicio.traeFechaActualizado();
			if (fechaAct != null){
				fechaActStr = Util.dateToString(fechaAct, "dd-MM-yyyy HH:mm");
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), act);
		}
	}
	
	private void calcularCobertura(){
		double totalSuperficie = 0;
		for (Medicion m : medList){
			if (m.getId() != null){
				clickCobertura = clickCobertura + m.getClick() * m.getSuperficie();
				totalSuperficie = totalSuperficie + m.getSuperficie();
			}
		}
		clickCobertura = clickCobertura / totalSuperficie;
	}
	
	private void calcularCrecimiento(){
		try {
			List<Medicion> list = MedicionServicio.traeCrecimientoMeds(Aplicaciones.predioWS.getId());
			List<Integer> potCalculados = new ArrayList<Integer>();
			double totalSuperficie = 0;
			
			for (Medicion m : list){
				if (potCalculados.contains(m.getPotreroId())){
					continue;
				}
				Medicion max = new Medicion();
				max.setId(0);
				max.setClick(0);
				Medicion max2do = new Medicion();
				max2do.setId(0);
				max2do.setClick(0);
				for (Medicion med : list){
					if (m.getPotreroId().intValue() == med.getPotreroId().intValue()){
						
						if (med.getId().intValue() > max.getId().intValue() &&
								med.getClick() > max.getClick()){
							
							max2do = max;
							max = med;
							
						} else if (med.getId().intValue() > max2do.getId().intValue() &&
								med.getClick() > max2do.getClick()){
							
							max2do = med;
						}
					}
				}
				if (max2do.getId().intValue() != 0){
					long diff = max.getFecha().getTime() - max2do.getFecha().getTime();
					long diffDays = diff / (24 * 60 * 60 * 1000);
					if (diffDays > 0){
						double clickDiff = max.getClick() - max2do.getClick();
						double clickXDia = clickDiff / diffDays;
						double crecimientoPot = Formulas.calculaCrecimiento(clickXDia);
						double superficiePot = m.getSuperficie();
						crecimiento = crecimiento + crecimientoPot * superficiePot;
						totalSuperficie = totalSuperficie + superficiePot;
						potCalculados.add(max.getPotreroId());
					}
				}
			}
			crecimiento = crecimiento / totalSuperficie;
			crecimiento = Formulas.roundForDisplay(crecimiento);

		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), act);
		}
	}
}