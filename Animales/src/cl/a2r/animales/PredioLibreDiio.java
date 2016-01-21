package cl.a2r.animales;

import java.util.List;

import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.wsservice.WSPredioLibreCliente;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PredioLibreDiio extends Activity implements View.OnClickListener{

	private Fragment frInyeccionTB, frBrucelosis;
	private TextView app, tvFaltantes, tvEncontrados;
	private ImageButton goBack;
	private Button btnInyeccion, btnMuestra;
	private RelativeLayout container;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_predio_libre);
		
		cargarInterfaz();
	}
	
	private void cargarInterfaz(){
		app = (TextView)findViewById(R.id.app);
		tvFaltantes = (TextView)findViewById(R.id.tvFaltantes);
		tvEncontrados = (TextView)findViewById(R.id.tvEncontrados);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		container = (RelativeLayout)findViewById(R.id.container);
		btnInyeccion = (Button)findViewById(R.id.btnInyeccion);
		btnInyeccion.setOnClickListener(this);
		btnMuestra = (Button)findViewById(R.id.btnMuestra);
		btnMuestra.setOnClickListener(this);
		frInyeccionTB = new PredioLibreInyeccionTB(this);
		frBrucelosis = new PredioLibreBrucelosis(this);
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container, frInyeccionTB);
		btnInyeccion.setBackgroundResource(R.drawable.tab_state_activated);
		btnMuestra.setBackgroundResource(R.drawable.tab_state_deactivated);
		transaction.commit();
		getFragmentManager().executePendingTransactions();
	}

	public void onClick(View arg0) {
		int id = arg0.getId();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		switch (id){
		case R.id.goBack:
			finish();
			break;
		case R.id.btnInyeccion:
			transaction.replace(R.id.container, frInyeccionTB);
			btnInyeccion.setBackgroundResource(R.drawable.tab_state_activated);
			btnMuestra.setBackgroundResource(R.drawable.tab_state_deactivated);
			transaction.commit();
			getFragmentManager().executePendingTransactions();
			break;
		case R.id.btnMuestra:
			transaction.replace(R.id.container, frBrucelosis);
			btnInyeccion.setBackgroundResource(R.drawable.tab_state_deactivated);
			btnMuestra.setBackgroundResource(R.drawable.tab_state_activated);
			transaction.commit();
			getFragmentManager().executePendingTransactions();
			break;
		}
	}
	
	protected  void onStart(){
		super.onStart();
		
		if (Login.user == 0){
			finish();
		}
		
	}

}
