package cl.a2r.sip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Predio;
import cl.a2r.sip.model.PredioLibre;

public class PredioLibreDAO {

    private static final String SQL_SELECT_PREDIO_LIBRE = ""
    		+ "select * from sip.ws_select_predio_libre(?)";
    
    private static final String SQL_SELECT_ALL_DIIO = ""
    		+ "select * from sip.ws_select_all_diio()";    
    
    public static List selectPredioLibre(Transaccion trx, Integer g_fundo_id) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_PREDIO_LIBRE );
        pst.setObject(1, g_fundo_id);
        res = pst.executeQuery();
        while (res.next() ){
        	PredioLibre pl = new PredioLibre();
        	pl.setId(res.getInt("g_procedimiento_instancia_id"));
        	pl.setNombreFundo(res.getString("name"));
        	pl.setFecha_inicio(res.getTimestamp("fecha_inicio"));
        	pl.setFecha_termino(res.getTimestamp("fecha_termino"));
        	pl.setEstado(res.getString("estado"));
        	list.add(pl);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectAllDiio(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_ALL_DIIO );
        res = pst.executeQuery();
        while (res.next() ){
        	Ganado g = new Ganado();
        	g.setId(res.getInt("g_ganado_id"));
        	g.setDiio(res.getInt("diio"));
        	g.setEid(Long.toString(res.getLong("eid")));
        	list.add(g);
        }
        res.close();
        pst.close();

        return list;
    }
	
}
