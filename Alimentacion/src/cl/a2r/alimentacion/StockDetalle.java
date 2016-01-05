package cl.a2r.alimentacion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.custom.ShowAlert;
import cl.a2r.custom.StockDetalleAdapter;
import cl.a2r.custom.Utility;
import cl.a2r.sap.model.Medicion;
import cl.ar2.sqlite.cobertura.Crecimiento;
import cl.ar2.sqlite.cobertura.MedicionServicio;
import cl.ar2.sqlite.cobertura.StockM;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class StockDetalle extends Activity implements View.OnClickListener, ListView.OnItemClickListener{
	
	private ListView lvStock;
	private ImageButton goBack;
	private TextView tvPotrero, tvCobertura, tvUpdate, tvFundo, tvClick, tvCrecimiento;
	private int cobertura;

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
		}
		
		cargarInterfaz();
		getStockPotrero(g_fundo_id, numero);
		tvFundo.setText(Aplicaciones.predioWS.getCodigo());
		tvPotrero.setText("P" + Integer.toString(numero));
	}
	
	private void cargarInterfaz(){
		lvStock = (ListView)findViewById(R.id.lvStock);
		lvStock.setOnItemClickListener(this);
		lvStock.setFocusable(false);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		tvCobertura = (TextView)findViewById(R.id.tvCobertura);
		tvUpdate = (TextView)findViewById(R.id.tvUpdate);
		tvFundo = (TextView)findViewById(R.id.tvFundo);
		tvPotrero = (TextView)findViewById(R.id.tvPotrero);
		tvClick = (TextView)findViewById(R.id.tvClick);
		tvCrecimiento = (TextView)findViewById(R.id.tvCrecimiento);
	}
	
	private void getStockPotrero(Integer g_fundo_id, Integer numero){
		try {
			List<StockM> list = MedicionServicio.traeStockPotrero(Stock.list, g_fundo_id, numero);
			for (StockM sm : list){
				double click = ((double) sm.getMed().getClickFinal().intValue() - (double) sm.getMed().getClickInicial().intValue()) / (double) sm.getMed().getMuestras().intValue();
				sm.getMed().setClick(roundForDisplay(click));
			}
			if (list.size() == 0){
				return;
			} else {
				System.out.println(list.size());
			}
			StockDetalleAdapter sAdapter = new StockDetalleAdapter(this, list);
			lvStock.setAdapter(sAdapter);
			
			cobertura = calcularCobertura(list);
			tvCobertura.setText(Integer.toString(cobertura) + " KgMs/Ha");
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			tvUpdate.setText(/*"�lt. Actualizaci�n\n" + */df.format(list.get(0).getActualizado()));
			tvClick.setText(Double.toString(calcularClickPromedio(cobertura)) + " Click");
			
			calcularCrecimiento(g_fundo_id, numero);
			Utility.setListViewHeightBasedOnChildren(lvStock);
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id){
		case R.id.goBack:
			finish();
			break;
		}
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
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
		tvCrecimiento.setText(Double.toString(crecimiento) + " KgMs/Ha/d�a");
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
	}

}
