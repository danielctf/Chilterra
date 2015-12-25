/*
 * WSChilterra
 * Todos los derechos reservados.
 */

package cl.a2r.sap.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

/**
 *
 * @author Miguel Vega Brante
 */
public class MailUtil {

    public static void send( Correo correo ) {
        Authenticator auth = null;

        try {

            // Create a mail session
            java.util.Properties props = new java.util.Properties();
            props.put("mail.smtp.host", correo.getHost());
            props.put("mail.smtp.port", correo.getPort());

            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");

            if ( correo.getUser() != null && !correo.getUser().trim().equals("") && correo.getPass() != null && !correo.getPass().trim().equals("") ) {
                props.put("mail.smtp.auth", "true");
                auth = new SMTPAuthenticator( correo.getUser(), correo.getPass() );
            }

            // crear sesion
            Session session = Session.getDefaultInstance(props, auth);

            // Construct the message
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(correo.getFrom()));
            msg.setRecipients(Message.RecipientType.TO, toInternetAddress(correo.getTo()));
            if ( correo.getCc() != null ) {
                msg.setRecipients(Message.RecipientType.CC, toInternetAddress(correo.getCc()));
            }
            msg.setSubject(correo.getSubject());
            msg.setHeader("Date", new Date().toString());
//            msg.setText(content);

            // Se compone la parte del texto
            BodyPart texto = new MimeBodyPart();
            texto.setText(correo.getBody());
           // Una MultiParte para agrupar texto e imagen.
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);

            for (Iterator it = correo.getAdjuntos().iterator(); it.hasNext(); ) {
               // Se compone el adjunto con la imagen
                Correo.Adjunto adj = (Correo.Adjunto) it.next();
                BodyPart adjunto = new MimeBodyPart();
                adjunto.setDataHandler( new DataHandler(new ByteArrayDataSource(adj.getData(), adj.getMime())));
                adjunto.setFileName(adj.getNombre());
                multiParte.addBodyPart(adjunto);
            }

            msg.setContent(multiParte);

            // Send the message
            Transport.send(msg);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }

    }

//    public static void send(String smtpHost, int smtpPort, String user, String pass, String from, String to, String subject, String content) {
//        Authenticator auth = null;
//
//        try {
//
//            // Create a mail session
//            java.util.Properties props = new java.util.Properties();
//            props.put("mail.smtp.host", smtpHost);
//            props.put("mail.smtp.port", "" + smtpPort);
//
////            props.put("mail.smtp.socketFactory.port", "465");
//            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
//            props.put("mail.smtp.socketFactory.fallback", "false");
//
//            if ( user != null && !user.trim().equals("") && pass != null && !pass.trim().equals("") ) {
//                props.put("mail.smtp.auth", "true");
//                auth = new SMTPAuthenticator( user, pass );
//            }
//
//            // crear sesion
//            Session session = Session.getDefaultInstance(props, auth);
//
//            // Construct the message
//            Message msg = new MimeMessage(session);
//            msg.setFrom(new InternetAddress(from));
//            msg.setRecipients(Message.RecipientType.TO, getRecipients(to));
//            msg.setSubject(subject);
//            msg.setText(content);
//
//            // Send the message
//            Transport.send(msg);
//        } catch (MessagingException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public static void sendAdjunto(String smtpHost, int smtpPort, String user, String pass, String from, String to, String cc, String subject, String content, List adjuntos) {
//        Authenticator auth = null;
//
//        try {
//
//            // Create a mail session
//            java.util.Properties props = new java.util.Properties();
//            props.put("mail.smtp.host", smtpHost);
//            props.put("mail.smtp.port", "" + smtpPort);
//
////            props.put("mail.smtp.socketFactory.port", "465");
//            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
//            props.put("mail.smtp.socketFactory.fallback", "false");
//
//            if ( user != null && !user.trim().equals("") && pass != null && !pass.trim().equals("") ) {
//                props.put("mail.smtp.auth", "true");
//                auth = new SMTPAuthenticator( user, pass );
//            }
//
//            // crear sesion
//            Session session = Session.getDefaultInstance(props, auth);
//
//            // Construct the message
//            Message msg = new MimeMessage(session);
//            msg.setFrom(new InternetAddress(from));
//            msg.setRecipients(Message.RecipientType.TO, getRecipients(to));
//            msg.setRecipients(Message.RecipientType.CC, getRecipients(cc));
//            msg.setSubject(subject);
//            msg.setHeader("Date", new Date().toString());
////            msg.setText(content);
//
//            // Se compone la parte del texto
//            BodyPart texto = new MimeBodyPart();
//            texto.setText(content);
//           // Una MultiParte para agrupar texto e imagen.
//            MimeMultipart multiParte = new MimeMultipart();
//            multiParte.addBodyPart(texto);
//
//            for (Iterator it = adjuntos.iterator(); it.hasNext(); ) {
//               // Se compone el adjunto con la imagen
//                AdjuntoCorreo adj = (AdjuntoCorreo) it.next();
//                BodyPart adjunto = new MimeBodyPart();
//                adjunto.setDataHandler( new DataHandler(new ByteArrayDataSource(adj.getArchivo(), adj.getMime())));
//                adjunto.setFileName(adj.getNombre());
//                multiParte.addBodyPart(adjunto);
//            }
//
//            msg.setContent(multiParte);
//
//            // Send the message
//            Transport.send(msg);
//        } catch (MessagingException ex) {
//            ex.printStackTrace();
//        }
//    }

    private static InternetAddress[] toInternetAddress(String listDir) {
        InternetAddress[] aTo = new InternetAddress[]{};
        List a = new ArrayList();

        listDir = listDir.replaceAll(";", ",");
        String[] s = listDir.split(",");
        for (int i=0 ; i < s.length; i++) {
            try {
                a.add(new InternetAddress(s[i].trim()));
            } catch (AddressException ex) {
                ex.printStackTrace();
            }
        }
        aTo = (InternetAddress[]) a.toArray( aTo);
        return aTo;
    }

    public static void main(String[] args) {
            // Send a test message
//            send("smtp.gmail.com", 465, "mvega@chilterra.com","coloso05", "soporte@chilterra.com", "mvega@comten.cl",
//                 "Prueba correo", "Este correo enviado CON Conexion a CHILterra.com");


//        // send adjunto
//        List listaAdj = new ArrayList();
//
//        File file = new File("c:/DTE-52-3994.pdf");
//
//        FileInputStream fis = new FileInputStream(file);
//        byte[] bytes = new byte[(int)file.length()];
//        fis.read(bytes);
//        fis.close();
//
//        AdjuntoCorreo adj = new AdjuntoCorreo();
//        adj.setArchivo(bytes);
//        adj.setNombre("prueba123.pdf");
//        adj.setMime("application/pdf");
//
//        listaAdj.add(adj);
//
//        file = new File("c:/F526T33.xml");
//
//        fis = new FileInputStream(file);
//        bytes = new byte[(int)file.length()];
//        fis.read(bytes);
//        fis.close();
//
//        adj = new AdjuntoCorreo();
//        adj.setArchivo(bytes);
//        adj.setNombre(file.getName());
//        adj.setMime("application/xml");
//
//        listaAdj.add(adj);
//
//
//        sendAdjunto("smtp.gmail.com", 465, "mvega@chilterra.com", "coloso05", "soporte@chilterra.com", "mvega@comten.cl,mizquierdo@chilterra.com",
//                "mvegabrante@gmail.com;mvega@chilterra.com",
//                 "Prueba correo", "Prueba de correo 23-10-2013 22:29", listaAdj);
    }

}
