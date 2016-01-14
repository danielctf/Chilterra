package cl.a2r.alimentacion;

import java.util.Date;

import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sap.model.Medicion;
import cl.ar2.sqlite.cobertura.MedicionServicio;
import cl.ar2.sqlite.cobertura.StockM;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MedicionEntrada extends Activity implements View.OnClickListener, View.OnKeyListener {

	private TextView tvClick, tvMS, tvFundo, tvSuperficie, tvRacion;
	private EditText etPotrero, etInicial, etFinal, etMuestras, etDieta, etVacas;
	private ImageButton goBack, logs, confirmarEntrada;
	private Medicion med;
	private double clickProm, superficie;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_medicion_entrada);
		
		cargarInterfaz();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Integer numeroPotrero = extras.getInt("numeroPotrero");
			etPotrero.setText(Integer.toString(numeroPotrero));
			superficie = extras.getDouble("superficie");
			tvSuperficie.setText(Double.toString(superficie) + " Hás");
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
		etDieta = (EditText)findViewById(R.id.etDieta);
		etDieta.setOnKeyListener(this);
		etVacas = (EditText)findViewById(R.id.etVacas);
		etVacas.setOnKeyListener(this);
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
		tvSuperficie = (TextView)findViewById(R.id.tvSuperficie);
		tvRacion = (TextView)findViewById(R.id.tvRacion);
		
		med = new Medicion();
		Mediciones.tipoMuestraActual = 1;
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
		etVacas.setText("");
		etDieta.setText("");
		updateStatus();
	}
	
	private void confirmarEntrada(){
		med.setCorreo(Login.mail);
		med.setFundoId(Aplicaciones.predioWS.getId());
		med.setTipoMuestraId(1);
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
				for (StockM sm : Stock.list){
					if (sm.getMed().getFundoId().intValue() == Aplicaciones.predioWS.getId() &&
							sm.getMed().getPotreroId().intValue() == Integer.parseInt(etPot)){
						
						superficie = sm.getMed().getSuperficie();
						tvSuperficie.setText(Double.toString(superficie) + " Hás");
						break;
					}
				}
			}
		} else {
			med.setPotreroId(null);
			superficie = 0;
			tvSuperficie.setText("");
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
			
			clickProm = calculaClick(med);
			med.setClick(roundForDisplay(clickProm));
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
				med.getPotreroId() != null && 
				!etDieta.getText().toString().equals("") &&
				!etVacas.getText().toString().equals("")){
			
			double racion = calculaRacion(Integer.parseInt(etDieta.getText().toString()), Integer.parseInt(etVacas.getText().toString()), clickProm, superficie);
			tvRacion.setText(Double.toString(racion) + " Raciones");
			med.setAnimales(Integer.parseInt(etVacas.getText().toString()));
			
			if (med.getClickInicial().intValue() >= med.getClickFinal().intValue()){
				confirmarEntrada.setEnabled(false);
			} else {
				confirmarEntrada.setEnabled(true);
			}
		} else {
			confirmarEntrada.setEnabled(false);
			tvRacion.setText("");
		}
	}
	
	private double calculaRacion(int dieta, int vacas, double click, double superficie){
		double demanda = dieta * vacas;
		double oferta = (click - 7) * 165 * superficie;
		double res = oferta / demanda;
		res = res * 2;
		res = roundForDisplay(res);
		return res;
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
		//Hay 4 calculaMSVerano, uno en entrada, residuo, control y semanal
		//tambien en la clase Stock y StockDetalle ir a funcion calcularClickPromedio()
		//calculaRacion()
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
