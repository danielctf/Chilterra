package com.example.bajas;

import java.sql.Connection;

import com.example.bt.*;
import com.example.testing.R;
import android.support.v7.app.ActionBarActivity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class DespliegaDatos extends ActionBarActivity{

	EditText dio, fundo;
	Button nextBtn;
	
	
	public static String dato;
	Messenger mService = null;
	final Messenger mMessenger = new Messenger(new IncomingHandler());
	class IncomingHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EIDService.MSG_UPDATE_STATUS:
				Bundle b1 = msg.getData();
				//textInfo1.setText(b1.getString("info1"));
				dato = b1.getString("info1");
				//LogMessage("datobaston: "+datoBaston);
				if (dato != "zzz"){
					dio.setText(dato);
				}
				break;
			case EIDService.MSG_UPDATE_LOG_APPEND:
				Bundle b2 = msg.getData();
				if (b2.getString("logappend").contains("Failed")) {
					failedToConnect();
				}
				break;
			case EIDService.MSG_THREAD_SUICIDE:
				failedToConnect();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}
	
	public void failedToConnect(){
		Dialog dialog = new Dialog(this){		
			public boolean dispatchTouchEvent(MotionEvent event){
		        dismiss();
		        return false;
		    }
		};
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.device_disconnected);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.show();
	}
	
	
	public void configService(){
		mService = new Messenger(MainActivity.globService);
		Message msg = Message.obtain(null, EIDService.MSG_REGISTER_CLIENT);
		msg.replyTo = mMessenger;
		try {
			mService.send(msg);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
		    // Here activity is brought to front, not created,
		    // so finishing this will get you to the last viewed activity
			finish();
			return;
		}
		setContentView(R.layout.activity_despliega_datos);
		
		dio = (EditText)findViewById(R.id.dio);
		dio.setEnabled(false);
		fundo = (EditText)findViewById(R.id.fundo);
		fundo.setEnabled(false);
		fundo.setText("EH1");
		
		if (EIDService.isRunning()){
			configService();
			//connectToSQL();
		}
		
	}
	
	/*
	private void connectToSQL(){
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		Connection con = null;
		String ConnURL = null;
		try{
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			ConnURL = "jdbc:jtds:sqlserver://200.6.115.178;databaseName"
		}
	}
	*/

	public void goNext(View v){
		EIDService.canRead = false;
		Intent i = new Intent(this, IngresaDatos.class);
		startActivity(i);
	}
	
	public void onResume() {
		super.onResume();
		EIDService.canRead = true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.despliega_datos, menu);
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
