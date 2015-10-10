package com.example.registroleche;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class OrdenaLeche extends Activity {

	TextView predio, fecha;
	Dialog dialog;
	ListView lv;
	ArrayAdapter<String> lvContent;
	static String hora = null;
	String fechaActual = null;
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
		setContentView(R.layout.activity_ordenaleche);

		initialize();
		
	}

	public void goOLT(View v){
		dialog = new Dialog(this){};
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.ampm);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.drawable.trans_black));
		dialog.show();
		
		
		lv = (ListView)dialog.findViewById(R.id.hora);
		lv.setAdapter(lvContent);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				dialog.dismiss();
				Intent in = new Intent(OrdenaLeche.this, RegistrarOLT.class);
				if (arg0.getItemAtPosition(position).equals("AM")){	
					hora = "AM";
					startActivity(in);
				}else{
					if (arg0.getItemAtPosition(position).equals("PM")){
						hora = "PM";

						try {
							//Login.connectSQL();
							//Statement stm = Login.conn.createStatement();
							ResultSet res = stm.executeQuery("SELECT ampm FROM dbo2.rLeche_Ternero WHERE fecha = '" + Principal.load_date() + "' AND id_estanque = " + Principal.id_estanque_olt);
							int cont = 0;
							boolean existsAM = false;
							while (res.next()){
								cont++;
								if (res.getString(1).equals("AM")){
									existsAM = true;
								}
							}
							
							if (existsAM == false && cont == 0){
								noHayOrdenaAM(in);
								return;
							}
							//Login.conn.close();
							startActivity(in);
							
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		
	}
	
	public void goOLB(View v){
		
		dialog = new Dialog(this){};
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.ampm);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.drawable.trans_black));
		dialog.show();
		
		
		lv = (ListView)dialog.findViewById(R.id.hora);
		lv.setAdapter(lvContent);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				dialog.dismiss();
				Intent in = new Intent(OrdenaLeche.this, RegistrarOLB.class);
				if (arg0.getItemAtPosition(position).equals("AM")){	
					hora = "AM";
					startActivity(in);
				}else{
					if (arg0.getItemAtPosition(position).equals("PM")){
						hora = "PM";

						try {
							ResultSet res = stm.executeQuery("SELECT ampm FROM dbo2.rLeche_Buena WHERE fecha = '" + Principal.load_date() + "' AND id_estanque = " + Principal.id_estanque_olb);
							int cont = 0;
							boolean existsAM = false;
							while (res.next()){
								cont++;
								if (res.getString(1).equals("AM")){
									existsAM = true;
								}
							}
							
							if (existsAM == false && cont == 0){
								noHayOrdenaAM(in);
								return;
							}
							//Login.conn.close();
							startActivity(in);
							
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

	}
	
	private void noHayOrdenaAM(final Intent in){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("No hay una Ordeña AM registrada\n\nEstá seguro que desea registrar\nuna ordeña PM sin antes haber\nregistrado una ordeña AM ?");
	    
	    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
	        	dialog.dismiss();
	        	startActivity(in);
	        }
	    });
	    
	    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
	        	dialog.dismiss();
	        }
	    });
	    
	    builder.show();
	}
	
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    
	    if (RegistrarOLB2.finished || RegistrarOLT2.finished){
	    	finish();
	    	return;
	    }
	    if (Login.destroyByIdle){
	    	Intent i = new Intent(this, Login.class);
	    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
	    	startActivity(i);
	    	this.finish();
	    }
	    Login.resetDisconnectTimer();
	}
	
	public void onUserInteraction(){
		Login.resetDisconnectTimer();
	}
	
	private void initialize(){
		predio = (TextView)findViewById(R.id.predio);
		fecha = (TextView)findViewById(R.id.fecha);
		predio.setText("Predio: " + Principal.predioSeleccionado);
		fecha.setText("Fecha: " + Principal.load_date());
		fechaActual = Principal.load_date();
		
		lvContent = new ArrayAdapter<String>(this, R.layout.listview_white_text);
		lvContent.add("AM");
		lvContent.add("PM");
		
		try {
			stm = Login.conn.createStatement();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e){
			
		}
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.olb, menu);
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
