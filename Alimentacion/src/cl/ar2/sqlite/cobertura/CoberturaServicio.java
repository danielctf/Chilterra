/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.ar2.sqlite.cobertura;

import android.database.SQLException;
import cl.a2r.common.AppException;
import cl.a2r.sap.model.Medicion;
import cl.ar2.sqlite.dao.CoberturaDAO;
import cl.ar2.sqlite.dao.SqLiteTrx;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miguel Vega Brante
 */
public class CoberturaServicio {


    public static void insertaMedicion(Medicion med) throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                CoberturaDAO.insertaMedicion(trx, med);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

    }

    public static List traeMediciones() throws AppException {
        List list = new ArrayList();
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                list = CoberturaDAO.selectMediciones(trx);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return list;
    }

}
