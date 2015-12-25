package cl.ar2.sqlite.cobertura;

import java.util.ArrayList;
import java.util.List;

import android.database.SQLException;
import cl.a2r.common.AppException;
import cl.a2r.sap.model.Medicion;
import cl.ar2.sqlite.dao.CoberturaDAO;
import cl.ar2.sqlite.dao.MedicionDAO;
import cl.ar2.sqlite.dao.SqLiteTrx;

public class MedicionServicio {

    public static void insertaMedicion(Medicion med) throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                MedicionDAO.insertaMedicion(trx, med);
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
                list = MedicionDAO.selectMediciones(trx);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return list;
    }
    
    public static void deleteMedicion(Integer sqliteId) throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                MedicionDAO.deleteMedicion(trx, sqliteId);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }
        }
    }
	
}
