package cl.a2r.sip.wsservice;

import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;
import cl.a2r.sip.model.Areteo;
import cl.a2r.sip.model.Parto;

public class WSAreteosCliente {

	   public static List traeRaza() throws AppException {

	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeRaza" );

	        Object obj = ServiceWS.invocaWS("WSAreteos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	            return (List) obj;
	        }
	   }
	   
	   public static List traeCollarAreteo(Integer fundoId) throws AppException {

	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeCollarAreteo" );
	        params.add("fundoId", fundoId);

	        Object obj = ServiceWS.invocaWS("WSAreteos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	            return (List) obj;
	        }
	   }
	   
	   public static List traeTipoGanado() throws AppException {

	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeTipoGanado" );

	        Object obj = ServiceWS.invocaWS("WSAreteos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	            return (List) obj;
	        }
	   }
	   
	   public static List traeAreteosEncontrados(Integer fundoId) throws AppException {

	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeAreteosEncontrados" );
	        params.add("fundoId", fundoId);

	        Object obj = ServiceWS.invocaWS("WSAreteos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	            return (List) obj;
	        }
	   }
	   
	   public static List traeAreteosFaltantes(Integer fundoId) throws AppException {

	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeAreteosFaltantes" );
	        params.add("fundoId", fundoId);

	        Object obj = ServiceWS.invocaWS("WSAreteos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	            return (List) obj;
	        }
	   }
	   
	    public static void liberaCollar(Integer collarId, Integer usuarioId) throws AppException {

	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "liberaCollar" );
	        params.add("collarId", collarId);
	        params.add("usuarioId", usuarioId);
	        
	        Object obj = ServiceWS.invocaWS("WSAreteos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        }
	    }
	    
	    public static void insertaAreteoAlta(Areteo alta) throws AppException {

	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "insertaAreteoAlta" );
	        params.add("alta", alta);
	        
	        Object obj = ServiceWS.invocaWS("WSAreteos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        }
	    }
	    
	    public static void insertaAreteoAparicion(Areteo aparicion) throws AppException {

	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "insertaAreteoAparicion" );
	        params.add("aparicion", aparicion);
	        
	        Object obj = ServiceWS.invocaWS("WSAreteos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        }
	    }
	    
	    public static void insertaAreteoCambioDiio(Areteo cambiodiio) throws AppException {

	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "insertaAreteoCambioDiio" );
	        params.add("cambiodiio", cambiodiio);
	        
	        Object obj = ServiceWS.invocaWS("WSAreteos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        }
	    }
	    
	   public static List traeApariciones(Integer usuarioId, Integer fundoId) throws AppException {

	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeApariciones");
	        params.add("usuarioId", usuarioId);
	        params.add("fundoId", fundoId);

	        Object obj = ServiceWS.invocaWS("WSAreteos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	            return (List) obj;
	        }
	   }	    
	
	   public static List traeCambioDiio(Integer usuarioId, Integer fundoId) throws AppException {

	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeCambioDiio");
	        params.add("usuarioId", usuarioId);
	        params.add("fundoId", fundoId);

	        Object obj = ServiceWS.invocaWS("WSAreteos", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	            return (List) obj;
	        }
	   }	
	   
}
