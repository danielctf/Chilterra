package cl.a2r.sap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cl.a2r.sap.common.Util;
import cl.a2r.sap.model.Medicion;

public class MedicionDAO {
	
	private static final String SQL_INSERTA_MEDICION = ""
			+ "select * from sap.ws_insert_medicion(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
    
    public static void insertaMedicion(Transaccion trx, Medicion med) throws SQLException {

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERTA_MEDICION );
        pst.setObject(1, med.getUsuarioId());
        pst.setObject(2, med.getClickInicial());
        pst.setObject(3, med.getClickFinal());
        pst.setObject(4, med.getMuestras());
        pst.setObject(5, med.getMateriaSeca());
        pst.setObject(6, med.getMedidorId());
        pst.setObject(7, med.getTipoMuestraId());
        pst.setObject(8, med.getPotreroId());
        pst.setObject(9, med.getFundoId());
        pst.setObject(10, Util.dateToSqlDate(med.getFecha()));
        pst.executeQuery();

        pst.close();
        
    }
	
}
