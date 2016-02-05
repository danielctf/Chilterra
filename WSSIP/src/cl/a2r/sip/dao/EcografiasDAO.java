package cl.a2r.sip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.sip.model.EcografiaEstado;
import cl.a2r.sip.model.EcografiaNota;
import cl.a2r.sip.model.EcografiaProblema;
import cl.a2r.sip.model.Ecografista;
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
	
}
