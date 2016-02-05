package cl.a2r.sip.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.dao.EcografiasDAO;
import cl.a2r.sip.dao.Transaccion;

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
	
}
