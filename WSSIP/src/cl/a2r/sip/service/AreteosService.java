package cl.a2r.sip.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.dao.AreteosDAO;
import cl.a2r.sip.dao.BajasDAO;
import cl.a2r.sip.dao.PartosDAO;
import cl.a2r.sip.dao.Transaccion;
import cl.a2r.sip.model.Areteo;
import cl.a2r.sip.model.Parto;

public class AreteosService {

    public static List traeRaza() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = AreteosDAO.selectRaza(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("AreteosService.traeRaza()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeCollarAreteo(Integer fundoId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = AreteosDAO.selectCollarAreteo(trx, fundoId);
            } catch (SQLException ex) {
                AppLog.logSevere("AreteosService.traeCollarAreteo()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeTipoGanado() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = AreteosDAO.selectTipoGanado(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("AreteosService.traeTipoGanado()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeAreteosEncontrados(Integer fundoId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = AreteosDAO.selectCandidatosEncontrados(trx, fundoId);
            } catch (SQLException ex) {
                AppLog.logSevere("AreteosService.traeAreteosEncontrados()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeAreteosFaltantes(Integer fundoId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = AreteosDAO.selectCandidatosFaltantes(trx, fundoId);
            } catch (SQLException ex) {
                AppLog.logSevere("AreteosService.traeAreteosFaltantes()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
	
    public static void liberaCollar(Integer collarId, Integer usuarioId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                AreteosDAO.liberaCollar(trx, collarId, usuarioId);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("AreteosService.liberaCollar()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
    }
    
    public static void insertaAreteoAlta(Areteo alta) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                AreteosDAO.insertaAreteoAlta(trx, alta);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("AreteosService.insertaAreteoAlta()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
    }
    
    public static void insertaAreteoAparicion(Areteo aparicion) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                AreteosDAO.insertaAreteoAparicion(trx, aparicion);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("AreteosService.insertaAreteoAparicion()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
    }
    
    public static void insertaAreteoCambioDiio(Areteo cambiodiio) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                AreteosDAO.insertaAreteoCambioDiio(trx, cambiodiio);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("AreteosService.insertaAreteoCambioDiio()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
    }
    
    public static List traeApariciones(Integer usuarioId, Integer fundoId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = AreteosDAO.selectApariciones(trx, usuarioId, fundoId);
            } catch (SQLException ex) {
                AppLog.logSevere("AreteosService.traeApariciones()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeCambioDiio(Integer usuarioId, Integer fundoId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = AreteosDAO.selectCambioDiio(trx, usuarioId, fundoId);
            } catch (SQLException ex) {
                AppLog.logSevere("AreteosService.traeCambioDiio()", ex);
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
