package cl.a2r.alimentacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.Util;
import cl.a2r.custom.ActionItem;
import cl.a2r.custom.AsyncGetStock;
import cl.a2r.custom.AsyncStock;
import cl.a2r.custom.Formulas;
import cl.a2r.custom.QuickAction;
import cl.a2r.custom.ShowAlert;
import cl.a2r.custom.StockAdapter;
import cl.a2r.custom.Utility;
import cl.a2r.sap.model.Medicion;
import cl.ar2.sqlite.cobertura.Crecimiento;
import cl.ar2.sqlite.cobertura.MedicionServicio;
import cl.ar2.sqlite.cobertura.StockM;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Stock extends Activity implements View.OnClickListener, ListView.OnItemClickListener, QuickAction.OnActionItemClickListener{

	private ListView lvStock;
	private ImageButton goBack, sort;
	public ImageButton update;
	private TextView tvCobertura, tvUpdate, tvFundo, tvClick, tvCrecimiento;
	public TextView tvSync;
	private List<Medicion> medList;
	private int cobertura;
	private ProgressBar loading3;
	private String[] items = {"Materia Seca", "Fecha", "Potrero"};
	private String[] filterItems = {"Entrada", "Residuo", "Semanal", "Control"};
	private boolean[] filterChecked = {true, true, true, true};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_stock);
		
		cargarInterfaz();
		new AsyncGetStock(this).execute();
	}
	
	private void cargarInterfaz(){
		lvStock = (ListView)findViewById(R.id.lvStock);
		lvStock.setOnItemClickListener(this);
		lvStock.setFocusable(false);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		sort = (ImageButton)findViewById(R.id.sort);
		sort.setOnClickListener(this);
		update = (ImageButton)findViewById(R.id.update);
		update.setOnClickListener(this);
		tvSync = (TextView)findViewById(R.id.tvSync);
		tvSync.setOnClickListener(this);
		tvCobertura = (TextView)findViewById(R.id.tvCobertura);
		tvUpdate = (TextView)findViewById(R.id.tvUpdate);
		tvFundo = (TextView)findViewById(R.id.tvFundo);
		tvClick = (TextView)findViewById(R.id.tvClick);
		tvCrecimiento = (TextView)findViewById(R.id.tvCrecimiento);
		loading3 = (ProgressBar)findViewById(R.id.loading3);
		loading3.setVisibility(View.INVISIBLE);
		
	}
	
	public void onClick(View v) {
		int id = v.getId();
		switch (id){
		case R.id.goBack:
			finish();
			break;
		case R.id.tvSync:
		case R.id.update:
			new AsyncStock(Stock.this).execute();
			break;
		case R.id.sort:
			String[] items = {"Ordenar por", "Filtrar por"};
			ActionItem t1 = new ActionItem(1, items);
			QuickAction quickAction = new QuickAction(this, QuickAction.VERTICAL);
			quickAction.setOnActionItemClickListener(this);
			quickAction.addActionItem(t1);
			quickAction.show(v);
			break;
		}
	}

	public void onItemClick(QuickAction source, int pos, int actionId, int lvPos) {
		if (lvPos == 0){
			ShowAlert.selectItem("Ordenar por", items, this, new DialogInterface.OnClickListener(){
				
				public void onClick(DialogInterface dialog, int which) {
					ordenarPor(which);
				}});
		} else if (lvPos == 1){
			ShowAlert.multipleChoice("Filtrar por", filterItems, filterChecked, this, new DialogInterface.OnMultiChoiceClickListener() {

				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					filterChecked[which] = isChecked;
				}
			}, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					filtrarPor();
				}
			});
		}
	}

	public void getStock(){
		try {
			tvFundo.setText(Aplicaciones.predioWS.getCodigo());
			medList = MedicionServicio.traeMedicionFundo(Aplicaciones.predioWS.getId());
			StockAdapter mAdapter = new StockAdapter(this, medList);
			lvStock.setAdapter(mAdapter);
			
			calcularCobertura();
			calcularCrecimiento();
			traeUltimaActualizacion();
			Utility.setListViewHeightBasedOnChildren(lvStock);
			
			for (int i = 0; i < filterChecked.length; i++){
				filterChecked[i] = true;
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	private void traeUltimaActualizacion(){
		try {
			Date fechaAct = MedicionServicio.traeFechaActualizado();
			if (fechaAct != null){
				String fechaActStr = Util.dateToString(fechaAct, "dd-MM-yyyy HH:mm");
				tvUpdate.setText(fechaActStr);
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	private void calcularCobertura(){
		double clickCobertura = 0;
		double totalSuperficie = 0;
		for (Medicion m : medList){
			if (m.getId() != null){
				clickCobertura = clickCobertura + m.getClick() * m.getSuperficie();
				totalSuperficie = totalSuperficie + m.getSuperficie();
			}
		}
		clickCobertura = clickCobertura / totalSuperficie;
		if (Math.round(clickCobertura) != 0){
			tvCobertura.setText(Integer.toString(Formulas.calculaMS(clickCobertura)) + " KgMs/Ha");
			tvClick.setText(Double.toString(Formulas.roundForDisplay(clickCobertura)) + " Click");
		}
	}
	
	private void calcularCrecimiento(){
		try {
			List<Medicion> list = MedicionServicio.traeCrecimientoMeds(Aplicaciones.predioWS.getId());
			List<Integer> potCalculados = new ArrayList<Integer>();
			double crecimiento = 0;
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
			if (crecimiento != 0){
				tvCrecimiento.setText(Double.toString(crecimiento) + " KgMs/Ha/día");
			}

		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	private void filtrarPor(){
		
		Utility.setListViewHeightBasedOnChildren(lvStock);
	}
	
	private void ordenarPor(int which){
		StockAdapter sAdapter;
		switch (which){
		case 0:
			//Cobertura

			break;
		case 1:
			//Fecha

			break;
		case 2:
			//Potrero

			break;
		}
		Utility.setListViewHeightBasedOnChildren(lvStock);
	}
	
	public void updateStatus(){
		/*
		try {
			int size = 0;
			List<Medicion> list = MedicionServicio.traeMediciones();
			List<Calificacion> list2 = MedicionServicio.traeCalificacion();
			for (Calificacion cal : list2){
				if (cal.getSincronizado().equals("N")){
					size++;
				}
			}
			size = size + list.size();
			if (size > 0){
				tvSync.setText(Integer.toString(size));
			} else {
				tvSync.setText("");
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
		*/
	}
	
	protected void onStart(){
		super.onStart();
		
		updateStatus();
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int id = arg0.getId();
		switch (id){
		case R.id.lvStock:
			Integer numero = ((StockM) arg0.getItemAtPosition(arg2)).getMed().getPotreroId();
			Integer ms = ((StockM) arg0.getItemAtPosition(arg2)).getMed().getMateriaSeca();
			Intent i = new Intent(this, StockDetalle.class);
			i.putExtra("g_fundo_id", Aplicaciones.predioWS.getId());
			i.putExtra("numero", numero);
			i.putExtra("ms", ms);
			i.putExtra("superficie", ((StockM) arg0.getItemAtPosition(arg2)).getMed().getSuperficie());
			startActivity(i);
			break;
		}
		
	}

}
