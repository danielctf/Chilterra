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

import cl.a2r.sip.model.Aplicacion;
import cl.a2r.sip.model.Predio;

/**
 *
 * @author Miguelon
 */
public class AutorizacionDAO {
    private static final String SQL_SELECT_ID_USUARIO = ""
            + "select * from sip.ws_valida_usuario(?)";

    private static final String SQL_SELECT_APLICACIONES = ""
            + "select * from sip.ws_select_aplicaciones(?)";
    
    private static final String SQL_SELECT_FUNDOS = ""
    		+ "select * from sip.ws_select_fundo()";
    
    public static List selectPredios(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_FUNDOS );
        res = pst.executeQuery();
        while (res.next() ){
            Predio predio = new Predio();
            predio.setId(res.getInt("g_fundo_id"));
            predio.setCodigo(res.getString("value"));
            predio.setNombre(res.getString("name"));
            list.add(predio);
        }
        res.close();
        pst.close();

        return list;
    }

    public static Integer selectIdusuario(Transaccion trx, String usuario) throws SQLException {
        Integer id = null;

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_ID_USUARIO );
        pst.setObject(1, usuario);
        res = pst.executeQuery();
        if (res.next() ){
            id = res.getInt("ad_user_id");
        }
        res.close();
        pst.close();

        return id;
    }

    public static List selectAplicaciones(Transaccion trx, Integer idUsuario) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_APLICACIONES );
        pst.setObject(1, idUsuario);
        res = pst.executeQuery();
        while (res.next() ){
            Aplicacion app = new Aplicacion();
            app.setId(res.getInt("g_aplicacion_id"));
            app.setCodigo(res.getString("value"));
            app.setNombre(res.getString("name"));
            String activa = (res.getString("isactive"));
            app.setActiva(activa);
            list.add(app);
        }
        res.close();
        pst.close();

        return list;
    }

}
