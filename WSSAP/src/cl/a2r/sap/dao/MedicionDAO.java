package cl.a2r.sap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.sap.common.Util;
import cl.a2r.sap.model.Calificacion;
import cl.a2r.sap.model.Medicion;

public class MedicionDAO {
	
	private static final String SQL_INSERTA_MEDICION = ""
			+ "select * from sap.ws_insert_medicion(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String SQL_SELECT_STOCK = ""
			+ "select * from sap.ws_select_stock()";
	
	private static final String SQL_INSERTA_CALIFICACION = ""
			+ "select * from sap.ws_update_calificacion(?, ?, ?, ?)";
	
	private static final String SQL_SELECT_CALIFICACION = ""
			+ "select * from sap.ws_select_calificacion()";
    
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
        pst.setObject(11, med.getAnimales());
        pst.executeQuery();

        pst.close();
        
    }
    
    public static List selectStock(Transaccion trx) throws SQLException {
    	List<Medicion> list = new ArrayList<Medicion>();
    	
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_STOCK );
        res = pst.executeQuery();
        while (res.next()){
        	Medicion med = new Medicion();
        	med.setId(res.getInt("a_medicion_id"));
        	med.setUsuarioId(res.getInt("createdby"));
        	med.setFecha(res.getDate("fecha_medicion"));
        	med.setClickInicial(res.getInt("inicial"));
        	med.setClickFinal(res.getInt("final"));
        	med.setMuestras(res.getInt("muestra"));
        	med.setMateriaSeca(res.getInt("materia_seca"));
        	med.setMedidorId(res.getInt("medidor"));
        	med.setPotreroId(res.getInt("numero"));
        	med.setTipoMuestraId(res.getInt("a_tipo_medicion_id"));
        	med.setTipoMuestraNombre(res.getString("value"));
        	med.setActualizado(res.getTimestamp(("actualizado")));
        	med.setSuperficie(res.getDouble("superficie"));
        	med.setFundoId(res.getInt("g_fundo_id"));
        	med.setAnimales(res.getInt("animales"));
        	list.add(med);
        }
        pst.close();
        res.close();
		return list;
    }
	
    public static void insertaCalificacion(Transaccion trx, List<Calificacion> calList, Integer g_usuario_id) throws SQLException {

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERTA_CALIFICACION );
        for (Calificacion cal : calList){
        	pst.setObject(1, g_usuario_id);
        	pst.setObject(2, cal.getG_fundo_id());
        	pst.setObject(3, cal.getNumero());
        	pst.setObject(4, cal.getCalificacion());
        	pst.executeQuery();
        }
        
        pst.close();
        
    }
    
    public static List selectCalificacion(Transaccion trx) throws SQLException {
    	List<Calificacion> calList = new ArrayList<Calificacion>();
    	
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_CALIFICACION );
        res = pst.executeQuery();
        while (res.next()){
        	
        	Calificacion cal = new Calificacion();
        	cal.setG_fundo_id(res.getInt("g_fundo_id"));
        	cal.setNumero(res.getInt("numero"));
        	cal.setCalificacion(res.getInt("calificacion"));
        	cal.setSincronizado("Y");
        	
        	calList.add(cal);
        }
        pst.close();
        res.close();
		return calList;
    }
    
}
