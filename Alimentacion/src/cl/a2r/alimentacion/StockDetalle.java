package cl.a2r.alimentacion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.alimentacion.R.color;
import cl.a2r.alimentacion.R.drawable;
import cl.a2r.common.AppException;
import cl.a2r.custom.ActionItem;
import cl.a2r.custom.QuickAction;
import cl.a2r.custom.ShowAlert;
import cl.a2r.custom.StockAdapter;
import cl.a2r.custom.StockDetalleAdapter;
import cl.a2r.custom.Utility;
import cl.a2r.sap.model.Calificacion;
import cl.a2r.sap.model.Medicion;
import cl.ar2.sqlite.cobertura.Crecimiento;
import cl.ar2.sqlite.cobertura.MedicionServicio;
import cl.ar2.sqlite.cobertura.StockM;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class StockDetalle extends Activity implements View.OnClickListener, ListView.OnItemClickListener, View.OnTouchListener, QuickAction.OnActionItemClickListener{
	
	private ListView lvStock;
	private ImageButton goBack, addMed, sort;
	private ImageButton btnEntrada, btnResiduo, btnControl, btnSemanal, btnCalificacion;
	private TextView tvPotrero, tvCobertura, tvUpdate, tvFundo, tvClick, tvCrecimiento, tvEntrada, tvResiduo, tvControl, tvSemanal, tvSuperficie, tvCalificacion, tvCalificacionEntr;
	private LinearLayout layoutCalculadora;
	private RelativeLayout toolbar;
	private ScrollView sv1;
	private String stance;
	private int cobertura, numero;
	private double superficie;
	private List<StockM> list, listaMediciones;
	private String[] filterItems = {"Entrada", "Residuo", "Semanal", "Control"};
	private boolean[] filterChecked = {true, true, true, true};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_stock_detalle);
		
		Bundle extras = getIntent().getExtras();
		Integer g_fundo_id = null;
		Integer numero = null;
		if (extras != null) {
		    g_fundo_id = extras.getInt("g_fundo_id");
		    numero = extras.getInt("numero");
		    cobertura = extras.getInt("ms");
		    this.numero = numero;
		    this.superficie = extras.getDouble("superficie");
		}
		
		cargarInterfaz();
		getStockPotrero(g_fundo_id, numero);
		tvFundo.setText(Aplicaciones.predioWS.getCodigo());
		tvPotrero.setText("P" + Integer.toString(numero));
		tvSuperficie.setText(Double.toString(this.superficie) + " Hás");
		updateCalificacion();
	}
	
	private void cargarInterfaz(){
		lvStock = (ListView)findViewById(R.id.lvStock);
		lvStock.setOnItemClickListener(this);
		lvStock.setOnTouchListener(this);
		lvStock.setFocusable(false);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		addMed = (ImageButton)findViewById(R.id.addMed);
		addMed.setOnClickListener(this);
		tvCobertura = (TextView)findViewById(R.id.tvCobertura);
		tvUpdate = (TextView)findViewById(R.id.tvUpdate);
		tvFundo = (TextView)findViewById(R.id.tvFundo);
		tvPotrero = (TextView)findViewById(R.id.tvPotrero);
		tvClick = (TextView)findViewById(R.id.tvClick);
		tvCrecimiento = (TextView)findViewById(R.id.tvCrecimiento);
		btnEntrada = (ImageButton)findViewById(R.id.btnEntrada);
		btnEntrada.setOnClickListener(this);
		btnResiduo = (ImageButton)findViewById(R.id.btnResiduo);
		btnResiduo.setOnClickListener(this);
		btnControl = (ImageButton)findViewById(R.id.btnControl);
		btnControl.setOnClickListener(this);
		btnSemanal = (ImageButton)findViewById(R.id.btnSemanal);
		btnSemanal.setOnClickListener(this);
		btnCalificacion = (ImageButton)findViewById(R.id.btnCalificacion);
		btnCalificacion.setOnClickListener(this);
		sort = (ImageButton)findViewById(R.id.sort);
		sort.setOnClickListener(this);
		layoutCalculadora = (LinearLayout)findViewById(R.id.layoutCalculadora);
		layoutCalculadora.setOnTouchListener(this);
		toolbar = (RelativeLayout)findViewById(R.id.toolbar);
		toolbar.setOnTouchListener(this);
		sv1 = (ScrollView)findViewById(R.id.scrollView1);
		sv1.setOnTouchListener(this);
		tvEntrada = (TextView)findViewById(R.id.tvEntrada);
		tvResiduo = (TextView)findViewById(R.id.tvResiduo);
		tvControl = (TextView)findViewById(R.id.tvControl);
		tvSemanal = (TextView)findViewById(R.id.tvSemanal);
		tvSuperficie = (TextView)findViewById(R.id.tvSuperficie);
		tvCalificacion = (TextView)findViewById(R.id.tvCalificacion);
		tvCalificacionEntr = (TextView)findViewById(R.id.tvCalificacionEntr);
	}
	
	private void getStockPotrero(Integer g_fundo_id, Integer numero){
		try {
			list = MedicionServicio.traeStockPotrero(Stock.list, g_fundo_id, numero);
			for (StockM sm : list){
				double click = ((double) sm.getMed().getClickFinal().intValue() - (double) sm.getMed().getClickInicial().intValue()) / (double) sm.getMed().getMuestras().intValue();
				sm.getMed().setClick(roundForDisplay(click));
			}
			if (list.size() == 0){
				return;
			}
			StockDetalleAdapter sAdapter = new StockDetalleAdapter(this, list);
			lvStock.setAdapter(sAdapter);
			listaMediciones = list;
			
			tvCobertura.setText(Integer.toString(cobertura) + " KgMs/Ha");
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			tvUpdate.setText(/*"Últ. Actualización\n" + */df.format(list.get(0).getActualizado()));
			tvClick.setText(Double.toString(calcularClickPromedio(cobertura)) + " Click");
			
			calcularCrecimiento(g_fundo_id, numero);
			Utility.setListViewHeightBasedOnChildren(lvStock);
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}

	public void onClick(View v) {
		int id = v.getId();
		Intent i;
		switch (id){
		case R.id.goBack:
			finish();
			break;
		case R.id.addMed:
			if (stance.equals("menu")){
				resetMenu();
			} else if (stance.equals("noMenu")){
				showMenu();
			}
			break;
		case R.id.btnEntrada:
			i = new Intent(this, MedicionEntrada.class);
			i.putExtra("numeroPotrero", numero);
			i.putExtra("superficie", superficie);
			startActivity(i);
			break;
		case R.id.btnResiduo:
			i = new Intent(this, MedicionResiduo.class);
			i.putExtra("numeroPotrero", numero);
			startActivity(i);
			break;
		case R.id.btnSemanal:
			i = new Intent(this, MedicionSemanal.class);
			i.putExtra("numeroPotrero", numero);
			startActivity(i);
			break;
		case R.id.btnControl:
			i = new Intent(this, MedicionControl.class);
			i.putExtra("numeroPotrero", numero);
			startActivity(i);
			break;
		case R.id.sort:
			String[] items = {"Filtrar por"};
			ActionItem t1 = new ActionItem(1, items);
			QuickAction quickAction = new QuickAction(this, QuickAction.VERTICAL);
			quickAction.setOnActionItemClickListener(this);
			quickAction.addActionItem(t1);
			quickAction.show(v);
			break;
		case R.id.btnCalificacion:
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
			LayoutInflater inflater = this.getLayoutInflater();
			View dialogView = inflater.inflate(R.layout.numberpicker, null);
			dialogBuilder.setView(dialogView);
			final NumberPicker np1 = (NumberPicker)dialogView.findViewById(R.id.np1);
			np1.setMinValue(1);
			np1.setMaxValue(10);
			np1.setWrapSelectorWheel(false);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					List<Calificacion> calList = new ArrayList<Calificacion>();
					Calificacion cal = new Calificacion();
					cal.setG_fundo_id(Aplicaciones.predioWS.getId());
					cal.setNumero(numero);
					cal.setCalificacion(np1.getValue());
					cal.setSincronizado("N");
					calList.add(cal);
					try {
						dialog.dismiss();
						resetMenu();
						MedicionServicio.insertaCalificacion(calList);
						updateCalificacion();
					} catch (AppException e) {
						ShowAlert.showAlert("Error", e.getMessage(), StockDetalle.this);
					}
				}
			});
			AlertDialog alertDialog = dialogBuilder.create();
			alertDialog.setTitle("Calificación");
			alertDialog.show();
			break;
		}
	}
	
	private void updateCalificacion(){
		try {
			List<Calificacion> list = MedicionServicio.traeCalificacion();
			for (Calificacion c : list){
				if (c.getG_fundo_id().intValue() == Aplicaciones.predioWS.getId().intValue() &&
						c.getNumero().intValue() == numero){
					
					tvCalificacion.setText("Calificación: " + Integer.toString(c.getCalificacion()));
				}
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
	}
	
	public void onItemClick(QuickAction source, int pos, int actionId, int lvPos) {
		if (lvPos == 0){
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
	
	private void filtrarPor(){
		List<StockM> filterList = new ArrayList<StockM>();
		List<StockM> potrerosNull = new ArrayList<StockM>();
		boolean mostrarNulos = true;
		for (int i = 0; i < filterChecked.length; i++){
			if (filterChecked[i]){
				for (StockM sm : list){
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
		listaMediciones = filterList;
		StockDetalleAdapter sAdapter = new StockDetalleAdapter(this, listaMediciones);
		lvStock.setAdapter(sAdapter);
		Utility.setListViewHeightBasedOnChildren(lvStock);
		ordenarPor(1);
	}
	
	private void ordenarPor(int which){
		if (list == null){
			return;
		}
		StockDetalleAdapter sAdapter;
		switch (which){
		case 1:
			//Id
	        for (int i = 0; i < listaMediciones.size(); i++){
	        	for (int j = 0; j < listaMediciones.size(); j++){
	        		if (listaMediciones.get(j).getMed().getId().intValue() < listaMediciones.get(i).getMed().getId().intValue()){
	        			StockM temp = listaMediciones.get(i);
	        			listaMediciones.set(i, listaMediciones.get(j));
	        			listaMediciones.set(j, temp);
	        		}
	        	}
	        }
			break;
		}
		Utility.setListViewHeightBasedOnChildren(lvStock);
	}
	
	private void calcularCrecimiento(Integer g_fundo_id, Integer numero){
		try {
			List<StockM> listFiltrada = MedicionServicio.traeStockCrecimiento(Stock.list, g_fundo_id, numero);
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
	
	private double roundForDisplay(double click){
		double res = 0;
		res = click * 10;
		res = Math.round(res);
		res = res / 10;
		return res;
	}
	
	private double calcularClickPromedio(int ms){
		double matSeca = ((double) ms - (double) 500) / (double) 140;
		return roundForDisplay(matSeca);
	}
	
	private void showMenu(){
		btnEntrada.setVisibility(View.VISIBLE);
		btnResiduo.setVisibility(View.VISIBLE);
		btnSemanal.setVisibility(View.VISIBLE);
		btnControl.setVisibility(View.VISIBLE);
		tvEntrada.setVisibility(View.VISIBLE);
		tvResiduo.setVisibility(View.VISIBLE);
		tvControl.setVisibility(View.VISIBLE);
		tvSemanal.setVisibility(View.VISIBLE);
		tvCalificacionEntr.setVisibility(View.VISIBLE);
		btnCalificacion.setVisibility(View.VISIBLE);
		Animation controlAnim = AnimationUtils.loadAnimation(this, R.anim.controlappear);
		Animation residuoAnim = AnimationUtils.loadAnimation(this, R.anim.residuoappear);
		Animation semanalAnim = AnimationUtils.loadAnimation(this, R.anim.semanalappear);
		Animation entradaAnim = AnimationUtils.loadAnimation(this, R.anim.entradaappear);
		Animation calificacionAnim = AnimationUtils.loadAnimation(this, R.anim.calificacionappear);
		btnControl.startAnimation(controlAnim);
		tvControl.startAnimation(controlAnim);
		btnResiduo.startAnimation(residuoAnim);
		tvResiduo.startAnimation(residuoAnim);
		btnEntrada.startAnimation(entradaAnim);
		tvEntrada.startAnimation(entradaAnim);
		tvSemanal.startAnimation(semanalAnim);
		btnSemanal.startAnimation(semanalAnim);
		tvCalificacionEntr.setAnimation(calificacionAnim);
		btnCalificacion.setAnimation(calificacionAnim);
		lvStock.setAlpha(0.1f);
		layoutCalculadora.setAlpha(0.1f);
		toolbar.setAlpha(0.1f);
		stance = "menu";
		
		addMed.setImageResource(R.drawable.ic_clear_white_36dp);
		addMed.setBackgroundResource(drawable.circlebutton_cancelar);
	}
	
	private void resetMenu(){
		btnEntrada.setVisibility(View.INVISIBLE);
		btnResiduo.setVisibility(View.INVISIBLE);
		btnControl.setVisibility(View.INVISIBLE);
		tvEntrada.setVisibility(View.INVISIBLE);
		tvResiduo.setVisibility(View.INVISIBLE);
		tvControl.setVisibility(View.INVISIBLE);
		tvSemanal.setVisibility(View.INVISIBLE);
		btnSemanal.setVisibility(View.INVISIBLE);
		tvCalificacionEntr.setVisibility(View.INVISIBLE);
		btnCalificacion.setVisibility(View.INVISIBLE);
		lvStock.setAlpha(1);
		layoutCalculadora.setAlpha(1);
		toolbar.setAlpha(1);
		addMed.setImageResource(R.drawable.ic_add_white_36dp);
		addMed.setBackgroundResource(drawable.circlebutton_green);
		stance = "noMenu";
	}
	
	protected  void onStart(){
		super.onStart();
		
		resetMenu();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (stance.equals("menu")){
			resetMenu();
		}
		return false;
	}

}
