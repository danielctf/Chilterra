package cl.a2r.sip.wsservice;

import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;
import cl.a2r.sip.model.Baja;
import cl.a2r.sip.model.Parto;

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
	   
	   public static List traePartoAnterior(Integer ganadoId) throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traePartoAnterior" );
	        params.add("ganadoId", ganadoId);
	        
	        Object obj = ServiceWS.invocaWS("WSPartos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else {
	            return (List) obj;
	        }
	   }
	   
	   public static List traePartos(Integer userId, Integer fundoId) throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traePartos" );
	        params.add("userId", userId);
	        params.add("fundoId", fundoId);
	        
	        Object obj = ServiceWS.invocaWS("WSPartos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else {
	            return (List) obj;
	        }
	   }
	   
	   public static List traeCandidatosEncontrados(Integer fundoId) throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeCandidatosEncontrados" );
	        params.add("fundoId", fundoId);
	        
	        Object obj = ServiceWS.invocaWS("WSPartos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else {
	            return (List) obj;
	        }
	   }
	   
	   public static List traeCandidatosFaltantes(Integer fundoId) throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeCandidatosFaltantes" );
	        params.add("fundoId", fundoId);
	        
	        Object obj = ServiceWS.invocaWS("WSPartos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else {
	            return (List) obj;
	        }
	   }
	   
	    public static void insertaParto(Parto parto) throws AppException {

	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "insertaParto" );
	        params.add("parto", parto);
	        
	        Object obj = ServiceWS.invocaWS("WSPartos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        }
	    }
	    
	    public static void confirmaParto(Integer ganadoId) throws AppException {

	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "confirmaParto" );
	        params.add("ganadoId", ganadoId);
	        
	        Object obj = ServiceWS.invocaWS("WSPartos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        }
	    }
	
}
