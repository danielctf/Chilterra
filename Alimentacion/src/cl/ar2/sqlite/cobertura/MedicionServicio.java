package cl.ar2.sqlite.cobertura;

import java.util.ArrayList;
import java.util.List;

import android.database.SQLException;
import cl.a2r.common.AppException;
import cl.a2r.sap.model.Calificacion;
import cl.a2r.sap.model.Medicion;
import cl.ar2.sqlite.dao.MedicionDAO;
import cl.ar2.sqlite.dao.SqLiteTrx;

public class MedicionServicio {

    public static void insertaMedicion(List<Medicion> medList, boolean comesFromCloud) throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                MedicionDAO.insertaMedicion(trx, medList, comesFromCloud);
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
    
    public static void insertaStock(List<Medicion> list) throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                MedicionDAO.insertaStock(trx, list);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }
        }
    }
    
    public static List traeStockTotal() throws AppException {
        List list = new ArrayList();
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                list = MedicionDAO.selectStockTotal(trx);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return list;
    }
    
    public static List traeStock(List<StockM> list, Integer g_fundo_id) throws AppException {
        List listFiltrada = new ArrayList();
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
            	listFiltrada = MedicionDAO.selectStock(trx, list, g_fundo_id);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return listFiltrada;
    }
    
    public static void deleteStock() throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                MedicionDAO.deleteStock(trx);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }
        }
    }
    
    public static List traeStockPotrero(List<StockM> list, Integer g_fundo_id, Integer a_potrero_id) throws AppException {
        List listFiltrada = new ArrayList();
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
            	listFiltrada = MedicionDAO.selectStockPotrero(trx, list, g_fundo_id, a_potrero_id);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return listFiltrada;
    }
    
    public static List traeStockCrecimiento(List<StockM> list, Integer g_fundo_id, Integer a_potrero_id) throws AppException {
        List listFiltrada = new ArrayList();
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
            	listFiltrada = MedicionDAO.selectStockCrecimiento(trx, list, g_fundo_id, a_potrero_id);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return listFiltrada;
    }
    
    public static void insertaCalificacion(List<Calificacion> calList) throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                MedicionDAO.insertaCalificacion(trx, calList);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }
        }
    }
    
    public static List traeCalificacion() throws AppException {
        List listFiltrada = new ArrayList();
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
            	listFiltrada = MedicionDAO.selectCalificacion(trx);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return listFiltrada;
    }
    
    public static void deleteCalificacion() throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                MedicionDAO.deleteCalificacion(trx);
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
