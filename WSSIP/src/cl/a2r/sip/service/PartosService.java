package cl.a2r.sip.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.dao.GanadoDAO;
import cl.a2r.sip.dao.PartosDAO;
import cl.a2r.sip.dao.Transaccion;

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
	
}
