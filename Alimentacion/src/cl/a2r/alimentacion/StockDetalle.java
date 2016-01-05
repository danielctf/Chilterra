package cl.a2r.alimentacion;

import java.text.SimpleDateFormat;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.custom.ShowAlert;
import cl.a2r.custom.StockDetalleAdapter;
import cl.a2r.custom.Utility;
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
	private TextView tvPotrero, tvCobertura, tvUpdate, tvFundo, tvClick;

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
			
			tvCobertura.setText(Integer.toString(calcularCobertura(list)) + " KgMs/Ha");
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			tvUpdate.setText("Últ. Actualización\n" + df.format(list.get(0).getActualizado()));
			
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

}
