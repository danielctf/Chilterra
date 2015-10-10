package com.example.registroleche;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.graphics.PorterDuff;

public class Principal extends Activity {
	
	Spinner spin;
	static TextView fecha;
	static String predioSeleccionado = null;
	static String date;
	static int id_predio = 0;
	static int id_estanque_olb = 0;
	static int id_estanque_olt = 0;
	static Statement stm;
	
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
		setContentView(R.layout.activity_principal);
		
		initialize();

	}

	public void generarSalida(View v){
		Intent i = new Intent(this, Salida.class);
		startActivity(i);
	}
	
	public void goOLB(View v){
		Intent i = new Intent(this, OrdenaLeche.class);
		startActivity(i);
	}
	
	private void load_spinner(){
		spin.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				predioSeleccionado = parent.getItemAtPosition(position).toString();
				try {
					//Login.connectSQL();
					//Statement stm = Login.conn.createStatement();
					ResultSet res = stm.executeQuery("SELECT e.id_estanque, e.tipo_ordena, p.id_predio FROM dbo2.rEstanque e, dbo2.rPredio p WHERE p.id_predio = e.id_predio and p.nombre = '" + predioSeleccionado + "'"); 
					while (res.next()){
						id_predio = res.getInt(3);
						if (res.getString(2).equals("OLB")){
							id_estanque_olb = res.getInt(1);
						}else{
							if (res.getString(2).equals("OLT")){
								id_estanque_olt = res.getInt(1);
							}
						}
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		try {
			//Login.connectSQL();
			//Statement stm = Login.conn.createStatement();
			ResultSet reset = stm.executeQuery("SELECT p.nombre FROM dbo2.rPredio p, dbo2.rUsuarioPredio up WHERE up.usuario = '" + Login.activeUser + "' AND p.id_predio = up.id_predio");
			ArrayList<String> tempArray = new ArrayList<String>();
			while (reset.next()){
				tempArray.add(reset.getString(1));
			}
			String[] arraySpinner = new String[tempArray.size()];
			for (int i = 0; i < tempArray.size(); i++){
				arraySpinner[i] = tempArray.get(i);
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, arraySpinner);
			spin.setAdapter(adapter);
			//Login.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static String load_date(){
		return date;
	}
	
	public static void setDate(Statement stm2){
		//Fecha Servidor
		try {
			ResultSet res = stm2.executeQuery("SELECT CONVERT(varchar(10), GETDATE(), 105)");
			if (res.next()){
				date = res.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Fecha telefono
		/*
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		return dateFormat.format(date);
		*/
	}
	
	public void onUserInteraction(){
		Login.resetDisconnectTimer();
	}
	
	
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    
	    if (RegistrarOLB2.finished || RegistrarOLT2.finished || GenerarEntrega.success){
	    	showAlert("Guardado Exitoso", "Datos actualizados correctamente\nen la base de datos");
	    	RegistrarOLB2.finished = false;
	    	RegistrarOLT2.finished = false;
	    	GenerarEntrega.success = false;
	    }
	    if (Login.destroyByIdle){
	    	Intent i = new Intent(this, Login.class);
	    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
	    	startActivity(i);
	    	this.finish();
	    }
	    Login.resetDisconnectTimer();
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
	
	private void initialize(){
		spin = (Spinner)findViewById(R.id.predios);
		spin.getBackground().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
		fecha = (TextView)findViewById(R.id.fecha);
		
		try {
			stm = Login.conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		load_spinner();
		fecha.setText(load_date());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.principal, menu);
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
