/*
 * Desarrollado por : Miguel Vega B.
 */

package cl.a2r.sip.service;

import cl.a2r.common.AppException;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.dao.BajasDAO;
import cl.a2r.sip.dao.Transaccion;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miguelon
 */
public class BajasService {

    public static List traeMotivos() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = BajasDAO.selectMotivoBaja(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("BajasService.traeMotivos()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }

    public static List traeCausas() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = BajasDAO.selectCausaBaja(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("BajasService.traeCausas()", ex);
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
