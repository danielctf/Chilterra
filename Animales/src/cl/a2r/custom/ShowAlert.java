package cl.a2r.custom;

import cl.a2r.animales.Aplicaciones;
import cl.a2r.animales.Baston;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

@SuppressWarnings("deprecation")
public class ShowAlert {

	public static void showAlert(String title, String msg, Context ctx){
		AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

           public void onClick(DialogInterface dialog,int id) {
        	   dialog.dismiss();
           }
           
         });

		alertDialog.show();
	}
	
	public static void askReconnect(String title, String msg, Context ctx, BluetoothDevice device){
		final BluetoothDevice dev = device;
		AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
        alertDialog.setButton2("Si", new DialogInterface.OnClickListener() {

           public void onClick(DialogInterface dialog,int id) {
	   		   ConnectThread connectThread = new ConnectThread(dev, true);
	   		   connectThread.start();
        	   dialog.dismiss();
           }
           
        });
		
		alertDialog.setButton("No", new DialogInterface.OnClickListener() {

	           public void onClick(DialogInterface dialog,int id) {
	        	   dialog.dismiss();
	           }
	           
	    });

		alertDialog.show();
	}
	
	public static void askYesNo(String title, String msg, Context ctx, OnClickListener listener){
		AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
	    alertDialog.setButton2("Si", listener);
		alertDialog.setButton("No", listener);
		alertDialog.show();
	}
	
	public static void askBaston(String title, String msg, Context ctx){
		final Context context = ctx;
		AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
	    alertDialog.setButton2("Si", new DialogInterface.OnClickListener() {
	
	       public void onClick(DialogInterface dialog,int id) {
	    	   Intent i = new Intent(context, Baston.class);
	    	   context.startActivity(i);
	    	   dialog.dismiss();
	       }
	       
	    });
		
		alertDialog.setButton("No", new DialogInterface.OnClickListener() {
	
	           public void onClick(DialogInterface dialog,int id) {
	        	    Aplicaciones.createSession();
		       		Intent i = new Intent(context, AppLauncher.getAppClass());
		       		context.startActivity(i);
		       		dialog.dismiss();
	           }
	           
	    });
	
		alertDialog.show();
	}
}
