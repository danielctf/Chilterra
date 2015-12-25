package cl.a2r.alimentacion;

import java.util.List;

import cl.a2r.custom.ShowAlert;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class Candidatos extends Activity implements View.OnClickListener, ListView.OnItemClickListener{

	private ListView lvCandidatos;
	private TextView tvApp;
	private ImageButton goBack;
	private String clickStance;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_candidatos);
		
		Bundle extras = getIntent().getExtras();
		String stance = null;
		if (extras != null) {
		    stance = extras.getString("stance");
		}
		
		cargarInterfaz();
		getCandidatosWS(stance);
	}
	
	private void cargarInterfaz(){
		lvCandidatos = (ListView)findViewById(R.id.lvCandidatos);
		lvCandidatos.setOnItemClickListener(this);
		tvApp = (TextView)findViewById(R.id.app);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		clickStance = "";
	}
	
	@SuppressWarnings("unchecked")
	private void getCandidatosWS(String stance){
		switch (stance){

		}
		
	}
	
	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id){
		case R.id.goBack:
			finish();
			break;
		}
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (clickStance){

		}
		
	}
	
	protected void onStart(){
		super.onStart();
		
		if (Login.user == 0){
			finish();
		}
	}
	
}
