package cl.a2r.custom;

import cl.a2r.alimentacion.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

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
	
	public static void selectItem(String title, String[] items, Context ctx, OnClickListener listener){
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title);
		builder.setItems(items, listener);
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void multipleChoice(String title, String[] items, boolean[] checked, Context ctx, OnMultiChoiceClickListener listener, OnClickListener okListener){
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title);
		builder.setMultiChoiceItems(items, checked, listener);
		final AlertDialog alert = builder.create();
		alert.setButton("OK", okListener);
		alert.show();
	}
	
}
