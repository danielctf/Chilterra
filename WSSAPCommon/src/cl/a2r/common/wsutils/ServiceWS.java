/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.a2r.common.wsutils;

import cl.a2r.common.AppException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 *
 * @author Miguel Vega Brante
 */
public class ServiceWS {

    private static final String sURL = "http://200.6.115.179/WSSAP/";
    private static final String sURLSSL = "http://200.6.115.179/WSSAP/secure/";
    
    //private static final String sURL = "http://201.187.98.131:9090/WSSIP/";
    //private static final String sURLSSL = "http://201.187.98.131:9443/WSSIP/secure/";


    public static Object invocaWS(String servlet, ParamServlet params) {

//        params.add("idApp", idApp);

        Object obj = leeUrl(servlet, params);
        if ( obj != null && obj.getClass().getName().contains("AppException")) {
            if ( ((AppException)obj).getMessage().contains("Fallo autorización") ) {
            }
        }
        return obj;
    }


    public static Object invocaWSSSL(String servlet, ParamServlet params) {

        Object obj = leeUrlSSL(servlet, params);
        if ( obj != null && obj.getClass().getName().contains("AppException")) {
            if ( ((AppException)obj).getMessage().contains("Fallo autorización") ) {
            }
        }
        return obj;

    }


   private static Object leeUrl(String servlet, ParamServlet params) {
       Object ret = null;

       try {
            URL url = new URL(sURL + servlet);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Agregar token
//            params.add("token", token );
            // Parametros de envío
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-java-serialized-object");
            ObjectOutputStream output;
            output = new ObjectOutputStream( con.getOutputStream( ) );
            output.writeObject( params );
            output.flush( );
            output.close( );

            // Respuesta
            ObjectInputStream ois = new ObjectInputStream(con.getInputStream());
            try {
                ret = ois.readObject();
            } catch (ClassNotFoundException ex) {
            }
            ois.close();
            con.disconnect();

        } catch ( MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

       return ret;
   }

   private static Object leeUrlSSL(String servlet, ParamServlet params) {
       Object ret = null;

       try {
            URL url = new URL(sURLSSL + servlet);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            con.setSSLSocketFactory(new MySSLFactory(""));
            con.setHostnameVerifier( new HostnameVerifier() {
                
                public boolean verify(String string, SSLSession ssls) {
                    return true;
                }
            });
            // Agregar token
//            params.add("token", token );
            // Parametros de envío
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-java-serialized-object");
            ObjectOutputStream output;
            output = new ObjectOutputStream( con.getOutputStream( ) );
            output.writeObject( params );
            output.flush( );
            output.close( );

            // Respuesta
            ObjectInputStream ois = new ObjectInputStream(con.getInputStream());
            try {
                ret = ois.readObject();
            } catch (ClassNotFoundException ex) {
            }
            ois.close();
            con.disconnect();

        } catch ( MalformedURLException ex) {
            ex.printStackTrace();
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

       return ret;
   }


}
