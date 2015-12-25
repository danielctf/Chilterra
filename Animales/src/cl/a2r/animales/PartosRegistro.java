package cl.a2r.animales;

import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ShowAlert;
import cl.a2r.login.R;
import cl.a2r.object.SexoObject;
import cl.a2r.sip.model.CollarParto;
import cl.a2r.sip.model.Parto;
import cl.a2r.sip.model.TipoParto;
import cl.a2r.sip.wsservice.WSPartosCliente;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class PartosRegistro extends Fragment implements View.OnClickListener{

	private RadioGroup rg;
	public static Spinner spinnerTipoParto, spinnerSexo, spinnerCollar, spinnerSubTipoParto;
	private View v;
	public static ImageButton confirmarRegistro;
	public static TextView tvSexo, tvCollar, tvSubTipoParto;
	public static boolean isMuerto, isNotPartoNatural;
	
	public static Parto partoWS = new Parto();
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	v = inflater.inflate(R.layout.activity_partos_registro, container, false);
    	
		rg = (RadioGroup)v.findViewById(R.id.radioGroup1);
		spinnerTipoParto = (Spinner)v.findViewById(R.id.spinnerTipoParto);
		spinnerSubTipoParto = (Spinner)v.findViewById(R.id.spinnerSubTipoParto);
		spinnerSexo = (Spinner)v.findViewById(R.id.spinnerEdad);
		spinnerCollar = (Spinner)v.findViewById(R.id.spinnerTipoAnimal);
		confirmarRegistro = (ImageButton)v.findViewById(R.id.confirmarRegistro);
		confirmarRegistro.setOnClickListener(this);
		tvSexo = (TextView)v.findViewById(R.id.textViewSexo);
		tvCollar = (TextView)v.findViewById(R.id.textViewCollar);
		tvSubTipoParto = (TextView)v.findViewById(R.id.textViewSubTipo);
		
		partoWS.setUserId(Login.user);
		partoWS.setPredioId(Aplicaciones.predioWS.getId());
		partoWS.setGanadoId(0);
		partoWS.setTipoPartoId(0);
		partoWS.setSubTipoParto(0);
		partoWS.setEstado("V");
		partoWS.setSexo("");
		partoWS.setCollarId(0);
		
		isMuerto = false;
		isNotPartoNatural = true;
		
    	cargarListeners();
		getTipoPartosWS();
		getSubTipoPartoWS();
		getSexoWS();
		getCollaresWS();
    	
    	return v;
	}
	
	@SuppressWarnings("unchecked")
	private void getTipoPartosWS(){
		
		List<TipoParto> list = null;
		try {
			list = WSPartosCliente.traeTipoPartos();
			TipoParto t = new TipoParto();
			t.setId(0);
			t.setCodigo("");
			t.setNombre("");
			list.add(0, t);
			
			ArrayAdapter<TipoParto> mAdapter = new ArrayAdapter<TipoParto>(this.getActivity(), android.R.layout.simple_list_item_1, list);
			spinnerTipoParto.setAdapter(mAdapter);
		} catch (AppException ex) {
			ShowAlert.showAlert("Error", ex.getMessage(), this.getActivity());
		}
	
	}
	
	@SuppressWarnings("unchecked")
	private void getSubTipoPartoWS(){
		List<TipoParto> list = null;
		try {
			list = WSPartosCliente.traeSubTipoPartos();
			TipoParto t = new TipoParto();
			t.setId(0);
			t.setCodigo("");
			t.setNombre("");
			list.add(0, t);
			
			ArrayAdapter<TipoParto> mAdapter = new ArrayAdapter<TipoParto>(this.getActivity(), android.R.layout.simple_list_item_1, list);
			spinnerSubTipoParto.setAdapter(mAdapter);
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
	private void getCollaresWS(){
		List<CollarParto> list = null;
		try {
			list = WSPartosCliente.traeCollares(Aplicaciones.predioWS.getId());
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
	
	private void setEstadoAnimal(View v){
		int id = v.getId();
		switch (id){
		case R.id.radioVivo:
			tvSexo.setVisibility(View.VISIBLE);
			tvCollar.setVisibility(View.VISIBLE);
			spinnerSexo.setVisibility(View.VISIBLE);
			spinnerCollar.setVisibility(View.VISIBLE);
			partoWS.setEstado("V");
			isMuerto = false;
			updateStatus();
			break;
		case R.id.radioMuerto:
			tvSexo.setVisibility(View.INVISIBLE);
			tvCollar.setVisibility(View.INVISIBLE);
			spinnerSexo.setVisibility(View.INVISIBLE);
			spinnerCollar.setVisibility(View.INVISIBLE);
			partoWS.setEstado("M");
			isMuerto = true;
			updateStatus();
			break;
		}
	}
	
	private void updateStatus(){
		if (isNotPartoNatural){
			tvSubTipoParto.setVisibility(View.INVISIBLE);
			spinnerSubTipoParto.setVisibility(View.INVISIBLE);
		}else{
			tvSubTipoParto.setVisibility(View.VISIBLE);
			spinnerSubTipoParto.setVisibility(View.VISIBLE);
		}
		Partos.updateRegistro();
	}
	
	
	private void cargarListeners(){
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener(){
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	            RadioButton rb = (RadioButton)v.findViewById(checkedId);
	            setEstadoAnimal(rb);
	        }
	    });
		
		spinnerTipoParto.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				partoWS.setTipoPartoId(((TipoParto) arg0.getSelectedItem()).getId());
				if (((TipoParto) arg0.getSelectedItem()).getNombre().equals("Natural")){
					isNotPartoNatural = false;
				}else{
					isNotPartoNatural = true;
				}
				updateStatus();
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
		
		spinnerSubTipoParto.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				partoWS.setSubTipoParto(((TipoParto) arg0.getSelectedItem()).getId());
				updateStatus();
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
		
		spinnerSexo.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				partoWS.setSexo(((SexoObject) arg0.getItemAtPosition(arg2)).getId());
				updateStatus();
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
		
		spinnerCollar.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				partoWS.setCollarId(((CollarParto) arg0.getItemAtPosition(arg2)).getId());
				updateStatus();
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
	}
	
    Handler hand = new Handler();
    Runnable run = new Runnable() { 
        public void run() { 
			try {
				WSPartosCliente.insertaParto(partoWS);
				Toast.makeText(PartosRegistro.this.getActivity().getApplicationContext(), "Registro guardado exitosamente", Toast.LENGTH_LONG).show();
				
				Calculadora.ganadoId = 0;
				Calculadora.diio = 0;
				Calculadora.predio = 0;
				Calculadora.activa = "";
				Calculadora.sexo = "";
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.detach(PartosRegistro.this).attach(PartosRegistro.this).commit();
				getFragmentManager().executePendingTransactions();
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), PartosRegistro.this.getActivity());
				confirmarRegistro.setVisibility(View.VISIBLE);
			}
        }
    }; 

	public void onClick(View v) {
		if (isOnline() == false){
			return;
		}
		int id = v.getId();
		switch (id){
		case R.id.confirmarRegistro:
			if (isNotPartoNatural && isMuerto){
				partoWS.setSubTipoParto(null);
				partoWS.setSexo(null);
				partoWS.setCollarId(null);
			} else {
				if (isNotPartoNatural && isMuerto == false){
					partoWS.setSubTipoParto(null);
				}else{
					if (isNotPartoNatural == false && isMuerto){
						partoWS.setSexo(null);
						partoWS.setCollarId(null);
					}
				}
			}
			
			confirmarRegistro.setVisibility(View.INVISIBLE);
			hand.postDelayed(run, 100);
			break;
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
