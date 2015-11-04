/*
 * WSChilterra
 * Todos los derechos reservados.
 */

package cl.a2r.sip.mail;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miguel Vega Brante
 */
public class Correo {
    private String host = "smtp.gmail.com";
    private String port = "465";
    private String user = "desarrollador@chilterra.com";
    private String pass = "tatin123";

    private String from;
    private String to;
    private String cc;
    private String subject;
    private String body;
    private List adjuntos;

    public Correo() {
        adjuntos = new ArrayList();
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * @param pass the pass to set
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * @return the cc
     */
    public String getCc() {
        return cc;
    }

    /**
     * @param cc the cc to set
     */
    public void setCc(String cc) {
        this.cc = cc;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String header) {
        this.subject = header;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    public List getAdjuntos() {
        return this.adjuntos;
    }

    public void addAdjunto(byte[] data, String mime, String nombre) {
        adjuntos.add(new Adjunto(data, mime, nombre));
    }

    public boolean enviar() {
        boolean ret = true;

        MailUtil.send(this);

        return ret;
    }

    public class Adjunto {
        private byte[] data;
        private String mime;
        private String nombre;

        private Adjunto(byte[] data, String mime, String nombre) {
            this.data = data;
            this.mime = mime;
            this.nombre = nombre;
        }

        /**
         * @return the data
         */
        public byte[] getData() {
            return data;
        }

        /**
         * @param data the data to set
         */
        public void setData(byte[] data) {
            this.data = data;
        }

        /**
         * @return the mime
         */
        public String getMime() {
            return mime;
        }

        /**
         * @param mime the mime to set
         */
        public void setMime(String mime) {
            this.mime = mime;
        }

        /**
         * @return the nombre
         */
        public String getNombre() {
            return nombre;
        }

        /**
         * @param nombre the nombre to set
         */
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
    }
}
