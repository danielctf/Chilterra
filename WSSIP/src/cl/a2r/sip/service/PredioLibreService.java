package cl.a2r.sip.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.dao.PredioLibreDAO;
import cl.a2r.sip.dao.Transaccion;

public class PredioLibreService {

    public static List traePredioLibre(Integer g_fundo_id) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = PredioLibreDAO.selectPredioLibre(trx, g_fundo_id);
            } catch (SQLException ex) {
                AppLog.logSevere("PredioLibreService.traePredioLibre()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeAllDiio() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = PredioLibreDAO.selectAllDiio(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("PredioLibreService.traeAllDiio()", ex);
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
