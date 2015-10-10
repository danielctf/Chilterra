package com.example.registroleche;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
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

public class GenerarEntrega extends Activity {

	EditText hora, no_guia, temperatura, lts, fecha;
	Button foto, mas, menos;
	RadioGroup rg;
	int TAKE_PHOTO_CODE = 0;
	int count = 0;
	String path = null;
	byte[] imgBytes;
	String strFile = null;
	String selectedState;

	Calendar date;
	SimpleDateFormat format;
	
	String horaTotal;
	int no_guiaTotal, tempTotal, ltsTotal, lts_actuales;
	long allowMaxDays;
	long differenceDone;
	static boolean success;
	static boolean esNula = false;
	static int id_leche_buena;
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
        	setContentView(R.layout.activity_generar_entrega);
        }else{
        	if (Login.height == 800 && Login.width == 480){
        		setContentView(R.layout.activity_generar_entrega_800x480);
        	}else{
        		setContentView(R.layout.activity_generar_entrega_800x480);
        	}
        }
		
		initialize();
		checkDates();
		buscarLitrosEnEstanque();
		verSiEsNula();
		
	}
	
	private void verSiEsNula(){
		if (esNula){
			showAlert("Entrega Anulada", "Entrega Anulada\n\nModifique los litros y datos correspondientes");
			lts.setEnabled(true);
		}
	}
	
	private void buscarLitrosEnEstanque(){
		//Login.connectSQL();
		try {
			//Statement stm = Login.conn.createStatement();
			ResultSet res = stm.executeQuery("SELECT lts_actuales FROM dbo2.rEstanque WHERE id_estanque = " + Principal.id_estanque_olb);
			if (res.next()){
				ltsTotal = res.getInt(1);
				lts_actuales = ltsTotal;
				lts.setText(Integer.toString(ltsTotal));
			}
			//Login.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void checkDates(){
		//Login.connectSQL();
		try {
			//Statement stm = Login.conn.createStatement();
			ResultSet res = stm.executeQuery("SELECT MAX(id_leche_buena), CONVERT(varchar(10), MAX(fecha), 105) from dbo2.rLeche_Buena where id_estanque = " + Principal.id_estanque_olb);
			if (res.next()){
				if (!(Principal.load_date().contains(res.getString(2)))){
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					Date lastDate = sdf.parse(res.getString(2));
					Date actualDate = sdf.parse(Principal.load_date());
					long diff = actualDate.getTime() - lastDate.getTime();
					allowMaxDays = diff / (24 * 60 * 60 * 1000);
					differenceDone = 0;
					editableDate();
				}
				id_leche_buena = res.getInt(1);
			}
			//Login.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (NullPointerException e){
			finish();
		}
	}
	
	private void editableDate(){
		if (allowMaxDays > 0){
			menos.setEnabled(true);
		}else{
			menos.setEnabled(false);
		}
		if (differenceDone > 0){
			mas.setEnabled(true);
		}else{
			mas.setEnabled(false);
		}
	}
	
	public void goMenos(View v){
	    date.add(Calendar.DATE, -1);
		fecha.setText(format.format(date.getTime()));
		allowMaxDays--;
		differenceDone++;
		editableDate();
	}
	
	public void goMas(View v){
	    date.add(Calendar.DATE, 1);
		fecha.setText(format.format(date.getTime()));
		allowMaxDays++;
		differenceDone--;
		editableDate();
	}
	
	public void takePicture(View v){
		final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/RegistroLeche/"; 
        File newdir = new File(dir); 
        newdir.mkdirs();
     
        count++;
        String file = dir + "Entrega" + count + ".jpg";
        path = file;
        File newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException e) {}       

        Uri outputFileUri = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
	    	foto.setText("Entrega" + count + ".jpg");
	    	convertImage();
	    }
	}
	
	private void convertImage(){
		try {
			File imgPath = new File(path);
			imgBytes = FileUtils.readFileToByteArray(imgPath);
			strFile = Base64.encodeToString(imgBytes, Base64.NO_WRAP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void goConfirmarEntrega(View v){
		if (hora.getText().toString().equals("") || no_guia.getText().toString().equals("") || temperatura.getText().toString().equals("") || lts.getText().toString().equals("") || foto.getText().toString().contains("Hacer Foto")){
			showAlert("Error", "Debe ingresar todos los campos y tomar una foto de la boleta entregada");
			return;
		}
		if (ltsTotal == 0){
			showAlert("Error", "No puede generar una entrega sin leche en el estanque");
			return;
		}
		horaTotal = hora.getText().toString();
		no_guiaTotal = Integer.parseInt(no_guia.getText().toString());
		tempTotal = Integer.parseInt(temperatura.getText().toString());
		ltsTotal = Integer.parseInt(lts.getText().toString());
		
		//Login.connectSQL();
		try {
			
			if (selectedState.contains("completa")){
				if (lts_actuales - ltsTotal < 0){
					showAlert("Error", "Los litros ingresados no pueden ser mayor a los que posee el estanque");
					return;
				}
				stm.executeUpdate("INSERT INTO dbo2.rEntrega (hora, no_guia, temperatura, litros, fecha, estado, foto, id_estanque, usuario, id_leche_buena) VALUES ('" + horaTotal + "', " + no_guiaTotal + ", " + tempTotal + ", " + ltsTotal + ", '" + format.format(date.getTime()) + "', '" + "VALIDA" + "', '" + strFile + "', " + Principal.id_estanque_olb + ", '" + Login.activeUser + "', " + id_leche_buena + ")");
				stm.executeUpdate("UPDATE dbo2.rEstanque SET lts_actuales = " + 0 + " WHERE id_estanque = " + Principal.id_estanque_olb);
				if (esNula){

					ResultSet res2 = stm.executeQuery("SELECT total, litrosantesordena FROM dbo2.rLeche_Buena WHERE id_leche_buena = " + id_leche_buena);
					int litrosOrdena = 0;
					double rendimiento = 0;
					if (res2.next()){
						litrosOrdena = ltsTotal - res2.getInt(2);
						double tempLtsOrdena = litrosOrdena;
						rendimiento = tempLtsOrdena / res2.getDouble(1);
					}
					stm.executeUpdate("UPDATE dbo2.rLeche_Buena SET litrosdespuesordena = " + ltsTotal + ", litrosordena = " + litrosOrdena + ", rendimiento = " + rendimiento + " WHERE id_leche_buena = " + id_leche_buena);
					//añadir update regla al update anterior
					esNula = false;
				}
				
			}
			if (selectedState.contains("Anular")){
				//dar fecha, litros
				//estado NULA
				if (lts_actuales - ltsTotal < 0){
					showAlert("Error", "Los litros ingresados no pueden ser mayor a los que posee el estanque");
					return;
				}
				stm.executeUpdate("INSERT INTO dbo2.rEntrega (hora, no_guia, temperatura, litros, fecha, estado, foto, id_estanque, usuario, id_leche_buena) VALUES ('" + horaTotal + "', " + no_guiaTotal + ", " + tempTotal + ", " + ltsTotal + ", '" + format.format(date.getTime()) + "', '" + "NULA" + "', '" + strFile + "', " + Principal.id_estanque_olb + ", '" + Login.activeUser + "', " + id_leche_buena + ")");
				esNula = true;
				Intent i = new Intent(this, GenerarEntrega.class);
				startActivity(i);
				
			}
			if (selectedState.contains("parcial")){
				//cambiar litros actuales estanque (restar lo q salio en la boleta)
				//dar litros
				if (lts_actuales - ltsTotal < 0){
					showAlert("Error", "Los litros ingresados no pueden ser mayor a los que posee el estanque");
					return;
				}
				stm.executeUpdate("INSERT INTO dbo2.rEntrega (hora, no_guia, temperatura, litros, fecha, estado, foto, id_estanque, usuario, id_leche_buena) VALUES ('" + horaTotal + "', " + no_guiaTotal + ", " + tempTotal + ", " + ltsTotal + ", '" + format.format(date.getTime()) + "', '" + "VALIDA" + "', '" + strFile + "', " + Principal.id_estanque_olb + ", '" + Login.activeUser + "', " + id_leche_buena + ")");
				stm.executeUpdate("UPDATE dbo2.rEstanque SET lts_actuales = " + (lts_actuales - ltsTotal) + " WHERE id_estanque = " + Principal.id_estanque_olb);
				esNula = false;
				
			}
			
			success = true;
			finish();
			
		} catch (SQLException e) {
			if (e.getErrorCode() == 241){
				showAlert("Error", "Hora no válida\n\nEjemplo 1: 5:35\nEjemplo 2: 16:50");
			}else{
				if (e.getErrorCode() == 2627){
					showAlert("Error", "N° Guia ya existe, verifique nuevamente\n\nSi el problema persiste, contactarse con oficina");
				}
			}
		}

	}

	
	private void initialize(){
		selectedState = "completa";
		success = false;
		horaTotal = null;
		no_guiaTotal = 0;
		tempTotal = 0;
		ltsTotal = 0;
		allowMaxDays = 0;
		lts_actuales = 0;
		hora = (EditText)findViewById(R.id.hora);
		no_guia = (EditText)findViewById(R.id.no_guia);
		temperatura = (EditText)findViewById(R.id.temperatura);
		lts = (EditText)findViewById(R.id.lts);
		foto = (Button)findViewById(R.id.foto);
		mas = (Button)findViewById(R.id.mas);
		menos = (Button)findViewById(R.id.menos);
		fecha = (EditText)findViewById(R.id.fecha);
		fecha.setText(Principal.load_date());
		fecha.setEnabled(false);
		mas.setEnabled(false);
		menos.setEnabled(false);
		lts.setEnabled(false);
		date = new GregorianCalendar();
		format = new SimpleDateFormat("dd-MM-yyyy");
		
		try {
			stm = Login.conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		temperatura.setImeOptions(EditorInfo.IME_ACTION_DONE);
		lts.setImeOptions(EditorInfo.IME_ACTION_DONE);
		
		rg = (RadioGroup)findViewById(R.id.radioGroup);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	    {
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	            RadioButton rb = (RadioButton)findViewById(checkedId);
	            selectedState = rb.getText().toString();
	            if (esNula == false){
	            	if (rb.getText().toString().contains("completa") || rb.getText().toString().contains("Anular")){
	            		lts.setText(Integer.toString(lts_actuales));
	            	}
		            if (rb.getText().toString().contains("parcial")){
		            	lts.setEnabled(true);
		            }else{
		            	lts.setEnabled(false);
		            }
	            }
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.generar_entrega, menu);
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
