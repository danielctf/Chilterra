package cl.a2r.custom;

import java.util.List;

import cl.a2r.animales.Aplicaciones;
import cl.a2r.animales.BajaGanado;
import cl.a2r.animales.BajaGanadoEdicion;
import cl.a2r.animales.Login;
import cl.a2r.animales.Partos;
import cl.a2r.common.AppException;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.wsservice.WSBajasCliente;
import cl.a2r.sip.wsservice.WSPartosCliente;

@SuppressWarnings("rawtypes")
public class AppLauncher {

	private static Class launchApp;
	private static Class launchEditableApp;
	private static boolean hasLogAccess;
	private static int appId;
	
	public static void setAppClass(int codigoApp){
		appId = codigoApp;
		switch (codigoApp){
		case 1:
			launchApp = BajaGanado.class;
			launchEditableApp = BajaGanadoEdicion.class;
			hasLogAccess = false;
			break;
		case 4:
			launchApp = Partos.class;
			hasLogAccess = false;
		}
	}
	
	public static Class getAppClass(){
		return launchApp;
	}
	
	public static Class getEditableAppClass(){
		return launchEditableApp;
	}

	public static boolean getHasLogAccess() {
		return hasLogAccess;
	}
	
	public static List getLogs(){
		List<Ganado> list = null;
		switch (appId){
		case 1:
			try {
				list = WSBajasCliente.traeBajas(Login.user, Aplicaciones.predioWS.getId());
			} catch (AppException e1) {
				e1.printStackTrace();
			}
			break;
		case 4:
			try {
				list = WSPartosCliente.traePartos(Login.user, Aplicaciones.predioWS.getId());
			} catch (AppException e) {
				e.printStackTrace();
			}
			break;
		}
		return list;
	}
	
}
