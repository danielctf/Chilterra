package cl.a2r.sip.wsservice;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;
import cl.a2r.sip.model.Instancia;

public class WSInstanciasCliente {

	   public static Integer insertaInstancia(Instancia instancia, Integer appId) throws AppException {

	        ParamServlet params = new ParamServlet();
	        params.add("servicio", "insertaProc" );
	        params.add("instancia", instancia );
	        params.add("appId", appId );

	        Object obj = ServiceWS.invocaWS("WSInstancias", params );
	        if ( obj != null && obj.getClass().getName().contains("AppException")) {
	            throw (AppException) obj;
	        } else if (obj == null){
	        	throw new AppException("Error de conexión a Internet", null);
	        } else {
	        	return (Integer) obj;
	        }
	   }
}
