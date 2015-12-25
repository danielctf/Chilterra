package cl.a2r.sip.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.dao.BajasDAO;
import cl.a2r.sip.dao.GanadoDAO;
import cl.a2r.sip.dao.PartosDAO;
import cl.a2r.sip.dao.Transaccion;
import cl.a2r.sip.model.Baja;
import cl.a2r.sip.model.GanadoLogs;
import cl.a2r.sip.model.Parto;

public class PartosService {

    public static List traeCollares(Integer predioId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = PartosDAO.selectCollares(trx, predioId);
            } catch (SQLException ex) {
                AppLog.logSevere("PartosService.traeCollares()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeTipoPartos() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = PartosDAO.selectTipoPartos(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("PartosService.traeTipoPartos()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeSubTipoPartos() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = PartosDAO.selectSubTipoPartos(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("PartosService.traeSubTipoPartos()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traePartoAnterior(Integer ganadoId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = PartosDAO.selectPartoAnterior(trx, ganadoId);
            } catch (SQLException ex) {
                AppLog.logSevere("PartosService.traePartoAnterior()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traePartoPorConfirmar(Integer ganadoId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = PartosDAO.selectPartoPorConfirmar(trx, ganadoId);
            } catch (SQLException ex) {
                AppLog.logSevere("PartosService.traePartoPorConfirmar()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traePartos(Integer userId, Integer fundoId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = PartosDAO.selectPartos(trx, userId, fundoId);
            } catch (SQLException ex) {
                AppLog.logSevere("PartosService.traePartos()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeCandidatosEncontrados(Integer fundoId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = PartosDAO.selectCandidatosEncontrados(trx, fundoId);
            } catch (SQLException ex) {
                AppLog.logSevere("PartosService.traePartosEncontrados()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeCandidatosFaltantes(Integer fundoId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = PartosDAO.selectCandidatosFaltantes(trx, fundoId);
            } catch (SQLException ex) {
                AppLog.logSevere("PartosService.traeCandidatosFaltantes()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static void insertaParto(Parto parto) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                PartosDAO.insertParto(trx, parto);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("PartosService.insertaParto()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
    }
    
    public static void confirmaParto(Integer ganadoId, Integer usuarioId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                PartosDAO.confirmaParto(trx, ganadoId, usuarioId);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("PartosService.confirmaParto()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
    }
    
    public static void deshacerRegistroParto(GanadoLogs gl) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                PartosDAO.deshacerRegistroParto(trx, gl);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("PartosService.deshacerRegistroParto()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
    }
	
}
