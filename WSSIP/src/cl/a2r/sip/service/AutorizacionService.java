/*
 * Desarrollado por : Miguel Vega B.
 */

package cl.a2r.sip.service;

import cl.a2r.common.AppException;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.dao.AreteosDAO;
import cl.a2r.sip.dao.AutorizacionDAO;
import cl.a2r.sip.dao.Transaccion;
import cl.a2r.sip.model.Areteo;
import cl.a2r.sip.model.Sesion;

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
    
    public static Integer traeUsuario(String usuario) throws AppException {
        Integer id;

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                id = AutorizacionDAO.selectIdUsuario(trx, usuario);
            } catch (SQLException ex) {
                AppLog.logSevere("AutorizacionService.traeUsuario()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return id;
    }
    
    public static Integer traeVersionAndroid() throws AppException {
        Integer ver;

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                ver = AutorizacionDAO.selectVersionAndroid(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("AutorizacionService.traeVersionAndroid()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return ver;
    }
    
    public static Integer insertaSesion(Sesion sesion) throws AppException {
        Integer sesionId;

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
            	sesionId = AutorizacionDAO.insertaSesion(trx, sesion);
            	trx.commit();
            } catch (SQLException ex) {
            	trx.rollback();
                AppLog.logSevere("AutorizacionService.insertaSesion()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return sesionId;
    }
    
    public static void insertaX1Z1() throws AppException {

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
            	AutorizacionDAO.insertX1Z1(trx);
            	trx.commit();
            } catch (SQLException ex) {
            	trx.rollback();
                AppLog.logSevere("AutorizacionService.insertaX1Z1()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
    }

}
