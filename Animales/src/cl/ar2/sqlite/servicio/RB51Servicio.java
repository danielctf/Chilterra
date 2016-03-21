package cl.ar2.sqlite.servicio;

import java.util.ArrayList;
import java.util.List;

import android.database.SQLException;
import cl.a2r.common.AppException;
import cl.a2r.sip.model.VRB51;
import cl.ar2.sqlite.dao.RB51DAO;
import cl.ar2.sqlite.dao.SalvatajesDAO;
import cl.ar2.sqlite.dao.SqLiteTrx;

public class RB51Servicio {
	
    public static void insertaRB51(List<VRB51> list) throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                RB51DAO.insertRB51(trx, list);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }
        }
    }
    
    public static List traeRB51() throws AppException {
        List list = new ArrayList();
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                list = RB51DAO.selectRB51(trx);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return list;
    }
    
    public static void deleteGanRB51(Integer id) throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                RB51DAO.deleteGanRB51(trx, id);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }
        }
    }
    
    public static void deleteRB51() throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                RB51DAO.deleteRB51(trx);
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
