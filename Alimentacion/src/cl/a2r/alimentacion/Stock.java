package cl.a2r.alimentacion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.custom.ShowAlert;
import cl.a2r.custom.StockAdapter;
import cl.a2r.custom.Utility;
import cl.a2r.sap.model.Medicion;
import cl.a2r.sap.wsservice.WSMedicionCliente;
import cl.ar2.sqlite.cobertura.MedicionServicio;
import cl.ar2.sqlite.cobertura.RegistroMedicion;
import cl.ar2.sqlite.cobertura.StockM;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class Stock extends Activity implements View.OnClickListener, ListView.OnItemClickListener{

	private ListView lvStock;
	private ImageButton goBack, update;
	private TextView tvCobertura, tvUpdate, tvFundo;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_stock);
		
		cargarInterfaz();
		getStock();
		Utility.setListViewHeightBasedOnChildren(lvStock);
		
	}
	
	private void cargarInterfaz(){
		lvStock = (ListView)findViewById(R.id.lvStock);
		lvStock.setOnItemClickListener(this);
		lvStock.setFocusable(false);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		update = (ImageButton)findViewById(R.id.update);
		update.setOnClickListener(this);
		tvCobertura = (TextView)findViewById(R.id.tvCobertura);
		tvUpdate = (TextView)findViewById(R.id.tvUpdate);
		tvFundo = (TextView)findViewById(R.id.tvFundo);
	}
	
	public void onClick(View v) {
		int id = v.getId();
		switch (id){
		case R.id.goBack:
			finish();
			break;
		case R.id.update:
			update();
			break;
		}
	}
	
	private void getStock(){
		try {
			tvFundo.setText(Aplicaciones.predioWS.getCodigo());
			List<StockM> list2 = MedicionServicio.traeStock(Aplicaciones.predioWS.getId());
			for (StockM sm : list2){
				System.out.println(sm.getId());
				double click = ((double) sm.getMed().getClickFinal().intValue() - (double) sm.getMed().getClickInicial().intValue()) / (double) sm.getMed().getMuestras().intValue();
				sm.getMed().setClick(roundForDisplay(click));
			}
			if (list2.size() == 0){
				return;
			}
			StockAdapter sAdapter = new StockAdapter(this, list2);
			lvStock.setAdapter(sAdapter);
			
			tvCobertura.setText(Integer.toString(calcularCobertura(list2)) + " KgMs/Ha");
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			tvUpdate.setText("Últ. Actualización\n" + df.format(list2.get(0).getActualizado()));
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
	}
	
    Handler hand = new Handler();
    Runnable run = new Runnable() {
        public void run() { 
    		try {
    			List<Medicion> list = WSMedicionCliente.traeStock();
    			MedicionServicio.deleteStock();
    			MedicionServicio.insertaStock(list);
    			ShowAlert.showAlert("Actualización", "Stock Actualizado", Stock.this);
    			getStock();
    		} catch (AppException e) {
    			ShowAlert.showAlert("Error", e.getMessage(), Stock.this);
    		} finally {
    			update.setVisibility(View.VISIBLE);
    		}
        }
    }; 
	
	private void update(){
		update.setVisibility(View.INVISIBLE);
		hand.postDelayed(run, 100);
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int id = arg0.getId();
		switch (id){
		case R.id.lvStock:
			
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

}
