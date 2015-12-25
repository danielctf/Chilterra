package cl.a2r.sip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.sip.model.CollarParto;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.GanadoLogs;
import cl.a2r.sip.model.Parto;
import cl.a2r.sip.model.TipoParto;

public class PartosDAO {

    private static final String SQL_SELECT_COLLARES = ""
            + "select * from sip.ws_select_collar(?)";
    
    private static final String SQL_SELECT_TIPOPARTOS = ""
            + "select * from sip.ws_select_tipoparto()";
    
    private static final String SQL_SELECT_SUBTIPOPARTOS = ""
            + "select * from sip.ws_select_subtipoparto()";
    
    private static final String SQL_SELECT_PARTOANTERIOR = ""
    		+ "select * from sip.ws_select_parto_anterior(?)";
    
    private static final String SQL_SELECT_PARTOS = ""
    		+ "select * from sip.ws_select_partos(?, ?)";
    
    private static final String SQL_SELECT_PARTOS_ENCONTRADOS = ""
    		+ "select * from sip.ws_select_partos_ce(?)";
    
    private static final String SQL_SELECT_PARTOS_FALTANTES = ""
    		+ "select * from sip.ws_select_partos_cf(?)";
    
    private static final String SQL_INSERT_PARTO = ""
    		+ "select * from sip.ws_insert_parto(?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_CONFIRMA_PARTO = ""
    		+ "select * from sip.ws_confirma_parto(?, ?)";
    
    private static final String SQL_SELECT_PARTO_POR_CONFIRMAR = ""
    		+ "select * from sip.ws_select_parto_por_confirmar(?)";
    
    private static final String SQL_DESHACER_REGISTRO_PARTO = ""
    		+ "select * from sip.ws_deshacer_registro_parto(?, ?)";

    public static List selectCollares(Transaccion trx, Integer predioId) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_COLLARES );
        pst.setObject(1, predioId);
        res = pst.executeQuery();
        while (res.next() ){
            CollarParto c = new CollarParto();
            c.setId(res.getInt("g_collar_id"));
            c.setPredioId(res.getInt("g_fundo_id"));
            c.setCodigo(res.getString("value"));
            c.setNombre(res.getString("name"));
            list.add(c);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectTipoPartos(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        
        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_TIPOPARTOS );
        res = pst.executeQuery();
        while (res.next() ){
            TipoParto t = new TipoParto();
            t.setId(res.getInt("g_tipoparto_id"));
            t.setNombre(res.getString("name"));
            list.add(t);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectSubTipoPartos(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_SUBTIPOPARTOS );
        res = pst.executeQuery();
        while (res.next() ){
            TipoParto t = new TipoParto();
            t.setId(res.getInt("g_parto_subtipo_id"));
            t.setNombre(res.getString("name"));
            list.add(t);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectPartoAnterior(Transaccion trx, Integer ganadoId) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_PARTOANTERIOR );
        pst.setObject(1, ganadoId);
        res = pst.executeQuery();
        
        while (res.next() ){
            Parto t = new Parto();
            t.setGanadoId(res.getInt("g_ganado_id"));
            t.setEstadoParto(res.getString("g_estado_parto"));
            list.add(t);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectPartos(Transaccion trx, Integer userId, Integer fundoId) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_PARTOS );
        pst.setObject(1, userId);
        pst.setObject(2, fundoId);
        res = pst.executeQuery();
        
        while (res.next() ){
        	GanadoLogs gl = new GanadoLogs();
        	gl.setGanadoId(res.getInt("g_ganado_id"));
        	gl.setDiio(res.getInt("diio"));
        	gl.setLogId(res.getInt("g_parto_id"));
            list.add(gl);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectCandidatosEncontrados(Transaccion trx, Integer fundoId) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_PARTOS_ENCONTRADOS );
        pst.setObject(1, fundoId);
        res = pst.executeQuery();
        
        while (res.next() ){
            Ganado g = new Ganado();
            g.setId(res.getInt("g_ganado_id"));
            g.setDiio(res.getInt("g_diio"));
            g.setActiva(res.getString("activa"));
            g.setPredio(res.getInt("g_fundo_id"));
            list.add(g);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectCandidatosFaltantes(Transaccion trx, Integer fundoId) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_PARTOS_FALTANTES );
        pst.setObject(1, fundoId);
        res = pst.executeQuery();
        
        while (res.next() ){
            Ganado g = new Ganado();
            g.setId(res.getInt("g_ganado_id"));
            g.setDiio(res.getInt("g_diio"));
            g.setActiva(res.getString("activa"));
            g.setPredio(res.getInt("g_fundo_id"));
            list.add(g);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static void insertParto(Transaccion trx, Parto parto) throws SQLException {
	    List list = new ArrayList();
	
	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet res = null;
	
	    conn = trx.getConn();
	    pst = conn.prepareStatement( SQL_INSERT_PARTO );
	    pst.setObject(1, parto.getUserId());
	    pst.setObject(2, parto.getPredioId());
	    pst.setObject(3, parto.getGanadoId());
	    pst.setObject(4, parto.getTipoPartoId());
	    pst.setObject(5, parto.getSubTipoParto());
	    pst.setObject(6, parto.getEstado());
	    pst.setObject(7, parto.getSexo());
	    pst.setObject(8, parto.getCollarId());
	    pst.executeQuery();

	    pst.close();
    }
    
    public static void confirmaParto(Transaccion trx, Integer ganadoId, Integer usuarioId) throws SQLException {
	    List list = new ArrayList();
	
	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet res = null;
	
	    conn = trx.getConn();
	    pst = conn.prepareStatement( SQL_CONFIRMA_PARTO );
	    pst.setObject(1, ganadoId);
	    pst.setObject(2, usuarioId);
	    pst.executeQuery();

	    pst.close();
    }
    
    public static List selectPartoPorConfirmar(Transaccion trx, Integer ganadoId) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_PARTO_POR_CONFIRMAR );
        pst.setObject(1, ganadoId);
        res = pst.executeQuery();
        
        while (res.next() ){
            Parto t = new Parto();
            t.setGanadoId(res.getInt("g_ganado_id"));
            t.setEstadoParto(res.getString("g_estado_parto"));
            list.add(t);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static void deshacerRegistroParto(Transaccion trx, GanadoLogs gl) throws SQLException {
	    List list = new ArrayList();
	
	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet res = null;
	
	    conn = trx.getConn();
	    pst = conn.prepareStatement( SQL_DESHACER_REGISTRO_PARTO );
	    pst.setObject(1, gl.getUsuarioId());
	    pst.setObject(2, gl.getLogId());
	    pst.executeQuery();

	    pst.close();
    }
	
}
