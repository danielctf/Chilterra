package cl.a2r.sip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cl.a2r.sip.model.Instancia;

public class InstanciasDAO {

	//(?, ?, ?) = (p_usuario_id, p_fundo_id, p_aplicacion_id)
    private static final String SQL_INSERT_PROCEDIMIENTO = ""
            + "select * from sip.ws_insert_procedimiento_instancia(?, ?, ?)";
	
    public static void insertProc(Transaccion trx, Instancia instancia, Integer appId) throws SQLException {

        Connection conn = null;
        PreparedStatement pst = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERT_PROCEDIMIENTO );
        pst.setObject(1, instancia.getUsuarioId());
        pst.setObject(2, instancia.getFundoId());
        pst.setObject(3, appId);
        pst.executeQuery();
        pst.close();
    }
    
}
