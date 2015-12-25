package cl.a2r.custom;

import java.util.List;

import cl.a2r.alimentacion.Aplicaciones;
import cl.a2r.alimentacion.Login;
import cl.a2r.alimentacion.R;
import cl.a2r.common.AppException;
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
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Calculadora extends Activity implements View.OnClickListener {
	
	private ImageButton goBack, borrar;
	private TextView tvdiio;
	private Button uno, dos, tres, cuatro, cinco, seis, siete, ocho, nueve, cero, aceptar;
	private boolean diioInverso;
	private String diioActual, diioActualInverso;
	
	public static Integer potrero;
	public static String activa ="", sexo = "";
	public static boolean isAreteo = false, isSalvataje = false;
	private String errMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_calculadora);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
		
		cargarInterfaz();
	}

	private void cargarInterfaz(){
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		borrar = (ImageButton)findViewById(R.id.borrar);
		borrar.setOnClickListener(this);
		uno = (Button)findViewById(R.id.uno);
		uno.setOnClickListener(this);
		dos = (Button)findViewById(R.id.dos);
		dos.setOnClickListener(this);
		tres = (Button)findViewById(R.id.tres);
		tres.setOnClickListener(this);
		cuatro = (Button)findViewById(R.id.cuatro);
		cuatro.setOnClickListener(this);
		cinco = (Button)findViewById(R.id.cinco);
		cinco.setOnClickListener(this);
		seis = (Button)findViewById(R.id.seis);
		seis.setOnClickListener(this);
		siete = (Button)findViewById(R.id.siete);
		siete.setOnClickListener(this);
		ocho = (Button)findViewById(R.id.ocho);
		ocho.setOnClickListener(this);
		nueve = (Button)findViewById(R.id.nueve);
		nueve.setOnClickListener(this);
		cero = (Button)findViewById(R.id.cero);
		cero.setOnClickListener(this);
		aceptar = (Button)findViewById(R.id.aceptar);
		aceptar.setOnClickListener(this);
		tvdiio = (TextView)findViewById(R.id.diio);
		
		diioInverso = false;
		
	}
	
	public void onClick(View v) {
		int id = v.getId();
		switch (id){
		case R.id.uno:
			tvdiio.setText(tvdiio.getText() + "1");
			break;
		case R.id.dos:
			tvdiio.setText(tvdiio.getText() + "2");
			break;
		case R.id.tres:
			tvdiio.setText(tvdiio.getText() + "3");
			break;
		case R.id.cuatro:
			tvdiio.setText(tvdiio.getText() + "4");
			break;
		case R.id.cinco:
			tvdiio.setText(tvdiio.getText() + "5");
			break;
		case R.id.seis:
			tvdiio.setText(tvdiio.getText() + "6");
			break;
		case R.id.siete:
			tvdiio.setText(tvdiio.getText() + "7");
			break;
		case R.id.ocho:
			tvdiio.setText(tvdiio.getText() + "8");
			break;
		case R.id.nueve:
			tvdiio.setText(tvdiio.getText() + "9");
			break;
		case R.id.cero:
			tvdiio.setText(tvdiio.getText() + "0");
			break;
		case R.id.aceptar:
			if (tvdiio.getText().toString().equals("") ||
				Integer.parseInt(tvdiio.getText().toString()) == 0){
				
				ShowAlert.showAlert("Error", "Debe ingresar un Potrero", this);
				return;
			}
			potrero = Integer.parseInt(tvdiio.getText().toString());
			if(verificarPotrero()){
				finish();
			}
			break;
		case R.id.goBack:
			finish();
			break;
		case R.id.borrar:
			if (tvdiio.getText().length() > 0){
				tvdiio.setText(tvdiio.getText().toString().substring(0, tvdiio.getText().length() - 1));
			}
			break;
		
		}
	}
	
	private boolean verificarPotrero(){
		if (Aplicaciones.predioWS.getPotreros() < potrero){
			ShowAlert.showAlert("Error", "El Potrero ingresado no existe", this);
			potrero = null;
			return false;
		}
		return true;
	}
	
	private boolean validarDiioInverso(){
		for (int i = 0; i < diioActual.length(); i++){
			if (diioActual.charAt(i) != diioActualInverso.charAt(diioActualInverso.length() - 1 - i)){
				return false;
			}
		}
		return true;
	}
	
	protected  void onStart(){
		super.onStart();
	}


}
