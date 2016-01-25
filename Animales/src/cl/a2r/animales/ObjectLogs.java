package cl.a2r.animales;

import java.util.ArrayList;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ObjectLogs extends Activity implements View.OnClickListener{

	private ImageButton goBack;
	private TextView app;
	private ListView listViewHistorial;
	private Button confirmarCambios;
	private Spinner spinnerMangada;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_object_logs);
	}
	
	private void cargarInterfaz(){
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		listViewHistorial = (ListView)findViewById(R.id.listViewHistorial);
		//listViewHistorial.setOnClickListener(this);
		confirmarCambios = (Button)findViewById(R.id.confirmarCambios);
		confirmarCambios.setOnClickListener(this);
		confirmarCambios.setVisibility(View.INVISIBLE);
		app = (TextView)findViewById(R.id.app);
		spinnerMangada = (Spinner)findViewById(R.id.spinnerMangada);
		
	}
	
	private void cargarListeners(){
		spinnerMangada.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				getDiiosMangada();
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	
	private void getDiiosMangada(){
		
	}

	public void onClick(View arg0) {
		
		
	}

}
