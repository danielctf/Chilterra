/*
 * Desarrollado por : Miguel Vega B.
 */

package cl.a2r.sip.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class Transaccion {
    private Connection conn;
    
    /** Creates a new instance of Transaccion */
    public Transaccion(boolean isTrx) {
        if (isTrx)
            setConn(ManagerDB.getConnectionRW());
        else
            setConn(ManagerDB.getConnectionRO());
    }

    private void setConn(Connection conn) {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }
  
    public boolean commit() {
        boolean ret = false;
        try {
            conn.commit();
            ret = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }
 
    public boolean rollback() {
        boolean ret = false;
        try {
            conn.rollback();
            ret = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }
 
    public static Transaccion getTransaccion(boolean isTrx) {
        Transaccion trx = new Transaccion(isTrx);
        if (trx.getConn() == null)
            return null;
        return trx;
    }
    
    public boolean close(){        
       boolean ret = false;
       
       ret = ManagerDB.closeConnection(conn);
       conn = null;
       return ret;
       
    }    
}
