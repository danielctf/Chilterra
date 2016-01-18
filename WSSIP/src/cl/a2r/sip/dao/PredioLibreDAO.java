package cl.a2r.sip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.sip.common.Util;
import cl.a2r.sip.model.Areteo;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.InyeccionTB;
import cl.a2r.sip.model.PPD;
import cl.a2r.sip.model.Predio;
import cl.a2r.sip.model.PredioLibre;

public class PredioLibreDAO {

    private static final String SQL_SELECT_PREDIO_LIBRE = ""
    		+ "select * from sip.ws_select_predio_libre(?)";
    
    private static final String SQL_SELECT_ALL_DIIO = ""
    		+ "select * from sip.ws_select_all_diio()";
    
    private static final String SQL_SELECT_GAN_TUBERCULINA = ""
    		+ "select * from sip.ws_select_gan_proc_tuberculina(?)";
    
    private static final String SQL_SELECT_TUBERCULINA_PPD = ""
    		+ "select * from sip.ws_select_tuberculina_ppd()";
    
    private static final String SQL_INSERT_GANADO_TUBERCULINA = ""
    		+ "select * from sip.ws_insert_gan_proc_tuberculina(?, ?, ?, ?, ?)";
    
    private static final String SQL_INSERT_PREDIO_LIBRE = ""
    		+ "select * from sip.ws_insert_predio_libre(?, ?)";
    
    public static List selectPredioLibre(Transaccion trx, Integer g_fundo_id) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_PREDIO_LIBRE );
        pst.setObject(1, g_fundo_id);
        res = pst.executeQuery();
        while (res.next() ){
        	PredioLibre pl = new PredioLibre();
        	pl.setId(res.getInt("g_procedimiento_instancia_id"));
        	pl.setNombreFundo(res.getString("name"));
        	pl.setFecha_inicio(res.getTimestamp("fecha_inicio"));
        	pl.setFecha_termino(res.getTimestamp("fecha_termino"));
        	pl.setEstado(res.getString("estado"));
        	list.add(pl);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectAllDiio(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_ALL_DIIO );
        res = pst.executeQuery();
        while (res.next() ){
        	Ganado g = new Ganado();
        	g.setId(res.getInt("g_ganado_id"));
        	g.setDiio(res.getInt("diio"));
        	g.setEid(Long.toString(res.getLong("eid")));
        	g.setPredio(res.getInt("g_fundo_id"));
        	list.add(g);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectGanadoTuberculina(Transaccion trx, Integer instancia) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_GAN_TUBERCULINA );
        pst.setObject(1, instancia);
        res = pst.executeQuery();
        while (res.next() ){
        	InyeccionTB tb = new InyeccionTB();
        	tb.setGanadoID(res.getInt("g_ganado_id"));
        	tb.setFundoId(res.getInt("g_fundo_id"));
        	tb.setInstancia(res.getInt("g_gan_procedimiento_id"));
        	tb.setGanadoDiio(res.getInt("diio"));
        	tb.setTuboPPDSerie(res.getInt("serie"));
        	tb.setTuboPPDId(res.getInt("g_medicamento_control_id"));
        	tb.setSincronizado("Y");
        	list.add(tb);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectTuberculinaPPD(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_TUBERCULINA_PPD );
        res = pst.executeQuery();
        while (res.next() ){
        	PPD p = new PPD();
        	p.setId(res.getInt("g_medicamento_control_id"));
        	p.setSerie(res.getInt("serie"));
        	p.setLote(res.getInt("lote"));
        	p.setVencimiento(res.getDate("vencimiento"));
        	p.setCantidad(res.getInt("cantidad"));
        	list.add(p);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static void insertGanadoTuberculina(Transaccion trx, List<InyeccionTB> list) throws SQLException {
	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet res = null;
	
	    conn = trx.getConn();
	    pst = conn.prepareStatement( SQL_INSERT_GANADO_TUBERCULINA );
	    for (InyeccionTB tb : list){
	    	pst.setObject(1, tb.getUsuarioId());
	    	pst.setObject(2, tb.getInstancia());
	    	pst.setObject(3, tb.getGanadoID());
	    	pst.setObject(4, tb.getTuboPPDId());
	    	pst.setObject(5, Util.dateToSqlDate(tb.getFecha_dosis()));
	    	pst.executeQuery();
	    }

	    pst.close();
    }
    
    public static void insertPredioLibre(Transaccion trx, Integer p_usuario_id, Integer g_fundo_id) throws SQLException {
	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet res = null;
	
	    conn = trx.getConn();
	    pst = conn.prepareStatement( SQL_INSERT_PREDIO_LIBRE );
	    pst.setObject(1, p_usuario_id);
	    pst.setObject(2, g_fundo_id);
	    pst.executeQuery();

	    pst.close();
    }
	
}
