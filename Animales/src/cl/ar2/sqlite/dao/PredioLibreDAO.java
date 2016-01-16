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
			+ "INSERT INTO diio (id, diio, eid) VALUES (?, ?, ?)";
	
	private static final String SQL_SELECT_DIIO = ""
			+ "SELECT id, diio, eid FROM diio WHERE diio = ?";
	
	private static final String SQL_SELECT_EID = ""
			+ "SELECT id, diio, eid FROM diio WHERE eid = ?";
	
	private static final String SQL_ALL = ""
			+ "SELECT id, diio, eid FROM diio";
	
	private static final String SQL_INSERTA_GANADO_PL = ""
			+ "INSERT INTO predio_libre (ganadoId, ganadoDiio, tuboPPDId) VALUES (?, ?, ?)";
	
	private static final String SQL_SELECT_GANADO_PL = ""
			+ "SELECT ganadoId, ganadoDiio, tuboPPDId FROM predio_libre";
	
	private static final String SQL_EXISTS_GANADO_PL = ""
			+ "SELECT ganadoId, ganadoDiio, tuboPPDId FROM predio_libre WHERE ganadoId = ?";

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
        	list.add(g);
            hayReg = c.moveToNext();
        }

        return list;
    }
    
    public static void insertaGanadoPL(SqLiteTrx trx, InyeccionTB tb) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERTA_GANADO_PL);
        
    	statement.clearBindings();
    	statement.bindLong(1, tb.getGanadoID());
    	statement.bindLong(2, tb.getGanadoDiio());
    	statement.bindLong(3, tb.getTuboPPDId());
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
        	i.setGanadoDiio(c.getInt(c.getColumnIndex("ganadoDiio")));
        	i.setTuboPPDId(c.getInt(c.getColumnIndex("tuboPPDId")));
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
	
}
