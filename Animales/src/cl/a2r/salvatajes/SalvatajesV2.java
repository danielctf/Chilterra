package cl.a2r.salvatajes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import cl.a2r.animales.Login;
import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.ConnectThread;
import cl.a2r.custom.ConnectedThread;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Salvataje;
import cl.ar2.sqlite.servicio.SalvatajesServicio;
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
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class SalvatajesV2 extends Activity implements View.OnClickListener, ListView.OnItemClickListener, ListView.OnItemLongClickListener{

	private ListView lvGrupos;
	private ImageButton btnAddGrupo, goBack, share;
	private TextView tvApp;
	private boolean[] checked;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_salvataje);
		
		cargarInterfaz();
		getGrupos();
	}
	
	private void cargarInterfaz(){
		goBack = (ImageButton)findViewById(R.id.goBack);
		goBack.setOnClickListener(this);
		btnAddGrupo = (ImageButton)findViewById(R.id.btnAddGrupo);
		btnAddGrupo.setOnClickListener(this);
		share = (ImageButton)findViewById(R.id.share);
		share.setOnClickListener(this);
		lvGrupos = (ListView)findViewById(R.id.lvGrupos);
		lvGrupos.setOnItemClickListener(this);
		lvGrupos.setOnItemLongClickListener(this);
		tvApp = (TextView)findViewById(R.id.app);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int id = arg0.getId();
		switch (id){
		case R.id.lvGrupos:
			Intent i = new Intent(this, SalvatajesDiio.class);
			Integer grupoId = ((Salvataje) arg0.getItemAtPosition(arg2)).getGrupoId();
			String nombreGrupo = ((Salvataje) arg0.getItemAtPosition(arg2)).getNombreGrupo();
			i.putExtra("grupoId", grupoId);
			i.putExtra("nombreGrupo", nombreGrupo);
			startActivity(i);
			break;
		}
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id){
		case R.id.goBack:
			finish();
			break;
		case R.id.btnAddGrupo:
			agregarGrupo();
			break;
		case R.id.share:
			enviarCorreo();
			break;
		}
	}
	
	private void enviarCorreo(){
		List<Salvataje> sList = null;
		try {
			sList = SalvatajesServicio.traeGrupos();
		} catch (AppException e1) {
			ShowAlert.showAlert("Error", e1.getMessage(), this);
		}
		String[] items = new String[sList.size()];
		checked = new boolean[sList.size()];
		int i = 0;
		for (Salvataje s : sList){
			items[i] = s.getNombreGrupo();
			checked[i] = true;
			i++;
		}
		ShowAlert.multipleChoice("Enviar Grupos", "Seleccione los grupos a enviar",
				items, checked, this, new DialogInterface.OnMultiChoiceClickListener() {
					
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						checked[which] = isChecked;
					}
				}, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface arg0, int arg1) {
						if (arg1 == -2){
							send();
						}
					}
				});
	}
	
	private void send(){
		List<Salvataje> sList = null;
		try {
			sList = SalvatajesServicio.traeGrupos();
		} catch (AppException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ArrayList<Uri> uris = new ArrayList<Uri>();
		for (int j = 0; j < checked.length; j++){
			if (checked[j]){
				File file   = null;
				File root   = Environment.getExternalStorageDirectory();
				if (root.canWrite()){
					File dir = new File (root.getAbsolutePath() + "/Bastoneo");
				    dir.mkdirs();
				    file   =   new File(dir, "Grupo " + sList.get(j).getNombreGrupo() + ".csv");
				    FileOutputStream out   =   null;
				    try {
				        out = new FileOutputStream(file);
				        } catch (FileNotFoundException e) {
				            e.printStackTrace();
				        }
				        try {
				        	byte[] endl = "\n".getBytes();
				        	byte[] coma = ";".getBytes();
				        	List<Ganado> ganList = SalvatajesServicio.traeDiios(sList.get(j).getGrupoId());
				        	for (Ganado g : ganList){
				        		if (g.getEid() != null){
				        			out.write(g.getEid().getBytes());
				        			out.write(coma);
				        			out.write(g.getObservacion().getBytes());
				        			out.write(endl);
				        		}
				        	}
				        	out.close();
				        } catch (IOException e) {
				            ShowAlert.showAlert("Error", "VOLCADO A FICHERO\n" + e.getMessage(), this);
				        } catch (AppException e) {
				        	ShowAlert.showAlert("Error", e.getMessage(), this);
				        }
				}
				Uri u = Uri.fromFile(file);
				uris.add(u);
			}
		}
		
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		final Intent ei = new Intent(Intent.ACTION_SEND_MULTIPLE);
		ei.setType("plain/text");
		ei.putExtra(Intent.EXTRA_SUBJECT, "Salvataje " + df.format(new Date()));
		ei.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
		startActivityForResult(Intent.createChooser(ei, "Compartir"), 12345);
	}
	
	private void getGrupos(){
		try {
			List<Salvataje> list = SalvatajesServicio.traeGrupos();
			ArrayAdapter<Salvataje> mAdapter = new ArrayAdapter<Salvataje>(this, android.R.layout.simple_list_item_1, list);
			lvGrupos.setAdapter(mAdapter);
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this);
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
		    public void onClick(DialogInterface dialog, int which) {
		    	dialog.cancel();
		    }
		});
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		    	if (input.getText().toString().equals("")){
		    		ShowAlert.showAlert("Error", "Debe ingresar un título", SalvatajesV2.this);
		    		return;
		    	}
		    	Salvataje s = new Salvataje();
		    	s.setNombreGrupo(input.getText().toString());
		    	s.setFecha(new Date());
		    	try {
					SalvatajesServicio.insertaGrupo(s);
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), SalvatajesV2.this);
				} finally {
					getGrupos();
				}
		    }
		});
	
		builder.show();
	}

	public boolean onItemLongClick(final AdapterView<?> arg0, View arg1, final int arg2,
			long arg3) {
		
		int id = arg0.getId();
		switch (id){
		case R.id.lvGrupos:
			ShowAlert.askYesNo("Borrar", "¿Está seguro de borrar el grupo seleccionado?", this, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if (which == -2){
						try {
							Integer grupoId = ((Salvataje) arg0.getItemAtPosition(arg2)).getGrupoId();
							SalvatajesServicio.deleteGrupo(grupoId);
							SalvatajesServicio.deleteGrupoDiio(grupoId);
						} catch (AppException e) {
							ShowAlert.showAlert("Error", e.getMessage(), SalvatajesV2.this);
						} finally {
							getGrupos();
						}
					}
				}
			});
			break;
		}
		
		return true;
	}
	
	protected void onStart(){
		super.onStart();
		
		if (Login.user == 0){
			finish();
		}
		
		ConnectThread.setHandler(mHandler);
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
    			ShowAlert.askReconnect("Error", "Se perdió la conexión con el bastón\n¿Intentar reconectar?", SalvatajesV2.this, (BluetoothDevice) msg.obj);
    			break;
    		case ConnectThread.RETRY_CONNECTION:
    			ConnectThread connectThread = new ConnectThread((BluetoothDevice) msg.obj, true);
    			connectThread.start();
    			break;
    		}
    	}
    };
	
}
