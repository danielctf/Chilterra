/**
 * SMTPAuthenticator.java
 * @author Miguelon
 */

package cl.a2r.sip.mail;

import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends javax.mail.Authenticator {
    private String user;
    private String pass;
    
    public SMTPAuthenticator( String u, String p) {
        super();
        user = u;
        pass = p;
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
       return new PasswordAuthentication(user, pass);
    }
    
}
