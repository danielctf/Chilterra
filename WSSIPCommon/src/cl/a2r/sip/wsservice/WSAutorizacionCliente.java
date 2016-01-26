/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.a2r.sip.wsservice;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;
import cl.a2r.sip.model.Sesion;

import java.util.List;

/**
 *
 * @author Miguel Vega Brante
 */
public class WSAutorizacionCliente {

   public static List traeAplicaciones(Integer idUsuario) throws AppException {

       ParamServlet params = new ParamServlet();
       params.add("servicio", "traeAplicaciones" );
       params.add("idUsuario", idUsuario );

       Object obj = ServiceWS.invocaWS("WSAutorizacion", params );
       if ( obj != null && obj.getClass().getName().contains("AppException")) {
           throw (AppException) obj;
       } else if (obj == null){
    	   throw new AppException("Error de conexión a Internet", null);
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
       } else if (obj == null){
    	   throw new AppException("Error de conexión a Internet", null);
       } else {
           return (List) obj;
       }

   }
   
   public static Integer traeUsuario(String usuario) throws AppException {

       ParamServlet params = new ParamServlet();
       params.add("servicio", "traeUsuario");
       params.add("usuario", usuario);

       Object obj = ServiceWS.invocaWS("WSAutorizacion", params );
       if ( obj != null && obj.getClass().getName().contains("AppException")) {
           throw (AppException) obj;
       } else if (obj == null){
    	   throw new AppException("Usuario no habilitado", null);
       } else {
           return (Integer) obj;
       }

   }
   
   public static Integer traeVersionAndroid() throws AppException {

       ParamServlet params = new ParamServlet();
       params.add("servicio", "traeVersionAndroid");

       Object obj = ServiceWS.invocaWS("WSAutorizacion", params );
       if ( obj != null && obj.getClass().getName().contains("AppException")) {
           throw (AppException) obj;
       } else if (obj == null){
    	   throw new AppException("Error de conexión a Internet", null);
       } else {
           return (Integer) obj;
       }

   }
   
   public static Integer insertaSesion(Sesion sesion) throws AppException {

       ParamServlet params = new ParamServlet();
       params.add("servicio", "insertaSesion");
       params.add("sesion", sesion);

       Object obj = ServiceWS.invocaWS("WSAutorizacion", params );
       if ( obj != null && obj.getClass().getName().contains("AppException")) {
           throw (AppException) obj;
       } else if (obj == null){
    	   throw new AppException("Error de conexión a Internet", null);
       } else {
           return (Integer) obj;
       }

   }
   
   public static void insertaX1Z1() throws AppException {

       ParamServlet params = new ParamServlet();
       params.add("servicio", "insertaX1Z1");
       Object obj = ServiceWS.invocaWS("WSAutorizacion", params );
       if ( obj != null && obj.getClass().getName().contains("AppException")) {
           throw (AppException) obj;
       } else if (obj == null){
    	   throw new AppException("Error de conexión a Internet", null);
       }
   }

}
