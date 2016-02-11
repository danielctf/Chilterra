package cl.a2r.sip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.sip.common.Util;
import cl.a2r.sip.model.Ecografia;
import cl.a2r.sip.model.EcografiaEstado;
import cl.a2r.sip.model.EcografiaNota;
import cl.a2r.sip.model.EcografiaProblema;
import cl.a2r.sip.model.Ecografista;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Inseminacion;
import cl.a2r.sip.model.Raza;

public class EcografiasDAO {

	private static final String SQL_SELECT_ECOGRAFISTAS = ""
			+ "select * from sip.ws_select_ecografistas()";
	
	private static final String SQL_SELECT_ECOGRAFIA_ESTADO = ""
			+ "select * from sip.ws_select_ecografia_estado()";
	
	private static final String SQL_SELECT_ECOGRAFIA_PROBLEMA = ""
			+ "select * from sip.ws_select_ecografia_problema()";
	
	private static final String SQL_SELECT_ECOGRAFIA_NOTA = ""
			+ "select * from sip.ws_select_ecografia_nota()";
	
	private static final String SQL_SELECT_INSEMINACIONES = ""
			+ "select * from sip.ws_select_eco_inseminaciones()";
	
	private static final String SQL_SELECT_ECOGRAFIAS = ""
			+ "select * from sip.ws_select_ecografias()";
	
	private static final String SQL_INSERT_ECOGRAFIA = ""
			+ "select * from sip.ws_insert_ecografia(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
    public static List selectEcografistas(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_ECOGRAFISTAS );
        res = pst.executeQuery();
        while (res.next() ){
        	Ecografista e = new Ecografista();
        	e.setId(res.getInt("g_ecografista_id"));
        	e.setCodigo(res.getString("value"));
        	e.setNombre(res.getString("nombre"));
        	e.setAlias(res.getString("alias"));
        	list.add(e);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectEcografiaEstado(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_ECOGRAFIA_ESTADO );
        res = pst.executeQuery();
        while (res.next() ){
        	EcografiaEstado e = new EcografiaEstado();
        	e.setId(res.getInt("g_ecografia_estado_id"));
        	e.setCodigo(res.getString("value"));
        	e.setNombre(res.getString("name"));
        	list.add(e);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectEcografiaProblema(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_ECOGRAFIA_PROBLEMA );
        res = pst.executeQuery();
        while (res.next() ){
        	EcografiaProblema e = new EcografiaProblema();
        	e.setId(res.getInt("g_ecografia_problema_id"));
        	e.setCodigo(res.getString("value"));
        	e.setNombre(res.getString("name"));
        	list.add(e);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectEcografiaNota(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_ECOGRAFIA_NOTA );
        res = pst.executeQuery();
        while (res.next() ){
        	EcografiaNota e = new EcografiaNota();
        	e.setId(res.getInt("g_ecografia_nota_id"));
        	e.setCodigo(res.getString("value"));
        	e.setNombre(res.getString("name"));
        	list.add(e);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectInseminaciones(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_INSEMINACIONES );
        res = pst.executeQuery();
        while (res.next() ){
        	Inseminacion i = new Inseminacion();
        	i.setId(res.getInt("g_procedimiento_inseminacion_id"));
        	i.setFecha(res.getTimestamp("fecha_inseminacion"));
        	i.getGan().setId(res.getInt("g_ganado_id"));
        	i.setSincronizado("Y");
        	list.add(i);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectEcografias(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_ECOGRAFIAS );
        res = pst.executeQuery();
        while (res.next() ){
        	Ecografia e = new Ecografia();
        	//e.setId(res.getInt("g_procedimiento_ecografia_id"));
        	e.setFecha(res.getTimestamp("fecha_ecografia"));
        	e.getGan().setId(res.getInt("g_ganado_id"));
        	e.setDias_prenez(res.getInt("dias_prenez"));
        	e.setInseminacionId(res.getInt("g_procedimiento_inseminacion_id"));
        	e.setSincronizado("Y");
        	list.add(e);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static void insertEcografia(Transaccion trx, List<Ecografia> ecoList, Integer usuarioId) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERT_ECOGRAFIA );
        
        for (Ecografia e : ecoList){
        	pst.setObject(1, usuarioId);
        	pst.setObject(2, e.getGan().getId());
        	pst.setObject(3, e.getGan().getPredio());
        	pst.setObject(4, Util.dateToSqlDate(e.getFecha()));
        	pst.setObject(5, e.getDias_prenez());
        	pst.setObject(6, e.getEcografistaId());
        	pst.setObject(7, e.getInseminacionId());
        	pst.setObject(8, e.getEstadoId());
        	pst.setObject(9, e.getProblemaId());
        	pst.setObject(10, e.getNotaId());
        	pst.executeQuery();	
        }

        pst.close();

    }
	
}
