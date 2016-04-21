package cl.a2r.sip.wsservice;

import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;
import cl.a2r.sip.model.Pesaje;

public class WSPesajesCliente {
	
	   public static List traePesaje() throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "traePesaje" );

	       Object obj = ServiceWS.invocaWS("WSPesajes", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       } else {
	           return (List) obj;
	       }
	   }
	   
	   public static void insertaPesaje(List<Pesaje> list, Integer usuarioId) throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "insertaPesaje");
	       params.add("list", list);
	       params.add("usuarioId", usuarioId);
	       
	       Object obj = ServiceWS.invocaWS("WSPesajes", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       }
	   }
}
