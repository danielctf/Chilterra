package cl.a2r.sip.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.dao.EcografiasDAO;
import cl.a2r.sip.dao.RB51DAO;
import cl.a2r.sip.dao.Transaccion;
import cl.a2r.sip.model.Bang;
import cl.a2r.sip.model.Ecografia;
import cl.a2r.sip.model.VRB51;

public class RB51Service {

    public static List traeBang() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = RB51DAO.selectBang(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("RB51Service.traeBang()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeCandidatos(Integer g_fundo_id) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = RB51DAO.selectCandidatos(trx, g_fundo_id);
            } catch (SQLException ex) {
                AppLog.logSevere("RB51Service.traeCandidatos()", ex);
                if (ex.getSQLState().equals("SIP03")){
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
        return list;
    }
    
    public static List traeGanadoRB51() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = RB51DAO.selectGanadoRB51(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("RB51Service.traeGanadoRB51()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static Integer traeNumeroVacuna() throws AppException {
        Integer numeroVacuna = null;

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                numeroVacuna = RB51DAO.selectNumeroVacuna(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("RB51Service.traeNumeroVacuna()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return numeroVacuna;
    }
    
    public static List traeMedicamentos(Integer appId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = RB51DAO.selectMedicamentos(trx, appId);
            } catch (SQLException ex) {
                AppLog.logSevere("RB51Service.traeMedicamentos()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static void deleteBang(List<Bang> list, Integer usuarioId) throws AppException {

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                RB51DAO.deleteBang(trx, list, usuarioId);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("RB51Service.deleteBang()", ex);
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
    
    public static void insertaRB51(List<VRB51> list, Integer usuarioId) throws AppException {

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                RB51DAO.insertRB51(trx, list, usuarioId);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("RB51Service.insertaRB51()", ex);
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
