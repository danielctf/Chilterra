package cl.a2r.sap.wsservice;

import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;
import cl.a2r.sap.model.Calificacion;
import cl.a2r.sap.model.Medicion;

public class WSMedicionCliente {

	   public static void insertaMedicion(List<Medicion> medList, String correo) throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "insertaMedicion" );
	       params.add("medList", medList );
	       params.add("correo", correo);

	       Object obj = ServiceWS.invocaWS("WSMedicion", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexi�n a Internet", null);
	       }

	   }
	   
	   public static List traeStock() throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeStock" );

	       Object obj = ServiceWS.invocaWS("WSMedicion", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexi�n a Internet", null);
	       } else {
	    	   return (List) obj;
	       }

	   }
	   
	   public static void insertaCalificacion(List<Calificacion> calList, String correo) throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "insertaCalificacion" );
	       params.add("calList", calList);
	       params.add("correo", correo);

	       Object obj = ServiceWS.invocaWS("WSMedicion", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexi�n a Internet", null);
	       }

	   }
	   
	   public static List traeCalificacion() throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeCalificacion" );

	       Object obj = ServiceWS.invocaWS("WSMedicion", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexi�n a Internet", null);
	       } else {
	    	   return (List) obj;
	       }

	   }
	
}