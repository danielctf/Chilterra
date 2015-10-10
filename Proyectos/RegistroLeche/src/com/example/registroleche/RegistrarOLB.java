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
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

public class RegistrarOLB extends Activity {

	EditText pino1, pino2, pino3, recalto, cojas, total;
	Button siguiente;
	static int totalVacas = 0;
	static int cantidadPino1 = 0;
	static int cantidadPino2 = 0;
	static int cantidadPino3 = 0;
	static int cantidadRecAlto = 0;
	static int cantidadCojas = 0;
	static boolean existsInDB = false;
	static boolean blockAM;
	Statement stm;
	
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
        	setContentView(R.layout.activity_registrar_olb);
        }else{
        	if (Login.height == 800 && Login.width == 480){
        		setContentView(R.layout.activity_registrar_olb_800x480);
        	}else{
        		setContentView(R.layout.activity_registrar_olb_800x480);
        	}
        }

		initialize();
		checkIfDataExists();
		
		/*
		SurfaceView v = (SurfaceView)findViewById(R.id.sv1);
		GifRun w = new GifRun();
		w.LoadGiff(v, this, R.drawable.working);
		*/
	}
	
	private void checkIfDataExists(){
		
		try {
			ResultSet res = stm.executeQuery("SELECT pino1, pino2, pino3, cojas, recuentoalto, total, ampm FROM dbo2.rLeche_Buena WHERE fecha = '" + Principal.load_date() + "' AND id_estanque = " + Principal.id_estanque_olb);
			int cont = 0;
			while (res.next()){
				if (res.getString(7).equals(OrdenaLeche.hora)){
					pino1.setText(Integer.toString(res.getInt(1)));
					pino2.setText(Integer.toString(res.getInt(2)));
					pino3.setText(Integer.toString(res.getInt(3)));
					cojas.setText(Integer.toString(res.getInt(4)));
					recalto.setText(Integer.toString(res.getInt(5)));
					total.setText(Integer.toString(res.getInt(6)));
					changeTotalCount();
					existsInDB = true;
				}
				cont++;
			}
			blockAM = false;
			if (cont == 0){
				existsInDB = false;
			}else{
				if (cont == 2){
					blockAM = true;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void changeTotalCount(){
		cantidadPino1 = 0;
		cantidadPino2 = 0;
		cantidadPino3 = 0;
		cantidadRecAlto = 0;
		cantidadCojas = 0;
		
		if (!(pino1.getText().toString().equals(""))){
			cantidadPino1 = Integer.parseInt(pino1.getText().toString());
		}
		if (!(pino2.getText().toString().equals(""))){
			cantidadPino2 = Integer.parseInt(pino2.getText().toString());
		}
		if (!(pino3.getText().toString().equals(""))){
			cantidadPino3 = Integer.parseInt(pino3.getText().toString());
		}
		if (!(recalto.getText().toString().equals(""))){
			cantidadRecAlto = Integer.parseInt(recalto.getText().toString());
		}
		if (!(cojas.getText().toString().equals(""))){
			cantidadCojas = Integer.parseInt(cojas.getText().toString());
		}
		
		totalVacas = cantidadPino1 + cantidadPino2 + cantidadPino3 + cantidadRecAlto + cantidadCojas;
		total.setText(Integer.toString(totalVacas));
	}

	public void goSiguiente(View v){
		if (totalVacas == 0){
			sinAnimales();
			return;
		}
		Intent i = new Intent(this, RegistrarOLB2.class);
		startActivity(i);
	}
	
	private void sinAnimales(){
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Error");
		alertDialog.setMessage("Debe ingresar Animales");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

           public void onClick(DialogInterface dialog,int id) {
        	   dialog.dismiss();
           }
           
         });

		alertDialog.show();
	}
	
	private void initialize(){
		totalVacas = 0;
		cantidadPino1 = 0;
		cantidadPino2 = 0;
		cantidadPino3 = 0;
		cantidadRecAlto = 0;
		cantidadCojas = 0;
		
		try {
			stm = Login.conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		siguiente = (Button)findViewById(R.id.siguiente);
		
		pino1 = (EditText)findViewById(R.id.pino1);
		pino1.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				changeTotalCount();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		pino2 = (EditText)findViewById(R.id.pino2);
		pino2.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				changeTotalCount();
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
		pino3 = (EditText)findViewById(R.id.pino3);
		pino3.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				changeTotalCount();
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
		recalto = (EditText)findViewById(R.id.recalto);
		recalto.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				changeTotalCount();
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
		recalto.setImeOptions(EditorInfo.IME_ACTION_DONE);
		
		cojas = (EditText)findViewById(R.id.cojas);
		cojas.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				changeTotalCount();
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
		total = (EditText)findViewById(R.id.total);
		total.setEnabled(false);
	}
	
	public void onUserInteraction(){
		Login.resetDisconnectTimer();
	}
	
	@Override
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    if (Login.destroyByIdle){
	    	Intent i = new Intent(this, Login.class);
	    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
	    	startActivity(i);
	    	//this.finish();
	    	return;
	    }
	    
	    if (RegistrarOLB2.finished){
	    	finish();
	    	return;
	    }
	    
	    checkIfDataExists();
	    Login.resetDisconnectTimer();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registrar_olb, menu);
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
