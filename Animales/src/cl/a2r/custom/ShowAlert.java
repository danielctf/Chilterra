package cl.a2r.custom;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;

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
	
	public static void realizarMovimiento(String title, String msg, Context ctx){
		AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
	    alertDialog.setButton2("Si", new DialogInterface.OnClickListener() {
	
	       public void onClick(DialogInterface dialog,int id) {
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
}
