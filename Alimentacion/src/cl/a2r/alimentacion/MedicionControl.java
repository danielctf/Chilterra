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

public class MedicionControl extends Activity implements View.OnClickListener, View.OnKeyListener {

	private TextView tvClick, tvMS, tvFundo;
	private EditText etPotrero, etInicial, etFinal, etMuestras;
	private ImageButton goBack, logs, confirmarEntrada;
	private Medicion med;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_medicion_control);
		
		cargarInterfaz();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Integer numeroPotrero = extras.getInt("numeroPotrero");
			etPotrero.setText(Integer.toString(numeroPotrero));
		}
	}
	
	private void cargarInterfaz(){
		etPotrero = (EditText)findViewById(R.id.etPotrero);
		etPotrero.setOnKeyListener(this);
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
		tvClick = (TextView)findViewById(R.id.tvClick);
		tvMS = (TextView)findViewById(R.id.tvMS);
		tvFundo = (TextView)findViewById(R.id.tvFundo);
		tvFundo.setText(Aplicaciones.predioWS.getCodigo());
		
		med = new Medicion();
		Mediciones.tipoMuestraActual = 4;
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
		}
	}
	
	private void clearScreen(){
		med = new Medicion();
		Calculadora.potrero = null;
		etPotrero.setText("");
		etInicial.setText("");
		etFinal.setText("");
		etMuestras.setText("");
		updateStatus();
	}
	
	private void confirmarEntrada(){
		med.setCorreo(Login.mail);
		med.setFundoId(Aplicaciones.predioWS.getId());
		med.setTipoMuestraId(4);
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
		String etPot = etPotrero.getText().toString();
		if (!etPot.equals("")){
			if (Integer.parseInt(etPot) == 0 || Integer.parseInt(etPot) > Aplicaciones.predioWS.getPotreros()){
				ShowAlert.showAlert("Error", "Potrero no existe", this);
				etPotrero.setText("");
				return;
			} else {
				med.setPotreroId(Integer.parseInt(etPot));
			}
		} else {
			med.setPotreroId(null);
		}
		
		if (!etInicial.getText().toString().equals("")){
			med.setClickInicial(Integer.parseInt(etInicial.getText().toString()));
		} else {
			med.setClickInicial(null);
		}
		
		if (!etFinal.getText().toString().equals("")){
			med.setClickFinal(Integer.parseInt(etFinal.getText().toString()));
		} else {
			med.setClickFinal(null);
		}
		
		if (!etMuestras.getText().toString().equals("")){
			med.setMuestras(Integer.parseInt(etMuestras.getText().toString()));
		} else {
			med.setMuestras(null);
		}
		
		if (med.getClickFinal() != null &&
				med.getClickInicial() != null &&
				med.getMuestras() != null){
			med.setClick(roundForDisplay(calculaClick(med)));
			tvClick.setText(Double.toString(roundForDisplay(med.getClick())) + " Click");
			med.setMateriaSeca(calculaMSVerano(calculaClick(med)));
			tvMS.setText(Integer.toString(med.getMateriaSeca()) + " KgMs/Ha");
		} else {
			tvClick.setText("");
			tvMS.setText("");
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
		res = (int) Math.round(click * (double)165 + (double)1250);
		return res;
	}
	
	protected  void onStart(){
		super.onStart();

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