package cl.a2r.sip.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.dao.EcografiasDAO;
import cl.a2r.sip.dao.PredioLibreDAO;
import cl.a2r.sip.dao.SecadosDAO;
import cl.a2r.sip.dao.Transaccion;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.InyeccionTB;
import cl.a2r.sip.model.Secado;

public class SecadosService {
	
    public static List traeMedicamentos(Integer appId) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = SecadosDAO.selectMedicamento(trx, appId);
            } catch (SQLException ex) {
                AppLog.logSevere("SecadosService.traeMedicamentos()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeEstadosLeche() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = SecadosDAO.selectEstadoLeche(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("SecadosService.traeEstadosLeche()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static void insertaEstadoLeche(List<Secado> secList, Integer usuarioId) throws AppException {

        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                SecadosDAO.insertEstadoLeche(trx, secList, usuarioId);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("SecadosService.insertaEstadoLeche()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
    }
    
    public static List traeAllDiio() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = SecadosDAO.selectAllDiio(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("SecadosService.traeAllDiio()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeGanado() throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = SecadosDAO.selectGanado(trx);
            } catch (SQLException ex) {
                AppLog.logSevere("SecadosService.traeGanado()", ex);
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
