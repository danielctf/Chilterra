package cl.a2r.animales;

import java.util.List;

import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.wsservice.WSAutorizacionCliente;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.Plus.PlusOptions;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
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
	private boolean upToDate;
	
	private SignInButton signIn;
	
	public static int user;
	public static int sesionId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    
        mGoogleApiClient = buildGoogleAPIClient();
        upToDate = isUpToDate();
        
        cargarInterfaz();
    }
    
    private void cargarInterfaz(){
        signIn = (SignInButton)findViewById(R.id.signIn);
        signIn.setOnClickListener(this);
    }
    
    private boolean isUpToDate(){
    	try {
    		PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			Integer ver = WSAutorizacionCliente.traeVersionAndroid();
			
			if (ver.intValue() != pInfo.versionCode){
				return false;
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		} catch (NameNotFoundException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
    	
    	return true;
    }
    
    private GoogleApiClient buildGoogleAPIClient() {
    	   return new GoogleApiClient.Builder(this)
    	   		 .addConnectionCallbacks(this)
    	         .addOnConnectionFailedListener(this)
    	         .addApi(Plus.API, PlusOptions.builder().build())
    	         .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }
    
	public void onConnected(Bundle connectionHint) {
        try {
        	String correo = Plus.AccountApi.getAccountName(mGoogleApiClient);
            user = WSAutorizacionCliente.traeUsuario(correo);
			mSignInClicked = false;
			Toast.makeText(getApplicationContext(), "Sesión Iniciada", Toast.LENGTH_LONG).show();
			mGoogleApiClient.disconnect();
			Intent i = new Intent(this, Aplicaciones.class);
			startActivity(i);
			mConnectionResult = null;
        } catch (AppException ex) {
            ShowAlert.showAlert("Error", ex.getMessage(), this);
            processSignOut();
        }
		
	}
	
	private void processSignOut(){
		if (mGoogleApiClient.isConnected()){
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
		}
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
		if (!(upToDate)){
			ShowAlert.askYesNo("Actualización", "La aplicación no está actualizada a su versión mas reciente.\n¿Desea descargarla ahora?", this,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							if (which == -2){
								final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
								try {
								    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
								} catch (android.content.ActivityNotFoundException anfe) {
								    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
								}
							}
						}
					});
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
	    } else {
			mGoogleApiClient.connect();
	    }
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == RESULT_OK) {
	    	mGoogleApiClient.connect();
	    }
	}
	
	protected  void onStart(){
		super.onStart();
		ConnectThread.setHandler(mHandler);
		
		if (isOnline() == false){
			return;
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
	
	//---------------------------------------------------------------------------
	//------------------------DATOS ENVIADOS DESDE BASTÓN------------------------
	//---------------------------------------------------------------------------
	
    private Handler mHandler = new Handler(){
    	@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		switch(msg.what){
    		case ConnectThread.SUCCESS_CONNECT:
    			BluetoothSocket mmSocket = (BluetoothSocket) ((List<Object>) msg.obj).get(0);
    			BluetoothDevice mmDevice = (BluetoothDevice) ((List<Object>) msg.obj).get(1);
    	        ConnectedThread connectedThread = new ConnectedThread(mmSocket, mmDevice);
    	        connectedThread.start();
    			break;
    		case ConnectedThread.MESSAGE_READ:
    			String EID = (String) msg.obj;
    			System.out.println(EID);
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", Login.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };
    
}
