package cl.a2r.sip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Pesaje;

public class PesajesDAO {
	
    private static final String SQL_SELECT_PESAJE = ""
            + "select * from sip.ws_pesaje_select_pesaje()";
    
    private static final String SQL_INSERT_PESAJE = ""
            + "select * from sip.ws_pesaje_insert_pesaje(?, ?, ?, ?, ?)";

    public static List selectPesaje(Transaccion trx) throws SQLException {
        List list = new ArrayList();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet res = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_SELECT_PESAJE );
        res = pst.executeQuery();
        while (res.next() ){
        	Pesaje p = new Pesaje();
        	Ganado g = new Ganado();
        	g.setId(res.getInt("g_ganado_id"));
        	g.setDiio(res.getInt("diio"));
        	g.setPredio(res.getInt("g_fundo_id"));
        	p.setFecha(res.getTimestamp("fecha_pesaje"));
        	p.setPeso(res.getDouble("peso"));
        	p.setSincronizado("Y");
        	p.setGan(g);
            list.add(p);
        }
        res.close();
        pst.close();

        return list;
    }
    
    public static void insertPesaje(Transaccion trx, List<Pesaje> list, Integer usuarioId) throws SQLException {

        Connection conn = null;
        PreparedStatement pst = null;

        conn = trx.getConn();
        pst = conn.prepareStatement( SQL_INSERT_PESAJE );
        for (Pesaje p : list){
        	pst.setObject(1, usuarioId);
        	pst.setObject(2, p.getGan().getId());
        	pst.setObject(3, p.getGan().getPredio());
        	pst.setObject(4, new Timestamp(p.getFecha().getTime()));
        	pst.setObject(5, p.getPeso());
        	pst.executeQuery();
        }
        pst.close();
    }
    
}
