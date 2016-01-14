package cl.a2r.animales;

import java.util.List;

import cl.a2r.animales.R;
import cl.a2r.common.AppException;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ShowAlert;
import cl.a2r.sip.model.Areteo;
import cl.a2r.sip.model.Raza;
import cl.a2r.sip.model.TipoGanado;
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
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AreteosAparicion extends Fragment implements View.OnClickListener{

	public static Spinner spinnerTipoAnimal, spinnerEdad, spinnerRaza;
	private static ImageButton confirmarAparicion;
	
	private static final String[] edadTernera = {"", "1 Mes", "2 Meses", "3 Meses", "4 Meses", "5 Meses", "6 Meses", "7 Meses", "8 Meses", "9 Meses", "10 Meses", "11 Meses"};
	private static final String[] edadVaquilla = {"", "1 Año 1 Mes", "1 Año 2 Meses", "1 Año 3 Meses", "1 Año 4 Meses", "1 Año 5 Meses", "1 Año 6 Meses", "1 Año 7 Meses", "1 Año 8 Meses", "1 Año 9 Meses", "1 Año 10 Meses", "1 Año 11 Meses"};
	private static final String[] edadVaca = {"", "2 Años", "3 Años", "4 Años", "5 Años", "6 Años", "7 Años", "8 Años", "9 Años", "10 Años", "11 o más"};
	private static final String[] ini = {""};
	private String tipoAnimal = "";
	
	public static Areteo aparicionWS = new Areteo();
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.activity_areteo_aparicion, container, false);
    	
		aparicionWS.setUserId(Login.user);
		aparicionWS.setPredioId(Aplicaciones.predioWS.getId());
    	aparicionWS.setDiio(0);
    	aparicionWS.setEdadEnMeses(0);
    	aparicionWS.setRazaId(0);
    	
    	spinnerTipoAnimal = (Spinner)v.findViewById(R.id.spinnerTipoAnimal);
    	spinnerEdad = (Spinner)v.findViewById(R.id.spinnerEdad);
    	spinnerRaza = (Spinner)v.findViewById(R.id.spinnerRaza);
    	confirmarAparicion = (ImageButton)v.findViewById(R.id.confirmarAparicion);
    	confirmarAparicion.setOnClickListener(this);
    	
    	cargarListeners();
    	getTipoAnimalWS();
    	getRazaWS();
    	return v;
	}
	
	private void cargarListeners(){
		spinnerTipoAnimal.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String nombre = ((TipoGanado) arg0.getItemAtPosition(arg2)).getNombre();
				setSpinnerEdad(nombre);
				updateStatus();
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		spinnerEdad.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				setEdadEnMeses();
				updateStatus();
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		spinnerRaza.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				aparicionWS.setRazaId(((Raza) arg0.getItemAtPosition(arg2)).getId());
				updateStatus();
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getTipoAnimalWS(){
		List list = null;
		try {
			list = WSAreteosCliente.traeTipoGanado();
			TipoGanado tg = new TipoGanado();
			tg.setId(0);
			tg.setNombre("");
			list.add(0, tg);
			
			ArrayAdapter<TipoGanado> mAdapter = new ArrayAdapter<TipoGanado>(this.getActivity(), android.R.layout.simple_list_item_1, list);
			spinnerTipoAnimal.setAdapter(mAdapter);
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this.getActivity());
		}
		
	}
	
	private void setSpinnerEdad(String tipoGanado){
		String[] list = {""};
		switch (tipoGanado){
		case "":
			list = ini;
			break;
		case "Ternera":
			aparicionWS.setSexo("H");
			list = edadTernera;
			tipoAnimal = "Ternera";
			break;
		case "Ternero":
			aparicionWS.setSexo("M");
			list = edadTernera;
			tipoAnimal = "Ternera";
			break;
		case "Vaquilla":
			aparicionWS.setSexo("H");
			list = edadVaquilla;
			tipoAnimal = "Vaquilla";
			break;
		case "Torete":
			aparicionWS.setSexo("M");
			list = edadVaquilla;
			tipoAnimal = "Vaquilla";
			break;
		case "Vaca":
			aparicionWS.setSexo("H");
			list = edadVaca;
			tipoAnimal = "Vaca";
			break;
		case "Toro":
			aparicionWS.setSexo("M");
			list = edadVaca;
			tipoAnimal = "Vaca";
			break;
		case "Buey":
			aparicionWS.setSexo("I");
			list = edadVaca;
			tipoAnimal = "Vaca";
			break;
		case "Indefinido":
			aparicionWS.setSexo("I");
			break;
		}
		ArrayAdapter<String> mApdater = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, list);
		spinnerEdad.setAdapter(mApdater);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getRazaWS(){
		List list = null;
		try {
			list = WSAreteosCliente.traeRaza();
			Raza r = new Raza();
			r.setId(0);
			r.setNombre("");
			list.add(0, r);
			ArrayAdapter<Raza> mApdater = new ArrayAdapter<Raza>(this.getActivity(), android.R.layout.simple_list_item_1, list);
			spinnerRaza.setAdapter(mApdater);
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this.getActivity());
		}
	}
	
	private void setEdadEnMeses(){
		int edad = 0;
		
		switch (tipoAnimal){
		case "Ternera":
			edad = spinnerEdad.getSelectedItemPosition();
			break;
		case "Vaquilla":
			edad = 12 + spinnerEdad.getSelectedItemPosition();
			break;
		case "Vaca":
			edad = 12 + 12 * spinnerEdad.getSelectedItemPosition();
			break;
		}
		aparicionWS.setEdadEnMeses(edad);
	}

	public static void updateStatus(){
		if (aparicionWS.getDiio() != 0 &&
				spinnerTipoAnimal.getSelectedItemPosition() != 0 &&
				spinnerEdad.getSelectedItemPosition() != 0 &&
				aparicionWS.getRazaId() != 0){
			
			Areteos.goBack.setVisibility(View.INVISIBLE);
			Areteos.tvApp.setVisibility(View.INVISIBLE);
			Areteos.undo.setVisibility(View.VISIBLE);
			Areteos.deshacer.setVisibility(View.VISIBLE);
			Areteos.logs.setVisibility(View.INVISIBLE);
			confirmarAparicion.setVisibility(View.VISIBLE);
		}else if (aparicionWS.getDiio() == 0 &&
				spinnerTipoAnimal.getSelectedItemPosition() == 0 &&
				spinnerEdad.getSelectedItemPosition() == 0 &&
				aparicionWS.getRazaId() == 0){
			
			Areteos.goBack.setVisibility(View.VISIBLE);
			Areteos.tvApp.setVisibility(View.VISIBLE);
			Areteos.undo.setVisibility(View.INVISIBLE);
			Areteos.deshacer.setVisibility(View.INVISIBLE);
			Areteos.logs.setVisibility(View.VISIBLE);
			confirmarAparicion.setVisibility(View.INVISIBLE);
		} else {
			Areteos.goBack.setVisibility(View.INVISIBLE);
			Areteos.tvApp.setVisibility(View.INVISIBLE);
			Areteos.undo.setVisibility(View.VISIBLE);
			Areteos.deshacer.setVisibility(View.VISIBLE);
			Areteos.logs.setVisibility(View.INVISIBLE);
			confirmarAparicion.setVisibility(View.INVISIBLE);
		}
	}
	
    Handler hand = new Handler();
    Runnable run = new Runnable() { 
        public void run() { 
			try {
				WSAreteosCliente.insertaAreteoAparicion(aparicionWS);
				Toast.makeText(AreteosAparicion.this.getActivity(), "Registro guardado exitosamente", Toast.LENGTH_LONG).show();
				//AreteosAparicion.this.getActivity().finish();
				Calculadora.ganadoId = 0;
				Calculadora.diio = 0;
				Calculadora.predio = 0;
				Calculadora.activa = "";
				Calculadora.sexo = "";
				Areteos.diio = 0;
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.detach(AreteosAparicion.this).attach(AreteosAparicion.this).commit();
				getFragmentManager().executePendingTransactions();
				Areteos.updateStatus();
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), AreteosAparicion.this.getActivity());
				confirmarAparicion.setVisibility(View.VISIBLE);
			}
        }
    }; 
	
	public void onClick(View v) {
		if (isOnline() == false){
			return;
		}
		int id = v.getId();
		switch (id){
		case R.id.confirmarAparicion:
			confirmarAparicion.setVisibility(View.INVISIBLE);
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
