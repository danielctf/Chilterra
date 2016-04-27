package cl.a2r.sip.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.dao.AreteosDAO;
import cl.a2r.sip.dao.AuditoriaDAO;
import cl.a2r.sip.dao.Transaccion;
import cl.a2r.sip.dao.TrasladosDAO;
import cl.a2r.sip.model.Areteo;
import cl.a2r.sip.model.Auditoria;
import cl.a2r.sip.model.DctoAdem;
import cl.a2r.sip.model.FMA;
import cl.a2r.sip.model.Instancia;
import cl.a2r.sip.model.Traslado;

public class TrasladosService {

    public static List traeTransportistas() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = TrasladosDAO.selectTransportistas(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("TrasladosService.traeTransportistas()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeChofer(Integer transportistaId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = TrasladosDAO.selectChofer(trx, transportistaId);
            } catch (SQLException ex) {
                AppLog.logSevere("TrasladosService.traeChofer()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeCamion(Integer transportistaId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = TrasladosDAO.selectCamion(trx, transportistaId);
            } catch (SQLException ex) {
                AppLog.logSevere("TrasladosService.traeCamion()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeAcoplado(Integer transportistaId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = TrasladosDAO.selectAcoplado(trx, transportistaId);
            } catch (SQLException ex) {
                AppLog.logSevere("TrasladosService.traeAcoplado()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeArrieros() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = TrasladosDAO.selectArrieros(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("TrasladosService.traeArrieros()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static Integer insertaMovimiento(Traslado traslado) throws AppException {
    	Integer g_movimiento_id;

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                g_movimiento_id = TrasladosDAO.insertaMovimiento(trx, traslado);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("TrasladosService.insertaMovimiento()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return g_movimiento_id;
    }
    
    public static DctoAdem insertaMovtoAdem(Integer g_movimiento_id) throws AppException {
        DctoAdem d;

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                d = TrasladosDAO.insertaMovtoAdem(trx, g_movimiento_id);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("TrasladosService.insertaMovtoAdem()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return d;
    }
    
    public static void generaXMLTraslado(FMA fma) throws AppException {

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                TrasladosDAO.generarXMLTraslado(trx, fma);
            } catch (SQLException ex) {
                AppLog.logSevere("TrasladosService.generaXMLTraslado()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
    }
    
    public static List traeMovimientosEP() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = TrasladosDAO.selectMovimientosEP(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("TrasladosService.traeMovimientosEP()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static Traslado traeMovimiento(Integer nro_documento) throws AppException {
        Traslado t;

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                t = TrasladosDAO.selectMovimiento(trx, nro_documento);
            } catch (SQLException ex) {
                AppLog.logSevere("TrasladosService.traeMovimiento()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return t;
    }
    
    public static void insertaMovtoConfirm(Traslado traslado) throws AppException {

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                TrasladosDAO.insertaMovtoConfirm(trx, traslado);
                TrasladosDAO.reubicaGanado(trx, traslado);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("TrasladosService.insertaMovtoConfirm()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
    }
    
    public static Integer insertaMovtoReubicacion(Traslado traslado) throws AppException {
    	Integer g_movimiento_id;
        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                g_movimiento_id = TrasladosDAO.insertaMovtoReubica(trx, traslado);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("TrasladosService.insertaMovtoReubicacion()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return g_movimiento_id;
    }
    
    public static void reubicaGanado(Traslado traslado) throws AppException {
    	
        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                TrasladosDAO.reubicaGanado(trx, traslado);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("TrasladosService.reubicaGanado()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        
    }
    
    //--------------------- TRASLADOS V2 ----------------------------
    
    public static List traeTraslados(Integer fundoId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = TrasladosDAO.selectTraslados(trx, fundoId);
            } catch (SQLException ex) {
                AppLog.logSevere("TrasladosService.traeTraslados()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static void borrarTraslado(Instancia instancia) throws AppException {
        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                TrasladosDAO.borrarTraslado(trx, instancia);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
                AppLog.logSevere("TrasladosService.borrarTraslado()", ex);
                if (ex.getSQLState().equals("SIP05")){
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
    
    public static List traeChofer() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = TrasladosDAO.selectChofer(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("TrasladosService.traeChofer()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeCamion() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = TrasladosDAO.selectCamion(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("TrasladosService.traeCamion()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeAcoplado() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = TrasladosDAO.selectAcoplado(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("TrasladosService.traeAcoplado()", ex);
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
