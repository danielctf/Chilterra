package cl.a2r.sip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.sip.model.EstadoLeche;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Medicamento;
import cl.a2r.sip.model.MedicamentoControl;
import cl.a2r.sip.model.Secado;

public class SecadosDAO {
	
    private static final String SQL_SELECT_MEDICAMENTO = ""
            + "select * from sip.ws_select_medicamento(?)";

    private static final String SQL_SELECT_ESTADO_LECHE = ""
            + "select * from sip.ws_select_estado_leche()";
    
    private static final String SQL_INSERT_ESTADO_LECHE = ""
            + "select * from sip.ws_insert_estado_leche(?, ?, ?, ?, ?)";
    
    private static final String SQL_SELECT_ALL_DIIO = ""
            + "select * from sip.ws_select_all_diio()";
    
    public static List selectMedicamento(Transaccion trx, Integer appId) throws SQLException {
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
        	m.setId(res.getInt("g_medicamento_control_id"));
        	m.setSerie(res.getInt("serie"));
        	m.setLote(res.getInt("lote"));
        	m.setLote(res.getInt("cantidad"));
        	m.getMed().setNombre(res.getString("name"));
        	m.getMed().setId(res.getInt("g_medicamento_id"));
            list.add(m);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectEstadoLeche(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_ESTADO_LECHE );
        res = pst.executeQuery();
        while (res.next() ){
        	EstadoLeche e = new EstadoLeche();
        	e.setId(res.getInt("g_estado_leche_id"));
        	e.setCodigo(res.getString("value"));
        	e.setNombre(res.getString("name"));
        	list.add(e);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static void insertEstadoLeche(Transaccion trx, List<Secado> secList, Integer usuarioId) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERT_ESTADO_LECHE );
        for (Secado s : secList){
        	pst.setObject(1, usuarioId);
        	pst.setObject(2, s.getGan().getPredio());
        	pst.setObject(3, s.getGan().getId());
        	pst.setObject(4, s.getGan().getEstadoLecheId());
        	pst.setObject(5, s.getMed().getId());
        	pst.executeQuery();
        }
        pst.close();
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
        	g.setEstadoLecheId(res.getInt("g_estado_leche_id"));
            list.add(g);
        }
        res.close();
        pst.close();

        return list;
    }
    
}
