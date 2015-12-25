package cl.a2r.animales;

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
import android.widget.ImageButton;
import android.widget.Toast;
import cl.a2r.common.AppException;
import cl.a2r.custom.Calculadora;
import cl.a2r.custom.ShowAlert;
import cl.a2r.login.R;
import cl.a2r.sip.model.Areteo;
import cl.a2r.sip.wsservice.WSAreteosCliente;

public class AreteosCambioDiio extends Fragment implements View.OnClickListener{

	private static ImageButton confirmaCambioDiio;
	
	public static Areteo cambioDiioWS = new Areteo();
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.activity_areteo_cambiodiio, container, false);
    	
    	confirmaCambioDiio = (ImageButton)v.findViewById(R.id.confirmaCambioDiio);
    	confirmaCambioDiio.setOnClickListener(this);
    	
    	cambioDiioWS.setUserId(Login.user);
    	cambioDiioWS.setGanadoId(0);
		cambioDiioWS.setDiio(0);
		cambioDiioWS.setDiioAnterior(0);
    	
    	return v;
	}
	
	public static void updateStatus(){
		if (cambioDiioWS.getGanadoId() != 0 &&
				cambioDiioWS.getDiio() != 0 &&
				cambioDiioWS.getDiioAnterior() != 0) {
			
			Areteos.textViewDiioAnterior.setText("");
			Areteos.despliegaDiioAnterior.setText(Integer.toString(cambioDiioWS.getDiioAnterior()));
			Areteos.goBack.setVisibility(View.INVISIBLE);
			Areteos.tvApp.setVisibility(View.INVISIBLE);
			Areteos.undo.setVisibility(View.VISIBLE);
			Areteos.deshacer.setVisibility(View.VISIBLE);
			Areteos.logs.setVisibility(View.INVISIBLE);
			confirmaCambioDiio.setVisibility(View.VISIBLE);
			
		} else if (cambioDiioWS.getGanadoId() == 0 &&
				cambioDiioWS.getDiioAnterior() == 0) {
			
			Areteos.textViewDiioAnterior.setText("DIIO ANTERIOR:");
			Areteos.despliegaDiioAnterior.setText("");
			Areteos.goBack.setVisibility(View.VISIBLE);
			Areteos.tvApp.setVisibility(View.VISIBLE);
			Areteos.undo.setVisibility(View.INVISIBLE);
			Areteos.deshacer.setVisibility(View.INVISIBLE);
			Areteos.logs.setVisibility(View.VISIBLE);
			confirmaCambioDiio.setVisibility(View.INVISIBLE);
			
		} else if (cambioDiioWS.getGanadoId() != 0 &&
				cambioDiioWS.getDiioAnterior() != 0) {
			
			Areteos.textViewDiioAnterior.setText("");
			Areteos.despliegaDiioAnterior.setText(Integer.toString(cambioDiioWS.getDiioAnterior()));
			Areteos.goBack.setVisibility(View.INVISIBLE);
			Areteos.tvApp.setVisibility(View.INVISIBLE);
			Areteos.undo.setVisibility(View.VISIBLE);
			Areteos.deshacer.setVisibility(View.VISIBLE);
			Areteos.logs.setVisibility(View.INVISIBLE);
			confirmaCambioDiio.setVisibility(View.INVISIBLE);
		}
		
	}

    Handler hand = new Handler();
    Runnable run = new Runnable() {
        public void run() { 
			try {
				WSAreteosCliente.insertaAreteoCambioDiio(cambioDiioWS);
				Toast.makeText(AreteosCambioDiio.this.getActivity(), "Registro guardado exitosamente", Toast.LENGTH_LONG).show();
				Calculadora.ganadoId = 0;
				Calculadora.diio = 0;
				Calculadora.predio = 0;
				Calculadora.activa = "";
				Calculadora.sexo = "";
				Areteos.diio = 0;
				Areteos.diioAnterior = 0;
				Areteos.ganadoId = 0;
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.detach(AreteosCambioDiio.this).attach(AreteosCambioDiio.this).commit();
				getFragmentManager().executePendingTransactions();
				Areteos.updateStatus();
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), AreteosCambioDiio.this.getActivity());
				confirmaCambioDiio.setVisibility(View.VISIBLE);
			}
        }
    }; 
	
	public void onClick(View v) {
		if (isOnline() == false){
			return;
		}
		int id = v.getId();
		switch (id){
		case R.id.confirmaCambioDiio:
			confirmaCambioDiio.setVisibility(View.INVISIBLE);
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
