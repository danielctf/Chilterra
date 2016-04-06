package cl.a2r.busquedas;

import java.util.List;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.GanadoBusqueda;
import cl.ar2.sqlite.servicio.BusquedasServicio;

public class Logs extends Activity implements View.OnClickListener{

	private ImageButton goBack;
	private TextView app;
	private ListView listViewHistorial;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_mangada_logs);
		
		cargarInterfaz();
		getLogs();
	}
	
	private void cargarInterfaz(){
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		listViewHistorial = (ListView)findViewById(R.id.listViewHistorial);
		app = (TextView)findViewById(R.id.app);
		app.setText("Candidatos Encontrados");
	}
	
	private void getLogs(){
		try {
			List<GanadoBusqueda> list = BusquedasServicio.traeGanBusqueda();
			ArrayAdapter<GanadoBusqueda> mAdapter = new ArrayAdapter<GanadoBusqueda>(this, android.R.layout.simple_list_item_1, list);
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
	
}
