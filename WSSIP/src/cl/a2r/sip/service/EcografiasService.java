package cl.a2r.sip.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.dao.EcografiasDAO;
import cl.a2r.sip.dao.PredioLibreDAO;
import cl.a2r.sip.dao.Transaccion;
import cl.a2r.sip.model.Brucelosis;
import cl.a2r.sip.model.Ecografia;

public class EcografiasService {

    public static List traeEcografistas() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = EcografiasDAO.selectEcografistas(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("EcografiasService.traeEcografistas()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeEcografiaEstado() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = EcografiasDAO.selectEcografiaEstado(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("EcografiasService.traeEcografiaEstado()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeEcografiaProblema() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = EcografiasDAO.selectEcografiaProblema(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("EcografiasService.traeEcografiaProblema()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeEcografiaNota() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = EcografiasDAO.selectEcografiaNota(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("EcografiasService.traeEcografiaNota()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeInseminaciones() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = EcografiasDAO.selectInseminaciones(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("EcografiasService.traeInseminaciones()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeEcografias() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = EcografiasDAO.selectEcografias(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("EcografiasService.traeEcografias()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static void insertaEcografia(List<Ecografia> ecoList, Integer usuarioId) throws AppException {

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                EcografiasDAO.insertEcografia(trx, ecoList, usuarioId);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("EcografiasService.insertaEcografia()", ex);
                if (ex.getSQLState().equals("SIP02")){
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
	
}
