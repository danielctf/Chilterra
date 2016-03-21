package cl.a2r.rb51;

import java.util.List;

import cl.a2r.animales.Aplicaciones;
import cl.a2r.animales.Login;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.secados.Logs;
import cl.a2r.secados.Secados;
import cl.a2r.secados.Sincronizacion;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Secado;
import cl.a2r.sip.model.Traslado;
import cl.ar2.sqlite.servicio.PredioLibreServicio;
import cl.ar2.sqlite.servicio.SecadosServicio;
import cl.ar2.sqlite.servicio.TrasladosServicio;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class RB51 extends Activity implements View.OnClickListener{

	private Integer mangadaActual;
	private boolean isMangadaCerrada;
	private Ganado gan;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_rb51);
		
		cargarInterfaz();
	}
	
	private void cargarInterfaz(){
		
		isMangadaCerrada = false;
		gan = new Ganado();
		
	}
	
	private void mostrarCandidatos(){
		
	}
	
	public void onClick(View v) {
		int id = v.getId();
		switch (id){
		case R.id.goBack:
			ShowAlert.askYesNo("Advertencia", "¿Seguro que desea salir de la aplicación?", this, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if (which == -2){
						finish();
					}
				}
			});
			break;
		case R.id.logs:
			Intent i = new Intent(this, Logs.class);
			i.putExtra("mangadaActual", mangadaActual);
			startActivity(i);
			break;
		case R.id.confirmarAnimal:
			agregarAnimal();
			break;
		case R.id.tvDiio:
			startActivity(new Intent(this, Calculadora.class));
			break;
		case R.id.cerrarMangada:
			verificarCierreMangada();
			break;
		case R.id.sync:
			new Sincronizacion(this, Login.user).execute();
			break;
		}
	}
	
	private void agregarAnimal(){
		if (isMangadaCerrada){
			isMangadaCerrada = false;
			mangadaActual++;
		}
	}
	
	private void verificarCierreMangada(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Title");
	
		// Set up the input
		final EditText input = new EditText(this);
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		builder.setView(input);
		// Set up the buttons
		builder.setTitle("Animales");
		builder.setMessage("Ingrese numero de Animales en Mangada");
		builder.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	dialog.cancel();
		    }
		});
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	try {
		    		int ingresados = Integer.parseInt(input.getText().toString());
					List<Secado> list = SecadosServicio.traeGanadoASincronizar();
					int totalesManga = 0;
					for (Secado s : list){
						if (s.getGan().getMangada().intValue() == mangadaActual.intValue()){
							totalesManga++;
						}
					}
					if (totalesManga == ingresados){
						cerrarMangada(true);
					} else {
						ShowAlert.showAlert("Error", "El número de animales ingresado no coincide con la mangada actual", RB51.this);
					}
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), RB51.this);
				} catch (NumberFormatException e){
					ShowAlert.showAlert("Error", "Debe ingresar un numero", RB51.this);
				}
		    	
		    }
		});
	
		builder.show();
	}
	
	private void updateStatus(){
		
	}
	
	private void cerrarMangada(boolean showMsg){
		if (showMsg){
			Toast.makeText(this, "Mangada Cerrada", Toast.LENGTH_LONG).show();
		}
		isMangadaCerrada = true;
		updateStatus();
	}
	
	private void clearScreen(){
		gan = new Ganado();
		resetCalculadora();
		updateStatus();
	}
	
	private void resetCalculadora(){
		Calculadora.gan = null;
	}
	
	private void verReubicacion(final Ganado gan){
		if (gan.getPredio().intValue() != Aplicaciones.predioWS.getId().intValue()){
			ShowAlert.askYesNo("Predio", "El Animal figura en otro predio\n¿Esta seguro que el DIIO es correcto?", this, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					if (arg1 == -2){
						try {
							Traslado t = new Traslado();
							t.setFundoOrigenId(gan.getPredio());
							t.setFundoDestinoId(Aplicaciones.predioWS.getId());
							t.getGanado().add(gan);
							TrasladosServicio.insertaReubicacion(t);
							TrasladosServicio.updateGanadoFundo(Aplicaciones.predioWS.getId(), gan.getId());
							gan.setPredio(Aplicaciones.predioWS.getId());
							showDiio(gan);
						} catch (AppException e) {
							ShowAlert.showAlert("Error", e.getMessage(), RB51.this);
						}
					}
				}
			});
		} else {
			showDiio(gan);
		}
	}
	
	private void showDiio(Ganado gan){
		updateStatus();
	}
	
	private void checkDiioStatus(Ganado gan){
		if (gan != null){
			try {
				boolean exists = SecadosServicio.existsGanado(gan.getId());
				if (!exists){
					clearScreen();
					verReubicacion(gan);
				} else {
					Toast.makeText(this, "Animal ya existe", Toast.LENGTH_LONG).show();
				}
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this);
			}
		}
		updateStatus();
	}
	
	public void onDestroy(){
		super.onStart();

		Calculadora.isPredioLibre = false;
		resetCalculadora();
	}
	
	public void onStart(){
		super.onStart();
		
		try {
			mangadaActual = SecadosServicio.mangadaActual();
			if (mangadaActual == null){
				mangadaActual = 0;
				cerrarMangada(false);
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
		}
		
		mostrarCandidatos();
		
		Calculadora.isPredioLibre = true;
		ConnectThread.setHandler(mHandler);
		
		checkDiioStatus(Calculadora.gan);
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
    			EID = EID.trim();
    			long temp = Long.parseLong(EID);
    			EID = Long.toString(temp);
    			try {
					Ganado g = PredioLibreServicio.traeEID(EID);
					if (g != null){
						checkDiioStatus(g);
					} else {
						ShowAlert.showAlert("Error", "DIIO no existe", RB51.this);
					}
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), RB51.this);
				}
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", RB51.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };

}
