/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.a2r.sip.wsservice;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;
import cl.a2r.sip.model.Baja;

import java.util.List;

/**
 *
 * @author Miguel Vega Brante
 */
public class WSBajasCliente {

    public static List traeMotivos() throws AppException {

        ParamServlet params = new ParamServlet();
        params.add("servicio", "traeMotivos" );

        Object obj = ServiceWS.invocaWS("WSBajas", params );
        if ( obj != null && obj.getClass().getName().contains("AppException")) {
            throw (AppException) obj;
        } else {
            return (List) obj;
        }

    }

    public static List traeCausas() throws AppException {

        ParamServlet params = new ParamServlet();
        params.add("servicio", "traeCausas" );

        Object obj = ServiceWS.invocaWS("WSBajas", params );
        if ( obj != null && obj.getClass().getName().contains("AppException")) {
            throw (AppException) obj;
        } else {
            return (List) obj;
        }

    }
    
    public static void insertaBaja(Baja baja) throws AppException {

        ParamServlet params = new ParamServlet();
        params.add("servicio", "insertaBaja" );
        params.add("baja", baja);
        
        Object obj = ServiceWS.invocaWS("WSBajas", params );
        if ( obj != null && obj.getClass().getName().contains("AppException")) {
            throw (AppException) obj;
        }
    }
    
    public static List traeBajas(Integer userId, Integer fundoId) throws AppException {

        ParamServlet params = new ParamServlet();
        params.add("servicio", "traeBajas" );
        params.add("userId", userId);
        params.add("fundoId", fundoId);
        
        Object obj = ServiceWS.invocaWS("WSBajas", params );
        if ( obj != null && obj.getClass().getName().contains("AppException")) {
            throw (AppException) obj;
        } else {
            return (List) obj;
        }
    }


}
