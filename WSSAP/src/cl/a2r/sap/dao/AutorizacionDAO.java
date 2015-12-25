/*
 * Desarrollado por : Miguel Vega B.
 */

package cl.a2r.sap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.sap.model.Aplicacion;
import cl.a2r.sap.model.Predio;
import cl.a2r.sap.model.Sesion;

/**
 *
 * @author Miguelon
 */
public class AutorizacionDAO {

	private static final String SQL_VALIDA_USUARIO = ""
			+ "select * from sap.ws_valida_usuario(?)";
	
    public static Integer validaUsuario(Transaccion trx, String correo) throws SQLException {
    	Integer g_usuario_id = null;

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_VALIDA_USUARIO );
        pst.setObject(1, correo);
        res = pst.executeQuery();
        if (res.next()){
        	g_usuario_id = res.getInt("g_usuario_id");
        }
        res.close();
        pst.close();

        return g_usuario_id;
    }

}
