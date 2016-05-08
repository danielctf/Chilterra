package cl.a2r.sip.service;

import java.sql.SQLException;

import cl.a2r.common.AppException;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.dao.InstanciasDAO;
import cl.a2r.sip.dao.Transaccion;
import cl.a2r.sip.model.Instancia;

public class InstanciasService {

    public static Integer insertaProc(Instancia instancia, Integer appId) throws AppException {
    	Integer superInstanciaId = null;
        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                superInstanciaId = InstanciasDAO.insertProc(trx, instancia, appId);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
                AppLog.logSevere("ProcedimientosService.insertaProc()", ex);
                if (ex.getSQLState().equals("SIP06")){
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
        return superInstanciaId;
    }
	
}
