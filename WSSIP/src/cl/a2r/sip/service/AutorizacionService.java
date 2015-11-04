/*
 * Desarrollado por : Miguel Vega B.
 */

package cl.a2r.sip.service;

import cl.a2r.common.AppException;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.dao.AutorizacionDAO;
import cl.a2r.sip.dao.Transaccion;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miguelon
 */
public class AutorizacionService {

    public static List traeAplicaciones(Integer idUsuario) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = AutorizacionDAO.selectAplicaciones(trx, idUsuario);
            } catch (SQLException ex) {
                AppLog.logSevere("ProductosService.traeTransportistas()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traePredios() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = AutorizacionDAO.selectPredios(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("AutorizacionService.traePredios()", ex);
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
