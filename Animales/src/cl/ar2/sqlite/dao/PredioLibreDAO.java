package cl.ar2.sqlite.dao;

import java.util.ArrayList;
import java.util.List;

import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.InyeccionTB;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;

public class PredioLibreDAO {
	
	private static final String SQL_DELETE_DIIO = ""
			+ "DELETE FROM diio";
	
	private static final String SQL_INSERTA_DIIO = ""
			+ "INSERT INTO diio (id, diio, eid, fundoId) VALUES (?, ?, ?, ?)";
	
	private static final String SQL_SELECT_DIIO = ""
			+ "SELECT id, diio, eid, fundoId FROM diio WHERE diio = ?";
	
	private static final String SQL_SELECT_EID = ""
			+ "SELECT id, diio, eid, fundoId FROM diio WHERE eid = ?";
	
	private static final String SQL_ALL = ""
			+ "SELECT id, diio, eid, fundoId FROM diio";
	
	private static final String SQL_SELECT_DIIO_FUNDO = ""
			+ "SELECT id, diio, eid, fundoId FROM diio WHERE fundoId = ?";
	
	private static final String SQL_INSERTA_GANADO_PL = ""
			+ "INSERT INTO predio_libre (ganadoId, fundoId, instancia, ganadoDiio, tuboPPDId, sincronizado) VALUES (?, ?, ?, ?, ?, ?)";
	
	private static final String SQL_SELECT_GANADO_PL = ""
			+ "SELECT ganadoId, fundoId, instancia, ganadoDiio, tuboPPDId, sincronizado FROM predio_libre";
	
	private static final String SQL_EXISTS_GANADO_PL = ""
			+ "SELECT ganadoId, fundoId, instancia, ganadoDiio, tuboPPDId, sincronizado FROM predio_libre WHERE ganadoId = ?";
	
	private static final String SQL_DELETE_SINCRONIZADO = ""
			+ " DELETE FROM predio_libre WHERE sincronizado = ?";
	
	private static final String SQL_DELETE_PL = ""
			+ " DELETE FROM predio_libre";

    public static void deleteDiio(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_DIIO);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    public static void insertaDiio(SqLiteTrx trx, List<Ganado> list) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERTA_DIIO);
        
        for (Ganado g : list){
        	statement.clearBindings();
        	statement.bindLong(1, g.getId());
        	statement.bindLong(2, g.getDiio());
        	statement.bindString(3, g.getEid());
        	statement.bindLong(4, g.getPredio());
        	statement.executeInsert();
        }
    }
    
    public static Ganado selectDiio(SqLiteTrx trx, Integer diio) throws SQLException {
        Ganado g = null;
        boolean hayReg;
        
        String[] args = {Integer.toString(diio)};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_DIIO, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {

        	g = new Ganado();
        	g.setId(c.getInt(c.getColumnIndex("id")));
        	g.setDiio(c.getInt(c.getColumnIndex("diio")));
        	g.setEid(c.getString(c.getColumnIndex("eid")));
        	g.setPredio(c.getInt(c.getColumnIndex("fundoId")));
            hayReg = c.moveToNext();
        }

        return g;
    }
    
    public static Ganado selectEID(SqLiteTrx trx, String eid) throws SQLException {
        Ganado g = null;
        boolean hayReg;

        String[] args = {eid};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_EID, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {

        	g = new Ganado();
        	g.setId(c.getInt(c.getColumnIndex("id")));
        	g.setDiio(c.getInt(c.getColumnIndex("diio")));
        	g.setEid(c.getString(c.getColumnIndex("eid")));
        	g.setPredio(c.getInt(c.getColumnIndex("fundoId")));
            hayReg = c.moveToNext();
        }

        return g;
    }
    
    public static List selectAll(SqLiteTrx trx) throws SQLException {
        List list = new ArrayList();
        boolean hayReg;

        Cursor c = trx.getDB().rawQuery(SQL_ALL, null);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	
        	Ganado g = new Ganado();
        	g.setId(c.getInt(c.getColumnIndex("id")));
        	g.setDiio(c.getInt(c.getColumnIndex("diio")));
        	g.setEid(c.getString(c.getColumnIndex("eid")));
        	g.setPredio(c.getInt(c.getColumnIndex("fundoId")));
        	list.add(g);
            hayReg = c.moveToNext();
        }

        return list;
    }
    
    public static void insertaGanadoPL(SqLiteTrx trx, InyeccionTB tb) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERTA_GANADO_PL);
        
    	statement.clearBindings();
    	statement.bindLong(1, tb.getGanadoID());
    	statement.bindLong(2, tb.getFundoId());
    	statement.bindLong(3, tb.getInstancia());
    	statement.bindLong(4, tb.getGanadoDiio());
    	statement.bindLong(5, tb.getTuboPPDId());
    	statement.bindString(6, tb.getSincronizado());
    	statement.executeInsert();
    }
    
    public static List selectGanadoPL(SqLiteTrx trx) throws SQLException {
        List list = new ArrayList();
        boolean hayReg;

        Cursor c = trx.getDB().rawQuery(SQL_SELECT_GANADO_PL, null);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	InyeccionTB i = new InyeccionTB();
        	i.setGanadoID(c.getInt(c.getColumnIndex("ganadoId")));
        	i.setFundoId(c.getInt(c.getColumnIndex("fundoId")));
        	i.setInstancia(c.getInt(c.getColumnIndex("instancia")));
        	i.setGanadoDiio(c.getInt(c.getColumnIndex("ganadoDiio")));
        	i.setTuboPPDId(c.getInt(c.getColumnIndex("tuboPPDId")));
        	i.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
        	list.add(i);
            hayReg = c.moveToNext();
        }

        return list;
    }
    
    public static boolean existsGanadoPL(SqLiteTrx trx, Integer ganadoId) throws SQLException {
        boolean exists = false;
        boolean hayReg;

        String[] args = {Integer.toString(ganadoId)};
        Cursor c = trx.getDB().rawQuery(SQL_EXISTS_GANADO_PL, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	exists = true;
            hayReg = c.moveToNext();
        }

        return exists;
    }
    
    public static void deleteSincronizado(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_SINCRONIZADO);
    	statement.clearBindings();
    	statement.bindString(1, "Y");
    	statement.executeUpdateDelete();
    }
    
    public static void deletePL(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_PL);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    public static List selectDiioFundo(SqLiteTrx trx, Integer g_fundo_id) throws SQLException {
        List list = new ArrayList();
        boolean hayReg;

        String[] args = {Integer.toString(g_fundo_id)};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_DIIO_FUNDO, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	Ganado g = new Ganado();
        	g.setId(c.getInt(c.getColumnIndex("id")));
        	g.setDiio(c.getInt(c.getColumnIndex("diio")));
        	g.setEid(c.getString(c.getColumnIndex("eid")));
        	g.setPredio(c.getInt(c.getColumnIndex("fundoId")));
        	list.add(g);
            hayReg = c.moveToNext();
        }

        return list;
    }

}
