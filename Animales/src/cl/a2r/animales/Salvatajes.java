package cl.a2r.animales;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.login.R;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Salvataje;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Salvatajes extends Activity implements View.OnClickListener, ListView.OnItemClickListener{
	
	private ImageButton goBack, addGrupo, logs, share, confirmarAnimal, deleteGrupo, undo;
	private TextView despliegaDiio, textViewDiio, tvGrupoActual, deshacer, tvTotal, tvApp;
	private RelativeLayout layoutCalculadora;
	private ListView lvGrupos;
	public static Integer grupoIdActual;
	private Integer diio;
	private String EID;
	private ArrayAdapter<Salvataje> mAdapter;
	public static List<Salvataje> salvataje = new ArrayList<Salvataje>();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_salvataje);
		
		Calculadora.isSalvataje = true;
		
		cargarInterfaz();

	}
	
	private void cargarInterfaz(){
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		undo = (ImageButton)findViewById(R.id.undo);
		undo.setOnClickListener(this);
		deshacer = (TextView)findViewById(R.id.deshacer);
		deshacer.setOnClickListener(this);
		addGrupo = (ImageButton)findViewById(R.id.addGrupo);
		addGrupo.setOnClickListener(this);
		logs = (ImageButton)findViewById(R.id.logs);
		logs.setOnClickListener(this);
		share = (ImageButton)findViewById(R.id.share);
		share.setOnClickListener(this);
		confirmarAnimal = (ImageButton)findViewById(R.id.confirmarAnimal);
		confirmarAnimal.setOnClickListener(this);
		deleteGrupo = (ImageButton)findViewById(R.id.deleteGrupo);
		deleteGrupo.setOnClickListener(this);
		despliegaDiio = (TextView)findViewById(R.id.despliegaDiio);
		despliegaDiio.setOnClickListener(this);
		textViewDiio = (TextView)findViewById(R.id.textViewDiio);
		textViewDiio.setOnClickListener(this);
		tvGrupoActual = (TextView)findViewById(R.id.tvGrupoActual);
		tvGrupoActual.setOnClickListener(this);
		layoutCalculadora = (RelativeLayout)findViewById(R.id.layoutCalculadora);
		layoutCalculadora.setOnClickListener(this);
		lvGrupos = (ListView)findViewById(R.id.lvGrupos);
		lvGrupos.setOnItemClickListener(this);
		tvTotal = (TextView)findViewById(R.id.tvTotal);
		tvApp = (TextView)findViewById(R.id.app);
		
		diio = 0;
		mAdapter = new ArrayAdapter<Salvataje>(this, android.R.layout.simple_list_item_1, salvataje);
		lvGrupos.setAdapter(mAdapter);
		
	}
	
	private void updateStatus(){
		
		//Habilita diio si hay algun grupo seleccionado
		if (grupoIdActual != null){
			textViewDiio.setEnabled(true);
			despliegaDiio.setEnabled(true);
			layoutCalculadora.setEnabled(true);
			deleteGrupo.setEnabled(true);
			for (Salvataje s : salvataje){
				if (s.getGrupoId().intValue() == grupoIdActual.intValue()){
					tvGrupoActual.setText(s.getNombreGrupo());
				}
			}
		} else {
			textViewDiio.setEnabled(false);
			despliegaDiio.setEnabled(false);
			layoutCalculadora.setEnabled(false);
			deleteGrupo.setEnabled(false);
			tvGrupoActual.setText("");
		}
		
		//Interfaz
		if (diio != 0 || EID != null){
			if (EID != null){
				long eid = Long.valueOf(EID.trim());
				EID = Long.toString(eid);
				despliegaDiio.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
				despliegaDiio.setText(EID);
			} else {
				despliegaDiio.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
				despliegaDiio.setText(Integer.toString(diio));
			}
			textViewDiio.setText("");
			confirmarAnimal.setEnabled(true);
			deleteGrupo.setEnabled(false);
			deshacer.setVisibility(View.VISIBLE);
			undo.setVisibility(View.VISIBLE);
			goBack.setVisibility(View.INVISIBLE);
			share.setVisibility(View.INVISIBLE);
			tvTotal.setVisibility(View.INVISIBLE);
			logs.setVisibility(View.INVISIBLE);
			tvApp.setVisibility(View.INVISIBLE);
		} else {
			textViewDiio.setText("DIIO:");
			despliegaDiio.setText("");
			confirmarAnimal.setEnabled(false);
			deshacer.setVisibility(View.INVISIBLE);
			undo.setVisibility(View.INVISIBLE);
			goBack.setVisibility(View.VISIBLE);
			share.setVisibility(View.VISIBLE);
			tvTotal.setVisibility(View.VISIBLE);
			logs.setVisibility(View.VISIBLE);
			tvApp.setVisibility(View.VISIBLE);
		}
		
		//Actualiza contador de animales (tvTotal)
		boolean exists = false;
		for (Salvataje s : salvataje){
			if (grupoIdActual != null && s.getGrupoId().intValue() == grupoIdActual.intValue()){
				tvTotal.setText(Integer.toString(s.getGanado().size()));
				exists = true;
			}
		}
		
		if (!(exists)){
			tvTotal.setText("");
		}
		deleteGrupo.setVisibility(View.INVISIBLE);
	}
	
	public void onClick(View arg0) {
		int id = arg0.getId();
		Intent i;
		switch (id){
		case R.id.goBack:
			finish();
			break;
		case R.id.addGrupo:
			agregarGrupo();
			break;
		case R.id.textViewDiio:
		case R.id.despliegaDiio:
		case R.id.layoutCalculadora:
			i = new Intent(this, Calculadora.class);
			startActivity(i);
			break;
		case R.id.confirmarAnimal:
			agregarAnimal();
			break;
		case R.id.deleteGrupo:
			eliminarGrupo();
			break;
		case R.id.logs:
			i = new Intent(this, Logs.class);
			startActivity(i);
			break;
		case R.id.deshacer:
		case R.id.undo:
			clearScreen();
			break;
		case R.id.share:
			enviarCorreo();
			break;
		}
		updateStatus();
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int id = arg0.getId();
		switch (id){
		case R.id.lvGrupos:
			grupoIdActual = ((Salvataje) arg0.getItemAtPosition(arg2)).getGrupoId();
			tvGrupoActual.setText(((Salvataje) arg0.getItemAtPosition(arg2)).getNombreGrupo());
			break;
		}
		updateStatus();
	}
	
	private void enviarCorreo(){
		if (grupoIdActual == null){
			ShowAlert.showAlert("Grupo", "Debe seleccionar un grupo", this);
			return;
		}
		
		List<Ganado> gTo = null;
		String nombreGrupo = null;
		for (Salvataje s : salvataje){
			if (s.getGrupoId().intValue() == grupoIdActual.intValue()){
				if (s.getGanado().size() == 0){
					ShowAlert.showAlert("Animales", "No tiene Animales registrados", this);
					return;
				} else {
					nombreGrupo = s.getNombreGrupo();
					gTo = s.getGanado();
				}
			}
		}
		
		File file   = null;
		File root   = Environment.getExternalStorageDirectory();
		if (root.canWrite()){
			File dir = new File (root.getAbsolutePath() + "/Bastoneo");
		    dir.mkdirs();
		    file   =   new File(dir, "Bastoneo Grupo " + nombreGrupo + ".csv");
		    FileOutputStream out   =   null;
		    try {
		        out = new FileOutputStream(file);
		        } catch (FileNotFoundException e) {
		            e.printStackTrace();
		        }
		        try {
		        	byte[] endl = "\n".getBytes();
		        	for (Ganado g : gTo){
		        		if (g.getEid() != null){
		        			out.write(g.getEid().getBytes());
		        			out.write(endl);
		        		} else {
		        			out.write(g.getDiio().toString().getBytes());
		        			out.write(endl);
		        		}
		        	}
		        	out.close();
		        } catch (IOException e) {
		            ShowAlert.showAlert("Error", "VOLCADO A FICHERO\n" + e.getMessage(), this);
		        }
		}
		
		Uri u = Uri.fromFile(file);
		
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Bastoneo Grupo " + nombreGrupo);
		sendIntent.putExtra(Intent.EXTRA_STREAM, u);
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
		
	}
	
	private void eliminarGrupo(){
		Salvataje toRemove = null;
		for (Salvataje s : salvataje){
			if (grupoIdActual != null && s.getGrupoId().intValue() == grupoIdActual.intValue()){
				toRemove = s;
			}
		}
		mAdapter.remove(toRemove);
		
		//Reajusta los id's de los grupo, por si agrega otro
		int newGroup = 1;
		for (Salvataje s : salvataje){
			s.setGrupoId(newGroup);
			newGroup++;
		}
		
		grupoIdActual = null;
		tvGrupoActual.setText("");
	}
	
	private void agregarAnimal(){
		for (Salvataje s : salvataje){
			if (grupoIdActual != null && s.getGrupoId().intValue() == grupoIdActual.intValue()){
				Ganado g = new Ganado();
				g.setDiio(diio);
				g.setEid(EID);
				s.getGanado().add(g);
				Toast.makeText(this, "Animal Insertado", Toast.LENGTH_LONG).show();
				clearScreen();
			}
		}
	}
	
	private void agregarGrupo(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Set up the input
		final EditText input = new EditText(this);
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);
		// Set up the buttons
		builder.setTitle("Titulo");
		builder.setMessage("Ingrese un título para el grupo");
		builder.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	dialog.cancel();
		    }
		});
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	if (input.getText().toString().length() == 0){
		    		ShowAlert.showAlert("Error", "Debe ingresar un título", Salvatajes.this);
		    		return;
		    	}
		    	String nombreGrupo = input.getText().toString();
		    	Salvataje s = new Salvataje();
		    	s.setGrupoId(salvataje.size() + 1);
		    	s.setNombreGrupo(nombreGrupo);
		    	salvataje.add(s);
		    	grupoIdActual = s.getGrupoId();
		    	tvGrupoActual.setText(nombreGrupo);
		    	updateStatus();
		    }
		});
	
		builder.show();
	}
	
	private void resetCalculadora(){
		Calculadora.ganadoId = 0;
		Calculadora.diio = 0;
		Calculadora.predio = 0;
		Calculadora.activa = "";
		Calculadora.sexo = "";
		Calculadora.tipoGanado = 0;
	}
	
	private void clearScreen(){
		diio = 0;
		EID = null;
		resetCalculadora();
	}
	
	protected  void onDestroy(){
		super.onStart();
		
		Calculadora.isSalvataje = false;
		resetCalculadora();
	}
	
	protected void onStart(){
		super.onStart();
		
		if (Login.user == 0){
			finish();
		}
		
		ConnectThread.setHandler(mHandler);

		checkDiioStatus(Calculadora.diio, null);
	}
	
	private void checkDiioStatus(int diio, String EID){	
		if (diio != 0 && (diio == this.diio)){
			return;
		}
		
		if (EID != null && (EID == this.EID)){
			return;
		}
		
		if (diio == 0 && EID == null){
			updateStatus();
			return;
		}
		
		if (EID != null){
			long eid = Long.valueOf(EID.trim());
			EID = Long.toString(eid);
		}
		
		for (Salvataje s : salvataje){
			if (grupoIdActual != null && s.getGrupoId().intValue() == grupoIdActual.intValue()){
				for (Ganado g : s.getGanado()){
					if ((diio != 0 && g.getDiio().intValue() == diio) || (EID != null && EID.equals(g.getEid()))){
						Toast.makeText(this, "Animal ya existe", Toast.LENGTH_LONG).show();
						return;
					}
				}
			}
		}
		
		this.EID = EID;
		this.diio = diio;
		
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
    			String EID = (String) msg.obj;
    			if (grupoIdActual != null){
    				checkDiioStatus(0, EID);
    			}
    			break;
    		case ConnectedThread.CONNECTION_INTERRUPTED:
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", Salvatajes.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };

}
