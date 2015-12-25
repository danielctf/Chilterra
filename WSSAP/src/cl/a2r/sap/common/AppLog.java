/*
 * WSChilterra
 * Todos los derechos reservados.
 */

package cl.a2r.sap.common;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Miguel Vega Brante
 */
public class AppLog {
    @SuppressWarnings("NonConstantLogger")
    private static Logger logger = null;

    public static void logSevere(String mensaje, Exception ex) {
        log(Level.SEVERE, mensaje, ex);
    }

    public static void logWarning(String mensaje, Exception ex) {
        log(Level.WARNING, mensaje, ex);
    }

    public static void logInfo(String mensaje, Exception ex) {
        log(Level.INFO, mensaje, ex);
    }

    private static void log(Level level, String mensaje, Exception ex) {

        if (logger == null) {
            creaLog();
        }
        logger.log(level, mensaje, ex);

    }

    private static void creaLog() {
        String cata;

        logger = Logger.getLogger("WSSIP");
        FileHandler fh;
        try {
            cata = System.getenv("CATALINA_HOME");
            if ( cata == null || cata.equals("null")) {
                cata = "";
            }
            fh = new FileHandler( cata + "/logs/WSSIP.log");
//              fh = new FileHandler( Aplicacion.realPath + "/logs/PSistemas.logger");
            fh.setFormatter(new SimpleFormatter() );
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }



}
