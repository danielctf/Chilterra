package cl.ar2.sqlite.cobertura;

import java.util.ArrayList;
import java.util.List;

import android.database.SQLException;
import cl.a2r.common.AppException;
import cl.a2r.sap.model.Medicion;
import cl.a2r.sap.model.Persona;
import cl.ar2.sqlite.dao.GoogleDAO;
import cl.ar2.sqlite.dao.MedicionDAO;
import cl.ar2.sqlite.dao.SqLiteTrx;

public class GoogleServicio {

    public static void insertaMedicion(Persona p) throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                GoogleDAO.insertaPersona(trx, p);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }
        }
    }
    
    public static Persona traePersona(String correo) throws AppException {
        Persona p = new Persona();
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                p = GoogleDAO.selectPersona(trx, correo);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return p;
    }
    
    public static void updatePhoto(Persona newP) throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                GoogleDAO.updatePhoto(trx, newP);
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
