package cl.a2r.custom;

import java.util.ArrayList;
import java.util.List;

import cl.a2r.animales.Aplicaciones;
import cl.a2r.animales.Areteos;
import cl.a2r.animales.BajaGanado;
import cl.a2r.animales.Login;
import cl.a2r.animales.Partos;
import cl.a2r.animales.PredioLibreLobby;
import cl.a2r.animales.Salvatajes;
import cl.a2r.animales.Traslados;
import cl.a2r.common.AppException;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.GanadoLogs;
import cl.a2r.sip.model.Salvataje;
import cl.a2r.sip.wsservice.WSAreteosCliente;
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
			hasLogAccess = false;
			break;
		case 2:
			launchApp = Areteos.class;
			hasLogAccess = false;
			break;
		case 4:
			launchApp = Partos.class;
			hasLogAccess = true;
			break;
		case 6:
			launchApp = Traslados.class;
			hasLogAccess = true;
			break;
		case 12:
			launchApp = Salvatajes.class;
			hasLogAccess = true;
			break;
		case 13:
			launchApp = PredioLibreLobby.class;
			hasLogAccess = false;
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
	
	@SuppressWarnings("unchecked")
	public static List getLogs(){
		List<GanadoLogs> list = null;
		switch (appId){
		case 1:
			try {
				list = WSBajasCliente.traeBajas(Login.user, Aplicaciones.predioWS.getId());
			} catch (AppException e1) {
				e1.printStackTrace();
			}
			break;
		case 2:
			try {
				if (Areteos.logStance == 2){
					//Logs Apariciones
					list = WSAreteosCliente.traeApariciones(Login.user, Aplicaciones.predioWS.getId());
				} else if (Areteos.logStance == 3){
					//Logs Cambio Diio
					list = WSAreteosCliente.traeCambioDiio(Login.user, Aplicaciones.predioWS.getId());
				}
			} catch (AppException e2) {
				e2.printStackTrace();
			}
			break;
		case 4:
			try {
				list = WSPartosCliente.traePartos(Login.user, Aplicaciones.predioWS.getId());
			} catch (AppException e) {
				e.printStackTrace();
			}
			break;
		case 12:
			list = new ArrayList<GanadoLogs>();
			if (Salvatajes.salvataje.size() == 0){
				return list;
			}
			for (Ganado g : Salvatajes.salvataje.get(Salvatajes.grupoIdActual - 1).getGanado()){
				GanadoLogs gl = new GanadoLogs();
				//gl.setGanadoId(g.getDiio());
				if (g.getEid() != null){
					gl.setEid(g.getEid());
				} else {
					gl.setEid(Integer.toString(g.getDiio()));
				}
				list.add(gl);
			}
			break;
		}
		return list;
	}
	
	public static void deleteLogs(List<String> deletedLogId){
		switch (appId){
		case 4:
			for (int i = 0; i < deletedLogId.size(); i++){
				GanadoLogs gl = new GanadoLogs();
				gl.setLogId(Integer.parseInt(deletedLogId.get(i)));
				gl.setUsuarioId(Login.user);
				try {
					WSPartosCliente.deshacerRegistroParto(gl);
				} catch (AppException e) {
					e.printStackTrace();
				}
			}
			break;
		case 12:
			List<Ganado> toRemove = new ArrayList<Ganado>();
			for (Salvataje s : Salvatajes.salvataje){
				for (Ganado g : s.getGanado()){
					for (String str : deletedLogId){
						if (str.equals(g.getEid())){
							toRemove.add(g);
						} else if (str.equals(g.getDiio().toString())){
							toRemove.add(g);
						}
					}
				}
			}
			for (Salvataje s : Salvatajes.salvataje){
				if (s.getGrupoId().intValue() == Salvatajes.grupoIdActual.intValue()){
					s.getGanado().removeAll(toRemove);
				}
			}
			break;
		}
	}
	
}
