package cl.a2r.traslados;

import java.util.ArrayList;
import java.util.List;
import cl.a2r.animales.Candidatos;
import cl.a2r.animales.R;
import cl.a2r.auditoria.Logs;
import cl.a2r.common.AppException;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Ganado;
import cl.ar2.sqlite.servicio.PredioLibreServicio;
import cl.ar2.sqlite.servicio.TrasladosServicio;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Animales extends Fragment implements View.OnClickListener{
	
	private ImageButton confirmarAnimal, cerrarMangada;
	private TextView tvDiio, tvTotalAnimales, tvMangada, tvAnimalesMangada, tvFaltantes, tvEncontrados, tvInfo;
	private LinearLayout llEncontrados, llFaltantes;
	private Integer mangadaActual;
	private boolean isMangadaCerrada;
	private Ganado gan;
	
	private View v;
	private Activity act;
	
	public Animales(Activity act){
		this.act = act;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	v = inflater.inflate(R.layout.fragment_animales, container, false);

    	cargarInterfaz();
    	
    	return v;
	}
	
	private void cargarInterfaz(){
		cerrarMangada = (ImageButton)v.findViewById(R.id.cerrarMangada);
		cerrarMangada.setOnClickListener(this);
		confirmarAnimal = (ImageButton)v.findViewById(R.id.confirmarAnimal);
		confirmarAnimal.setOnClickListener(this);
		tvDiio = (TextView)v.findViewById(R.id.tvDiio);
		tvDiio.setOnClickListener(this);
    	tvTotalAnimales = (TextView)v.findViewById(R.id.tvTotalAnimales);
    	tvMangada = (TextView)v.findViewById(R.id.tvMangada);
    	tvAnimalesMangada = (TextView)v.findViewById(R.id.tvAnimalesMangada);
		tvFaltantes = (TextView)v.findViewById(R.id.tvFaltantes);
		tvEncontrados = (TextView)v.findViewById(R.id.tvEncontrados);
		llEncontrados = (LinearLayout)v.findViewById(R.id.llEncontrados);
		llEncontrados.setOnClickListener(this);
		llFaltantes = (LinearLayout)v.findViewById(R.id.llFaltantes);
		llFaltantes.setOnClickListener(this);
		tvInfo = (TextView)v.findViewById(R.id.tvInfo);
		
		isMangadaCerrada = false;
		gan = new Ganado();
	}
	
	public void onClick(View v) {
		int id = v.getId();
		Intent i;
		switch (id){
		case R.id.confirmarAnimal:
			agregarAnimal();
			break;
		case R.id.tvDiio:
			startActivity(new Intent(act, Calculadora.class));
			break;
		case R.id.cerrarMangada:
			verificarCierreMangada();
			break;
		case R.id.llEncontrados:
			i = new Intent(act, Logs.class);
			i.putExtra("mangadaActual", mangadaActual);
			startActivity(i);
			break;
		case R.id.llFaltantes:
			i = new Intent(act, Candidatos.class);
			i.putExtra("stance", "auditoriaFaltantes");
			startActivity(i);
			break;
		}
	}
	
	private void mostrarCandidatos(){
		try {
			List<Ganado> list = TrasladosServicio.traeGanadoTraslado();
			
			//Encontrados
			tvEncontrados.setText(Integer.toString(list.size()));

			//Contadores mangada
			tvTotalAnimales.setText(Integer.toString(list.size()));
			tvMangada.setText(Integer.toString(mangadaActual));
			int animalesMangada = 0;
			for (Ganado g : list){
				if (g.getMangada().intValue() == mangadaActual.intValue()){
					animalesMangada++;
				}
			}
			tvAnimalesMangada.setText(Integer.toString(animalesMangada));
			
			actualizaInfoTraslado(list);
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), act);
		}

	}
	
	private void actualizaInfoTraslado(List<Ganado> list){
		int buey = 0;
		int toro = 0;
		int ternera = 0;
		int torete = 0;
		int ternero = 0;
		int vaca = 0;
		int vaquilla = 0;
		tvInfo.setText("");
		for (Ganado g : list){
			switch (g.getTipoGanadoId().intValue()){
			case 1:
				buey++;
				break;
			case 3:
				toro++;
				break;
			case 4:
				ternera++;
				break;
			case 5:
				torete++;
				break;
			case 6:
				ternero++;
				break;
			case 7:
				vaca++;
				break;
			case 8:
				vaquilla++;
				break;
			}
		}
		if (buey > 0){
			tvInfo.setText(tvInfo.getText() + "Buey: " + Integer.toString(buey) + "\n");
		}
		if (toro > 0){
			tvInfo.setText(tvInfo.getText() + "Toro: " + Integer.toString(toro) + "\n");
		}
		if (ternera > 0){
			tvInfo.setText(tvInfo.getText() + "Ternera: " + Integer.toString(ternera) + "\n");
		}
		if (torete > 0){
			tvInfo.setText(tvInfo.getText() + "Torete: " + Integer.toString(torete) + "\n");
		}
		if (ternero > 0){
			tvInfo.setText(tvInfo.getText() + "Ternero: " + Integer.toString(ternero) + "\n");
		}
		if (vaca > 0){
			tvInfo.setText(tvInfo.getText() + "Vaca: " + Integer.toString(vaca) + "\n");
		}
		if (vaquilla > 0){
			tvInfo.setText(tvInfo.getText() + "Vaquilla: " + Integer.toString(vaquilla) + "\n");
		}
	}
	
	private void agregarAnimal(){
		if (isMangadaCerrada){
			isMangadaCerrada = false;
			mangadaActual++;
		}
		
		gan.setMangada(mangadaActual);
		List<Ganado> list = new ArrayList<Ganado>();
		list.add(gan);
		TrasladosV2.superInstancia.getInstancia().setGanList(list);
		
		try {
			TrasladosServicio.insertaTraslado(TrasladosV2.superInstancia);
			Toast.makeText(act, "Animal Registrado", Toast.LENGTH_LONG).show();
			clearScreen();
			mostrarCandidatos();
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), act);
		}
		
	}
	
	private void verificarCierreMangada(){
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		builder.setTitle("Title");
	
		// Set up the input
		final EditText input = new EditText(act);
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
		    		List<Ganado> list = TrasladosServicio.traeGanadoTraslado();
					int totalesManga = 0;
					for (Ganado g : list){
						if (g.getMangada().intValue() == mangadaActual.intValue()){
							totalesManga++;
						}
					}
					if (totalesManga == ingresados){
						cerrarMangada(true);
					} else {
						ShowAlert.showAlert("Error", "El número de animales ingresado no coincide con la mangada actual", act);
					}
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), act);
				} catch (NumberFormatException e){
					ShowAlert.showAlert("Error", "Debe ingresar un numero", act);
				}
		    	
		    }
		});
	
		builder.show();
	}
	
	private void cerrarMangada(boolean showMsg){
		if (showMsg){
			Toast.makeText(act, "Mangada Cerrada", Toast.LENGTH_LONG).show();
		}
		isMangadaCerrada = true;
		updateStatus();
	}
	
	private void updateStatus(){
		if (gan.getId() != null && gan.getDiio() != null){
			tvDiio.setText(Integer.toString(gan.getDiio()));
			tvDiio.setGravity(Gravity.CENTER_HORIZONTAL);
			confirmarAnimal.setEnabled(true);
		} else {
			tvDiio.setGravity(Gravity.LEFT);
			tvDiio.setText("DIIO:");
			confirmarAnimal.setEnabled(false);
		}
		
		if (isMangadaCerrada){
			cerrarMangada.setEnabled(false);
		} else {
			cerrarMangada.setEnabled(true);
		}
	}
	
	private void showDiio(Ganado gan){
		this.gan = gan;
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
	
	private void checkDiioStatus(Ganado gan){
		if (gan != null){
			try {
				boolean exists = TrasladosServicio.existsGanado(gan.getId());
				clearScreen();
				if (!exists){
					showDiio(gan);
					System.out.println("tipogan: "+gan.getTipoGanadoId());
				} else {
					ShowAlert.showAlert("Animal", "Animal ya existe", act);
				}
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), act);
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
			mangadaActual = TrasladosServicio.traeMangadaActual();
			if (mangadaActual == null){
				mangadaActual = 0;
				cerrarMangada(false);
			}
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), act);
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
						ShowAlert.showAlert("Error", "DIIO no existe", act);
					}
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), act);
				}
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", act, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };

}
