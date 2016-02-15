package cl.a2r.salvatajes;

import java.util.ArrayList;
import java.util.List;

import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Salvataje;
import cl.ar2.sqlite.servicio.SalvatajesServicio;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class Logs extends Activity implements View.OnClickListener, ListView.OnItemLongClickListener{
	
	private ImageButton goBack, undo;
	private TextView app, deshacer;
	private ListView lvDiios;
	private Button confirmarCambios;
	private Integer grupoId;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_logs);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    grupoId = extras.getInt("grupoId");
		}
		
		cargarInterfaz();
		getLogs();
	}
	
	private void cargarInterfaz(){
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		lvDiios = (ListView)findViewById(R.id.listViewHistorial);
		lvDiios.setOnItemLongClickListener(this);
		undo = (ImageButton)findViewById(R.id.undo);
		undo.setVisibility(View.GONE);
		deshacer = (TextView)findViewById(R.id.deshacer);
		deshacer.setVisibility(View.GONE);
		confirmarCambios = (Button)findViewById(R.id.confirmarCambios);
		confirmarCambios.setVisibility(View.GONE);
		app = (TextView)findViewById(R.id.app);
		app.setText("Historial");
	}
	
	private void getLogs(){
		try {
			List<Ganado> list = SalvatajesServicio.traeDiios(grupoId);
			ArrayAdapter<Ganado> mAdapter = new ArrayAdapter<Ganado>(this, android.R.layout.simple_list_item_1, list);
			lvDiios.setAdapter(mAdapter);
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
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
							Integer id = ((Ganado) arg0.getItemAtPosition(arg2)).getId();
							SalvatajesServicio.deleteDiio(id);
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

	public void onClick(View v) {
		int id = v.getId();
		switch (id){
		case R.id.goBack:
			finish();
			break;
		}
	}
	
}
