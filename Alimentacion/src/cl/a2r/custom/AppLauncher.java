package cl.a2r.custom;

import cl.a2r.alimentacion.Stock;

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
			launchApp = Stock.class;
			hasLogAccess = true;
			break;
		}
	}
	
	public static int getAppId(){
		return appId;
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
