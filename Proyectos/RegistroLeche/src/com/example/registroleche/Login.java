package com.example.registroleche;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class Login extends Activity {

	static final long DISCONNECT_TIMEOUT = 1200000;
	
	static Connection conn;
	EditText usuario, password;
	static String activeUser = null;
	static Statement stm;
	static boolean destroyByIdle;
	static int width, height;
	
    public static Handler disconnectHandler = new Handler(){
    };

    public static Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
        	destroyByIdle = true;
        }
    };
	
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
		
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
		setContentView(R.layout.activity_login);
        
		//Moto E: 960x540
		//LG-e450g y HUAWEI y300-0151: 800x480
		
        if (!(height == 888 && width == 540) && !(height == 800 && width == 480)){
        	showAlert("Advertencia", "La aplicación no está adaptada a la resolución de su telefono. Las pantallas pueden no verse centradas");
        }
        
		initialize();
	}

	public void goLogin(View v){
		if (destroyByIdle){
			connectSQL();
			destroyByIdle = false;
		}
		
		try {
			ResultSet reset = stm.executeQuery("SELECT usuario FROM dbo2.rUsuario WHERE usuario = '" + usuario.getText().toString() + "' AND clave = '" + password.getText().toString() + "'");
			if (reset.next()){
				activeUser = reset.getString(1);
				Intent i = new Intent(this, Principal.class);
				startActivity(i);
				finish();
				//EDIT conn.close();
			}else{
				showAlert("Error", "Usuario inválido");
			}
		} catch (java.sql.SQLException e) {
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
	
	public static void connectSQL(){
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		Log.i("Android"," MySQL Connect Example.");
		try {
		String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		driver = "net.sourceforge.jtds.jdbc.Driver";
		Class.forName(driver).newInstance();
		String connString;
		connString = "jdbc:jtds:sqlserver://200.6.115.178;databaseName=L47;user=SaludAnimal;password=saludani1";
		conn = DriverManager.getConnection(connString);
		Log.w("Connection","open");
		
		stm = conn.createStatement();
		stm.executeUpdate("SET DATEFORMAT DMY");
		Principal.setDate(stm);
		
		}catch (SQLException se){
			Log.w("Error connection SQL","" + se.getMessage());
		}catch (ClassNotFoundException e){
			Log.w("Error connection Classnotfound","" + e.getMessage());
		} catch (Exception e){
			Log.w("Error connection Exc","" + e.getMessage());
			e.printStackTrace();
		}
	}
	
    public static void resetDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }
    
	//load date cambiar y buscar forma de bloquear ordeña AM cuando hay PM y ambas cuando hay salida
	public void onUserInteraction(){
		resetDisconnectTimer();
	}
	
	private void initialize(){
		destroyByIdle = false;
		usuario = (EditText)findViewById(R.id.usuario);
		password = (EditText)findViewById(R.id.password);
		connectSQL();
	}
	
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    resetDisconnectTimer();
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
