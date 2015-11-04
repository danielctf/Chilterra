package cl.a2r.common.wsutils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

/**
 * Provide a SSLSocketFactory that allows SSL connections to be
 * made without validating the server's certificate.  This is more
 * convenient for some applications, but is less secure as it allows 
 * "man in the middle" attacks.
 */
public class MySSLFactory extends WrappedFactory {

    /**
     * We provide a constructor that takes an unused argument solely
     * because the ssl calling code will look for this constructor
     * first and then fall back to the no argument constructor, so
     * we avoid an exception and additional reflection lookups.
     */
    public MySSLFactory(String arg) throws GeneralSecurityException {

//        if (!arg.equals("hola")) {
//            throw new GeneralSecurityException("Usuario no Autorizado");
//        }

        SSLContext ctx = SSLContext.getInstance("TLS"); // or "SSL" ?

        ctx.init(null,
                 new TrustManager[] { new NonValidatingTM() }, 
                 null );

        _factory = new MySSLSocketFactory(ctx);
//        _factory = ctx.getSocketFactory();

    }


//    @Override
//    public Socket createSocket(String host, int port) throws IOException,
//            UnknownHostException {
////        AFUNIXSocket socket = AFUNIXSocket.newInstance();
////        socket.connect(new AFUNIXSocketAddress(new File(path)), port);
//        System.out.println("paso1");
//        return new Socket( host, port);
//    }
//    @Override
//    public Socket createSocket(InetAddress host, int port) throws IOException {
//        System.out.println("paso2");
//        return new Socket("localhost", 1111);
//    }
//
//    @Override
//    public Socket createSocket(String host, int port, InetAddress localHost,
//            int localPort) throws IOException, UnknownHostException {
//        System.out.println("paso3");
//        return new Socket("localhost", 1111);
//    }
//
//    @Override
//    public Socket createSocket(InetAddress address, int port,
//            InetAddress localAddress, int localPort) throws IOException {
//        System.out.println("paso4");
//        return new Socket("localhost", 1111);
//    }
    
    static class NonValidatingTM implements X509TrustManager {


        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

    
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

   
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }

}

