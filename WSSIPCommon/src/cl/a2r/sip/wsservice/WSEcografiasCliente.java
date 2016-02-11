package cl.a2r.sip.wsservice;

import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;
import cl.a2r.sip.model.Ecografia;

public class WSEcografiasCliente {

	   public static List traeEcografistas() throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeEcografistas" );

	       Object obj = ServiceWS.invocaWS("WSEcografias", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }

	   }
	   
	   public static List traeEcografiaEstado() throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeEcografiaEstado" );

	       Object obj = ServiceWS.invocaWS("WSEcografias", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }

	   }
	   
	   public static List traeEcografiaProblema() throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeEcografiaProblema" );

	       Object obj = ServiceWS.invocaWS("WSEcografias", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }

	   }
	   
	   public static List traeEcografiaNota() throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeEcografiaNota" );

	       Object obj = ServiceWS.invocaWS("WSEcografias", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }

	   }
	   
	   public static List traeInseminaciones() throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeInseminaciones" );

	       Object obj = ServiceWS.invocaWS("WSEcografias", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }

	   }
	   
	   public static List traeEcografias() throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeEcografias" );

	       Object obj = ServiceWS.invocaWS("WSEcografias", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }

	   }
	   
	   public static void insertaEcografia(List<Ecografia> ecoList, Integer usuarioId) throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "insertaEcografia" );
	       params.add("ecoList", ecoList);
	       params.add("usuarioId", usuarioId);

	       Object obj = ServiceWS.invocaWS("WSEcografias", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } 

	   }
	
}
