/*
 * Desarrollado por : Miguel Vega B.
 */

package cl.a2r.sap.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ManagerDB {
    private static final String jdbcDriver = "org.postgresql.Driver";

    private static final String dbUser = "postgres";
    private static final String dbPassword = "pg1234";
    private static final String urlConn = "jdbc:postgresql://200.6.115.179:5432/chilterra";

    //private static final String dbUser = "postgres";
    //private static final String dbPassword = "pg1234";
    //private static final String urlConn = "jdbc:postgresql://201.187.98.131:5432/chilterra_desa";
            
    static {
        try {
            //Driver
            Class.forName(jdbcDriver);
            //connectionRW = getConnectionRW();
            //connectionRO = getConnectionRO();
        }
        catch( Exception ex){
            //System.out.println( ex.toString() );
        }
    }
    
  
    public static Connection getConnectionRW(){        
        Connection connectionRW = null;
        try {
            if( connectionRW==null ){
                DriverManager.setLoginTimeout(10);
                connectionRW = DriverManager.getConnection(urlConn, dbUser, dbPassword);
                connectionRW.setAutoCommit(false);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return connectionRW;
    }
    
    public static Connection getConnectionRO(){        
        Connection connectionRO = null;
        try {
            if( connectionRO==null ){
                DriverManager.setLoginTimeout(10);
                connectionRO = DriverManager.getConnection(urlConn, dbUser, dbPassword);
                connectionRO.setAutoCommit(false);
                connectionRO.setReadOnly(true);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return connectionRO;
    }          
    
    public static boolean closeConnection(Connection connection) {
        //return true;
        if( connection==null)
            return true;
        try {
            connection.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
         
    }        

    public static boolean isOnline(){        
        Connection connectionRO = null;
        boolean ret = true;
        try {
            DriverManager.setLoginTimeout(5);
            connectionRO = DriverManager.getConnection(urlConn, dbUser, dbPassword);
            connectionRO.close();
        } catch (SQLException ex) {
            ret = false;
            ex.printStackTrace();
        }
        return ret;
    }          
    
        
}