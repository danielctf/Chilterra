package cl.a2r.sip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.sip.model.Auditoria;
import cl.a2r.sip.model.Ganado;

public class AuditoriaDAO {

    private static final String SQL_INSERT_AUDITORIA = ""
            + "select * from sip.ws_auditoria_insert_auditoria(?, ?)";
    
    private static final String SQL_SELECT_AUDITORIA = ""
            + "select * from sip.ws_auditoria_select_auditorias(?)";
    
    private static final String SQL_CERRAR_AUDITORIA = ""
    		+ "select * from sip.ws_auditoria_cerrar_auditoria(?, ?, ?)";
    
    private static final String SQL_BORRAR_AUDITORIA = ""
    		+ "select * from sip.ws_auditoria_eliminar_auditoria(?, ?)";
    
    private static final String SQL_INSERT_GANADO = ""
    		+ "select * from sip.ws_auditoria_insert_ganado(?, ?, ?, ?)";
    
    private static final String SQL_SELECT_GANADO = ""
    		+ "select * from sip.ws_auditoria_select_ganado(?)";
	
    public static void insertAuditoria(Transaccion trx, Auditoria a, Integer usuarioId) throws SQLException {

        Connection conn = null;
        PreparedStatement pst = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERT_AUDITORIA );
        pst.setObject(1, usuarioId);
        pst.setObject(2, a.getFundoId());
        pst.executeQuery();
        pst.close();
    }
    
    public static List selectAuditoria(Transaccion trx, Integer fundoId) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_AUDITORIA );
        pst.setObject(1, fundoId);
        res = pst.executeQuery();
        while (res.next() ){
        	Auditoria a = new Auditoria();
        	a.setId(res.getInt("g_procedimiento_instancia_id"));
        	a.setFundoId(res.getInt("g_fundo_id"));
        	a.setFecha_inicio(res.getTimestamp("fecha_inicio"));
        	a.setEstado(res.getString("estado"));
            list.add(a);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List cerrarAuditoria(Transaccion trx, Auditoria a, Integer usuarioId) throws SQLException {
    	List list = new ArrayList();
    	
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        
        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_CERRAR_AUDITORIA );
        pst.setObject(1, usuarioId);
        pst.setObject(2, a.getId());
        pst.setObject(3, a.getFirma());
        res = pst.executeQuery();
        while (res.next()){
        	Ganado g = new Ganado();
        	g.setId(res.getInt("g_ganado_id"));
        	list.add(g);
        }
        
        res.close();
        pst.close();
        
        return list;
    }
    
    public static void borrarAuditoria(Transaccion trx, Auditoria a, Integer usuarioId) throws SQLException {

        Connection conn = null;
        PreparedStatement pst = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_BORRAR_AUDITORIA );
        pst.setObject(1, usuarioId);
        pst.setObject(2, a.getId());
        pst.executeQuery();
        pst.close();
    }
    
    public static void insertGanado(Transaccion trx, List<Auditoria> auList, Integer usuarioId) throws SQLException {

        Connection conn = null;
        PreparedStatement pst = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERT_GANADO );
        for (Auditoria a : auList){
            for (Ganado g : a.getGanList()){
                pst.setObject(1, usuarioId);
                pst.setObject(2, g.getId());
                pst.setObject(3, new Timestamp(g.getFecha().getTime()));
                pst.setObject(4, a.getId());
                pst.executeQuery();	
            }	
        }
        pst.close();
    }
    
    public static Auditoria selectGanado(Transaccion trx, Integer instancia) throws SQLException {
        Auditoria a = new Auditoria();
        a.setId(instancia);
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_GANADO );
        pst.setObject(1, instancia);
        res = pst.executeQuery();
        while (res.next() ){
        	Ganado g = new Ganado();
        	g.setId(res.getInt("g_ganado_id"));
        	g.setDiio(res.getInt("diio"));
        	g.setPredio(res.getInt("g_fundo_id"));
        	g.setFecha(res.getTimestamp("fecha_auditoria"));
        	g.setSincronizado("Y");
            a.getGanList().add(g);
        }
        res.close();
        pst.close();

        return a;
    }
    
}
