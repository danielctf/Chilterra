package cl.a2r.sip.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.dao.PesajesDAO;
import cl.a2r.sip.dao.Transaccion;
import cl.a2r.sip.model.Pesaje;

public class PesajesService {
	
    public static void insertaPesaje(List<Pesaje> list, Integer usuarioId) throws AppException {
        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                PesajesDAO.insertPesaje(trx, list, usuarioId);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
                AppLog.logSevere("PesajesService.insertaPesaje()", ex);
                if (ex.getSQLState().equals("SIP04")){
                	throw new AppException(ex.getMessage(), null);
                } else {
                	throw new AppException("No se pudo recuperar los registros.", null);
                }
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
    }
    
    public static List traePesaje() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = PesajesDAO.selectPesaje(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("PesajesService.traePesaje()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
	
}
