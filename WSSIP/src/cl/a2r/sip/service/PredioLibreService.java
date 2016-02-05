package cl.a2r.sip.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.dao.AreteosDAO;
import cl.a2r.sip.dao.PredioLibreDAO;
import cl.a2r.sip.dao.Transaccion;
import cl.a2r.sip.model.Areteo;
import cl.a2r.sip.model.Brucelosis;
import cl.a2r.sip.model.InyeccionTB;
import cl.a2r.sip.model.ResultadoBrucelosis;

public class PredioLibreService {

    public static List traePredioLibre(Integer g_fundo_id) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = PredioLibreDAO.selectPredioLibre(trx, g_fundo_id);
            } catch (SQLException ex) {
                AppLog.logSevere("PredioLibreService.traePredioLibre()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeAllDiio() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = PredioLibreDAO.selectAllDiio(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("PredioLibreService.traeAllDiio()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeGanadoTuberculina(Integer instancia) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = PredioLibreDAO.selectGanadoTuberculina(trx, instancia);
            } catch (SQLException ex) {
                AppLog.logSevere("PredioLibreService.traeGanadoTuberculina()", ex);
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
    
    public static void insertaGanadoTuberculina(List<InyeccionTB> list) throws AppException {

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                PredioLibreDAO.insertGanadoTuberculina(trx, list);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("PredioLibreService.insertaGanadoTuberculina()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
    }
	
    public static List traeTuberculinaPPD() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = PredioLibreDAO.selectTuberculinaPPD(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("PredioLibreService.traeGanadoTuberculina()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static void insertaPredioLibre(Integer p_usuario_id, Integer g_fundo_id) throws AppException {

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                PredioLibreDAO.insertPredioLibre(trx, p_usuario_id, g_fundo_id);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("PredioLibreService.insertaPredioLibre()", ex);
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
    
    public static void updateLecturaTB(List<InyeccionTB> list) throws AppException {

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                PredioLibreDAO.updateLecturaTB(trx, list);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("PredioLibreService.updateLecturaTB()", ex);
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
    
    public static List traeGanadoBrucelosis(Integer instancia) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = PredioLibreDAO.selectGanadoBrucelosis(trx, instancia);
            } catch (SQLException ex) {
                AppLog.logSevere("PredioLibreService.traeGanadoBrucelosis()", ex);
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
    
    public static void insertaGanadoBrucelosis(List<Brucelosis> list) throws AppException {

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                PredioLibreDAO.insertGanadoBrucelosis(trx, list);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("PredioLibreService.insertaGanadoBrucelosis()", ex);
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
    
    public static void cerrarInstancia(Integer g_usuario_id, Integer instancia) throws AppException {

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                PredioLibreDAO.cerrarInstancia(trx, g_usuario_id, instancia);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("PredioLibreService.cerrarInstancia()", ex);
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
    
    public static void insertaResultadoBrucelosis(ResultadoBrucelosis rb) throws AppException {

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                PredioLibreDAO.insertResultadoBrucelosis(trx, rb);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("PredioLibreService.insertaResultadoBrucelosis()", ex);
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
