package cl.a2r.sip.wsservice;

import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;

public class WSPartosCliente {

	   public static List traeCollares(Integer predioId) throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeCollares" );
	        params.add("predioId", predioId );
	
	        Object obj = ServiceWS.invocaWS("WSPartos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else {
	            return (List) obj;
	        }
	   }
	   
	   public static List traeTipoPartos() throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeTipoPartos" );
	
	        Object obj = ServiceWS.invocaWS("WSPartos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else {
	            return (List) obj;
	        }
	   }
	   
	   public static List traeSubTipoPartos() throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeSubTipoPartos" );
	
	        Object obj = ServiceWS.invocaWS("WSPartos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else {
	            return (List) obj;
	        }
	   }
	
}
