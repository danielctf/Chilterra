/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.a2r.common.wsutils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author Miguelon
 */
public class MySSLSocketFactory extends SSLSocketFactory {
    SSLContext sslc = null;

    public MySSLSocketFactory( SSLContext sslContext) {
        this.sslc = sslContext;
    }

 
    public String[] getDefaultCipherSuites() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public String[] getSupportedCipherSuites() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public Socket createSocket(Socket socket, String string, int i, boolean bln) throws IOException {
        System.out.println("aviso:" + string +"-"+i);
        Socket s = sslc.getSocketFactory().createSocket(socket, string, i, bln);
//        s.getOutputStream().write("hola loco".getBytes(),0,"hola loco".getBytes().length);
        return s;
    }

    
    public Socket createSocket(String string, int i) throws IOException, UnknownHostException {
        System.out.println("aviso2:" + string +"-"+i);
        Socket s = sslc.getSocketFactory().createSocket(string, i);
        return s;
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public Socket createSocket(String string, int i, InetAddress ia, int i1) throws IOException, UnknownHostException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public Socket createSocket(InetAddress ia, int i) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public Socket createSocket(InetAddress ia, int i, InetAddress ia1, int i1) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
