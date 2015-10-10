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
import android.widget.EditText;

public class RegistrarOLT extends Activity {

	EditText calostro, antibiotico, LBTerneros, cojas, recalto, mastitis, resguardo, total;
	static int calostroTotal, antibioticoTotal, LBTernerosTotal, cojasTotal, recaltoTotal, mastitisTotal, resguardoTotal, totalVacas;
	static boolean existsInDB = false;
	Statement stm;
	static boolean blockAM;
	
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
        	setContentView(R.layout.activity_registrar_olt);
        }else{
        	if (Login.height == 800 && Login.width == 480){
        		setContentView(R.layout.activity_registrar_olt_800x480);
        	}else{
        		setContentView(R.layout.activity_registrar_olt_800x480);
        	}
        }
        
		initialize();
		checkIfDataExists();
		
	}
	
	private void checkIfDataExists(){
		
		try {
			ResultSet res = stm.executeQuery("SELECT calostro, antibioticos, lechebuenaternero, cojas, recuentoalto, mastitis, resguardo, total, ampm FROM dbo2.rLeche_Ternero WHERE fecha = '" + Principal.load_date() + "' AND id_estanque = " + Principal.id_estanque_olt);
			int cont = 0;
			while (res.next()){
				if (res.getString(9).equals(OrdenaLeche.hora)){
					calostro.setText(Integer.toString(res.getInt(1)));
					antibiotico.setText(Integer.toString(res.getInt(2)));
					LBTerneros.setText(Integer.toString(res.getInt(3)));
					cojas.setText(Integer.toString(res.getInt(4)));
					recalto.setText(Integer.toString(res.getInt(5)));
					mastitis.setText(Integer.toString(res.getInt(6)));
					resguardo.setText(Integer.toString(res.getInt(7)));
					total.setText(Integer.toString(res.getInt(8)));
					update();
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

	private void update(){
		calostroTotal = 0;
		antibioticoTotal = 0;
		LBTernerosTotal = 0;
		cojasTotal = 0;
		recaltoTotal = 0;
		mastitisTotal = 0;
		resguardoTotal = 0;
		totalVacas = 0;
		
		if (!(calostro.getText().toString().equals(""))){
			calostroTotal = Integer.parseInt(calostro.getText().toString());
		}
		if (!(antibiotico.getText().toString().equals(""))){
			antibioticoTotal = Integer.parseInt(antibiotico.getText().toString());
		}
		if (!(LBTerneros.getText().toString().equals(""))){
			LBTernerosTotal = Integer.parseInt(LBTerneros.getText().toString());
		}
		if (!(cojas.getText().toString().equals(""))){
			cojasTotal = Integer.parseInt(cojas.getText().toString());
		}
		if (!(recalto.getText().toString().equals(""))){
			recaltoTotal = Integer.parseInt(recalto.getText().toString());
		}
		if (!(mastitis.getText().toString().equals(""))){
			mastitisTotal = Integer.parseInt(mastitis.getText().toString());
		}
		if (!(resguardo.getText().toString().equals(""))){
			resguardoTotal = Integer.parseInt(resguardo.getText().toString());
		}
		
		totalVacas = calostroTotal + antibioticoTotal + LBTernerosTotal + cojasTotal + recaltoTotal + mastitisTotal + resguardoTotal;
		total.setText(Integer.toString(totalVacas));

	}
	
	public void goSiguienteOLT(View v){
		if (totalVacas == 0){
			sinAnimales();
			return;
		}

		Intent i = new Intent(this, RegistrarOLT2.class);
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
		try {
			stm = Login.conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		calostroTotal = 0;
		antibioticoTotal = 0;
		LBTernerosTotal = 0;
		cojasTotal = 0;
		recaltoTotal = 0;
		mastitisTotal = 0;
		resguardoTotal = 0;
		totalVacas = 0;
		
		calostro = (EditText)findViewById(R.id.calostro);
		calostro.addTextChangedListener(new TextWatcher(){
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
		antibiotico = (EditText)findViewById(R.id.antibiotico);
		antibiotico.addTextChangedListener(new TextWatcher(){
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
		LBTerneros = (EditText)findViewById(R.id.LBTerneros);
		LBTerneros.addTextChangedListener(new TextWatcher(){
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
		cojas = (EditText)findViewById(R.id.cojas);
		cojas.addTextChangedListener(new TextWatcher(){
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
		recalto = (EditText)findViewById(R.id.recalto);
		recalto.addTextChangedListener(new TextWatcher(){
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
		mastitis = (EditText)findViewById(R.id.mastitis);
		mastitis.addTextChangedListener(new TextWatcher(){
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
		resguardo = (EditText)findViewById(R.id.resguardo);
		resguardo.addTextChangedListener(new TextWatcher(){
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
		resguardo.setImeOptions(EditorInfo.IME_ACTION_DONE);
		
		total = (EditText)findViewById(R.id.total);
		
		total.setEnabled(false);
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
	    
	    if (RegistrarOLT2.finished){
	    	finish();
	    	return;
	    }
	    
	    checkIfDataExists();
	    Login.resetDisconnectTimer();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registrar_olt, menu);
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
