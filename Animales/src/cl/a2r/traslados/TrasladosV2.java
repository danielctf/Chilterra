package cl.a2r.traslados;

import cl.a2r.animales.R;
import cl.a2r.animales.TrasladosSalida;
import cl.a2r.sip.model.Persona;
import cl.a2r.sip.model.Predio;
import cl.a2r.sip.model.Transportista;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class TrasladosV2 extends Activity implements View.OnClickListener{

	private Fragment frEncabezado, frAnimales;
	private Button btnEncabezado, btnAnimales;
	
	private Spinner spinnerOrigen, spinnerDestino, spinnerTipoTransporte, spinnerTransportista, spinnerChofer, spinnerCamion, spinnerAcoplado;
	private TextView despliegaGD, tvApp, deshacer;
	private TextView tvVacas, tvVaquillas, tvTerneras, tvToros, tvToretes, tvTerneros, tvAnimales, tvBueyes;
	private TextView tvChofer, tvCamion, tvAcoplado, tvTransportista;
	private LinearLayout layoutAnimales;
	private ImageButton goBack, undo, confirmarMovimiento;
	private ProgressBar loading;
	private boolean hayTransportista;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_traslado_salida_v2);
		
		cargarInterfaz();
	}

	private void cargarInterfaz(){
		frEncabezado = new Encabezado();
		frAnimales = new Animales();
		
		btnEncabezado = (Button)findViewById(R.id.btnEncabezado);
		btnEncabezado.setOnClickListener(this);
		btnAnimales = (Button)findViewById(R.id.btnAnimales);
		btnAnimales.setOnClickListener(this);
		
		btnEncabezado.performClick();
		/*
		tvVacas = (TextView)findViewById(R.id.tvVacas);
		tvVaquillas = (TextView)findViewById(R.id.tvVaquillas);
		tvTerneras = (TextView)findViewById(R.id.tvTerneras);
		tvToros = (TextView)findViewById(R.id.tvToros);
		tvToretes = (TextView)findViewById(R.id.tvToretes);
		tvTerneros = (TextView)findViewById(R.id.tvTerneros);
		tvAnimales = (TextView)findViewById(R.id.tvAnimales);
		tvBueyes = (TextView)findViewById(R.id.tvBueyes);
		tvVacas.setOnClickListener(this);
		tvVaquillas.setOnClickListener(this);
		tvTerneras.setOnClickListener(this);
		tvToros.setOnClickListener(this);
		tvToretes.setOnClickListener(this);
		tvTerneros.setOnClickListener(this);
		tvAnimales.setOnClickListener(this);
		layoutAnimales = (LinearLayout)findViewById(R.id.layoutAnimales);
		layoutAnimales.setOnClickListener(this);
		tvTransportista = (TextView)findViewById(R.id.tvTransportista);
		tvChofer = (TextView)findViewById(R.id.tvChofer);
		tvCamion = (TextView)findViewById(R.id.tvCamion);
		tvAcoplado = (TextView)findViewById(R.id.tvAcoplado);
		
		spinnerChofer = (Spinner)findViewById(R.id.spinnerChofer);
		spinnerCamion = (Spinner)findViewById(R.id.spinnerCamion);
		spinnerAcoplado = (Spinner)findViewById(R.id.spinnerAcoplado);
		spinnerTransportista = (Spinner)findViewById(R.id.spinnerTransportista);
		spinnerTipoTransporte = (Spinner)findViewById(R.id.spinnerTipoTransporte);
		spinnerOrigen = (Spinner)findViewById(R.id.spinnerOrigen);
		spinnerDestino = (Spinner)findViewById(R.id.spinnerDestino);
		despliegaGD = (TextView)findViewById(R.id.despliegaGD);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		undo = (ImageButton)findViewById(R.id.undo);
		undo.setOnClickListener(this);
		tvApp = (TextView)findViewById(R.id.app);
		deshacer = (TextView)findViewById(R.id.deshacer);
		deshacer.setOnClickListener(this);
		confirmarMovimiento = (ImageButton)findViewById(R.id.confirmarMovimiento);
		confirmarMovimiento.setOnClickListener(this);
		loading = (ProgressBar)findViewById(R.id.loading);
		*/
	}
	
	/*
	private void cargarListeners(){
		spinnerTipoTransporte.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				switch (arg2){
				case 0:
					//Con Camión
					tvChofer.setVisibility(View.VISIBLE);
					spinnerChofer.setVisibility(View.VISIBLE);
					tvCamion.setVisibility(View.VISIBLE);
					spinnerCamion.setVisibility(View.VISIBLE);
					tvAcoplado.setVisibility(View.VISIBLE);
					spinnerAcoplado.setVisibility(View.VISIBLE);
					tvTransportista.setText("Transportista");
					
					ArrayAdapter<Transportista> mAdapter = new ArrayAdapter<Transportista>(TrasladosSalida.this, android.R.layout.simple_list_item_1, transportistas);
					spinnerTransportista.setAdapter(mAdapter);
					
					trasladoSalida.setArrieroId(null);
					hayTransportista = true;
					break;
				case 1:
					//Sin Camión
					tvChofer.setVisibility(View.INVISIBLE);
					spinnerChofer.setVisibility(View.INVISIBLE);
					tvCamion.setVisibility(View.INVISIBLE);
					spinnerCamion.setVisibility(View.INVISIBLE);
					tvAcoplado.setVisibility(View.INVISIBLE);
					spinnerAcoplado.setVisibility(View.INVISIBLE);
					tvTransportista.setText("Arriero");
					
					ArrayAdapter<Persona> mAdapter2 = new ArrayAdapter<Persona>(TrasladosSalida.this, android.R.layout.simple_list_item_1, arrieros);
					spinnerTransportista.setAdapter(mAdapter2);
					
					trasladoSalida.setTransportistaId(null);
					trasladoSalida.setChoferId(null);
					trasladoSalida.setCamionId(null);
					trasladoSalida.setAcopladoId(null);
					hayTransportista = false;
					break;
				}
				updateStatus();
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
	}
*/
	@Override
	public void onClick(View v) {
		int id = v.getId();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		switch (id){
		case R.id.btnEncabezado:
			transaction.replace(R.id.container, frEncabezado);
			btnEncabezado.setBackgroundResource(R.drawable.tab_state_activated);
			btnAnimales.setBackgroundResource(R.drawable.tab_state_deactivated);
			transaction.commit();
			getFragmentManager().executePendingTransactions();
			break;
		case R.id.btnAnimales:
			transaction.replace(R.id.container, frAnimales);
			btnEncabezado.setBackgroundResource(R.drawable.tab_state_deactivated);
			btnAnimales.setBackgroundResource(R.drawable.tab_state_activated);
			transaction.commit();
			getFragmentManager().executePendingTransactions();
			break;
		}
		
	}
	
}
