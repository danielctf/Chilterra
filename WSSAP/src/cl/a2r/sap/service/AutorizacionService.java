/*
 * Desarrollado por : Miguel Vega B.
 */

package cl.a2r.sap.service;

import cl.a2r.common.AppException;
import cl.a2r.sap.common.AppLog;
import cl.a2r.sap.dao.AutorizacionDAO;
import cl.a2r.sap.dao.MedicionDAO;
import cl.a2r.sap.dao.Transaccion;
import cl.a2r.sap.model.Sesion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miguelon
 */
public class AutorizacionService {

    public static Integer validaUsuario(String correo) throws AppException {
        Integer g_movimiento_id = null;

        Transaccion trx = Transaccion.getTransaccion(false);
        if (trx != null){
            try {
                g_movimiento_id = AutorizacionDAO.validaUsuario(trx, correo);
            } catch (SQLException ex) {
                AppLog.logSevere("MedicionService.validaUsuario()", ex);
                if (ex.getSQLState().equals("SAP01")){
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
        return g_movimiento_id;
    }

}
