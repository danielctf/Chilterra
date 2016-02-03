package cl.a2r.sap.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.sap.common.AppLog;
import cl.a2r.sap.dao.AutorizacionDAO;
import cl.a2r.sap.dao.MedicionDAO;
import cl.a2r.sap.dao.Transaccion;
import cl.a2r.sap.model.Calificacion;
import cl.a2r.sap.model.Medicion;

public class MedicionService {
	
    public static void insertaMedicion(Medicion med) throws AppException {
        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                MedicionDAO.insertaMedicion(trx, med);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("MedicionService.insertaMedicion()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
    }
    
    public static List traeStock() throws AppException {
        List<Medicion> list = new ArrayList<Medicion>();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
            	list = MedicionDAO.selectStock(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("MedicionService.traeStock()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static void insertaCalificacion(List<Calificacion> calList, Integer g_usuario_id) throws AppException {
        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                MedicionDAO.insertaCalificacion(trx, calList, g_usuario_id);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("MedicionService.insertaCalificacion()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
    }
    
    public static List traeCalificacion() throws AppException {
        List<Medicion> list = new ArrayList<Medicion>();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
            	list = MedicionDAO.selectCalificacion(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("MedicionService.traeCalificacion()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traePotreros() throws AppException {
        List<Medicion> list = new ArrayList<Medicion>();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
            	list = MedicionDAO.selectPotreros(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("MedicionService.traePotreros()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeTipoMedicion() throws AppException {
        List<Medicion> list = new ArrayList<Medicion>();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
            	list = MedicionDAO.selectTipoMedicion(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("MedicionService.traeTipoMedicion()", ex);
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
