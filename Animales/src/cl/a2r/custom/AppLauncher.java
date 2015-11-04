package cl.a2r.custom;

import cl.a2r.animales.BajaGanado;
import cl.a2r.animales.BajaGanadoEdicion;
import cl.a2r.animales.Partos;

@SuppressWarnings("rawtypes")
public class AppLauncher {

	private static Class launchApp;
	private static Class launchEditableApp;
	private static boolean hasLogAccess;
	
	public static void setAppClass(int codigoApp){
		switch (codigoApp){
		case 1:
			launchApp = BajaGanado.class;
			launchEditableApp = BajaGanadoEdicion.class;
			hasLogAccess = false;
			break;
		case 4:
			launchApp = Partos.class;
			hasLogAccess = true;
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
	
}
