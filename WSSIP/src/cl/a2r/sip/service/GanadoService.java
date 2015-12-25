package cl.a2r.sip.service;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.dao.GanadoDAO;
import cl.a2r.sip.dao.Transaccion;

public class GanadoService {
	
    public static List traeGanado(Integer diio) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = GanadoDAO.selectGanado(trx, diio);
            } catch (SQLException ex) {
                AppLog.logSevere("GanadoService.traeGanado()", ex);
                if (ex.getSQLState().equals("SIP01")){
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
    
    public static List traeGanadoBaston(long eid) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = GanadoDAO.selectGanadoBaston(trx, eid);
            } catch (SQLException ex) {
                AppLog.logSevere("GanadoService.traeGanadoBaston()", ex);
                if (ex.getSQLState().equals("SIP01")){
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
    
    public static Integer traeDiio(Integer p_diio) throws AppException {
        Integer diio;

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                diio = GanadoDAO.selectDiio(trx, p_diio);
            } catch (SQLException ex) {
                AppLog.logSevere("GanadoService.traeDiio()", ex);
                if (ex.getSQLState().equals("SIP01")){
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
        return diio;
    }
    
    public static Integer traeDiioBaston(long eid) throws AppException {
        Integer diio;

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                diio = GanadoDAO.selectDiioBaston(trx, eid);
            } catch (SQLException ex) {
                AppLog.logSevere("GanadoService.traeDiioBaston()", ex);
                if (ex.getSQLState().equals("SIP01")){
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
        return diio;
    }
}
