package cl.a2r.sip.wsservice;

import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;
import cl.a2r.sip.model.Auditoria;
import cl.a2r.sip.model.Ganado;

public class WSAuditoriaCliente {

	   public static List traeAuditoria(Integer fundoId) throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeAuditoria" );
	       params.add("fundoId", fundoId);

	       Object obj = ServiceWS.invocaWS("WSAuditoria", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }
	   }
	   
	   public static void insertaAuditoria(Auditoria auditoria, Integer usuarioId) throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "insertaAuditoria");
	       params.add("auditoria", auditoria);
	       params.add("usuarioId", usuarioId);
	       
	       Object obj = ServiceWS.invocaWS("WSAuditoria", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       }
	   }
	   
	   public static void cerrarAuditoria(Auditoria auditoria, Integer usuarioId) throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "cerrarAuditoria");
	       params.add("auditoria", auditoria);
	       params.add("usuarioId", usuarioId);
	       
	       Object obj = ServiceWS.invocaWS("WSAuditoria", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       }
	   }
	   
	   public static void borrarAuditoria(Auditoria auditoria, Integer usuarioId) throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "borrarAuditoria");
	       params.add("auditoria", auditoria);
	       params.add("usuarioId", usuarioId);
	       
	       Object obj = ServiceWS.invocaWS("WSAuditoria", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       }
	   }
	   
	   public static void insertaGanado(List<Auditoria> auList, Integer usuarioId) throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "insertaGanado");
	       params.add("auList", auList);
	       params.add("usuarioId", usuarioId);
	       
	       Object obj = ServiceWS.invocaWS("WSAuditoria", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       }
	   }
	   
	   public static Auditoria traeGanado(Integer instancia) throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeGanado" );
	       params.add("instancia", instancia);

	       Object obj = ServiceWS.invocaWS("WSAuditoria", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (Auditoria) obj;
	       }
	   }
	
}
