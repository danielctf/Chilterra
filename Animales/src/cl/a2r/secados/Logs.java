package cl.a2r.secados;

import java.util.ArrayList;
import java.util.List;

import cl.a2r.animales.Aplicaciones;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Secado;
import cl.a2r.sip.model.VRB51;
import cl.ar2.sqlite.servicio.SecadosServicio;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Logs extends Activity implements View.OnClickListener, ListView.OnItemLongClickListener{

	private ImageButton goBack;
	private TextView app;
	private ListView listViewHistorial;
	private Spinner spinnerMangada;
	private Integer mangadaActual;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_mangada_logs);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mangadaActual = extras.getInt("mangadaActual");
		}
		
		cargarInterfaz();
		cargarListeners();
		getMangadas();

	}
	
	private void cargarInterfaz(){
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		listViewHistorial = (ListView)findViewById(R.id.listViewHistorial);
		listViewHistorial.setOnItemLongClickListener(this);
		app = (TextView)findViewById(R.id.app);
		app.setText("Historial");
		spinnerMangada = (Spinner)findViewById(R.id.spinnerMangada);
	}
	
	private void cargarListeners(){
		spinnerMangada.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				mangadaActual = arg2;
				getLogs();
			}
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
	}
	
	private void getMangadas(){
		String[] items = new String[mangadaActual + 1];
		items[0] = "Sincronizados";
		for (int i = 0; i < mangadaActual; i++){
			items[i + 1] = "Mangada " + Integer.toString(i + 1);
		}
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
		spinnerMangada.setAdapter(mAdapter);
		spinnerMangada.setSelection(mangadaActual.intValue());
	}
	
	private void getLogs(){
		try {
			List<Secado> list = SecadosServicio.traeGanadoSecado(Aplicaciones.predioWS.getId());
			List<Secado> toShow = new ArrayList<Secado>();
			for (Secado s : list){
				if (spinnerMangada.getSelectedItemPosition() == 0 && s.getGan().getMangada() == null){
					toShow.add(s);
				} else if (s.getGan().getMangada() != null && s.getGan().getMangada().intValue() == mangadaActual.intValue()){
					toShow.add(s);
				}
			}
			ArrayAdapter<Secado> mAdapter = new ArrayAdapter<Secado>(this, android.R.layout.simple_list_item_1, toShow);
			listViewHistorial.setAdapter(mAdapter);
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

	public boolean onItemLongClick(final AdapterView<?> arg0, View arg1, final int arg2,
			long arg3) {
		
		int id = arg0.getId();
		switch (id){
		case R.id.listViewHistorial:
			ShowAlert.askYesNo("Borrar", "¿Está seguro de borrar el DIIO seleccionado?", this, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if (which == -2){
						try {
							String sincronizado = ((Secado) arg0.getItemAtPosition(arg2)).getSincronizado();
							if (sincronizado.equals("N")){
								Integer id = ((Secado) arg0.getItemAtPosition(arg2)).getId();
								SecadosServicio.deleteGanadoSecado(id);
							} else {
								ShowAlert.showAlert("Error", "No puede eliminar registros sincronizados", Logs.this);
							}
						} catch (AppException e) {
							ShowAlert.showAlert("Error", e.getMessage(), Logs.this);
						} finally {
							getLogs();
						}
					}
				}
			});
			break;
		}
		return false;
	}

}
