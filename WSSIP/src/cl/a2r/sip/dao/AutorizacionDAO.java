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

import cl.a2r.sip.dao.Transaccion;
import cl.a2r.sip.model.Aplicacion;
import cl.a2r.sip.model.Predio;
import cl.a2r.sip.model.Sesion;

/**
 *
 * @author Miguelon
 */
public class AutorizacionDAO {
    private static final String SQL_SELECT_USUARIO = ""
            + "select * from sip.ws_select_usuario(?)";

    private static final String SQL_SELECT_APLICACIONES = ""
            + "select * from sip.ws_select_aplicaciones(?)";
    
    private static final String SQL_SELECT_FUNDOS = ""
    		+ "select * from sip.ws_select_fundo()";
    
    private static final String SQL_SELECT_VERSION_ANDROID = ""
    		+ "select * from sip.ws_select_version_android()";
    
    private static final String SQL_INSERT_SESION = ""
    		+ "select * from sip.ws_insert_sesion(?, ?, ?, ?)";
    
    private static final String SQL_INSERT_X1_Z1 = ""
    		+ "select * from sip.ws_x1_z1()";
    
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
            predio.setRup(res.getString("rup"));
            list.add(predio);
        }
        res.close();
        pst.close();

        return list;
    }

    public static Integer selectIdUsuario(Transaccion trx, String usuario) throws SQLException {
        Integer id = null;

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_USUARIO );
        pst.setObject(1, usuario);
        res = pst.executeQuery();
        if (res.next() ){
            id = res.getInt("g_usuario_id");
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
    
    public static Integer selectVersionAndroid(Transaccion trx) throws SQLException {
        Integer ver = null;

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_VERSION_ANDROID );
        res = pst.executeQuery();
        
        if (res.next() ){
        	ver = res.getInt("version");
        }
        res.close();
        pst.close();

        return ver;
    }
    
    public static Integer insertaSesion(Transaccion trx, Sesion sesion) throws SQLException {
        Integer sesionId = null;

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERT_SESION );
        pst.setObject(1, sesion.getUsuarioId());
        pst.setObject(2, sesion.getFundoId());
        pst.setObject(3, sesion.getAppId());
        pst.setObject(4, sesion.getImei());
        res = pst.executeQuery();
        
        if (res.next()){
        	sesionId = res.getInt("sesion_id");
        }
        
        res.close();
        pst.close();

        return sesionId;
    }
    
    public static void insertX1Z1(Transaccion trx) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERT_X1_Z1 );
        pst.executeQuery();

        pst.close();
    }

}
