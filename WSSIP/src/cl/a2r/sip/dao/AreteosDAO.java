package cl.a2r.sip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.sip.model.Areteo;
import cl.a2r.sip.model.CollarParto;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.GanadoLogs;
import cl.a2r.sip.model.Parto;
import cl.a2r.sip.model.Raza;
import cl.a2r.sip.model.TipoGanado;

public class AreteosDAO {

	private static final String SQL_SELECT_RAZA = ""
			+ "select * from sip.ws_select_raza()";
	
	private static final String SQL_SELECT_COLLAR_ARETEO = ""
			+ "select * from sip.ws_select_collar_areteo(?)";
	
	private static final String SQL_SELECT_TIPO_GANADO = ""
			+ "select * from sip.ws_select_tipo_ganado()";
	
	private static final String SQL_SELECT_ARETEOS_ENCONTRADOS = ""
			+ "select * from sip.ws_select_areteos_ce(?)";
	
	private static final String SQL_SELECT_ARETEOS_FALTANTES = ""
			+ "select * from sip.ws_select_areteos_cf(?)";
	
	private static final String SQL_LIBERA_COLLAR = ""
			+ "select * from sip.ws_insert_areteo_libera_collar(?, ?)";
	
	private static final String SQL_INSERT_ARETEO_ALTA = ""
			+ "select * from sip.ws_insert_areteo_alta(?, ?, ?, ?, ?)";
	
	private static final String SQL_INSERT_ARETEO_APARICION = ""
			+ "select * from sip.ws_insert_areteo_aparicion(?, ?, ?, ?, ?, ?)";
	
	private static final String SQL_INSERT_ARETEO_CAMBIO_DIIO = ""
			+ "select * from sip.ws_insert_areteo_cambio_diio(?, ?, ?, ?)";
	
	private static final String SQL_SELECT_APARICIONES = ""
			+ "select * from sip.ws_select_apariciones(?, ?)";
	
	private static final String SQL_SELECT_CAMBIO_DIIO = ""
			+ "select * from sip.ws_select_cambio_diio(?, ?)";
	
    public static List selectRaza(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_RAZA );
        res = pst.executeQuery();
        while (res.next() ){
            Raza r = new Raza();
            r.setId(res.getInt("g_raza_id"));
            r.setActiva(res.getString("isactive"));
            r.setCodigo(res.getString("value"));
            r.setNombre(res.getString("name"));
            list.add(r);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectCollarAreteo(Transaccion trx, Integer fundoId) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_COLLAR_ARETEO );
        pst.setObject(1, fundoId);
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
    
    public static List selectTipoGanado(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_TIPO_GANADO );
        
        res = pst.executeQuery();
        while (res.next() ){
        	TipoGanado c = new TipoGanado();
        	c.setId(res.getInt("g_tipo_ganado_id"));
        	c.setActiva(res.getString("isactive"));
        	c.setCodigo(res.getString("value"));
        	c.setNombre(res.getString("name"));
            list.add(c);
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
        pst = conn.prepareStatement( SQL_SELECT_ARETEOS_ENCONTRADOS );
        pst.setObject(1, fundoId);
        res = pst.executeQuery();
        
        while (res.next() ){
            Areteo a = new Areteo();
            a.setCollarNombre(res.getString("name"));
            a.setDiio(res.getInt("diio"));
            list.add(a);
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
        pst = conn.prepareStatement( SQL_SELECT_COLLAR_ARETEO );
        pst.setObject(1, fundoId);
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
    
    public static void liberaCollar(Transaccion trx, Integer collarId, Integer usuarioId) throws SQLException {
	    List list = new ArrayList();
	
	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet res = null;
	
	    conn = trx.getConn();
	    pst = conn.prepareStatement( SQL_LIBERA_COLLAR );
	    pst.setObject(1, collarId);
	    pst.setObject(2, usuarioId);
	    pst.executeQuery();

	    pst.close();
    }
    
    public static void insertaAreteoAlta(Transaccion trx, Areteo alta) throws SQLException {
	    List list = new ArrayList();
	
	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet res = null;
	
	    conn = trx.getConn();
	    pst = conn.prepareStatement( SQL_INSERT_ARETEO_ALTA );
	    pst.setObject(1, alta.getUserId());
	    pst.setObject(2, alta.getDiio());
	    pst.setObject(3, alta.getCollarId());
	    pst.setObject(4, alta.getSexo());
	    pst.setObject(5, alta.getRazaId());
	    pst.executeQuery();

	    pst.close();
    }
    
    public static void insertaAreteoAparicion(Transaccion trx, Areteo aparicion) throws SQLException {
	    List list = new ArrayList();
	
	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet res = null;
	
	    conn = trx.getConn();
	    pst = conn.prepareStatement( SQL_INSERT_ARETEO_APARICION );
	    pst.setObject(1, aparicion.getUserId());
	    pst.setObject(2, aparicion.getPredioId());
	    pst.setObject(3, aparicion.getDiio());
	    pst.setObject(4, aparicion.getEdadEnMeses());
	    pst.setObject(5, aparicion.getSexo());
	    pst.setObject(6, aparicion.getRazaId());
	    pst.executeQuery();

	    pst.close();
    }
    
    public static void insertaAreteoCambioDiio(Transaccion trx, Areteo cambiodiio) throws SQLException {
	    List list = new ArrayList();
	
	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet res = null;
	
	    conn = trx.getConn();
	    pst = conn.prepareStatement( SQL_INSERT_ARETEO_CAMBIO_DIIO );
	    pst.setObject(1, cambiodiio.getUserId());
	    pst.setObject(2, cambiodiio.getGanadoId());
	    pst.setObject(3, cambiodiio.getDiio());
	    pst.setObject(4, cambiodiio.getDiioAnterior());
	    pst.executeQuery();

	    pst.close();
    }
    
    public static List selectApariciones(Transaccion trx, Integer usuarioId, Integer fundoId) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_APARICIONES );
        pst.setObject(1, usuarioId);
        pst.setObject(2, fundoId);
        res = pst.executeQuery();
        
        while (res.next() ){
        	GanadoLogs gl = new GanadoLogs();
        	gl.setGanadoId(res.getInt("g_ganado_id"));
        	gl.setDiio(res.getInt("diio"));
        	gl.setLogId(res.getInt("g_ganado_id"));
            list.add(gl);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectCambioDiio(Transaccion trx, Integer usuarioId, Integer fundoId) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_CAMBIO_DIIO );
        pst.setObject(1, usuarioId);
        pst.setObject(2, fundoId);
        res = pst.executeQuery();
        
        while (res.next() ){
        	GanadoLogs gl = new GanadoLogs();
        	gl.setGanadoId(res.getInt("g_ganado_id"));
        	gl.setDiio(res.getInt("diio"));
        	gl.setLogId(res.getInt("g_gan_diio_id"));
            list.add(gl);
        }
        res.close();
        pst.close();

        return list;
    }

}
