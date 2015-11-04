package cl.a2r.animales;

import cl.a2r.common.AppException;
import cl.a2r.custom.ShowAlert;
import cl.a2r.login.R;
import cl.a2r.sip.wsservice.WSAutorizacionCliente;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.Plus.PlusOptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class Login extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
	
	private final int ERROR_DIALOG_REQUEST_CODE = 11;
	private final int SIGN_IN_REQUEST_CODE = 10;

	private boolean mSignInClicked;
	private boolean mIntentInProgress;
	
	private GoogleApiClient mGoogleApiClient;
	private ConnectionResult mConnectionResult;
	
	private SignInButton signIn;
	
	public static int user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        /*
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			finish();
			return;
		}
		*/
        setContentView(R.layout.activity_login);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
        mGoogleApiClient = buildGoogleAPIClient();
        cargarInterfaz();
    }
    
    private void cargarInterfaz(){
        signIn = (SignInButton)findViewById(R.id.signIn);
        signIn.setOnClickListener(this);
    }
    
    private GoogleApiClient buildGoogleAPIClient() {
    	   return new GoogleApiClient.Builder(this)
    	   		 .addConnectionCallbacks(this)
    	         .addOnConnectionFailedListener(this)
    	         .addApi(Plus.API, PlusOptions.builder().build())
    	         .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }
    
	public void onConnected(Bundle connectionHint) {
		//AUTENTICAR POR WS ADEMPIERE
        //try {
        	String correo = Plus.AccountApi.getAccountName(mGoogleApiClient);
        //    user = WSAutorizacionCliente.traeIdUsaurio(correo);
        	user = 1;
        //} catch (AppException ex) {
        //    ShowAlert.showAlert("Error", ex.getMessage(), this);
        //}
        	
		mSignInClicked = false;
		Toast.makeText(getApplicationContext(), "Sesión Iniciada", Toast.LENGTH_LONG).show();
		mGoogleApiClient.disconnect();
		Intent i = new Intent(this, Aplicaciones.class);
		startActivity(i);
		finish();
		
	}

	public void onConnectionSuspended(int cause) {
		mGoogleApiClient.connect();
	}
	
	private void processSignIn(){
		if (!mGoogleApiClient.isConnecting()){
			processSignInError();
			mSignInClicked = true;
		}
	}

	public void onClick(View v) {
		if (isOnline() == false){
			return;
		}
		int id = v.getId();
		switch (id){
		case R.id.signIn:
			processSignIn();
			break;
		}
	}

	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, ERROR_DIALOG_REQUEST_CODE).show();
		    return;
		}
		if (!mIntentInProgress) {
			mConnectionResult = result;
			if (mSignInClicked) {
				processSignInError();
			}
	    }
	}
	
	private void processSignInError() {
		if (mConnectionResult != null && mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
	            mConnectionResult.startResolutionForResult(this, SIGN_IN_REQUEST_CODE);
	        } catch (SendIntentException e) {
	        	mIntentInProgress = false;
	        	mGoogleApiClient.connect();
	        }
	    }
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == RESULT_OK) {
	    	mGoogleApiClient.connect();
	    }
	}
	
	protected  void onStart(){
		super.onStart();
		
		if (isOnline()){
			mGoogleApiClient.connect();
		}
	}
	
	protected void onStop(){
		super.onStop();
		if (mGoogleApiClient.isConnected()){
			mGoogleApiClient.disconnect();
		}
	}
	
	public void onBackPressed(){
		finish();
	}
	
	private boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if ((netInfo != null && netInfo.isConnectedOrConnecting()) == false){
	    	ShowAlert.showAlert("Error", "No hay conexión a Internet", this);
	    }
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	}
    
}
