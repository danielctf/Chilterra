/*
 * Desarrollado por : Miguel Vega B.
 */

package cl.a2r.sip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.sip.model.CausaBaja;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.MotivoBaja;

/**
 *
 * @author Miguelon
 */
public class BajasDAO {

    private static final String SQL_SELECT_MOTIVO = ""
            + "select * from sip.ws_select_motivo_baja()";

    private static final String SQL_SELECT_CAUSA = ""
            + "select * from sip.ws_select_causa_baja()";
    
    private static final String SQL_SELECT_BAJAS = ""
    		+ "select * from l47.ws_select_bajas()";

    public static List selectBajas(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_BAJAS );
        res = pst.executeQuery();
        while (res.next() ){
            Ganado g = new Ganado();
            g.setId(res.getInt("g_ganado_id"));
            g.setDiio(res.getInt("g_ganado_diio"));
            list.add(g);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectMotivoBaja(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_MOTIVO );
        res = pst.executeQuery();
        while (res.next() ){
            MotivoBaja mb = new MotivoBaja();
            mb.setId(res.getInt("g_baja_motivo_id"));
            mb.setCodigo(res.getString("value"));
            mb.setNombre(res.getString("name"));
            list.add(mb);
        }
        res.close();
        pst.close();

        return list;
    }

        public static List selectCausaBaja(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_CAUSA );
        res = pst.executeQuery();
        while (res.next() ){
            CausaBaja cb = new CausaBaja();
            cb.setId(res.getInt("g_baja_causa_id"));
            cb.setCodigo(res.getString("value"));
            cb.setNombre(res.getString("name"));
            list.add(cb);
        }
        res.close();
        pst.close();

        return list;
    }

}
