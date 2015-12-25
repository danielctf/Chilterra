package cl.a2r.sap.wsservice;

import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;
import cl.a2r.sap.model.Medicion;

public class WSMedicionCliente {

	   public static void insertaMedicion(Medicion med) throws AppException {

	       ParamServlet params = new ParamServlet();
	       params.add("servicio", "insertaMedicion" );
	       params.add("med", med );

	       Object obj = ServiceWS.invocaWS("WSMedicion", params );
	       if ( obj != null && obj.getClass().getName().contains("AppException")) {
	           throw (AppException) obj;
	       } else if (obj == null){
	    	   throw new AppException("Error de conexión a Internet", null);
	       }

	   }
	
}
