/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.a2r.sip.wsservice;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;
import java.util.List;

/**
 *
 * @author Miguel Vega Brante
 */
public class WSAutorizacionCliente {

   public static Integer traeIdUsaurio(String usuario) throws AppException {

        ParamServlet params = new ParamServlet();
        params.add("servicio", "traeIdUsaurio" );
        params.add("usuario", usuario );

        Object obj = ServiceWS.invocaWS("WSAutorizacion", params );
        if ( obj != null && obj.getClass().getName().contains("AppException")) {
            throw (AppException) obj;
        } else {
            return (Integer) obj;
        }

   }

   public static List traeAplicaciones(Integer idUsuario) throws AppException {

       ParamServlet params = new ParamServlet();
       params.add("servicio", "traeAplicaciones" );
       params.add("idUsuario", idUsuario );

       Object obj = ServiceWS.invocaWS("WSAutorizacion", params );
       if ( obj != null && obj.getClass().getName().contains("AppException")) {
           throw (AppException) obj;
       } else {
           return (List) obj;
       }

   }
   
   public static List traePredios() throws AppException {

       ParamServlet params = new ParamServlet();
       params.add("servicio", "traePredios");

       Object obj = ServiceWS.invocaWS("WSAutorizacion", params );
       if ( obj != null && obj.getClass().getName().contains("AppException")) {
           throw (AppException) obj;
       } else {
           return (List) obj;
       }

   }

//   public static List autentica(Integer idApp, String usuario, String password) throws AppException {
//        List list = null;
//
//        ParamServlet params = new ParamServlet();
//        params.add("servicio", "autentica" );
//        params.add("usuario", usuario );
//        params.add("password", password );
//
//        Object obj = ServiceWS.invocaWS("WSAutorizacion", params );
//        if ( obj != null && obj.getClass().getName().contains("AppException")) {
//            throw (AppException) obj;
//        } else {
//            return (List) obj;
//        }
//
//    }


}
