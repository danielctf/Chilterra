package cl.a2r.login;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;

@SuppressLint("ResourceAsColor")
public class AlertView {

	public static void showError(String message, Context ctx)
	{
            showAlert("Error", message, ctx, true);
	}
	
	public static void showAlert(String message, Context ctx)
	{
            showAlert("Alert", message, ctx, false);
	}
	
	public static void showAlert(String title, String message, Context ctx) {
            showAlert( title, message, ctx, false);
        }

        public static void showAlert(String title, String message, Context ctx, boolean error)
	{
		//Create a builder	
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title);
		builder.setMessage(message);
		//add buttons and listener
		EmptyListener pl = new EmptyListener(error);
		builder.setPositiveButton("OK", pl);
		//Create the dialog
		AlertDialog ad = builder.create();
		//show
		ad.show();
		
	}

        public static void showAlertAndExecute(String title, String message, Context ctx, boolean error, DialogInterface.OnClickListener listener)
	{
		//Create a builder
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title);
		builder.setMessage(message);
		//add buttons and listener
		builder.setPositiveButton("OK", listener);
		//Create the dialog
		AlertDialog ad = builder.create();
		//show
		ad.show();
	}

}

class EmptyListener implements android.content.DialogInterface.OnClickListener {
    private boolean error = false;
        public EmptyListener(boolean error) {
            super();
            this.error = error;
        }

	@Override
	public void onClick(DialogInterface dialog, int which)	{
            if ( error ) {
                System.exit(0);
            }
	}	


}
