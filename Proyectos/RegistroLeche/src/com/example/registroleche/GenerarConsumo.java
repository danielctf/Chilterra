package com.example.registroleche;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class GenerarConsumo extends Activity {

	EditText hora, regla, lts, temperatura;
	Statement stm;
	RadioGroup rg;
	Button confirmar;
	
	String horaTotal, selectedState;
	double reglaTotal;
	int ltsTotal, temperaturaTotal, lts_actuales, id_leche_ternero;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        //                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			finish();
			return;
		}
		
        if (Login.height == 888 && Login.width == 540){
        	setContentView(R.layout.activity_generar_consumo);
        }else{
        	if (Login.height == 800 && Login.width == 480){
        		setContentView(R.layout.activity_generar_consumo_800x480);
        	}else{
        		setContentView(R.layout.activity_generar_consumo_800x480);
        	}
        }
		
		initialize();
		buscarLitrosEnEstanque();
		verSiTieneOrdena();
	}
	
	private void verSiTieneConsumo(){
		try {
			ResultSet res = stm.executeQuery("SELECT CONVERT(VARCHAR(8), hora, 108), regla, litros, temperatura FROM dbo2.rConsumo WHERE id_leche_ternero = " + id_leche_ternero);
			if (res.next()){
				confirmar.setEnabled(false);
				hora.setText(res.getString(1));
				regla.setText(Double.toString(res.getDouble(2)));
				lts.setText(Integer.toString(res.getInt(3)));
				temperatura.setText(Integer.toString(res.getInt(4)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void verSiTieneOrdena(){
		try {
			ResultSet res = stm.executeQuery("SELECT id_leche_ternero, ampm FROM dbo2.rLeche_Ternero WHERE id_estanque = " + Principal.id_estanque_olt + " AND fecha = '" + Principal.load_date() + "'");
			int cont = 0;
			id_leche_ternero = 0;
			while (res.next()){
				if (res.getString(2).contains(Salida.horaSalida)){
					id_leche_ternero = res.getInt(1);
				}
				cont++;
			}
			if (id_leche_ternero == 0){
				confirmar.setEnabled(false);
			}else{
				if (cont == 2 && Salida.horaSalida.equals("AM")){
					confirmar.setEnabled(false);
				}else{
					verSiTieneConsumo();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e){
			finish();
		}
	}
	
	private void buscarLitrosEnEstanque(){
		try {
			ResultSet res = stm.executeQuery("SELECT lts_actuales FROM dbo2.rEstanque WHERE id_estanque = " + Principal.id_estanque_olt);
			if (res.next()){
				lts_actuales = res.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void goConfirmarConsumo(View v){
		if (hora.getText().toString().equals("") || regla.getText().toString().equals("") || lts.getText().toString().equals("") || temperatura.getText().toString().equals("")){
			showAlert("Error", "Debe ingresar todos los campos");
			return;
		}
		
		horaTotal = hora.getText().toString();
		reglaTotal = Double.parseDouble(regla.getText().toString());
		ltsTotal = Integer.parseInt(lts.getText().toString());
		temperaturaTotal = Integer.parseInt(temperatura.getText().toString());
		
		if (ltsTotal > lts_actuales){
			showAlert("Error", "Los litros ingresados no pueden ser mayor a los que posee el estanque");
			return;
		}
		
		try {
			stm.executeUpdate("INSERT INTO dbo2.rConsumo (HORA, REGLA, LITROS, TEMPERATURA, FECHA, AMPM, ID_ESTANQUE, USUARIO, ID_LECHE_TERNERO) VALUES ('" + horaTotal + "', " + reglaTotal + ", " + ltsTotal + ", " + temperaturaTotal + ", '" + Principal.load_date() + "', '" + Salida.horaSalida + "', " + Principal.id_estanque_olt + ", '" + Login.activeUser + "', " + id_leche_ternero + ")");
			stm.executeUpdate("UPDATE dbo2.rEstanque SET lts_actuales = " + (lts_actuales - ltsTotal) + " WHERE id_estanque = " + Principal.id_estanque_olt);
			
			if (selectedState.contains("Durante")){
				ResultSet res = stm.executeQuery("SELECT total, litrosdespuesordena, litrosordena, rendimiento FROM dbo2.rLeche_Ternero WHERE id_leche_ternero = " + id_leche_ternero);
				double totalVacas = 0;
				int ltsDespues = 0;
				int ltsOrdena = 0;
				double rendimiento = 0;
				if (res.next()){
					totalVacas = res.getDouble(1);
					ltsDespues = res.getInt(2);
					ltsOrdena = res.getInt(3);
					rendimiento = res.getDouble(4);
				}
				ltsDespues = ltsDespues + ltsTotal;
				ltsOrdena = ltsOrdena + ltsTotal;
				double tempOrdena = ltsOrdena;
				rendimiento = tempOrdena / totalVacas;
				stm.executeUpdate("UPDATE dbo2.rLeche_Ternero SET litrosdespuesordena = " + ltsDespues + ", litrosordena = " + ltsOrdena + ", rendimiento = " + rendimiento + " WHERE id_leche_ternero = " + id_leche_ternero);
			}
			
		} catch (SQLException e) {
			if (e.getErrorCode() == 241){
				showAlert("Error", "Hora no válida\n\nEjemplo 1: 5:35\nEjemplo 2: 16:50");
			}
		}
		
		GenerarEntrega.success = true;
		finish();
		
	}
	
	private void initialize(){
		selectedState = "Despues";
		confirmar = (Button)findViewById(R.id.confirmar);
		hora = (EditText)findViewById(R.id.hora);
		regla = (EditText)findViewById(R.id.regla);
		lts = (EditText)findViewById(R.id.lts);
		temperatura = (EditText)findViewById(R.id.temperatura);
		temperatura.setImeOptions(EditorInfo.IME_ACTION_DONE);
		
		try {
			stm = Login.conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rg = (RadioGroup)findViewById(R.id.rg);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	    {
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	            RadioButton rb = (RadioButton)findViewById(checkedId);
	            selectedState = rb.getText().toString();
	        }
	    });
	}
	
	private void showAlert(String title, String msg){
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

           public void onClick(DialogInterface dialog,int id) {
        	   dialog.dismiss();
           }
           
         });

		alertDialog.show();
	}

	public void onUserInteraction(){
		Login.resetDisconnectTimer();
	}
	
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    if (Login.destroyByIdle){
	    	Intent i = new Intent(this, Login.class);
	    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
	    	startActivity(i);
	    	this.finish();
	    }
	    Login.resetDisconnectTimer();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.generar_consumo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
