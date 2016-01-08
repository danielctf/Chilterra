package cl.a2r.alimentacion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.custom.ActionItem;
import cl.a2r.custom.AsyncStock;
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
import android.os.Bundle;
import android.os.Handler;
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
	public static List<StockM> list;
	private List<StockM> listaPotreros, listaPotrerosMenu;
	private int cobertura;
	private ProgressBar loading;
	String[] items = {"Materia Seca", "Fecha", "Potrero"};
	String[] filterItems = {"Entrada", "Residuo", "Semanal", "Control"};
	boolean[] filterChecked = {true, true, true, true};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_stock);
		
		cargarInterfaz();
		getStock();
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
		loading = (ProgressBar)findViewById(R.id.loading);
	}
	
	public void onClick(View v) {
		int id = v.getId();
		switch (id){
		case R.id.goBack:
			finish();
			break;
		case R.id.tvSync:
		case R.id.update:
			update();
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
			list = MedicionServicio.traeStockTotal();
			if (list.size() == 0){
				return;
			}
			
			List<StockM> listaPotreros = MedicionServicio.traeStock(list, Aplicaciones.predioWS.getId());
			if (listaPotreros.size() == 0){
				return;
			}
			this.listaPotreros = listaPotreros;
			List<StockM> listaPotrerosConMediciones = new ArrayList<StockM>();
			for (StockM sm : listaPotreros){
				if (sm.getMed().getClickFinal() != null){
					double click = ((double) sm.getMed().getClickFinal().intValue() - (double) sm.getMed().getClickInicial().intValue()) / (double) sm.getMed().getMuestras().intValue();
					sm.getMed().setClick(roundForDisplay(click));
					listaPotrerosConMediciones.add(sm);
				}
			}
			listaPotrerosMenu = listaPotreros;
			StockAdapter sAdapter = new StockAdapter(this, listaPotreros);
			lvStock.setAdapter(sAdapter);
			
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			if (listaPotrerosConMediciones.size() > 0){
				cobertura = calcularCobertura(listaPotrerosConMediciones);
				tvCobertura.setText(Integer.toString(cobertura) + " KgMs/Ha");
				tvUpdate.setText(/*"Últ. Actualización\n" + */df.format(listaPotrerosConMediciones.get(0).getActualizado()));
				tvClick.setText(Double.toString(calcularClickPromedio(cobertura)) + " Click");
			} else if (list.size() > 0){
				tvCobertura.setText("0 KgMs/Ha");
				tvUpdate.setText(df.format(list.get(0).getActualizado()));
				tvClick.setText("0 Click");
			}
			
			calcularCrecimiento();
			Utility.setListViewHeightBasedOnChildren(lvStock);
			
			for (int i = 0; i < filterChecked.length; i++){
				filterChecked[i] = true;
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
    Handler hand = new Handler();
    Runnable run = new Runnable() {
        public void run() {
        	new AsyncStock(Stock.this).execute();
        }
    };
	
	private void update(){
		hand.postDelayed(run, 100);
	}
	
	private void calcularCrecimiento(){
		try {
			List<StockM> listFiltrada = MedicionServicio.traeStockCrecimiento(list, Aplicaciones.predioWS.getId(), 0);
			List<Crecimiento> cre = new ArrayList<Crecimiento>();
			for (int i = 0; i < Aplicaciones.predioWS.getPotreros().intValue(); i++){
				Medicion max = new Medicion();
				max.setId(0);
				Medicion max2 = new Medicion();
				max2.setId(0);
				boolean valid = false;
				for (StockM sm : listFiltrada){
					if (sm.getMed().getPotreroId().intValue() == (i+1) &&
							sm.getMed().getTipoMuestraNombre().equals("Semanal")){
						
						if (sm.getMed().getId().intValue() > max.getId().intValue()){
							max2 = max;
							max = sm.getMed();
						} else if (sm.getMed().getId().intValue() > max2.getId().intValue()){
							max2 = sm.getMed();
						}
					}
				}
				
				if (max2.getId().intValue() != 0){
					valid = true;
				}

				if (valid && max.getMateriaSeca().intValue() > max2.getMateriaSeca().intValue()){
					long diff = max.getFecha().getTime() - max2.getFecha().getTime();
					long diffDays = diff / (24 * 60 * 60 * 1000);
					if (diffDays < 0){
						diffDays = diffDays * -1;
					}
					if (diffDays > 0){
						double matSeca = max.getMateriaSeca().intValue() - max2.getMateriaSeca().intValue();
						double crecimiento = roundForDisplay(matSeca / (double) diffDays);
						
						Crecimiento c = new Crecimiento();
						c.setCrecimiento(crecimiento);
						c.setSuperficie(max.getSuperficie());
						cre.add(c);
					}
				}
			}
			despliegaCrecimiento(cre);
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	private void filtrarPor(){
		List<StockM> filterList = new ArrayList<StockM>();
		List<StockM> potrerosNull = new ArrayList<StockM>();
		boolean mostrarNulos = true;
		for (int i = 0; i < filterChecked.length; i++){
			if (filterChecked[i]){
				for (StockM sm : listaPotreros){
					if (sm.getId() != null){
						if (sm.getMed().getTipoMuestraId().intValue() == (i + 1)){
							filterList.add(sm);
						}
					} else {
						if (!potrerosNull.contains(sm)){
							potrerosNull.add(sm);
						}
					}
				}
			} else {
				mostrarNulos = false;
			}
		}
		if (mostrarNulos){
			filterList.addAll(potrerosNull);
		}
		listaPotrerosMenu = filterList;
		StockAdapter sAdapter = new StockAdapter(this, listaPotrerosMenu);
		lvStock.setAdapter(sAdapter);
		Utility.setListViewHeightBasedOnChildren(lvStock);
	}
	
	private void ordenarPor(int which){
		if (listaPotreros == null){
			return;
		}
		StockAdapter sAdapter;
		switch (which){
		case 0:
			//Cobertura
			List<StockM> toRemoveCob = new ArrayList<StockM>();
			List<StockM> blancosCob = new ArrayList<StockM>();
			for (int i = 0; i < listaPotrerosMenu.size(); i++){
				if (listaPotrerosMenu.get(i).getId() == null){
					blancosCob.add(listaPotrerosMenu.get(i));
					toRemoveCob.add(listaPotrerosMenu.get(i));
					continue;
				}
				for (int j = 0; j < listaPotrerosMenu.size(); j++){
					if (listaPotrerosMenu.get(i).getId() != null &&
							listaPotrerosMenu.get(j).getId() != null &&
							listaPotrerosMenu.get(i).getMed().getMateriaSeca().intValue() > listaPotrerosMenu.get(j).getMed().getMateriaSeca().intValue()){
						
						StockM temp = listaPotrerosMenu.get(i);
						listaPotrerosMenu.set(i, listaPotrerosMenu.get(j));
						listaPotrerosMenu.set(j, temp);
					}
				}
			}
			listaPotrerosMenu.removeAll(toRemoveCob);
			listaPotrerosMenu.addAll(blancosCob);
			sAdapter = new StockAdapter(this, listaPotrerosMenu);
			lvStock.setAdapter(sAdapter);
			break;
		case 1:
			//Fecha
			List<StockM> toRemove = new ArrayList<StockM>();
			List<StockM> blancos = new ArrayList<StockM>();
			for (int i = 0; i < listaPotrerosMenu.size(); i++){
				if (listaPotrerosMenu.get(i).getId() == null){
					blancos.add(listaPotrerosMenu.get(i));
					toRemove.add(listaPotrerosMenu.get(i));
					continue;
				}
				for (int j = 0; j < listaPotrerosMenu.size(); j++){
					if (listaPotrerosMenu.get(i).getId() != null &&
							listaPotrerosMenu.get(j).getId() != null &&
									listaPotrerosMenu.get(i).getMed().getFecha().compareTo(listaPotrerosMenu.get(j).getMed().getFecha()) > 0){
						
						StockM temp = listaPotrerosMenu.get(i);
						listaPotrerosMenu.set(i, listaPotrerosMenu.get(j));
						listaPotrerosMenu.set(j, temp);
					}
				}
			}
			listaPotrerosMenu.removeAll(toRemove);
			listaPotrerosMenu.addAll(blancos);
			sAdapter = new StockAdapter(this, listaPotrerosMenu);
			lvStock.setAdapter(sAdapter);
			break;
		case 2:
			//Potrero
			for (int i = 0; i < listaPotrerosMenu.size(); i++){
				for (int j = 0; j < listaPotrerosMenu.size(); j++){
					if (listaPotrerosMenu.get(i).getMed().getPotreroId().intValue() < listaPotrerosMenu.get(j).getMed().getPotreroId().intValue()){
						
						StockM temp = listaPotrerosMenu.get(i);
						listaPotrerosMenu.set(i, listaPotrerosMenu.get(j));
						listaPotrerosMenu.set(j, temp);
					}
				}
			}
			sAdapter = new StockAdapter(this, listaPotrerosMenu);
			lvStock.setAdapter(sAdapter);
			break;
		}
		Utility.setListViewHeightBasedOnChildren(lvStock);
	}
	
	public void updateStatus(){
		try {
			List<Medicion> list = MedicionServicio.traeMediciones();
			if (list.size() > 0){
				tvSync.setText(Integer.toString(list.size()));
			} else {
				tvSync.setText("");
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	protected void onStart(){
		super.onStart();
		
		loading.setVisibility(View.INVISIBLE);
		updateStatus();
	}
	
	private void despliegaCrecimiento(List<Crecimiento> cre){
		double totalSuperficie = 0;
		double crecimiento = 0;
		for (Crecimiento c : cre){
			crecimiento = crecimiento + c.getCrecimiento() * c.getSuperficie();
			totalSuperficie = totalSuperficie + c.getSuperficie();
		}
		crecimiento = roundForDisplay(crecimiento / totalSuperficie);
		tvCrecimiento.setText(Double.toString(crecimiento) + " KgMs/Ha/día");
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
			startActivity(i);
			break;
		}
		
	}
	
	private double roundForDisplay(double click){
		double res = 0;
		res = click * 10;
		res = Math.round(res);
		res = res / 10;
		return res;
	}
	
	private int calcularCobertura(List<StockM> list){
		double cobertura = 0;
		double totalSuperficie = 0;
		for (StockM sm : list){
			cobertura = cobertura + ((double) sm.getMed().getMateriaSeca().intValue() * sm.getMed().getSuperficie());
			totalSuperficie = totalSuperficie + sm.getMed().getSuperficie();
		}
		cobertura = cobertura / totalSuperficie;
		int coberturaPromedio = (int) Math.round(cobertura);
		return coberturaPromedio;
	}
	
	private double calcularClickPromedio(int ms){
		double matSeca = ((double) ms - (double) 500) / (double) 140;
		return roundForDisplay(matSeca);
		
		/*
		int totalClicks = 0;
		double click = 0;
		for (StockM sm : list){
			click = click + sm.getMed().getClick();
			totalClicks++;
		}
		click = click / (double) totalClicks;
		click = roundForDisplay(click);
		return click;
		*/
	}

}
