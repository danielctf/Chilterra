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
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
        return list;
    }
    
    public static List traeDIIO(long eid) throws AppException {
        List list = new ArrayList();

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                list = GanadoDAO.selectDIIO(trx, eid);
            } catch (SQLException ex) {
                AppLog.logSevere("GanadoService.traeDIIO()", ex);
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
