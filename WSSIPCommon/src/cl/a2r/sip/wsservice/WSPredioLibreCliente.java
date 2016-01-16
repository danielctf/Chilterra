package cl.a2r.sip.wsservice;

import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;

public class WSPredioLibreCliente {

    public static List traePredioLibre(Integer g_fundo_id) throws AppException {

        ParamServlet params = new ParamServlet();
        params.add("servicio", "traePredioLibre" );
        params.add("g_fundo_id", g_fundo_id);

        Object obj = ServiceWS.invocaWS("WSPredioLibre", params );
        if ( obj != null && obj.getClass().getName().contains("AppException")) {
            throw (AppException) obj;
        } else if (obj == null){
        	throw new AppException("Error de conexión a Internet", null);
        } else {
            return (List) obj;
        }

    }
	
    public static List traeAllDiio() throws AppException {

        ParamServlet params = new ParamServlet();
        params.add("servicio", "traeAllDiio" );

        Object obj = ServiceWS.invocaWS("WSPredioLibre", params );
        if ( obj != null && obj.getClass().getName().contains("AppException")) {
            throw (AppException) obj;
        } else if (obj == null){
        	throw new AppException("Error de conexión a Internet", null);
        } else {
            return (List) obj;
        }

    }
    
}
