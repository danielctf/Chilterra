package cl.a2r.sap.service;

import java.sql.SQLException;

import cl.a2r.common.AppException;
import cl.a2r.sap.common.AppLog;
import cl.a2r.sap.dao.MedicionDAO;
import cl.a2r.sap.dao.Transaccion;
import cl.a2r.sap.model.Medicion;

public class MedicionService {
	
    public static void insertaMedicion(Medicion med) throws AppException {
        Transaccion trx = Transaccion.getTransaccion(true);
        if (trx != null){
            try {
                MedicionDAO.insertaMedicion(trx, med);
                trx.commit();
            } catch (SQLException ex) {
                trx.rollback();
            	AppLog.logSevere("MedicionService.insertaMedicion()", ex);
                throw new AppException("No se pudo recuperar los registros.", null);
            } finally {
                trx.close();
            }
        } else {
            throw new AppException("No se pudo obtener la conexión.", null);
        }
    }
    
}
