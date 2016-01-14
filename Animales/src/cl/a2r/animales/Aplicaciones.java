package cl.a2r.animales;

import java.io.InputStream;
import java.util.List;

import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.AppLauncher;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.GridViewAdapter;
import cl.a2r.custom.RoundedImageView;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Aplicacion;
import cl.a2r.sip.model.Predio;
import cl.a2r.sip.model.Sesion;
import cl.a2r.sip.wsservice.WSAutorizacionCliente;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.Plus.PlusOptions;
import com.google.android.gms.plus.model.people.Person;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


@SuppressWarnings("unchecked")
public class Aplicaciones extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, ListView.OnItemClickListener {

	private final int PROFILE_PIC_SIZE = 100;
	private RoundedImageView roundedImage;
	private GoogleApiClient mGoogleApiClient;
	private GridViewAdapter gridAdapter;
	
	private GridView gridApps;
	private ImageButton menu;
	
	private DrawerLayout mDrawerLayout;
	private LinearLayout mDrawerLinear;
	private RelativeLayout fotoPortada;
	private ListView lvPredios, lvOptions;
	private ArrayAdapter<Predio> mAdapter;
	private ImageView circleView;
	private TextView predio, nombrePerfil, correoPerfil, tvVersion;
	private int width, height;
	private static boolean openMeOnce = true;
	private List<Aplicacion> apps;
	
	public static Predio predioWS = new Predio();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_aplicaciones);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
		mGoogleApiClient = buildGoogleAPIClient();
		
		cargarInterfaz();
		getPrediosWS();
		getAppsWS();
		checkIfAppIsRunning();

	}
	
	private void cargarInterfaz(){
		predio = (TextView)findViewById(R.id.predio);
		predio.setText("Aplicaciones");
		gridApps = (GridView)findViewById(R.id.gridApps);
		menu = (ImageButton)findViewById(R.id.menu);
		menu.setOnClickListener(this);
		lvPredios = (ListView)findViewById(R.id.lvPredios);
		lvOptions = (ListView)findViewById(R.id.lvOptions);
		String[] options = {"Conectar Bastón", "Cerrar Sesión"};
		lvOptions.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options));
		lvPredios.setOnItemClickListener(this);
		lvOptions.setOnItemClickListener(this);
		mDrawerLinear = (LinearLayout)findViewById(R.id.left_drawer);
		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer2Layout);
		//mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		fotoPortada = (RelativeLayout)findViewById(R.id.fotoPortada);
		fotoPortada.setOnClickListener(null);
		correoPerfil = (TextView)findViewById(R.id.profile_email);
		nombrePerfil = (TextView)findViewById(R.id.profile_name);
		circleView = (ImageView)findViewById(R.id.circleView);
		tvVersion = (TextView)findViewById(R.id.tvVersion);
		
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			tvVersion.setText("v"+pInfo.versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

	
	}
	
	private void checkIfAppIsRunning(){
		if (openMeOnce){
			mDrawerLayout.openDrawer(mDrawerLinear);
			openMeOnce = false;
		}else{
			predio.setText(predioWS.getNombre());
		}
	}
	
	private void getPrediosWS(){
		List<Predio> predios = null;
        try {
        	predios = WSAutorizacionCliente.traePredios();
            mAdapter = new ArrayAdapter<Predio>(this, android.R.layout.simple_list_item_1, predios);
    	    lvPredios.setAdapter(mAdapter);
        } catch (AppException ex) {
            ShowAlert.showAlert("Error", ex.getMessage(), this);
        }
	}
	
	private void getAppsWS(){
        try {
            apps = WSAutorizacionCliente.traeAplicaciones(Login.user);
        } catch (AppException ex) {
        	ShowAlert.showAlert("Error", ex.getMessage(), this);
        	return;
        }
		//Ordena las apps para que las que estan activas aparezcan al comienzo.
		for (int i = 0; i < apps.size(); i++){
			for (int j = i; j < apps.size(); j++){
				if (apps.get(j).getActiva().equals("Y")){
					Aplicacion temp = new Aplicacion();
					temp = apps.get(i);
					apps.set(i, apps.get(j));
					apps.set(j, temp);
					break;
				}
			}
		}
		
		gridAdapter = new GridViewAdapter(Aplicaciones.this, apps, width, height);
		gridApps.setAdapter(gridAdapter);
		gridApps.setOnItemClickListener(this);
		 
	}
	
    private GoogleApiClient buildGoogleAPIClient() {
 	   return new GoogleApiClient.Builder(this)
 	   		 .addConnectionCallbacks(this)
 	         .addOnConnectionFailedListener(this)
 	         .addApi(Plus.API, PlusOptions.builder().build())
 	         .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

	@Override
	public void onClick(View v) {
		if (isOnline() == false){
			return;
		}
		int id = v.getId();
		switch (id){
		case R.id.menu:
			mDrawerLayout.openDrawer(mDrawerLinear);
			break;

		}
	}
	
	protected  void onStart(){
		super.onStart();
		
		if (Login.user == 0){
			finish();
		}
		
		ConnectThread.setHandler(mHandler);
		
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
	
	protected void onDestroy() {
		super.onDestroy();
		
		openMeOnce = true;
	}
	
	public void onBackPressed(){		
		finish();
	}

	public void onConnectionFailed(ConnectionResult result) {
		
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

	public void onConnected(Bundle connectionHint) {
		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
			Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            nombrePerfil.setText(person.getDisplayName());
            correoPerfil.setText(Plus.AccountApi.getAccountName(mGoogleApiClient));
            cargarFotoPerfil();
		}
	}

	private void cargarFotoPerfil() {
	    try {
	        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
	            Person currentPerson = Plus.PeopleApi
	                    .getCurrentPerson(mGoogleApiClient);
	            String personPhotoUrl = currentPerson.getImage().getUrl();
	            personPhotoUrl = personPhotoUrl.substring(0,
	                    personPhotoUrl.length() - 2)
	                    + PROFILE_PIC_SIZE;
	 
	            new LoadProfileImage(circleView).execute(personPhotoUrl);
	            
	        } else {
	            Toast.makeText(getApplicationContext(),
	                    "Person information is null", Toast.LENGTH_LONG).show();
	        }
	    } catch (Exception e) {
	        ShowAlert.showAlert("Error", "Error de conexión a Internet", this);
	    }
	}
	 
	/**
	 * Background Async task to load user profile picture from url
	 * */
	private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;
	 
	    public LoadProfileImage(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }
	 
	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }
	 
	    protected void onPostExecute(Bitmap result) {
	    	roundedImage = new RoundedImageView(result);
	        bmImage.setImageDrawable(roundedImage);
	    }
	}
	
	public void onConnectionSuspended(int cause) {
		
	}
	
	public static void createSession(){
		Sesion s = new Sesion();
		s.setUsuarioId(Login.user);
		s.setFundoId(predioWS.getId());
		s.setAppId(AppLauncher.getAppId());
		s.setImei(null);
		
		try {
			Login.sesionId = WSAutorizacionCliente.insertaSesion(s);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void launchApplication(){
		Intent i = new Intent(this, AppLauncher.getAppClass());
		startActivity(i);
	}
	
	private void processSignOut(){
		if (mGoogleApiClient.isConnected()){
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			Toast.makeText(getApplicationContext(), "Sesión Finalizada", Toast.LENGTH_LONG).show();
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
			openMeOnce = true;
			finish();
		}
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int id = arg0.getId();
		switch (id){
		case R.id.lvPredios:
			predioWS.setId(((Predio) arg0.getItemAtPosition(arg2)).getId());
			predioWS.setCodigo(((Predio) arg0.getItemAtPosition(arg2)).getCodigo());
			predioWS.setNombre(((Predio) arg0.getItemAtPosition(arg2)).getNombre());
			predioWS.setRup(((Predio) arg0.getItemAtPosition(arg2)).getRup());
			predio.setText(predioWS.getNombre());
			mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case R.id.lvOptions:
			if (arg2 == 0){
				//Conectar Bastón
				Intent i = new Intent(this, Baston.class);
				startActivity(i);
			}else{
				//Cerrar Sesión
				processSignOut();
			}
			break;
		case R.id.gridApps:
			if (apps.get(arg2).getActiva().equals("Y")){
				 //SOLO SI SELECCIONÓ UN PREDIO PUEDE ENTRAR
				 if (predio.getText() != "Aplicaciones"){
					 AppLauncher.setAppClass(apps.get(arg2).getId());
				 } else {
					 ShowAlert.showAlert("Predio", "Debe seleccionar un predio\nantes de iniciar una aplicación.", Aplicaciones.this);
					 return;
				 }
				 
				 for (int i = 0; i < Baston.listaConectados.size(); i++){
					 if (!(Baston.listaConectados.get(i).isAlive())){
						 Baston.listaConectados.remove(i);
					 }
				 }
				 if (Baston.listaConectados.size() == 0){
					 ShowAlert.askBaston("Bastón", "No hay ningún bastón conectado\n¿Desea conectar con uno?", Aplicaciones.this);
				 } else {
					 createSession();
					 launchApplication();
				 }
			}
			break;
		}
	}
	
	//---------------------------------------------------------------------------
	//------------------------DATOS ENVIADOS DESDE BASTÓN------------------------
	//---------------------------------------------------------------------------
	
    private Handler mHandler = new Handler(){
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
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", Aplicaciones.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };

}
