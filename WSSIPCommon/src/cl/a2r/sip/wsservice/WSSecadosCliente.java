package cl.a2r.sip.wsservice;

import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Secado;

public class WSSecadosCliente {
	
	public static List traeMedicamentos(Integer appId) throws AppException {

		   ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeMedicamentos" );
	       params.add("appId", appId );

	       Object obj = ServiceWS.invocaWS("WSSecados", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }
	   }
	
	public static List traeEstadosLeche() throws AppException {

		   ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeEstadosLeche" );

	       Object obj = ServiceWS.invocaWS("WSSecados", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }
	}
	
	public static void insertaEstadoLeche(List<Secado> secList, Integer usuarioId) throws AppException {

		   ParamServlet params = new ParamServlet();
		   params.add("servicio", "insertaEstadoLeche" );
		   params.add("ganList", secList );
		   params.add("usuarioId", usuarioId );

	       Object obj = ServiceWS.invocaWS("WSSecados", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       }
	}
	
	public static List traeAllDiio() throws AppException {

		   ParamServlet params = new ParamServlet();
		   params.add("servicio", "traeAllDiio" );

	       Object obj = ServiceWS.invocaWS("WSSecados", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }
	}
	
}
