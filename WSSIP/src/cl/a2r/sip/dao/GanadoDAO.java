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
	
    private static final String SQL_SELECT_GANADO = ""
            + "select * from sip.ws_select_ganado(?)";
    
    private static final String SQL_SELECT_GANADO_BASTON = ""
    		+ "select * from sip.ws_select_ganado_baston(?)";
    
    private static final String SQL_SELECT_DIIO = ""
    		+ "select * from sip.ws_select_diio(?)";
    
    private static final String SQL_SELECT_DIIO_BASTON = ""
    		+ "select * from sip.ws_select_diio_baston(?)";
    
    private static final String SQL_SELECT_BUSQUEDA = ""
    		+ "select * from sip.ws_busqueda_select_busqueda()";
	
    public static List selectGanado(Transaccion trx, Integer diio) throws SQLException {
    	List list = new ArrayList();
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_GANADO );
        pst.setObject(1, diio);
        res = pst.executeQuery();
        if (res.next() ){
        	Ganado g = new Ganado();
            g.setId(res.getInt("g_ganado_id"));
            g.setDiio(res.getInt("g_diio"));
            g.setActiva(res.getString("g_activa"));
            g.setPredio(res.getInt("g_predio"));
            g.setSexo(res.getString("sexo"));
            g.setTipoGanadoId(res.getInt("g_tipo_ganado_id"));
            list.add(g);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectGanadoBaston(Transaccion trx, long eid) throws SQLException {
    	List list = new ArrayList();
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_GANADO_BASTON );
        pst.setLong(1, eid);
        res = pst.executeQuery();
        if (res.next() ){
        	Ganado g = new Ganado();
            g.setId(res.getInt("g_ganado_id"));
            g.setDiio(res.getInt("g_diio"));
            g.setActiva(res.getString("g_activa"));
            g.setPredio(res.getInt("g_predio"));
            g.setSexo(res.getString("sexo"));
            g.setTipoGanadoId(res.getInt("g_tipo_ganado_id"));
            list.add(g);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static Integer selectDiio(Transaccion trx, Integer diio) throws SQLException {
    	List list = new ArrayList();
    	
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_DIIO );
        pst.setObject(1, diio);
        res = pst.executeQuery();

        res.close();
        pst.close();

        return diio;
    }
    
    public static Integer selectDiioBaston(Transaccion trx, long eid) throws SQLException {
    	List list = new ArrayList();
    	Integer diio = null;
    	
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_DIIO_BASTON );
        pst.setLong(1, eid);
        res = pst.executeQuery();
        if (res.next()){
        	diio = res.getInt(1);
        }
        
        res.close();
        pst.close();

        return diio;
    }
    
    public static List selectGanadoBusqueda(Transaccion trx) throws SQLException {
    	List list = new ArrayList();
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_BUSQUEDA );

        res = pst.executeQuery();
        while (res.next() ){
        	Ganado g = new Ganado();
            g.setId(res.getInt("g_ganado_id"));
            g.setDiio(res.getInt("diio"));
            g.setFlag(res.getInt("flag"));
            g.setVenta(res.getInt("venta"));
            list.add(g);
        }
        res.close();
        pst.close();

        return list;
    }
    
}
