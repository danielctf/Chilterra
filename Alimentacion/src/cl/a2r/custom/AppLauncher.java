package cl.a2r.custom;

import java.util.ArrayList;
import java.util.List;

import cl.a2r.alimentacion.Aplicaciones;
import cl.a2r.alimentacion.Login;
import cl.a2r.alimentacion.MedicionEntrada;
import cl.a2r.alimentacion.Mediciones;
import cl.a2r.common.AppException;
import cl.a2r.sap.model.Medicion;
import cl.ar2.sqlite.cobertura.MedicionServicio;
import cl.ar2.sqlite.cobertura.RegistroMedicion;

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
			launchApp = Mediciones.class;
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
	
	public static List getLogs(){
		List<RegistroMedicion> list = new ArrayList<RegistroMedicion>();
		switch (appId){
		case 1:
			try {
				List<RegistroMedicion> allItems = MedicionServicio.traeMediciones();
				for (RegistroMedicion rm : allItems){
					if (rm.getMedicion().getTipoMuestraId().intValue() == Mediciones.tipoMuestraActual &&
							Aplicaciones.predioWS.getId().intValue() == rm.getMedicion().getFundoId().intValue()){
						list.add(rm);
					}
				}
			} catch (AppException e) {
				e.printStackTrace();
			}
			break;
		}
		return list;
	}
	
	public static void deleteLogs(List<RegistroMedicion> deletedLogId){
		switch (appId){
		case 1:
			try {
				List<RegistroMedicion> list = MedicionServicio.traeMediciones();
				for (RegistroMedicion rm : deletedLogId){
					for (RegistroMedicion rmList : list){
						if (rm.getId().intValue() == rmList.getId().intValue()){
							MedicionServicio.deleteMedicion(rmList.getId().intValue());
						}
					}
				}
			} catch (AppException e) {
				e.printStackTrace();
			}
			break;
		}
	}
	
}
