package cl.a2r.animales;

import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.EmptyFragment;
import cl.a2r.custom.ShowAlert;
import cl.a2r.login.R;
import cl.a2r.object.AreteoObject;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Traslado;
import cl.a2r.sip.wsservice.WSGanadoCliente;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Areteos extends Activity implements View.OnClickListener, DialogInterface.OnClickListener{

	private Fragment altaTab = new AreteosAlta();
	private Fragment emptyTab = new EmptyFragment();
	private Fragment aparicionTab = new AreteosAparicion();
	private Fragment cambioDiioTab = new AreteosCambioDiio();
	
	public static TextView despliegaDiio, textViewDiio, tvApp, deshacer, despliegaDiioAnterior, textViewDiioAnterior, tvEncontrados, tvFaltantes;
	public static ImageButton goBack, undo, logs, cows_left;
	private RelativeLayout layoutCambioDiio, fragmentContainer;
	public static RelativeLayout calculadora;
	private Spinner spinnerTipoAreteo;
	private static int stance;
	public static int ganadoId, diioAnterior, diio, logStance;
	private int tempGanadoId, tempDiioAnterior, tempPredio;
	private RadioButton radioDiio, radioDiioAnterior;
	private boolean isAreteo;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_areteo);
		
		cargarInterfaz();
		cargarListeners();
		getTiposAreteo();
		clearScreen();
	}
	
	private void cargarInterfaz(){
		despliegaDiio = (TextView)findViewById(R.id.despliegaDiio);
		despliegaDiio.setOnClickListener(this);
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		undo = (ImageButton)findViewById(R.id.undo);
		undo.setOnClickListener(this);
		calculadora = (RelativeLayout)findViewById(R.id.layoutCalculadora);
		calculadora.setOnClickListener(this);
		textViewDiio = (TextView)findViewById(R.id.textViewDiio);
		textViewDiio.setOnClickListener(this);
		tvApp = (TextView)findViewById(R.id.app);
		spinnerTipoAreteo = (Spinner)findViewById(R.id.spinnerTipoAreteo);
		deshacer = (TextView)findViewById(R.id.deshacer);
		deshacer.setOnClickListener(this);
		logs = (ImageButton)findViewById(R.id.logs);
		logs.setOnClickListener(this);
		layoutCambioDiio = (RelativeLayout)findViewById(R.id.layoutCambioDiio);
		fragmentContainer = (RelativeLayout)findViewById(R.id.fragmentContainer);
		layoutCambioDiio.setLayoutParams(new TableLayout.LayoutParams(0,0));
		layoutCambioDiio.setOnClickListener(this);
		despliegaDiioAnterior = (TextView)findViewById(R.id.despliegaDiioAnterior);
		despliegaDiioAnterior.setOnClickListener(this);
		textViewDiioAnterior = (TextView)findViewById(R.id.textViewDiioAnterior);
		textViewDiioAnterior.setOnClickListener(this);
		radioDiio = (RadioButton)findViewById(R.id.radioDiio);
		radioDiio.setOnClickListener(this);
		radioDiioAnterior = (RadioButton)findViewById(R.id.radioDiioAnterior);
		radioDiioAnterior.setOnClickListener(this);
		tvFaltantes = (TextView)findViewById(R.id.textViewFaltantes);
		tvEncontrados = (TextView)findViewById(R.id.textViewEncontrados);
		cows_left = (ImageButton)findViewById(R.id.cows_left);
		cows_left.setOnClickListener(this);
		isAreteo = true;
	}
	
	private void cargarListeners(){
		spinnerTipoAreteo.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				stance = ((AreteoObject) arg0.getItemAtPosition(arg2)).getId();
				switchStance();
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
	}
	
	private void getTiposAreteo(){
		AreteoObject ao = new AreteoObject();
		ao.setId(1);
		ao.setNombre("Alta");
		AreteoObject ao1 = new AreteoObject();
		ao1.setId(2);
		ao1.setNombre("Aparición");
		AreteoObject ao2 = new AreteoObject();
		ao2.setId(3);
		ao2.setNombre("Cambio DIIO");
		AreteoObject ao3 = new AreteoObject();
		ao3.setId(0);
		ao3.setNombre("");

		List<AreteoObject> list = new ArrayList<AreteoObject>();
		list.add(ao);
		list.add(ao1);
		list.add(ao2);
		list.add(0, ao3);
		
		ArrayAdapter<AreteoObject> mAdapter = new ArrayAdapter<AreteoObject>(this, android.R.layout.simple_list_item_1, list);
		spinnerTipoAreteo.setAdapter(mAdapter);
	}

	private void switchStance(){
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		switch (stance){
		case 0:
			tvFaltantes.setVisibility(View.INVISIBLE);
			tvEncontrados.setVisibility(View.INVISIBLE);
			cows_left.setVisibility(View.INVISIBLE);
			radioDiio.setVisibility(View.INVISIBLE);
			transaction.replace(R.id.fragmentContainer, emptyTab);
			transaction.addToBackStack(null);
			transaction.commit();
			getFragmentManager().executePendingTransactions();
			fragmentContainer.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.60f));
			layoutCambioDiio.setLayoutParams(new TableLayout.LayoutParams(0,0));
			break;
		case 1:
			tvFaltantes.setVisibility(View.VISIBLE);
			tvEncontrados.setVisibility(View.VISIBLE);
			cows_left.setVisibility(View.VISIBLE);
			radioDiio.setVisibility(View.INVISIBLE);
			transaction.replace(R.id.fragmentContainer, altaTab, "altaTab");
			transaction.addToBackStack(null);
			transaction.commit();
			getFragmentManager().executePendingTransactions();
			fragmentContainer.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.60f));
			layoutCambioDiio.setLayoutParams(new TableLayout.LayoutParams(0,0));
			break;
		case 2:
			tvFaltantes.setVisibility(View.INVISIBLE);
			tvEncontrados.setVisibility(View.INVISIBLE);
			cows_left.setVisibility(View.INVISIBLE);
			radioDiio.setVisibility(View.INVISIBLE);
			transaction.replace(R.id.fragmentContainer, aparicionTab);
			transaction.addToBackStack(null);
			transaction.commit();
			getFragmentManager().executePendingTransactions();
			fragmentContainer.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.60f));
			layoutCambioDiio.setLayoutParams(new TableLayout.LayoutParams(0,0));
			break;
		case 3:
			tvFaltantes.setVisibility(View.INVISIBLE);
			tvEncontrados.setVisibility(View.INVISIBLE);
			cows_left.setVisibility(View.INVISIBLE);
			radioDiio.setVisibility(View.VISIBLE);
			transaction.replace(R.id.fragmentContainer, cambioDiioTab);
			transaction.addToBackStack(null);
			transaction.commit();
			getFragmentManager().executePendingTransactions();
			fragmentContainer.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.45f));
			layoutCambioDiio.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.15f));
			break;
		}
		updateStatus();
	}
	
	public static void updateStatus(){
		if (diio != 0){
			textViewDiio.setText("");
			despliegaDiio.setText(Integer.toString(diio));
			goBack.setVisibility(View.INVISIBLE);
			tvApp.setVisibility(View.INVISIBLE);
			undo.setVisibility(View.VISIBLE);
			deshacer.setVisibility(View.VISIBLE);
			logs.setVisibility(View.INVISIBLE);
		} else {
			textViewDiio.setText("DIIO:");
			despliegaDiio.setText("");
			goBack.setVisibility(View.VISIBLE);
			tvApp.setVisibility(View.VISIBLE);
			undo.setVisibility(View.INVISIBLE);
			deshacer.setVisibility(View.INVISIBLE);
			logs.setVisibility(View.VISIBLE);
		}
		
		switch (stance){
		case 0:
			logs.setVisibility(View.INVISIBLE);
			break;
		case 1:
			AreteosAlta.altaWS.setDiio(diio);
			AreteosAlta.updateStatus();
			break;
		case 2:
			AreteosAparicion.aparicionWS.setDiio(diio);
			AreteosAparicion.updateStatus();
			break;
		case 3:
			AreteosCambioDiio.cambioDiioWS.setGanadoId(ganadoId);
			AreteosCambioDiio.cambioDiioWS.setDiioAnterior(diioAnterior);
			AreteosCambioDiio.cambioDiioWS.setDiio(diio);
			AreteosCambioDiio.updateStatus();
			break;
		}
	}
	
	private void clearScreen(){
		switch (stance){
		case 0:
			resetCalculadora();
			spinnerTipoAreteo.setSelection(0);
			diio = 0;
			break;
		case 1:
			AreteosAlta.altaWS.setDiio(0);
			AreteosAlta.altaWS.setCollarId(0);
			AreteosAlta.altaWS.setSexo("");
			AreteosAlta.altaWS.setRazaId(0);
			AreteosAlta.spinnerCollar.setSelection(0);
			AreteosAlta.spinnerSexo.setSelection(0);
			AreteosAlta.spinnerRaza.setSelection(0);
			textViewDiio.setVisibility(View.VISIBLE);
			despliegaDiio.setVisibility(View.VISIBLE);
			calculadora.setEnabled(true);
			stance = 0;
			switchStance();
			clearScreen();
			break;
		case 2:
			AreteosAparicion.aparicionWS.setDiio(0);
			AreteosAparicion.aparicionWS.setEdadEnMeses(0);
			AreteosAparicion.aparicionWS.setRazaId(0);
			AreteosAparicion.spinnerTipoAnimal.setSelection(0);
			AreteosAparicion.spinnerEdad.setSelection(0);
			AreteosAparicion.spinnerRaza.setSelection(0);
			stance = 0;
			switchStance();
			clearScreen();
			break;
		case 3:
			diioAnterior = 0;
			ganadoId = 0;
			AreteosCambioDiio.cambioDiioWS.setGanadoId(0);
			AreteosCambioDiio.cambioDiioWS.setDiio(0);
			AreteosCambioDiio.cambioDiioWS.setDiioAnterior(0);
			stance = 0;
			switchStance();
			clearScreen();
			break;
		}
		updateStatus();
	}

	public void onClick(View v) {
		if (isOnline() == false){
			return;
		}
		int id = v.getId();
		switch (id){
		case R.id.layoutCalculadora:
		case R.id.textViewDiio:
		case R.id.despliegaDiio:
			//Si se hace areteo, la calculadora y baston van a buscar el diio a la tabla diio y no al ganado, debido a q éste no existe
			Calculadora.isAreteo = true;
			Intent i = new Intent(this, Calculadora.class);
			startActivity(i);
			break;
		case R.id.despliegaDiioAnterior:
		case R.id.textViewDiioAnterior:
		case R.id.layoutCambioDiio:
			Calculadora.isAreteo = false;
			Intent i2 = new Intent(this, Calculadora.class);
			startActivity(i2);
			break;
		case R.id.undo:
		case R.id.deshacer:
			clearScreen();
			break;
		case R.id.goBack:
			finish();
			break;
		case R.id.radioDiio:
			isAreteo = true;
			Calculadora.isAreteo = true;
			radioDiioAnterior.setChecked(false);
			break;
		case R.id.radioDiioAnterior:
			isAreteo = false;
			Calculadora.isAreteo = false;
			radioDiio.setChecked(false);
			break;
		case R.id.cows_left:
			Intent i3 = new Intent(getApplicationContext(), Candidatos.class);
			i3.putExtra("stance","areteosFaltantes");
			startActivity(i3);
			break;
		case R.id.logs:
			Intent i4;
			if (stance == 1){
				i4 = new Intent(getApplicationContext(), Candidatos.class);
				i4.putExtra("stance","areteosEncontrados");
				startActivity(i4);
			} else if (stance == 2){
				logStance = 2;
				i4 = new Intent(getApplicationContext(), Logs.class);
				startActivity(i4);
			} else if (stance == 3){
				logStance = 3;
				i4 = new Intent(getApplicationContext(), Logs.class);
				startActivity(i4);
			}
			break;
		}
	}
	
	@SuppressWarnings("static-access")
	public void onClick(DialogInterface dialog, int which) {
		if (which == -2){
			this.diioAnterior = tempDiioAnterior;
			this.ganadoId = tempGanadoId;
			
			Traslado t = new Traslado();
			t.setUsuarioId(Login.user);
			t.setFundoOrigenId(tempPredio);
			t.setFundoDestinoId(Aplicaciones.predioWS.getId());
			Ganado g = new Ganado();
			g.setId(ganadoId);
			t.getGanado().add(g);
			
			try {
				WSGanadoCliente.reajustaGanado(t);
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this);
			}
			
			updateStatus();
		} else {
			resetCalculadora();
		}
	}
	
	protected void onStart(){
		super.onStart();
		
		if (Login.user == 0){
			finish();
		}
		
		ConnectThread.setHandler(mHandler);
		
		if (isOnline() == false){
			return;
		}
		
		checkDiioStatus(Calculadora.diio, Calculadora.ganadoId, Calculadora.activa, Calculadora.predio);
		
	}
	
	private void resetCalculadora(){
		Calculadora.ganadoId = 0;
		Calculadora.diio = 0;
		Calculadora.predio = 0;
		Calculadora.activa = "";
		Calculadora.sexo = "";
		Calculadora.tipoGanado = 0;
	}
	
	protected  void onDestroy(){
		super.onStart();
		
		Calculadora.isAreteo = false;
		resetCalculadora();
		diio = 0;
		stance = 0;
		ganadoId = 0;
		diioAnterior = 0;
	}
	
	public void onBackPressed(){
		if (isOnline()){
			finish();
		}
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
	
	@SuppressWarnings("static-access")
	private void checkDiioStatus(int diio, int ganadoId, String activa, int predio){
		if (activa.equals("N")){
			ShowAlert.showAlert("Error", "DIIO se encuentra dado de baja", this);
			return;
		}
		
		if (diio != 0 && (diio == this.diio || diio == diioAnterior)){
			Calculadora.isAreteo = isAreteo;
			return;
		}
		
		if (Calculadora.isAreteo) {
			this.diio = diio;
		} else {
			if (ganadoId != 0){
				if (Aplicaciones.predioWS.getId() != predio){
					tempDiioAnterior = diio;
					tempGanadoId = ganadoId;
					tempPredio = predio;
					ShowAlert.askYesNo("Predio", "El Animal figura en otro predio\n¿Esta seguro que el DIIO es correcto?", this, this);
					return;
				}
			}
			this.diioAnterior = diio;
			this.ganadoId = ganadoId;
		}
		
		Calculadora.isAreteo = isAreteo;
		updateStatus();
		
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
    			if (isOnline() == false){
    				return;
    			}
    			String EID = (String) msg.obj;
    			if (isAreteo) {
    				try {
						int l_diio = WSGanadoCliente.traeDiioBaston(EID);
						checkDiioStatus(l_diio, 0, "Y", 0);
					} catch (AppException e) {
						ShowAlert.showAlert("Error", e.getMessage(), Areteos.this);
					}
    			} else {
	    			try {
						List<Ganado> list = WSGanadoCliente.traeGanadoBaston(EID);
						if (list.size() == 0){
							ShowAlert.showAlert("Error", "DIIO no existe", Areteos.this);
							return;
						}
						for (Ganado g: list){
							checkDiioStatus(g.getDiio(), g.getId(), g.getActiva(), g.getPredio());
						}
					} catch (AppException ex) {
						ShowAlert.showAlert("Error", ex.getMessage(), Areteos.this);
					}
    			}
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", Areteos.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };
	
}
