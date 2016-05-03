package cl.a2r.sip.wsservice;

import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;
import cl.a2r.sip.model.DctoAdem;
import cl.a2r.sip.model.FMA;
import cl.a2r.sip.model.Instancia;
import cl.a2r.sip.model.Traslado;

public class WSTrasladosCliente {

	   public static List traeTransportistas() throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeTransportistas" );
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	            return (List) obj;
	        }
	   }
	   
	   public static List traeChofer(Integer transportistaId) throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeChofer" );
	        params.add("transportistaId", transportistaId );
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	            return (List) obj;
	        }
	   }
	   
	   public static List traeCamion(Integer transportistaId) throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeCamion" );
	        params.add("transportistaId", transportistaId );
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	            return (List) obj;
	        }
	   }
	   
	   public static List traeAcoplado(Integer transportistaId) throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeAcoplado" );
	        params.add("transportistaId", transportistaId );
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	            return (List) obj;
	        }
	   }
	   
	   public static List traeArrieros() throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeArrieros" );
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	            return (List) obj;
	        }
	   }
	   
	   public static DctoAdem insertaMovimiento(Traslado traslado) throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "insertaMovimiento" );
	        params.add("traslado", traslado );
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	        	return (DctoAdem) obj;
	        }
	   }
	   
	   public static void generaXMLTraslado(FMA fma) throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "generaXMLTraslado" );
	        params.add("fma", fma);
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        }
	   }
	   
	   public static List traeMovimientosEP() throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeMovimientosEP" );
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	        	return (List) obj;
	        }
	   }
	   
	   public static Traslado traeMovimiento(Integer nro_documento) throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeMovimiento");
	        params.add("nro_documento", nro_documento);
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	        	return (Traslado) obj;
	        }
	   }
	   
	   public static void insertaMovtoConfirm(Traslado traslado) throws AppException {
		   
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "insertaMovtoConfirm");
	        params.add("traslado", traslado);
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        }
	   }
	   
	   public static void insertaMovtoReubicacion(Traslado traslado) throws AppException {
		   
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "insertaMovtoReubicacion");
	        params.add("traslado", traslado);
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        }
	   }
	   
	   public static void reubicaGanado(Traslado traslado) throws AppException {
		   
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "reubicaGanado");
	        params.add("traslado", traslado);
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        }
	   }
	
	    //--------------------- TRASLADOS V2 ----------------------------
	   
	   public static List traeTraslados(Integer fundoId) throws AppException {
		   
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeTraslados");
	        params.add("fundoId", fundoId);
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	        	return (List) obj;
	        }
	   }
	   
	   public static void borrarTraslado(Instancia instancia) throws AppException {
		   
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "borrarTraslado");
	        params.add("instancia", instancia);
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        }
	   }
	   
	   public static List traeChofer() throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeChoferV2" );
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	            return (List) obj;
	        }
	   }
	   
	   public static List traeCamion() throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeCamionV2" );
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	            return (List) obj;
	        }
	   }
	   
	   public static List traeAcoplado() throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeAcopladoV2" );
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	            return (List) obj;
	        }
	   }
	   
	   public static void insertaTraslado(Instancia superInstancia) throws AppException {
			
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "insertaTraslado" );
	        params.add("superInstancia", superInstancia );
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        }
	   }
	   
	   public static List traeTraslado(Integer superInstanciaId) throws AppException {
		   
	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "traeTraslado");
	        params.add("superInstanciaId", superInstanciaId);
	
	        Object obj = ServiceWS.invocaWS("WSTraslados", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	        	return (List) obj;
	        }
	   }
	   
}
