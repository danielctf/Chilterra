package cl.a2r.sip.dao;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import cl.a2r.sip.model.Ganado;

public class GanadoDAO {
	
    private static final String SQL_SELECT_GANADO_ID = ""
            + "select * from l47.ws_select_ganado_id(?)";
    
    private static final String SQL_SELECT_DIIO = ""
    		+ "select * from l47.ws_select_diio(?)";
	
    public static List selectGanado(Transaccion trx, Integer diio) throws SQLException {
    	List list = new ArrayList();
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_GANADO_ID );
        pst.setObject(1, diio);
        res = pst.executeQuery();
        if (res.next() ){
        	Ganado g = new Ganado();
            g.setId(res.getInt("g_ganado_id"));
            g.setDiio(res.getInt("g_diio"));
            g.setActiva(res.getString("g_activa"));
            g.setPredio(res.getInt("g_predio"));
            list.add(g);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectDIIO(Transaccion trx, long eid) throws SQLException {
    	List list = new ArrayList();
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_DIIO );
        //pst.setObject(1, eid);
        pst.setLong(1, eid);
        res = pst.executeQuery();
        if (res.next() ){
        	Ganado g = new Ganado();
            g.setId(res.getInt("g_ganado_id"));
            g.setDiio(res.getInt("g_diio"));
            g.setActiva(res.getString("g_activa"));
            g.setPredio(res.getInt("g_predio"));
            list.add(g);
        }
        res.close();
        pst.close();

        return list;
    }
    
}
