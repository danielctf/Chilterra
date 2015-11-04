/*
 * Desarrollado por : Miguel Vega B.
 */

package cl.a2r.common;

/**
 *
 * @author Miguelon
 */
public class AppException extends Exception {
    private static final long serialVersionUID = 1L;

    String message;
    Exception ex;

    public AppException( String message, Exception ex ) {
        this.message = message;
        this.ex = ex;
    }

    public String getMessage() {
        return message;
    }

    public String getTrace() {
        String trace = "";

        if ( ex != null ) {

            trace += ex.toString() + "\n";
            StackTraceElement[] st = ex.getStackTrace();
            for ( int i=0; i < st.length; i++ ) {
                StackTraceElement ste = st[i];
                trace += "  at  " + ste.getClassName() + "." + ste.getMethodName() + "(" + ste.getFileName() + ":" + ste.getLineNumber() + ")\n";
            }

        }
        return trace;
    }

}
