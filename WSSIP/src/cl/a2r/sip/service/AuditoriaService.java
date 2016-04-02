package cl.a2r.sip.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.dao.AuditoriaDAO;
import cl.a2r.sip.dao.Transaccion;
import cl.a2r.sip.model.Auditoria;

public class AuditoriaService {

    public static void insertaAuditoria(Auditoria a, Integer usuarioId) throws AppException {
        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                AuditoriaDAO.insertAuditoria(trx, a, usuarioId);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
                AppLog.logSevere("AuditoriaService.insertaAuditoria()", ex);
                if (ex.getSQLState().equals("SIP04")){
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
	
    public static List traeAuditoria(Integer fundoId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = AuditoriaDAO.selectAuditoria(trx, fundoId);
            } catch (SQLException ex) {
                AppLog.logSevere("AuditoriaService.traeAuditoria()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static void cerrarAuditoria(Auditoria a, Integer usuarioId) throws AppException {
        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                AuditoriaDAO.cerrarAuditoria(trx, a, usuarioId);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
                AppLog.logSevere("AuditoriaService.cerrarAuditoria()", ex);
                if (ex.getSQLState().equals("SIP04")){
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
    
    public static void borrarAuditoria(Auditoria a, Integer usuarioId) throws AppException {
        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                AuditoriaDAO.borrarAuditoria(trx, a, usuarioId);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
                AppLog.logSevere("AuditoriaService.borrarAuditoria()", ex);
                if (ex.getSQLState().equals("SIP04")){
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
    
    public static void insertaGanado(List<Auditoria> auList, Integer usuarioId) throws AppException {
        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                AuditoriaDAO.insertGanado(trx, auList, usuarioId);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
                AppLog.logSevere("AuditoriaService.insertaGanado()", ex);
                if (ex.getSQLState().equals("SIP04")){
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
    
    public static Auditoria traeGanado(Integer instancia) throws AppException {
    	Auditoria a = new Auditoria();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                a = AuditoriaDAO.selectGanado(trx, instancia);
            } catch (SQLException ex) {
                AppLog.logSevere("AuditoriaService.traeGanado()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return a;
    }
    
}
