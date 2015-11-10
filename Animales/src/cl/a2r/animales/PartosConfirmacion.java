package cl.a2r.animales;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import cl.a2r.common.AppException;
import cl.a2r.custom.ShowAlert;
import cl.a2r.login.R;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.wsservice.WSPartosCliente;

public class PartosConfirmacion extends Fragment implements View.OnClickListener{

	public static ImageButton confirmarConfirmacion;
	public static int ganadoId;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.activity_partos_confirmacion, container, false);
    	
    	confirmarConfirmacion = (ImageButton)v.findViewById(R.id.confirmarConfirmacion);
    	confirmarConfirmacion.setOnClickListener(this);

    	updateStatus();
    	return v;
	}

	@SuppressWarnings("unchecked")
	private void updateStatus(){
		try {
			List<Ganado> list = WSPartosCliente.traeCandidatosEncontrados(Aplicaciones.predioWS.getId());
			Partos.tvEncontrados.setText(Integer.toString(list.size()));
			List<Ganado> list2 = WSPartosCliente.traeCandidatosFaltantes(Aplicaciones.predioWS.getId());
			Partos.tvFaltantes.setText(Integer.toString(list2.size()));
		} catch (AppException e) {
			ShowAlert.showAlert("Error", e.getMessage(), this.getActivity());
		}
		Partos.updateConfirmacion();
	}
	
	public void onClick(View v) {
		if (isOnline() == false){
			return;
		}
		int id = v.getId();
		switch (id){
		case R.id.confirmarConfirmacion:
			try {
				WSPartosCliente.confirmaParto(ganadoId);
				Toast.makeText(this.getActivity().getApplicationContext(), "Registro guardado exitosamente", Toast.LENGTH_LONG).show();
				this.getActivity().finish();
			} catch (AppException e) {
				ShowAlert.showAlert("Error", e.getMessage(), this.getActivity());
			}
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
