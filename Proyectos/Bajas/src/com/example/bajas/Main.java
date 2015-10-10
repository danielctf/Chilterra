package com.example.bajas;

import cl.a2r.login.Login;
import com.example.testing.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class Main extends Activity {
	TextView label;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
		
		Intent i = new Intent(this, Login.class);
		i.putExtra("ActivityName", "com.example.bajas.DespliegaDatos");
		startActivity(i);
		finish();
        
		label = (TextView)findViewById(R.id.label);
	}

	public void find(View v){
		/*
		Intent i = new Intent(this, MainActivity.class);
		i.putExtra("ActivityName", "com.example.bajas.DespliegaDatos");
		startActivity(i);
		finish();
		*/
	}
	
	public void connect(View v){
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
