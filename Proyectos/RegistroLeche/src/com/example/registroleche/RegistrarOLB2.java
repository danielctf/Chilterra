package com.example.registroleche;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.text.Editable;
import android.text.TextWatcher;
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

public class RegistrarOLB2 extends Activity {

	EditText antesregla, anteslitros, despuesregla, despueslitros, ordena, rendimiento;
	Button confirmar;
	double antesRegla = 0;
	double despuesRegla, rendimientoTotal;
	int antesLitros = 0;
	int id_leche_buena = 0;
	int despuesLitros, ordenaTotal;
	boolean existsInDB = false;
	static boolean finished = false;
	Statement stm;
	
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
        	setContentView(R.layout.activity_registrar_olb2);
        }else{
        	if (Login.height == 800 && Login.width == 480){
        		setContentView(R.layout.activity_registrar_olb2_800x480);
        	}else{
        		setContentView(R.layout.activity_registrar_olb2_800x480);
        	}
        }
        
		initialize();
		checkIfDataExists();
		checkBlockPorEntrega();
	}
	
	private void checkBlockPorEntrega(){
		if (RegistrarOLB.blockAM && OrdenaLeche.hora.equals("AM")){
			confirmar.setEnabled(false);
			return;
		}
		
		if (existsInDB){
			try {
				ResultSet res = stm.executeQuery("SELECT id_entrega FROM dbo2.rEntrega WHERE id_leche_buena = " + id_leche_buena);
				if (res.next()){
					confirmar.setEnabled(false);
				}else{
					confirmar.setEnabled(true);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	

	private void checkIfDataExists(){
		try {
			ResultSet res = stm.executeQuery("SELECT id_leche_buena, reglaantesordena, litrosantesordena, regladespuesordena, litrosdespuesordena, litrosordena, rendimiento FROM dbo2.rLeche_Buena WHERE fecha = '" + Principal.load_date() + "'" + " AND ampm = '" + OrdenaLeche.hora + "' AND id_estanque = " + Principal.id_estanque_olb);
			if (res.next()){
				id_leche_buena = res.getInt(1);
				if (res.getDouble(2) != 0.00 && res.getInt(3) != 0){
					antesregla.setText(Double.toString(res.getDouble(2)));
					anteslitros.setText(Integer.toString(res.getInt(3)));
				}else{
					antesregla.setText("");
					anteslitros.setText("");
				}
				despuesregla.setText(Double.toString(res.getDouble(4)));
				despueslitros.setText(Integer.toString(res.getInt(5)));
				ordena.setText(Integer.toString(res.getInt(6)));
				rendimiento.setText(Double.toString(res.getDouble(7)));
				existsInDB = true;
				update();
			}else{
				existsInDB = false;
				id_leche_buena = 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	    	return;
	    }
	    checkIfDataExists();
	    Login.resetDisconnectTimer();
	}

	
	public void goConfirmar(View v){
		if (ordena.getText().toString().equals("") || rendimiento.getText().toString().equals("")){
			showAlert("Error", "Debe ingresar todos los campos");
			return;
		}
		
		if (antesregla.getText().toString().equals("") && anteslitros.getText().toString().equals("")){
			antesRegla = 0;
			antesLitros = 0;
		}else{
			antesRegla = Double.parseDouble(antesregla.getText().toString());
			antesLitros = Integer.parseInt(anteslitros.getText().toString());
		}
		
		despuesRegla = Double.parseDouble(despuesregla.getText().toString());
		despuesLitros = Integer.parseInt(despueslitros.getText().toString());
		ordenaTotal = Integer.parseInt(ordena.getText().toString());
		rendimientoTotal = Double.parseDouble(rendimiento.getText().toString());
		
		try {
			stm.executeUpdate("UPDATE dbo2.rEstanque SET lts_actuales = " + despuesLitros + " WHERE id_estanque = " + Principal.id_estanque_olb);
			if (existsInDB){
				stm.executeUpdate("UPDATE dbo2.rLeche_Buena SET pino1 = " + RegistrarOLB.cantidadPino1 + ", pino2 = " + RegistrarOLB.cantidadPino2 + ", pino3 = " + RegistrarOLB.cantidadPino3 + ", cojas = " + RegistrarOLB.cantidadCojas + ", recuentoalto = " + RegistrarOLB.cantidadRecAlto + ", total = " + RegistrarOLB.totalVacas + ", reglaantesordena = " + antesRegla + ", litrosantesordena = " + antesLitros + ", regladespuesordena = " + despuesRegla + ", litrosdespuesordena = " + despuesLitros + ", litrosordena = " + ordenaTotal + ", rendimiento = " + rendimientoTotal + ", usuario = '" + Login.activeUser + "' WHERE id_leche_buena = " + id_leche_buena);
			}else{
				stm.executeUpdate("INSERT INTO dbo2.rLeche_Buena (pino1, pino2, pino3, cojas, recuentoalto, total, reglaantesordena, litrosantesordena, regladespuesordena, litrosdespuesordena, litrosordena, rendimiento, fecha, ampm, usuario, id_estanque) VALUES (" + RegistrarOLB.cantidadPino1 + ", " + RegistrarOLB.cantidadPino2 + ", " + RegistrarOLB.cantidadPino3 + ", " + RegistrarOLB.cantidadCojas + ", " + RegistrarOLB.cantidadRecAlto + ", " + RegistrarOLB.totalVacas + ", " + antesRegla + ", " + antesLitros + ", " + despuesRegla + ", " + despuesLitros + ", " + ordenaTotal + ", " + rendimientoTotal + ", '" + Principal.load_date() + "', '" + OrdenaLeche.hora + "', '" + Login.activeUser + "', " + Principal.id_estanque_olb + ")");
			}
			
			finished = true;
			finish();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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
	
	private void update(){
		finished = false;
		if (!(antesregla.getText().toString().equals("")) && !(anteslitros.getText().toString().equals("")) && !(despuesregla.getText().toString().equals("")) && !(despueslitros.getText().toString().equals(""))){
			int litrosfinal = Integer.parseInt(despueslitros.getText().toString());
			int litrosinicial = Integer.parseInt(anteslitros.getText().toString());
			ordena.setText(Integer.toString(litrosfinal-litrosinicial));
			double tempOrdena = Integer.parseInt(ordena.getText().toString());
			double tempTotalVacas = RegistrarOLT.totalVacas;
			double tempRendimiento = Math.round(tempOrdena/tempTotalVacas * 100);
			tempRendimiento = tempRendimiento / 100;
			rendimiento.setText(String.valueOf(tempRendimiento));
		}else{
			ordena.setText("");
			rendimiento.setText("");
		}
		
		if (antesregla.getText().toString().equals("") && anteslitros.getText().toString().equals("") && !(despuesregla.getText().toString().equals("")) && !(despueslitros.getText().toString().equals(""))){
			int litrosfinal = Integer.parseInt(despueslitros.getText().toString());
			ordena.setText(Integer.toString(litrosfinal));
			double tempOrdena = Integer.parseInt(ordena.getText().toString());
			double tempTotalVacas = RegistrarOLB.totalVacas;
			double tempRendimiento = Math.round(tempOrdena/tempTotalVacas * 100);
			tempRendimiento = tempRendimiento / 100;
			rendimiento.setText(String.valueOf(tempRendimiento));
		}
	}
	
	private void initialize(){
		try {
			stm = Login.conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		confirmar = (Button)findViewById(R.id.confirmar);
		antesregla = (EditText)findViewById(R.id.antesregla);
		antesregla.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				update();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		anteslitros = (EditText)findViewById(R.id.anteslitros);
		anteslitros.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				update();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		despuesregla = (EditText)findViewById(R.id.despuesregla);
		despuesregla.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				update();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		despueslitros = (EditText)findViewById(R.id.despueslitros);
		despueslitros.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				update();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		despueslitros.setImeOptions(EditorInfo.IME_ACTION_DONE);
		
		ordena = (EditText)findViewById(R.id.ordena);
		rendimiento = (EditText)findViewById(R.id.rendimiento);
		ordena.setEnabled(false);
		rendimiento.setEnabled(false);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registrar_olb2, menu);
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
