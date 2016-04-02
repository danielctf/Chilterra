package cl.a2r.sip.wsservice;

import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;
import cl.a2r.sip.model.Bang;
import cl.a2r.sip.model.VRB51;

public class WSRB51Cliente {

	public static List traeBang() throws AppException {

		   ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeBang" );

	       Object obj = ServiceWS.invocaWS("WSRB51", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }
	   }
	
	public static List traeCandidatos(Integer g_fundo_id) throws AppException {

		   ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeCandidatos" );
	       params.add("g_fundo_id", g_fundo_id);

	       Object obj = ServiceWS.invocaWS("WSRB51", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }
	   }
	
	public static List traeGanadoRB51() throws AppException {

		   ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeGanadoRB51" );

	       Object obj = ServiceWS.invocaWS("WSRB51", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }
	   }
	
	public static Integer traeNumeroVacuna() throws AppException {

		   ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeNumeroVacuna" );

	       Object obj = ServiceWS.invocaWS("WSRB51", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (Integer) obj;
	       }
	   }
	
	public static List traeMedicamentos(Integer appId) throws AppException {

		   ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeMedicamentos" );
	       params.add("appId", appId);

	       Object obj = ServiceWS.invocaWS("WSRB51", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }
	   }
	
	public static void deleteBang(List<Bang> bangList, Integer usuarioId) throws AppException {

		   ParamServlet params = new ParamServlet();
	       params.add("servicio", "deleteBang" );
	       params.add("bangList", bangList);
	       params.add("usuarioId", usuarioId);

	       Object obj = ServiceWS.invocaWS("WSRB51", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       }
	   }
	
	public static void insertaRB51(List<VRB51> rbList, Integer usuarioId) throws AppException {

		   ParamServlet params = new ParamServlet();
	       params.add("servicio", "insertaRB51" );
	       params.add("rbList", rbList);
	       params.add("usuarioId", usuarioId);

	       Object obj = ServiceWS.invocaWS("WSRB51", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       }
	   }
	
	public static List traeGanadoRB51Anterior() throws AppException {

		   ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeGanadoRB51Anterior" );

	       Object obj = ServiceWS.invocaWS("WSRB51", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }
	   }
	
}
