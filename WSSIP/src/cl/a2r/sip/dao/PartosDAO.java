package cl.a2r.sip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.sip.model.CollarParto;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.TipoParto;

public class PartosDAO {

    private static final String SQL_SELECT_COLLARES = ""
            + "select * from l47.ws_select_collar(?)";
    
    private static final String SQL_SELECT_TIPOPARTOS = ""
            + "select * from l47.ws_select_tipoparto()";
    
    private static final String SQL_SELECT_SUBTIPOPARTOS = ""
            + "select * from l47.ws_select_subtipoparto()";


    public static List selectCollares(Transaccion trx, Integer predioId) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_COLLARES );
        pst.setObject(1, predioId);
        res = pst.executeQuery();
        while (res.next() ){
            CollarParto c = new CollarParto();
            c.setId(res.getInt("g_collar_id"));
            c.setPredioId(res.getInt("g_fundo_id"));
            c.setCodigo(res.getString("value"));
            c.setNombre(res.getString("name"));
            list.add(c);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectTipoPartos(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        
        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_TIPOPARTOS );
        res = pst.executeQuery();
        while (res.next() ){
            TipoParto t = new TipoParto();
            t.setId(res.getInt("g_tipoparto_id"));
            t.setNombre(res.getString("name"));
            list.add(t);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectSubTipoPartos(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_SUBTIPOPARTOS );
        res = pst.executeQuery();
        while (res.next() ){
            TipoParto t = new TipoParto();
            t.setId(res.getInt("g_subtipoparto_id"));
            t.setNombre(res.getString("name"));
            list.add(t);
        }
        res.close();
        pst.close();

        return list;
    }
	
}
