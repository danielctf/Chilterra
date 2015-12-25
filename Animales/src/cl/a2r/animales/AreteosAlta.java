package cl.a2r.animales;

import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ShowAlert;
import cl.a2r.login.R;
import cl.a2r.object.SexoObject;
import cl.a2r.sip.model.Areteo;
import cl.a2r.sip.model.CollarParto;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Raza;
import cl.a2r.sip.wsservice.WSAreteosCliente;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class AreteosAlta extends Fragment implements View.OnClickListener{
	
	public static Spinner spinnerCollar, spinnerSexo, spinnerRaza;
	private static RadioGroup rg;
	private static ImageButton confirmarAlta;
	private static TextView tvSexo, tvRaza;
	private static boolean isMuerto;
	private View v;
	
	public static Areteo altaWS = new Areteo();
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	v = inflater.inflate(R.layout.activity_areteo_alta, container, false);
    	
		altaWS.setUserId(Login.user);
		altaWS.setPredioId(Aplicaciones.predioWS.getId());
		altaWS.setDiio(0);
    	altaWS.setCollarId(0);
    	altaWS.setSexo("");
    	altaWS.setRazaId(0);
    	
    	spinnerCollar = (Spinner)v.findViewById(R.id.spinnerCollar);
    	spinnerSexo = (Spinner)v.findViewById(R.id.spinnerSexo);
    	spinnerRaza = (Spinner)v.findViewById(R.id.spinnerRaza);
    	confirmarAlta = (ImageButton)v.findViewById(R.id.confirmarAlta);
    	confirmarAlta.setOnClickListener(this);
    	tvSexo = (TextView)v.findViewById(R.id.tvSexo);
    	tvRaza = (TextView)v.findViewById(R.id.tvRaza);
    	rg = (RadioGroup)v.findViewById(R.id.radioGroup1);
    	isMuerto = false;
    	
    	cargarListeners();
    	getCollarWS();
    	getSexoWS();
    	getRazaWS();
    	getCandidatosWS();
    	return v;
	}
	
	private void cargarListeners(){
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener(){
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	            RadioButton rb = (RadioButton)v.findViewById(checkedId);
	            setEstadoAnimal(rb);
	        }
	    });
		
		spinnerCollar.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				altaWS.setCollarId(((CollarParto) arg0.getItemAtPosition(arg2)).getId());
				updateStatus();
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		spinnerSexo.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				altaWS.setSexo(((SexoObject) arg0.getItemAtPosition(arg2)).getId());
				updateStatus();
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		spinnerRaza.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				altaWS.setRazaId(((Raza) arg0.getItemAtPosition(arg2)).getId());
				updateStatus();
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private void getCandidatosWS(){
		try {
			List<Ganado> list = WSAreteosCliente.traeAreteosEncontrados(Aplicaciones.predioWS.getId());
			Areteos.tvEncontrados.setText(Integer.toString(list.size()));
			List<Ganado> list2 = WSAreteosCliente.traeAreteosFaltantes(Aplicaciones.predioWS.getId());
			Areteos.tvFaltantes.setText(Integer.toString(list2.size()));
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this.getActivity());
		}
	}
	
	private void setEstadoAnimal(View v){
		int id = v.getId();
		switch (id){
		case R.id.radioVivo:
			spinnerSexo.setVisibility(View.VISIBLE);
			spinnerRaza.setVisibility(View.VISIBLE);
			tvSexo.setVisibility(View.VISIBLE);
			tvRaza.setVisibility(View.VISIBLE);
			Areteos.textViewDiio.setVisibility(View.VISIBLE);
			Areteos.despliegaDiio.setVisibility(View.VISIBLE);
			Areteos.calculadora.setEnabled(true);
			isMuerto = false;
			break;
		case R.id.radioMuerto:
			spinnerSexo.setVisibility(View.INVISIBLE);
			spinnerRaza.setVisibility(View.INVISIBLE);
			tvSexo.setVisibility(View.INVISIBLE);
			tvRaza.setVisibility(View.INVISIBLE);
			Areteos.textViewDiio.setVisibility(View.INVISIBLE);
			Areteos.despliegaDiio.setVisibility(View.INVISIBLE);
			Areteos.calculadora.setEnabled(false);
			isMuerto = true;
			break;
		}
		updateStatus();
	}
	
    Handler hand = new Handler();
    Runnable run = new Runnable() { 
        public void run() { 
			if (isMuerto){
				try {
					WSAreteosCliente.liberaCollar(altaWS.getCollarId(), Login.user);
					Toast.makeText(AreteosAlta.this.getActivity(), "Registro guardado exitosamente", Toast.LENGTH_LONG).show();
					//AreteosAlta.this.getActivity().finish();
					Calculadora.ganadoId = 0;
					Calculadora.diio = 0;
					Calculadora.predio = 0;
					Calculadora.activa = "";
					Calculadora.sexo = "";
					Areteos.diio = 0;
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					ft.detach(AreteosAlta.this).attach(AreteosAlta.this).commit();
					getFragmentManager().executePendingTransactions();
					Areteos.updateStatus();
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), AreteosAlta.this.getActivity());
					confirmarAlta.setVisibility(View.VISIBLE);
				}
			} else {
				try {
					WSAreteosCliente.insertaAreteoAlta(altaWS);
					Toast.makeText(AreteosAlta.this.getActivity().getApplicationContext(), "Registro guardado exitosamente", Toast.LENGTH_LONG).show();
					//AreteosAlta.this.getActivity().finish();
					Calculadora.ganadoId = 0;
					Calculadora.diio = 0;
					Calculadora.predio = 0;
					Calculadora.activa = "";
					Calculadora.sexo = "";
					Areteos.diio = 0;
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					ft.detach(AreteosAlta.this).attach(AreteosAlta.this).commit();
					getFragmentManager().executePendingTransactions();
					Areteos.updateStatus();
				} catch (AppException e) {
					ShowAlert.showAlert("Error", e.getMessage(), AreteosAlta.this.getActivity());
					confirmarAlta.setVisibility(View.VISIBLE);
				}
			}
        }
    }; 
	
	public void onClick(View v) {
		if (isOnline() == false){
			return;
		}
		int id = v.getId();
		switch (id){
		case R.id.confirmarAlta:
			confirmarAlta.setVisibility(View.INVISIBLE);
			hand.postDelayed(run, 100);
			break;
		}
	}
	
	public static void updateStatus(){
		if ((altaWS.getDiio() != 0 || isMuerto) &&
				altaWS.getCollarId() != 0 &&
				(altaWS.getSexo() != "" || isMuerto) &&
				(altaWS.getRazaId() != 0 || isMuerto)){
			
			Areteos.goBack.setVisibility(View.INVISIBLE);
			Areteos.tvApp.setVisibility(View.INVISIBLE);
			Areteos.undo.setVisibility(View.VISIBLE);
			Areteos.deshacer.setVisibility(View.VISIBLE);
			Areteos.logs.setVisibility(View.VISIBLE);
			confirmarAlta.setVisibility(View.VISIBLE);
		} else if ((altaWS.getDiio() == 0 || isMuerto) &&
				altaWS.getCollarId() == 0 &&
				(altaWS.getSexo() == "" || isMuerto) &&
				(altaWS.getRazaId() == 0 || isMuerto)){
			
			Areteos.goBack.setVisibility(View.VISIBLE);
			Areteos.tvApp.setVisibility(View.VISIBLE);
			Areteos.undo.setVisibility(View.INVISIBLE);
			Areteos.deshacer.setVisibility(View.INVISIBLE);
			Areteos.logs.setVisibility(View.VISIBLE);
			confirmarAlta.setVisibility(View.INVISIBLE);
		}else{
			Areteos.goBack.setVisibility(View.INVISIBLE);
			Areteos.tvApp.setVisibility(View.INVISIBLE);
			Areteos.undo.setVisibility(View.VISIBLE);
			Areteos.deshacer.setVisibility(View.VISIBLE);
			Areteos.logs.setVisibility(View.VISIBLE);
			confirmarAlta.setVisibility(View.INVISIBLE);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void getCollarWS(){
		List<CollarParto> list = null;
		try {
			list = WSAreteosCliente.traeCollarAreteo(Aplicaciones.predioWS.getId());
			CollarParto c = new CollarParto();
			c.setId(0);
			c.setNombre("");
			list.add(0, c);
			
			ArrayAdapter<CollarParto> mAdapter = new ArrayAdapter<CollarParto>(this.getActivity(), android.R.layout.simple_list_item_1, list);
			spinnerCollar.setAdapter(mAdapter);
		} catch (AppException ex) {
			ShowAlert.showAlert("Error", ex.getMessage(), this.getActivity());
		}
	}
	
	private void getSexoWS(){
		SexoObject s = new SexoObject();
		s.setId("H");
		s.setSexo("Hembra");
		SexoObject s1 = new SexoObject();
		s1.setId("M");
		s1.setSexo("Macho");
		SexoObject s3 = new SexoObject();
		s3.setId("");
		s3.setSexo("");
		
		List<SexoObject> list = new ArrayList<SexoObject>();
		list.add(s3);
		list.add(s);
		list.add(s1);
		
		ArrayAdapter<SexoObject> mAdapter = new ArrayAdapter<SexoObject>(this.getActivity(), android.R.layout.simple_list_item_1, list);
		spinnerSexo.setAdapter(mAdapter);
	}
	
	@SuppressWarnings("unchecked")
	private void getRazaWS(){
		List<Raza> list = null;
		try {
			list = WSAreteosCliente.traeRaza();
			Raza r = new Raza();
			r.setId(0);
			r.setNombre("");
			list.add(0, r);
			
			ArrayAdapter<Raza> mAdapter = new ArrayAdapter<Raza>(this.getActivity(), android.R.layout.simple_list_item_1, list);
			spinnerRaza.setAdapter(mAdapter);
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this.getActivity());
		}
		
	}
	
	
	private boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if ((netInfo != null && netInfo.isConnectedOrConnecting()) == false){
	    	ShowAlert.showAlert("Error", "No hay conexión a Internet", this.getActivity());
	    }
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	}

}
