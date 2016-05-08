package cl.a2r.sip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.sip.mail.Correo;
import cl.a2r.sip.model.Camion;
import cl.a2r.sip.model.Chofer;
import cl.a2r.sip.model.DctoAdem;
import cl.a2r.sip.model.FMA;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Instancia;
import cl.a2r.sip.model.Movimiento;
import cl.a2r.sip.model.Persona;
import cl.a2r.sip.model.Predio;
import cl.a2r.sip.model.Transportista;
import cl.a2r.sip.model.Traslado;
import cl.a2r.sip.model.TrasladoV2;
import cl.a2r.sip.service.InstanciasService;

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
	
	private static final String SQL_SELECT_TRASLADOS = ""
			+ "select * from sip.ws_traslado_select_traslados(?)";
	
	private static final String SQL_DELETE_TRASLADO = ""
			+ "select * from sip.ws_traslado_delete_traslado(?, ?)";
	
	private static final String SQL_SELECT_CHOFERV2 = ""
			+ "select * from sip.ws_traslado_select_chofer()";
	
	private static final String SQL_SELECT_CAMIONV2 = ""
			+ "select * from sip.ws_traslado_select_camion()";
	
	private static final String SQL_SELECT_ACOPLADOV2 = ""
			+ "select * from sip.ws_traslado_select_acoplado()";
	
	private static final String SQL_INSERT_TRASLADO = ""
			+ "select * from sip.ws_traslado_insert_traslado(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String SQL_INSERT_GANADO_SALIDA = ""
			+ "select * from sip.ws_traslado_insert_ganado_salida(?, ?, ?)";
	
	private static final String SQL_INSERT_MOVTO_ADEM = ""
			+ "select * from sip.ws_traslado_insert_movto_adem(?)";
	
	private static final String SQL_SELECT_FMA_XML = ""
			+ "select * from sip.ws_traslado_fma_xml(?, ?, ?, ?)";
	
	private static final String SQL_SELECT_TRASLADO = ""
			+ "select * from sip.ws_traslado_select_traslado(?)";
	
	private static final String SQL_INSERT_GANADO_ENTRADA = ""
			+ "select * from sip.ws_traslado_insert_ganado_entrada(?, ?, ?)";
	
	private static final String SQL_INSERT_CONFIRM = ""
			+ "select * from sip.ws_traslado_insert_confirm(?, ?)";
	
	private static final String SQL_REUBICA_GANADOV2 = ""
			+ "select * from sip.ws_traslado_reubica_ganado(?, ?)";
	
	private static final String SQL_VERIFICA_CONFIRM = ""
			+ "select * from sip.ws_traslado_verifica_confirm(?, ?)";
	
	private static final String SQL_REUBICACION_INSERT_GANADO = ""
			+ "select * from sip.ws_reubicacion_insert_ganado(?, ?, ?)";
	
	private static final String SQL_REUBICACION_COMPLETA_REUBICACION = ""
			+ "select * from sip.ws_reubicacion_completa_reubicacion(?, ?)";

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
        pst = conn.prepareStatement( SQL_SELECT_TRASLADOS );
        pst.setObject(1, fundoId);
        res = pst.executeQuery();
        while (res.next()){
        	Instancia superInstancia = new Instancia();
        	superInstancia.setId(res.getInt("g_superprocedimiento_instancia_id"));
        	superInstancia.setFundoId(res.getInt("g_fundo_instancia_id"));
        	Instancia instancia = new Instancia();
        	instancia.setId(res.getInt("g_procedimiento_instancia_id"));
        	instancia.setEstado(res.getString("estado_instancia"));
        	instancia.setUsuarioId(res.getInt("usuario"));
        	TrasladoV2 traslado = new TrasladoV2();
        	traslado.setId(res.getInt("g_procedimiento_instancia_movto_salida_id"));
        	traslado.setNro_documento(res.getInt("nro_documento"));
        	traslado.setFecha(res.getTimestamp("fecha_movimiento"));
        	Predio origen = new Predio();
        	origen.setId(res.getInt("g_fundo_origen_id"));
        	origen.setNombre(res.getString("fundo_origen_name"));
        	Predio destino = new Predio();
        	destino.setId(res.getInt("g_fundo_destino_id"));
        	destino.setNombre(res.getString("fundo_destino_name"));
        	traslado.setDescription(res.getString("description"));
        	
        	traslado.setOrigen(origen);
        	traslado.setDestino(destino);
        	instancia.setTraslado(traslado);
        	superInstancia.setInstancia(instancia);
        	list.add(superInstancia);
        }
        
        res.close();
        pst.close();

        return list;
    }
    
    public static void borrarTraslado(Transaccion trx, Instancia instancia) throws SQLException {

        Connection conn = null;
        PreparedStatement pst = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_DELETE_TRASLADO );
        pst.setObject(1, instancia.getId());
        pst.setObject(2, instancia.getUsuarioId());
        pst.executeQuery();
        pst.close();
    }
    
    public static List selectChofer(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_CHOFERV2 );
        res = pst.executeQuery();
        
        while (res.next() ){
        	Chofer t = new Chofer();
        	t.setId(res.getInt("g_chofer_id"));
        	t.setActiva(res.getString("isactive"));
        	t.setNombre(res.getString("nombre"));
        	t.setTransportistaId(res.getInt("g_transportista_id"));
            list.add(t);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectCamion(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_CAMIONV2 );
        res = pst.executeQuery();
        
        while (res.next() ){
        	Camion t = new Camion();
        	t.setId(res.getInt("g_camion_id"));
        	t.setActiva(res.getString("isactive"));
        	t.setNombre(res.getString("patente"));
        	t.setTransportistaId(res.getInt("g_transportista_id"));
            list.add(t);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static List selectAcoplado(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_ACOPLADOV2 );
        res = pst.executeQuery();
        
        while (res.next() ){
        	Camion t = new Camion();
        	t.setId(res.getInt("g_camion_id"));
        	t.setActiva(res.getString("isactive"));
        	t.setNombre(res.getString("patente"));
        	t.setTransportistaId(res.getInt("g_transportista_id"));
            list.add(t);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static Integer insertTraslado(Transaccion trx, Instancia superInstancia) throws SQLException {

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;
        
        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERT_TRASLADO );
        pst.setObject(1, superInstancia.getUsuarioId());
        pst.setObject(2, superInstancia.getInstancia().getTraslado().getOrigen().getId());
        pst.setObject(3, superInstancia.getInstancia().getTraslado().getDestino().getId());
        pst.setObject(4, superInstancia.getInstancia().getTraslado().getDescription());
        pst.setObject(5, superInstancia.getInstancia().getTraslado().getTransportistaId());
        pst.setObject(6, superInstancia.getInstancia().getTraslado().getChoferId());
        pst.setObject(7, superInstancia.getInstancia().getTraslado().getCamionId());
        pst.setObject(8, superInstancia.getInstancia().getTraslado().getAcopladoId());
        pst.setObject(9, superInstancia.getId());
        res = pst.executeQuery();
        
        Integer g_procedimiento_instancia_movto_salida_id = null;
        if (res.next()){
        	g_procedimiento_instancia_movto_salida_id = res.getInt("l_procedimiento_instancia_movto_salida_id");
        }
        
        pst = conn.prepareStatement( SQL_INSERT_GANADO_SALIDA );
        for (Ganado g : superInstancia.getInstancia().getGanList()){
            pst.setObject(1, superInstancia.getUsuarioId());
            pst.setObject(2, g.getId());
            pst.setObject(3, superInstancia.getId());
            pst.executeQuery();
        }

        res.close();
        pst.close();
        
        return g_procedimiento_instancia_movto_salida_id;
    }
    
    public static DctoAdem insertMovtoAdem(Transaccion trx, Integer g_procedimiento_instancia_movto_salida_id) throws SQLException {
    	DctoAdem d = new DctoAdem();
    	
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERT_MOVTO_ADEM );
        pst.setObject(1, g_procedimiento_instancia_movto_salida_id);
        res = pst.executeQuery();
        if (res.next()){
        	d.setIddocto(res.getInt("iddocto"));
        	d.setIdtipodocto(res.getInt("idtipodocto"));
        	d.setNrodocto(res.getString("nrodocto"));
        	d.setG_movimiento_id(g_procedimiento_instancia_movto_salida_id);
        }
        
        res.close();
        pst.close();

        return d;
    }
    
    public static void selectFmaXml(Transaccion trx, FMA fma) throws SQLException {
        String strXML = null;
        String correoDestino = null;
        String fundoOrigen = null;
        String fundoDestino = null;
        String rupOrigen = null;
        String rupDestino = null;
        Integer nro_documento = null;
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_FMA_XML );
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
        	nro_documento = res.getInt("nro_documento");
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
        correo.setSubject("FMA " + nro_documento + " - " + fundoOrigen + " - " + fundoDestino);
        correo.setBody("XML válido para generar un FMA en sitio web SIPEC" + "\n"
        		+ "http://sipecweb.sag.gob.cl/" + "\n"
        		+ "RUP " + fundoOrigen + ": " + rupOrigen + "\n"
        		+ "RUP " + fundoDestino + ": " + rupDestino + "\n");
        correo.addAdjunto(byteXML, "application/xml", "FMA " + nro_documento + ".xml" );
        correo.enviar();
        
        res.close();
        pst.close();

    }
    
    public static List selectTraslado(Transaccion trx, Integer g_superprocedimiento_instancia_id) throws SQLException {
    	List list = new ArrayList();
    	
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_TRASLADO );
        pst.setObject(1, g_superprocedimiento_instancia_id);
        res = pst.executeQuery();
        while (res.next()){
        	Ganado g = new Ganado();
        	g.setId(res.getInt("g_ganado_id"));
        	g.setDiio(res.getInt("diio"));
        	g.setTipoGanadoId(res.getInt("g_tipo_ganado_id"));
        	list.add(g);
        }
        
        res.close();
        pst.close();

        return list;
    }
    
    public static void insertConfirm(Transaccion trx, Instancia superInstancia) throws SQLException {
    	
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERT_GANADO_ENTRADA );
        for (Ganado g : superInstancia.getInstancia().getGanList()){
        	pst.setObject(1, superInstancia.getUsuarioId());
        	pst.setObject(2, g.getId());
        	pst.setObject(3, superInstancia.getId());
        	pst.executeQuery();
        }

        pst = conn.prepareStatement( SQL_INSERT_CONFIRM );
        pst.setObject(1, superInstancia.getUsuarioId());
        pst.setObject(2, superInstancia.getId());
        pst.executeQuery();
        
        pst = conn.prepareStatement( SQL_REUBICA_GANADOV2 );
        pst.setObject(1, superInstancia.getUsuarioId());
        pst.setObject(2, superInstancia.getId());
        pst.executeQuery();
        
        //Verificar si en la confirmacion vienen mas animales que en la salida
        pst = conn.prepareStatement( SQL_VERIFICA_CONFIRM );
        pst.setObject(1, superInstancia.getUsuarioId());
        pst.setObject(2, superInstancia.getId());
        pst.executeQuery();
        
        pst.close();

    }
    
    public static void insertReubicacion(Transaccion trx, List<Instancia> instList) throws SQLException, AppException {
    	
        Connection conn = null;
        PreparedStatement pst = null;

        conn = trx.getConn();
        
        for (Instancia i : instList){
        	Integer superInstanciaId = InstanciasService.insertaProc(i, 1005050);
        	pst = conn.prepareStatement( SQL_REUBICACION_INSERT_GANADO );
        	for (Ganado g : i.getGanList()){
        		pst.setObject(1, i.getUsuarioId());
        		pst.setObject(2, g.getId());
        		pst.setObject(3, superInstanciaId);
        		pst.executeQuery();
        	}
        	pst = conn.prepareStatement( SQL_REUBICACION_COMPLETA_REUBICACION );
        	pst.setObject(1, i.getUsuarioId());
        	pst.setObject(2, superInstanciaId);
        	pst.executeQuery();
        }
        
        //pst.close();

    }
	
}
