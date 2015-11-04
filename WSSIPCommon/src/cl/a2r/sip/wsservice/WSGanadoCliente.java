package cl.a2r.sip.wsservice;

import java.math.BigInteger;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;

public class WSGanadoCliente {
	
	   public static List traeGanado(Integer diio) throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeGanado" );
	       params.add("diio", diio );

	       Object obj = ServiceWS.invocaWS("WSGanado", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else {
	           return (List) obj;
	       }

	   }
	   
	   public static List traeDIIO(String eid) throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "traeDIIO" );
	       params.add("eid", eid );

	       Object obj = ServiceWS.invocaWS("WSGanado", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else {
	           return (List) obj;
	       }

	   }
}
