package cl.ar2.sqlite.servicio;

import java.util.ArrayList;
import java.util.List;

import android.database.SQLException;
import cl.a2r.common.AppException;
import cl.a2r.sip.model.Auditoria;
import cl.ar2.sqlite.dao.AuditoriasDAO;
import cl.ar2.sqlite.dao.EcografiasDAO;
import cl.ar2.sqlite.dao.SqLiteTrx;

public class AuditoriasServicio {

    public static void insertaAuditoria(Auditoria a) throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                AuditoriasDAO.insertAuditoria(trx, a);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }
        }
    }
    
    public static Auditoria traeInstanciaGanado(Integer instancia) throws AppException {
        Auditoria a = new Auditoria();
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                a = AuditoriasDAO.selectInstanciaGanado(trx, instancia);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return a;
    }
    
    public static List traeGanadoNoSync() throws AppException {
        List list = new ArrayList();
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                list = AuditoriasDAO.selectGanadoNoSync(trx);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return list;
    }
    
    public static void deleteGanado() throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                AuditoriasDAO.deleteGanado(trx);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }
        }
    }
    
    public static void deleteGanGanado(Integer ganadoId, Integer instancia) throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                AuditoriasDAO.deleteGanGanado(trx, ganadoId, instancia);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }
        }
    }
    
    public static void deleteGanadoSynced() throws AppException {
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(true);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                AuditoriasDAO.deleteGanadoSynced(trx);
                trx.commit();
            } catch ( SQLException ex ) {
                trx.rollback();
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }
        }
    }
    
    public static Auditoria traeCandidatosFaltantes(Integer fundoId, Integer instancia) throws AppException {
    	Auditoria a = new Auditoria();
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                a = AuditoriasDAO.selectCandidatosFaltantes(trx, fundoId, instancia);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return a;
    }
    
    public static boolean existsGanado(Integer ganadoId, Integer instancia) throws AppException {
    	boolean exists = false;
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                exists = AuditoriasDAO.existsGanado(trx, ganadoId, instancia);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return exists;
    }
    
    public static Auditoria traeInstanciaGanadoNoSync(Integer instancia) throws AppException {
    	Auditoria a = new Auditoria();
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
                a = AuditoriasDAO.selectInstanciaGanadoNoSync(trx, instancia);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return a;
    }
    
    public static Integer traeMangadaActual(Integer instancia) throws AppException {
    	Integer mangadaActual = null;
        SqLiteTrx trx;

        try {
            trx = SqLiteTrx.getTrx(false);
        } catch (Exception ex) {
            throw new AppException(ex.getMessage(), ex);
        }

        if (trx != null) {
            try {
            	mangadaActual = AuditoriasDAO.mangadaActual(trx, instancia);
            } catch ( SQLException ex ) {
                throw new AppException(ex.getMessage(), null);
            } finally {
                trx.close();
            }

        }

        return mangadaActual;
    }
	
}
