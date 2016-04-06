package cl.a2r.sip.wsservice;

import java.math.BigInteger;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;
import cl.a2r.sip.model.Traslado;

public class WSGanadoCliente {
	
	   public static List traeGanado(Integer diio) throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeGanado" );
	       params.add("diio", diio );

	       Object obj = ServiceWS.invocaWS("WSGanado", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }
	   }
	   
	   public static List traeGanadoBaston(String eid) throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeGanadoBaston" );
	       params.add("eid", eid );

	       Object obj = ServiceWS.invocaWS("WSGanado", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }
	   }
	   
	   public static Integer traeDiio(Integer diio) throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeDiio" );
	       params.add("diio", diio );

	       Object obj = ServiceWS.invocaWS("WSGanado", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (Integer) obj;
	       }
	   }
	   
	   public static Integer traeDiioBaston(String eid) throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeDiioBaston" );
	       params.add("eid", eid );

	       Object obj = ServiceWS.invocaWS("WSGanado", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (Integer) obj;
	       }
	   }
	   
	   public static void reajustaGanado(List<Traslado> trasList) throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "reajustaGanado" );
	       params.add("trasList", trasList );

	       Object obj = ServiceWS.invocaWS("WSGanado", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       }
	   }
	   
	   public static List traeGanadoBusqueda() throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeGanadoBusqueda" );

	       Object obj = ServiceWS.invocaWS("WSGanado", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }
	   }
}
