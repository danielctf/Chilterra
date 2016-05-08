package cl.a2r.sip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cl.a2r.sip.model.Instancia;

public class InstanciasDAO {

	//(?, ?, ?) = (p_usuario_id, p_fundo_id, p_aplicacion_id)
    private static final String SQL_INSERT_PROCEDIMIENTO = ""
            + "select * from sip.ws_insert_procedimiento_instancia(?, ?, ?)";
	
    public static Integer insertProc(Transaccion trx, Instancia instancia, Integer appId) throws SQLException {
    	Integer superInstanciaId = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        
        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERT_PROCEDIMIENTO );
        pst.setObject(1, instancia.getUsuarioId());
        pst.setObject(2, instancia.getFundoId());
        pst.setObject(3, appId);
        res = pst.executeQuery();
        if (res.next()){
        	superInstanciaId = res.getInt("procedimiento_instancia_id");
        }
        
        res.close();
        pst.close();
        
        return superInstanciaId;
    }
    
}
