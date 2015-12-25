package cl.a2r.alimentacion;

import java.util.Date;

import cl.a2r.common.AppException;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sap.model.Medicion;
import cl.ar2.sqlite.cobertura.CoberturaServicio;
import cl.ar2.sqlite.cobertura.MedicionServicio;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MedicionSemanal extends Activity implements View.OnClickListener, View.OnKeyListener {

	private TextView tvPotrero, tvClick, tvMS;
	private EditText etInicial, etFinal, etMuestras;
	private ImageButton goBack, logs, confirmarEntrada;
	private LinearLayout layoutCalculadora;
	private Integer potreroId;
	private Medicion med;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_medicion_semanal);
		
		cargarInterfaz();
	}
	
	private void cargarInterfaz(){
		etInicial = (EditText)findViewById(R.id.etClickInicial);
		etInicial.setOnKeyListener(this);
		etFinal = (EditText)findViewById(R.id.etClickFinal);
		etFinal.setOnKeyListener(this);
		etMuestras = (EditText)findViewById(R.id.etMuestras);
		etMuestras.setOnKeyListener(this);
		confirmarEntrada = (ImageButton)findViewById(R.id.confirmarEntrada);
		confirmarEntrada.setOnClickListener(this);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		logs = (ImageButton)findViewById(R.id.logs);
		logs.setOnClickListener(this);
		tvPotrero = (TextView)findViewById(R.id.tvPotrero);
		tvPotrero.setOnClickListener(this);
		tvClick = (TextView)findViewById(R.id.tvClick);
		tvMS = (TextView)findViewById(R.id.tvMS);
		layoutCalculadora = (LinearLayout)findViewById(R.id.layoutCalculadora);
		layoutCalculadora.setOnClickListener(this);
		
		med = new Medicion();
		Mediciones.tipoMuestraActual = 3;
	}

	public void onClick(View v) {
		int id = v.getId();
		Intent i;
		switch (id){
		case R.id.goBack:
			finish();
			break;
		case R.id.logs:
			i = new Intent(this, Logs.class);
			startActivity(i);
			break;
		case R.id.confirmarEntrada:
			confirmarEntrada();
			insertaEntrada();
			clearScreen();
			break;
		case R.id.layoutCalculadora:
		case R.id.tvPotrero:
			i = new Intent(this, Calculadora.class);
			startActivity(i);
			break;
		}
	}
	
	private void clearScreen(){
		med = new Medicion();
		potreroId = null;
		Calculadora.potrero = null;
		etInicial.setText("");
		etFinal.setText("");
		etMuestras.setText("");
		updateStatus();
	}
	
	private void confirmarEntrada(){
		med.setCorreo(Login.mail);
		med.setFundoId(Aplicaciones.predioWS.getId());
		med.setTipoMuestraId(3);
		med.setFecha(new Date());
	}
	
	private void insertaEntrada(){
        try {
            MedicionServicio.insertaMedicion(med);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
		
		Toast.makeText(this, "Registro guardado", Toast.LENGTH_LONG).show();
	}
	
	private void updateStatus(){
		med.setPotreroId(potreroId);
		if (!etInicial.getText().toString().equals("") && !etFinal.getText().toString().equals("") && !etMuestras.getText().toString().equals("")){
			med.setClickInicial(Integer.parseInt(etInicial.getText().toString()));
			med.setClickFinal(Integer.parseInt(etFinal.getText().toString()));
			med.setMuestras(Integer.parseInt(etMuestras.getText().toString()));
			med.setClick(roundForDisplay(calculaClick(med)));
			tvClick.setText("Click: " + Double.toString(roundForDisplay(med.getClick())));
			med.setMateriaSeca(calculaMSVerano(calculaClick(med)));
			tvMS.setText("MS: " + Integer.toString(med.getMateriaSeca()));
		} else {
			tvClick.setText("");
			tvMS.setText("");
		}
		
		if (potreroId != null){
			tvPotrero.setText("Potrero: " + Integer.toString(potreroId));
		} else {
			tvPotrero.setText("Potrero:");
		}
		
		if (med.getClickFinal() != null &&
				med.getClickInicial() != null &&
				med.getMuestras() != null &&
				med.getPotreroId() != null){
			
			if (med.getClickInicial().intValue() >= med.getClickFinal().intValue()){
				confirmarEntrada.setEnabled(false);
			} else {
				confirmarEntrada.setEnabled(true);
			}
		} else {
			confirmarEntrada.setEnabled(false);
		}
	}
	
	private double roundForDisplay(double click){
		double res = 0;
		res = click * 10;
		res = Math.round(res);
		res = res / 10;
		return res;
	}
	
	private double calculaClick(Medicion med){
		double res = 0;
		res = ((double)med.getClickFinal() - (double)med.getClickInicial()) / (double)med.getMuestras();
		return res;
	}
	
	private int calculaMSVerano(double click){
		int res = 0;
		res = (int) Math.round(click * (double)140 + (double)500);
		return res;
	}
	
	protected  void onStart(){
		super.onStart();
		
		potreroId = Calculadora.potrero;
		updateStatus();
	}
	
	protected  void onDestroy(){
		super.onStart();
		
		Calculadora.potrero = null;
	}

	public boolean onKey(View v, int keyCode, KeyEvent event) {
		updateStatus();
		return false;
	}

}
