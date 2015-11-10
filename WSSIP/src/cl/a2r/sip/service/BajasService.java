/*
 * Desarrollado por : Miguel Vega B.
 */

package cl.a2r.sip.service;

import cl.a2r.common.AppException;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.dao.BajasDAO;
import cl.a2r.sip.dao.Transaccion;
import cl.a2r.sip.model.Baja;

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
    
    public static void insertaBaja(Baja baja) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                BajasDAO.insertBaja(trx, baja);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("BajasService.insertaBaja()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
    }
    
    public static List traeBajas(Integer userId, Integer fundoId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = BajasDAO.selectBajas(trx, userId, fundoId);
            } catch (SQLException ex) {
                AppLog.logSevere("BajasService.traeBajas()", ex);
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
