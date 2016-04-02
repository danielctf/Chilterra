package cl.a2r.sip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.sip.model.Bang;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Medicamento;
import cl.a2r.sip.model.MedicamentoControl;
import cl.a2r.sip.model.VRB51;

public class RB51DAO {

    private static final String SQL_SELECT_BANG = ""
            + "select * from sip.ws_rb51_select_bang()";
    
    private static final String SQL_SELECT_CANDIDATOS = ""
            + "select * from sip.ws_rb51_select_candidatos(?)";
    
    private static final String SQL_SELECT_GANADO_RB51 = ""
            + "select * from sip.ws_rb51_select_ganado()";
    
    private static final String SQL_SELECT_NUMERO_VACUNA = ""
            + "select * from sip.ws_rb51_select_numero_vacuna()";
    
    private static final String SQL_SELECT_MEDICAMENTO = ""
            + "select * from sip.ws_select_medicamento(?)";
    
    private static final String SQL_DELETE_BANG = ""
            + "select * from sip.ws_rb51_delete_bang(?, ?)";
    
    private static final String SQL_INSERT_RB51 = ""
            + "select * from sip.ws_rb51_insert_rb51(?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_SELECT_RB51_ANTERIOR = ""
    		+ "select * from sip.ws_rb51_select_ganado_anterior()";
    
    public static List selectBang(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_BANG );
        res = pst.executeQuery();
        while (res.next() ){
        	Bang b = new Bang();
        	b.setId(res.getInt("g_bang_id"));
        	b.setBang(res.getString("bang"));
        	b.setBorrar("N");
            list.add(b);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectCandidatos(Transaccion trx, Integer g_fundo_id) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_CANDIDATOS );
        pst.setObject(1, g_fundo_id);
        res = pst.executeQuery();
        while (res.next() ){
        	Ganado g = new Ganado();
        	g.setId(res.getInt("g_ganado_id"));
        	g.setDiio(res.getInt("diio"));
            list.add(g);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectGanadoRB51(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_GANADO_RB51 );
        res = pst.executeQuery();
        while (res.next() ){
        	VRB51 rb = new VRB51();
        	Bang b = new Bang();
        	Ganado g = new Ganado();
        	MedicamentoControl m = new MedicamentoControl();
        	g.setId(res.getInt("g_ganado_id"));
        	g.setDiio(res.getInt("diio"));
        	g.setPredio(res.getInt("g_fundo_id"));
        	b.setId(res.getInt("g_bang_id"));
        	b.setBang(res.getString("bang"));
        	m.setId(res.getInt("g_medicamento_control_id"));
        	m.setSerie(res.getInt("serie"));
        	rb.setFecha(res.getTimestamp("fecha_vacuna"));
        	rb.setBang(b);
        	rb.setGan(g);
        	rb.setMed(m);
        	rb.setSincronizado("Y");
            list.add(rb);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static Integer selectNumeroVacuna(Transaccion trx) throws SQLException {
        Integer numeroVacuna = null;

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_NUMERO_VACUNA );
        res = pst.executeQuery();
        while (res.next() ){
        	numeroVacuna = res.getInt("numero");
        }
        res.close();
        pst.close();

        return numeroVacuna;
    }
    
    public static List selectMedicamentos(Transaccion trx, Integer appId) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_MEDICAMENTO );
        pst.setObject(1, appId);
        res = pst.executeQuery();
        while (res.next() ){
        	MedicamentoControl m = new MedicamentoControl();
        	Medicamento med = new Medicamento();
        	med.setId(res.getInt("g_medicamento_id"));
        	med.setNombre(res.getString("name"));
        	m.setId(res.getInt("g_medicamento_control_id"));
        	m.setSerie(res.getInt("serie"));
        	m.setLote(res.getInt("lote"));
        	m.setCantidad(res.getInt("cantidad"));
        	m.setMed(med);
            list.add(m);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static void deleteBang(Transaccion trx, List<Bang> list, Integer usuarioId) throws SQLException {

        Connection conn = null;
        PreparedStatement pst = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_DELETE_BANG );
        for (Bang b : list){
        	pst.setObject(1, usuarioId);
        	pst.setObject(2, b.getId());
        	pst.executeQuery();
        }
        pst.close();
    }
    
    public static void insertRB51(Transaccion trx, List<VRB51> list, Integer usuarioId) throws SQLException {

        Connection conn = null;
        PreparedStatement pst = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERT_RB51 );
        for (VRB51 rb : list){
        	pst.setObject(1, usuarioId);
        	pst.setObject(2, rb.getGan().getPredio());
        	pst.setObject(3, rb.getGan().getId());
        	pst.setObject(4, new Timestamp(rb.getFecha().getTime()));
        	pst.setObject(5, rb.getBang().getId());
        	pst.setObject(6, rb.getMed().getId());
        	pst.executeQuery();
        }
        pst.close();
    }
    
    public static List selectGanadoRB51Anterior(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_RB51_ANTERIOR );
        res = pst.executeQuery();
        while (res.next() ){
        	Ganado g = new Ganado();
        	g.setId(res.getInt("g_ganado_id"));
            list.add(g);
        }
        res.close();
        pst.close();

        return list;
    }
	
}
