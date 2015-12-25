package cl.a2r.custom;

import cl.a2r.alimentacion.Aplicaciones;
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
	
	public static void askYesNo(String title, String msg, Context ctx, OnClickListener listener){
		AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
	    alertDialog.setButton2("Si", listener);
		alertDialog.setButton("No", listener);
		alertDialog.show();
	}

}
