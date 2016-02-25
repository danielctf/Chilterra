package cl.ar2.sqlite.dao;

import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import cl.a2r.sip.model.Ganado;

public class SecadosDAO {
	
	private static final String SQL_INSERT_DIIO = ""
			+ "INSERT INTO diio (id, diio, eid, fundoId, g_estado_leche_id, mangada, sincronizado) "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?) ";
	
	private static final String SQL_UPDATE_DIIO = ""
			+ "UPDATE diio "
			+ " SET sincronizado = 'N' "
			+ " WHERE id = ? ";
	
	private static final String SQL_DELETE_SINCRONIZADOS = ""
			+ "DELETE FROM diio WHERE sincronizado = 'Y'";
	
	private static final String SQL_DELETE_ALL = ""
			+ "DELETE FROM diio";
	
	private static final String SQL_SELECT_DIIO = ""
			+ "SELECT id, diio, eid, fundoId, g_estado_leche_id, mangada, sincronizado "
			+ " FROM diio "
			+ " WHERE sincronizado = 'N' ";
	
	private static final String SQL_EXISTS_DIIO = ""
			+ "SELECT id "
			+ " FROM diio "
			+ " WHERE sincronizado = 'N' "
			+ " AND id = ? ";
	
	private static final String SQL_MANGADA_ACTUAL = ""
			+ "SELECT max(mangada) mangada "
			+ " FROM diio "
			+ " WHERE sincronizado = 'N' ";
	
	private static final String SQL_SELECT_GANADO = ""
			+ "SELECT id, diio, eid, fundoId, g_estado_leche_id, mangada, sincronizado "
			+ " FROM diio "
			+ " WHERE id = ? ";
	
    public static void insertDiio(SqLiteTrx trx, List<Ganado> ganList) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_DIIO);
        
        for (Ganado g : ganList){
        	statement.clearBindings();
        	statement.bindLong(1, g.getId());
        	statement.bindLong(2, g.getDiio());
        	statement.bindString(3, g.getEid());
        	statement.bindLong(4, g.getPredio());
        	if (g.getEstadoLecheId() != null){
        		statement.bindLong(5, g.getEstadoLecheId());
        	} else {
        		statement.bindNull(5);
        	}
        	if (g.getMangada() != null){
        		statement.bindLong(6, g.getMangada());
        	} else {
        		statement.bindNull(6);
        	}
        	statement.bindString(7, g.getSincronizado());
        	statement.executeInsert();
        }
    }
    
    public static void updateDiio(SqLiteTrx trx, Ganado g) throws SQLException {
        SQLiteStatement statement = trx.getDB().compileStatement(SQL_UPDATE_DIIO);
    	statement.clearBindings();
    	statement.bindLong(1, g.getId());
    	statement.executeUpdateDelete();
    }
    
    public static void deleteSincronizados(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_SINCRONIZADOS);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    public static void deleteAll(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_ALL);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    public static List selectDiio(SqLiteTrx trx) throws SQLException {
        List list = new ArrayList();
        boolean hayReg;
        
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_DIIO, null);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	Ganado g = new Ganado();
        	g.setId(c.getInt(c.getColumnIndex("id")));
        	g.setDiio(c.getInt(c.getColumnIndex("diio")));
        	g.setEid(c.getString(c.getColumnIndex("eid")));
        	g.setPredio(c.getInt(c.getColumnIndex("fundoId")));
        	if (!c.isNull(c.getColumnIndex("g_estado_leche_id"))){
        		g.setEstadoLecheId(c.getInt(c.getColumnIndex("g_estado_leche_id")));
        	}
        	if (!c.isNull(c.getColumnIndex("mangada"))){
        		g.setMangada(c.getInt(c.getColumnIndex("mangada")));
        	}
        	g.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
        	list.add(g);
            hayReg = c.moveToNext();
        }

        return list;
    }
    
    public static Integer mangadaActual(SqLiteTrx trx) throws SQLException {
        Integer mangadaActual = null;
        boolean hayReg;
        
        Cursor c = trx.getDB().rawQuery(SQL_MANGADA_ACTUAL, null);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	if (!c.isNull(c.getColumnIndex("mangada"))){
        		mangadaActual = c.getInt(c.getColumnIndex("mangada"));	
        	}
            hayReg = c.moveToNext();
        }
        return mangadaActual;
    }
    
    public static Ganado selectGanado(SqLiteTrx trx, Integer ganadoId) throws SQLException {
        Ganado g = null;
        boolean hayReg;
        
        String[] args = {Integer.toString(ganadoId)};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_GANADO, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	g = new Ganado();
        	g.setId(c.getInt(c.getColumnIndex("id")));
        	g.setDiio(c.getInt(c.getColumnIndex("diio")));
        	g.setEid(c.getString(c.getColumnIndex("eid")));
        	g.setPredio(c.getInt(c.getColumnIndex("fundoId")));
        	if (!c.isNull(c.getColumnIndex("g_estado_leche_id"))){
        		g.setEstadoLecheId(c.getInt(c.getColumnIndex("g_estado_leche_id")));
        	}
        	if (!c.isNull(c.getColumnIndex("mangada"))){
        		g.setMangada(c.getInt(c.getColumnIndex("mangada")));
        	}
        	g.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
            hayReg = c.moveToNext();
        }

        return g;
    }
	
}
