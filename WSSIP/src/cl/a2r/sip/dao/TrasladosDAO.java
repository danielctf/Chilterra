package cl.a2r.sip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.sip.mail.Correo;
import cl.a2r.sip.model.Camion;
import cl.a2r.sip.model.Chofer;
import cl.a2r.sip.model.DctoAdem;
import cl.a2r.sip.model.FMA;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Movimiento;
import cl.a2r.sip.model.Persona;
import cl.a2r.sip.model.Transportista;
import cl.a2r.sip.model.Traslado;

public class TrasladosDAO {

	private static final String SQL_SELECT_TRANSPORTISTAS = ""
			+ "select * from sip.ws_select_transportistas()";
	
	private static final String SQL_SELECT_CHOFER = ""
			+ "select * from sip.ws_select_chofer(?)";
	
	private static final String SQL_SELECT_CAMION = ""
			+ "select * from sip.ws_select_camion(?)";
	
	private static final String SQL_SELECT_ACOPLADO = ""
			+ "select * from sip.ws_select_acoplado(?)";
	
	private static final String SQL_SELECT_ARRIEROS = ""
			+ "select * from sip.ws_select_arrieros()";
	
	private static final String SQL_INSERTA_MOVIMIENTO = ""
			+ "select * from sip.ws_insert_movimiento(?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String SQL_INSERTA_MOVIMIENTO_GANADO = ""
			+ "select * from sip.ws_insert_movimiento_ganado(?, ?, ?)";
	
	public static final String SQL_GENERA_XML_TRASLADO = ""
			+ "select * from sip.ws_select_xml_traslado(?, ?, ?, ?)";
	
	private static final String SQL_CONFIRMA_MOVIMIENTO = ""
			+ "select * from sip.ws_ins_confirm_movto(?)";
	
	private static final String SQL_INSERTA_MOVTO_ADEM = ""
			+ "select * from sip.ws_ins_movto_adem(?)";
	
	private static final String SQL_SELECT_MOVTOS_EP = ""
			+ "select * from sip.ws_select_movimientos_ep()";
	
	private static final String SQL_SELECT_MOVTO = ""
			+ "select * from sip.ws_select_movimiento(?)";
	
	private static final String SQL_SELECT_MOVTO_GANADO = ""
			+ "select * from sip.ws_select_movimiento_ganado(?)";
	
	private static final String SQL_INSERTA_MOVTO_CONFIRM = ""
			+ "select * from sip.ws_insert_movimiento_confirmacion(?, ?)";
	
	private static final String SQL_INSERTA_MOVTO_CONFIRM_GANADO = ""
			+ "select * from sip.ws_insert_movimiento_confirmacion_ganado(?, ?, ?)";
	
	private static final String SQL_REUBICA_GANADO = ""
			+ "select * from sip.ws_reubica_ganado(?, ?, ?)";
	
	private static final String SQL_INSERTA_MOVTO_REUBICACION = ""
			+ "select * from sip.ws_insert_movto_reubicacion(?, ?)";
	
	//------------------------- TRASLADOS V2 ----------------------------
	
	private static final String SQL_SELECT_TRASLADO = ""
			+ "select * from sip.ws_traslado_select_traslado(?)";
	
	

    public static List selectTransportistas(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_TRANSPORTISTAS );
        res = pst.executeQuery();
        while (res.next() ){
        	Transportista t = new Transportista();
        	t.setId(res.getInt("g_transportista_id"));
        	t.setActiva(res.getString("isactive"));
        	t.setNombre(res.getString("name"));
            list.add(t);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectChofer(Transaccion trx, Integer transportistaId) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_CHOFER );
        pst.setObject(1, transportistaId);
        res = pst.executeQuery();
        
        while (res.next() ){
        	Chofer t = new Chofer();
        	t.setId(res.getInt("g_chofer_id"));
        	t.setActiva(res.getString("isactive"));
        	t.setNombre(res.getString("nombre"));
            list.add(t);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectCamion(Transaccion trx, Integer transportistaId) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_CAMION );
        pst.setObject(1, transportistaId);
        res = pst.executeQuery();
        
        while (res.next() ){
        	Camion t = new Camion();
        	t.setId(res.getInt("g_camion_id"));
        	t.setActiva(res.getString("isactive"));
        	t.setNombre(res.getString("patente"));
            list.add(t);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectAcoplado(Transaccion trx, Integer transportistaId) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_ACOPLADO );
        pst.setObject(1, transportistaId);
        res = pst.executeQuery();
        
        while (res.next() ){
        	Camion t = new Camion();
        	t.setId(res.getInt("g_camion_id"));
        	t.setActiva(res.getString("isactive"));
        	t.setNombre(res.getString("patente"));
            list.add(t);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectArrieros(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_ARRIEROS );

        res = pst.executeQuery();
        
        while (res.next() ){
        	Persona p = new Persona();
        	p.setId(res.getInt("g_usuario_id"));
        	p.setValue(res.getString("value"));
            list.add(p);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static Integer insertaMovimiento(Transaccion trx, Traslado traslado) throws SQLException {
        Integer g_movimiento_id = null;

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERTA_MOVIMIENTO );
        pst.setObject(1, traslado.getUsuarioId());
        pst.setObject(2, traslado.getFundoOrigenId());
        pst.setObject(3, traslado.getFundoDestinoId());
        pst.setObject(4, traslado.getTransportistaId());
        pst.setObject(5, traslado.getChoferId());
        pst.setObject(6, traslado.getCamionId());
        pst.setObject(7, traslado.getAcopladoId());
        pst.setObject(8, traslado.getDescripcion());
        res = pst.executeQuery();
        if (res.next() ){
        	g_movimiento_id = res.getInt(1);
        }
        
        pst = conn.prepareStatement( SQL_INSERTA_MOVIMIENTO_GANADO );
        for (Ganado g : traslado.getGanado()){
        	pst.setObject(1, traslado.getUsuarioId());
        	pst.setObject(2, g.getId());
        	pst.setObject(3, g_movimiento_id);
        	pst.executeQuery();
        }
        
        pst = conn.prepareStatement( SQL_CONFIRMA_MOVIMIENTO );
        pst.setObject(1, g_movimiento_id);
        pst.executeQuery();
        
        res.close();
        pst.close();

        return g_movimiento_id;
    }
    
    public static void generarXMLTraslado(Transaccion trx, FMA fma) throws SQLException {
        String strXML = null;
        String correoDestino = null;
        String fundoOrigen = null;
        String fundoDestino = null;
        String rupOrigen = null;
        String rupDestino = null;
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_GENERA_XML_TRASLADO );
        pst.setObject(1, fma.getUsuarioId());
        pst.setObject(2, fma.getG_movimiento_id());
        pst.setObject(3, fma.getFundoOrigenId());
        pst.setObject(4, fma.getFundoDestinoId());
        res = pst.executeQuery();
        
        if (res.next()){
        	strXML = res.getString("xmlmsg");
        	correoDestino = res.getString("mail");
        	fundoOrigen = res.getString("fundo_origen");
        	fundoDestino = res.getString("fundo_destino");
        	rupOrigen = res.getString("rup_origen");
        	rupDestino = res.getString("rup_destino");
        }
        
        //No se genera el FMA, debido a que el predio de origen y/o destino no esta registrado
        //en SIPEC o no tiene RUP asignado en SIP
        if (rupOrigen == null || rupDestino == null || (rupOrigen.equals(rupDestino))){
            res.close();
            pst.close();
            return;
        }
        
        byte[] byteXML = strXML.getBytes();
        
        Correo correo = new Correo();
        correo.setFrom("adempiere@chilterra.com");
        correo.setTo(correoDestino);
        correo.setSubject("FMA " + fma.getG_movimiento_id() + " - " + fundoOrigen + " - " + fundoDestino);
        correo.setBody("XML válido para generar un FMA en sitio web SIPEC" + "\n"
        		+ "http://sipecweb.sag.gob.cl/" + "\n"
        		+ "RUP " + fundoOrigen + ": " + rupOrigen + "\n"
        		+ "RUP " + fundoDestino + ": " + rupDestino + "\n");
        correo.addAdjunto(byteXML, "application/xml", "FMA " + fma.getG_movimiento_id() + ".xml" );
        correo.enviar();
        
        res.close();
        pst.close();

    }
    
    public static DctoAdem insertaMovtoAdem(Transaccion trx, Integer g_movimiento_id) throws SQLException {
    	DctoAdem d = new DctoAdem();
    	
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERTA_MOVTO_ADEM );
        pst.setObject(1, g_movimiento_id);
        res = pst.executeQuery();
        if (res.next()){
        	d.setIddocto(res.getInt("iddocto"));
        	d.setIdtipodocto(res.getInt("idtipodocto"));
        	d.setNrodocto(res.getString("nrodocto"));
        	d.setG_movimiento_id(g_movimiento_id);
        }
        
        res.close();
        pst.close();

        return d;
    }
    
    public static List selectMovimientosEP(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_MOVTOS_EP );
        
        res = pst.executeQuery();
        
        while (res.next() ){
        	Movimiento m = new Movimiento();
        	m.setG_movimiento_id(res.getInt("g_movimiento_id"));
        	m.setM_movement_id(res.getInt("m_movement_id"));
        	m.setNro_documento(res.getInt("nro_documento"));
        	m.setFundoOrigenId(res.getInt("g_fundo_origen_id"));
        	m.setFundoDestinoId(res.getInt("g_fundo_destino_id"));
        	m.setFundoOrigen(res.getString("fundo_origen"));
        	m.setFundoDestino(res.getString("fundo_destino"));
            list.add(m);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static Traslado selectMovimiento(Transaccion trx, Integer nro_documento) throws SQLException {
        Traslado t = new Traslado();
    
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_MOVTO );
        pst.setObject(1, nro_documento);
        res = pst.executeQuery();
        
        if (res.next() ){
        	t.setG_movimiento_id(res.getInt("g_movimiento_id"));
        	t.setM_movement_id(res.getInt("m_movement_id"));
        	t.setNro_documento(res.getInt("nro_documento"));
        	t.setFundoOrigenId(res.getInt("g_fundo_origen_id"));
        	t.setFundoDestinoId(res.getInt("g_fundo_destino_id"));
        }
        
        pst = conn.prepareStatement( SQL_SELECT_MOVTO_GANADO );
        pst.setObject(1, nro_documento);
        res = pst.executeQuery();
        
        while (res.next()){
        	Ganado g = new Ganado();
            g.setId(res.getInt("g_ganado_id"));
            g.setDiio(res.getInt("g_diio"));
            g.setActiva(res.getString("g_activa"));
            g.setPredio(res.getInt("g_predio"));
            g.setSexo(res.getString("sexo"));
            g.setTipoGanadoId(res.getInt("g_tipo_ganado_id"));
            t.getGanado().add(g);
        }
        
        res.close();
        pst.close();

        return t;
    }
    
    public static void insertaMovtoConfirm(Transaccion trx, Traslado traslado) throws SQLException {
    	
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERTA_MOVTO_CONFIRM );
        pst.setObject(1, traslado.getUsuarioId());
        pst.setObject(2, traslado.getG_movimiento_id());
        pst.executeQuery();

        pst = conn.prepareStatement( SQL_INSERTA_MOVTO_CONFIRM_GANADO );
        
        for (Ganado g : traslado.getGanado()){
            pst.setObject(1, traslado.getUsuarioId());
            pst.setObject(2, g.getId());
            pst.setObject(3, traslado.getG_movimiento_id());
            pst.executeQuery();
        }
        
        pst.close();

    }
    
    public static void reubicaGanado(Transaccion trx, Traslado traslado) throws SQLException {
    	
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_REUBICA_GANADO );
        
        for (Ganado g : traslado.getGanado()){
            pst.setObject(1, traslado.getUsuarioId());
            pst.setObject(2, g.getId());
            pst.setObject(3, traslado.getFundoDestinoId());
            pst.executeQuery();
        }
        
        pst.close();

    }
    
    public static Integer insertaMovtoReubica(Transaccion trx, Traslado traslado) throws SQLException {
    	Integer g_movimiento_id = null;
    	
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERTA_MOVTO_REUBICACION );
        pst.setObject(1, traslado.getUsuarioId());
        pst.setObject(2, traslado.getG_movimiento_id());
        res = pst.executeQuery();
        
        if (res.next()){
        	g_movimiento_id = res.getInt(1);
        }
        
        pst = conn.prepareStatement( SQL_INSERTA_MOVIMIENTO_GANADO );
        for (Ganado g : traslado.getGanado()){
        	pst.setObject(1, traslado.getUsuarioId());
        	pst.setObject(2, g.getId());
        	pst.setObject(3, g_movimiento_id);
        	pst.executeQuery();
        }
        
        pst = conn.prepareStatement( SQL_CONFIRMA_MOVIMIENTO );
        pst.setObject(1, g_movimiento_id);
        pst.executeQuery();
        
        res.close();
        pst.close();
        
        return g_movimiento_id;

    }
    
    //--------------------- TRASLADOS V2 ----------------------------
    
    public static List selectTraslados(Transaccion trx, Integer fundoId) throws SQLException {
    	List list = new ArrayList();
    	
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_TRASLADO );
        pst.setObject(1, fundoId);
        res = pst.executeQuery();
        while (res.next()){
        	
        }
        
        res.close();
        pst.close();

        return list;
    }
	
}
