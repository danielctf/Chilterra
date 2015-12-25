package cl.a2r.alimentacion;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.custom.AppLauncher;
import cl.a2r.custom.GridViewAdapter;
import cl.a2r.custom.RoundedImageView;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sap.model.Aplicacion;
import cl.a2r.sap.model.Persona;
import cl.a2r.sap.model.Predio;
import cl.ar2.sqlite.cobertura.GoogleServicio;
import cl.ar2.sqlite.dao.SqLiteTrx;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.Plus.PlusOptions;
import com.google.android.gms.plus.model.people.Person;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
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
	private TextView predio, nombrePerfil, correoPerfil;
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

		SqLiteTrx.Inicializa(this);
	}
	
	private void cargarInterfaz(){
		predio = (TextView)findViewById(R.id.predio);
		predio.setText("Aplicaciones");
		gridApps = (GridView)findViewById(R.id.gridApps);
		menu = (ImageButton)findViewById(R.id.menu);
		menu.setOnClickListener(this);
		lvPredios = (ListView)findViewById(R.id.lvPredios);
		lvOptions = (ListView)findViewById(R.id.lvOptions);
		String[] options = {"Cerrar Sesión"};
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
		List<Predio> list = new ArrayList<Predio>();
		Predio p;
		p = new Predio();
		p.setId(3)	;	p.setNombre("El Huite 1")	;	p.setPotreros(62);
		list.add(p);
		p = new Predio();
		p.setId(9)	;	p.setNombre("El Huite 2")	;	p.setPotreros(63);
		list.add(p);
		p = new Predio();
		p.setId(10)	;	p.setNombre("El Huite 3")	;	p.setPotreros(56);
		list.add(p);
		p = new Predio();
		p.setId(12)	;	p.setNombre("El Huite 5")	;	p.setPotreros(65);
		list.add(p);
		p = new Predio();
		p.setId(15)	;	p.setNombre("El Huite 8")	;	p.setPotreros(61);
		list.add(p);
		p = new Predio();
		p.setId(4)	;	p.setNombre("La Montaña")	;	p.setPotreros(67);
		list.add(p);
		p = new Predio();
		p.setId(7)	;	p.setNombre("San José")	;	p.setPotreros(61);
		list.add(p);
		p = new Predio();
		p.setId(6)	;	p.setNombre("Santa Genova")	;	p.setPotreros(85);
		list.add(p);
		p = new Predio();
		p.setId(8)	;	p.setNombre("Santa Laura")	;	p.setPotreros(89);
		list.add(p);
		
		ArrayAdapter<Predio> mAdapter = new ArrayAdapter<Predio>(this, android.R.layout.simple_list_item_1, list);
		lvPredios.setAdapter(mAdapter);
		
	}
	
	private void getAppsWS(){
		apps = new ArrayList<Aplicacion>();
		Aplicacion a = new Aplicacion();
		a.setId(1);
		a.setNombre("Medición");
		a.setActiva("Y");
		apps.add(a);
		//a = new Aplicacion();
		//a.setId(2);
		//a.setNombre("Stock");
		//a.setActiva("Y");
		//apps.add(a);
		
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
		int id = v.getId();
		switch (id){
		case R.id.menu:
			mDrawerLayout.openDrawer(mDrawerLinear);
			break;

		}
	}
	
	protected  void onStart(){
		super.onStart();
		
		mGoogleApiClient.connect();
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
	    	try {
		    	if (result != null){
		    		
			    	roundedImage = new RoundedImageView(result);
			        bmImage.setImageDrawable(roundedImage);

					Persona persona = GoogleServicio.traePersona(Login.mail);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					result.compress(Bitmap.CompressFormat.PNG, 100, stream);
					byte[] byteArray = stream.toByteArray();
					
					if (persona.getCorreo() == null){
						Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
						Persona p = new Persona();
						p.setNombre(person.getDisplayName());
						p.setCorreo(Login.mail);
						p.setPhoto(byteArray);
						GoogleServicio.insertaMedicion(p);
						return;
					}
					if (!Arrays.equals(byteArray, persona.getPhoto())){
						Persona newP = new Persona();
						newP.setCorreo(Login.mail);
						newP.setPhoto(byteArray);
						GoogleServicio.updatePhoto(newP);
					}
		    	} else {
		    		Persona persona = GoogleServicio.traePersona(Login.mail);
		    		if (persona.getCorreo() != null){
		    			Bitmap bitmap = BitmapFactory.decodeByteArray(persona.getPhoto() , 0, 
		    					persona.getPhoto() .length);
		    			
				    	roundedImage = new RoundedImageView(bitmap);
				        bmImage.setImageDrawable(roundedImage);
		    		}
		    	}
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), Aplicaciones.this);
			}
	    }
	}
	
	public void onConnectionSuspended(int cause) {
		
	}
	
	public static void createSession(){
		/*
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
		*/
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
			predioWS.setPotreros(((Predio) arg0.getItemAtPosition(arg2)).getPotreros());
			predio.setText(predioWS.getNombre());
			mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case R.id.lvOptions:
			System.out.println(arg2);
			if (arg2 == 0){
				processSignOut();
			}
			break;
		case R.id.gridApps:
			if (predio.getText() == "Aplicaciones"){
				ShowAlert.showAlert("Predio", "Debe seleccionar un predio\nantes de iniciar una aplicación.", Aplicaciones.this);
				return;
			}
			int appId = ((Aplicacion) arg0.getItemAtPosition(arg2)).getId();
			AppLauncher.setAppClass(appId);
			launchApplication();
			break;
		}
	}

}
