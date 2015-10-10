package com.example.registroleche;

import android.app.Activity;
import android.app.Dialog;
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

public class Salida extends Activity {

	TextView predio, fecha;
	Dialog dialog;
	ListView lv;
	ArrayAdapter<String> lvContent;
	static String horaSalida;
	
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
		setContentView(R.layout.activity_salida);
		initialize();
	}
	
	private void initialize(){
		predio = (TextView)findViewById(R.id.predio);
		fecha = (TextView)findViewById(R.id.fecha);
		
		fecha.setText("Fecha: " + Principal.load_date());
		predio.setText("Predio: " + Principal.predioSeleccionado);
		
		lvContent = new ArrayAdapter<String>(this, R.layout.listview_white_text);
		lvContent.add("AM");
		lvContent.add("PM");
	}
	
	public void generarEntrega(View v){
		Intent i = new Intent(this, GenerarEntrega.class);
		startActivity(i);
	}
	
	public void generarConsumo(View v){
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
				if (arg0.getItemAtPosition(position).equals("AM")){	
					horaSalida = "AM";
				}else{
					if (arg0.getItemAtPosition(position).equals("PM")){
						horaSalida = "PM";
					}
				}
				Intent i = new Intent(Salida.this, GenerarConsumo.class);
				startActivity(i);
			}
		});
		
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
	    if (GenerarEntrega.success){
	    	finish();
	    	return;
	    }
	    Login.resetDisconnectTimer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.salida, menu);
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
