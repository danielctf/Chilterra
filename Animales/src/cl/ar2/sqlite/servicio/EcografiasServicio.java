package cl.ar2.sqlite.servicio;

import java.util.ArrayList;
import java.util.List;

import android.database.SQLException;
import cl.a2r.common.AppException;
import cl.a2r.sip.model.Ecografia;
import cl.a2r.sip.model.Inseminacion;
import cl.ar2.sqlite.dao.EcografiasDAO;
import cl.ar2.sqlite.dao.SqLiteTrx;

public class EcografiasServicio {

    public static void insertaInseminacion(List<Inseminacion> list) throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                EcografiasDAO.insertInseminacion(trx, list);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }
        }
    }
    
    public static List traeInseminacionGanado(Integer ganadoId) throws AppException {
        List list = new ArrayList();
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                list = EcografiasDAO.selectInseminacionGanado(trx, ganadoId);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return list;
    }
    
    public static void deleteInseminaciones() throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                EcografiasDAO.deleteInseminaciones(trx);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }
        }
    }
    
    public static void deleteInseminacionesSincronizadas() throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                EcografiasDAO.deleteInseminacionesSincronizadas(trx);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }
        }
    }
    
    public static void insertaEcografia(List<Ecografia> list) throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                EcografiasDAO.insertEcografia(trx, list);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }
        }
    }
    
    public static List traeEcografias() throws AppException {
        List list = new ArrayList();
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                list = EcografiasDAO.selectEcografias(trx);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return list;
    }
    
    public static void deleteEcografiasSincronizadas() throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                EcografiasDAO.deleteEcografiasSincronizadas(trx);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }
        }
    }
    
    public static void deleteEcografias() throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                EcografiasDAO.deleteEcografias(trx);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }
        }
    }
    
    public static void deleteEcografiaId(Integer id) throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                EcografiasDAO.deleteEcografiaId(trx, id);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }
        }
    }
    
    public static List traeEcografiaGanado(Integer ganadoId) throws AppException {
        List list = new ArrayList();
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                list = EcografiasDAO.selectEcografiaGanado(trx, ganadoId);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return list;
    }
    
    public static Inseminacion traeInseminacionId(Integer insId) throws AppException {
        Inseminacion i = new Inseminacion();
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                i = EcografiasDAO.selectInseminacionId(trx, insId);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return i;
    }
    
    public static Integer traeMangadaActual() throws AppException {
        Integer mangadaActual = null;
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
            	mangadaActual = EcografiasDAO.selectMangadaActual(trx);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return mangadaActual;
    }
    
    public static void updateEcoFundo(Integer nuevoFundoId, Integer ganadoId) throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                EcografiasDAO.updateEcoFundo(trx, nuevoFundoId, ganadoId);
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
