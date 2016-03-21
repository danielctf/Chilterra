package cl.ar2.sqlite.dao;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Secado;

public class SecadosDAO {
	
	private static final String SQL_INSERT_DIIO = ""
			+ "INSERT INTO diio (id, diio, eid, fundoId, g_estado_leche_id) "
			+ " VALUES (?, ?, ?, ?, ?) ";
	
	private static final String SQL_INSERT_SECADO = ""
			+ "INSERT INTO secado (ganadoId, ganadoDiio, fundoId, mangada, med_control_id, "
			+ " serie, sincronizado ) "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?) ";
	
	private static final String SQL_SELECT_GANADO_A_SINCRONIZAR = ""
			+ "SELECT id, ganadoId, ganadoDiio, fundoId, mangada, med_control_id, "
			+ " serie, sincronizado "
			+ " FROM secado "
			+ " WHERE sincronizado = 'N' "
			+ " ORDER BY id DESC ";
	
	private static final String SQL_DELETE_ALL_SECADO = ""
			+ "DELETE FROM secado";
	
	private static final String SQL_DELETE_GANADO_SECADO = ""
			+ "DELETE FROM secado "
			+ " WHERE id = ? ";
	
	private static final String SQL_DELETE_ALL_DIIO = ""
			+ "DELETE FROM diio";
	
	private static final String SQL_MANGADA_ACTUAL = ""
			+ "SELECT max(mangada) mangada "
			+ " FROM secado "
			+ " WHERE sincronizado = 'N' ";
	
	private static final String SQL_SELECT_GANADO = ""
			+ "SELECT id, diio, eid, fundoId, g_estado_leche_id "
			+ " FROM diio "
			+ " WHERE id = ? ";
	
	private static final String SQL_EXISTS_GANADO = ""
			+ "SELECT id"
			+ " FROM secado "
			+ " WHERE ganadoId = ? ";
	
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
        	statement.executeInsert();
        }
    }
    
    public static void insertSecado(SqLiteTrx trx, Secado s) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_SECADO);
        
    	statement.clearBindings();
        statement.bindLong(1, s.getGan().getId());
        statement.bindLong(2, s.getGan().getDiio());
        statement.bindLong(3, s.getGan().getPredio());
        statement.bindLong(4, s.getGan().getMangada());
        if (s.getMed().getId() != null){
        	statement.bindLong(5, s.getMed().getId());
        	statement.bindString(6, s.getMed().getSerie().toString());
        } else {
        	statement.bindNull(5);
        	statement.bindNull(6);
        }
        statement.bindString(7, s.getSincronizado());
    	statement.executeInsert();
    	statement.close();
    }
    
    public static List selectGanASincronizar(SqLiteTrx trx) throws SQLException {
        List list = new ArrayList();
        boolean hayReg;
        
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_GANADO_A_SINCRONIZAR, null);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	Secado s = new Secado();
        	s.setId(c.getInt(c.getColumnIndex("id")));
        	Ganado g = new Ganado();
        	g.setId(c.getInt(c.getColumnIndex("ganadoId")));
        	g.setDiio(c.getInt(c.getColumnIndex("ganadoDiio")));
        	g.setPredio(c.getInt(c.getColumnIndex("fundoId")));
        	g.setMangada(c.getInt(c.getColumnIndex("mangada")));
        	if (!c.isNull(c.getColumnIndex("med_control_id"))){
        		s.getMed().setId(c.getInt(c.getColumnIndex("med_control_id")));
        		s.getMed().setSerie(c.getInt(c.getColumnIndex("serie")));
        	}
        	s.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
        	s.setGan(g);
        	list.add(s);
            hayReg = c.moveToNext();
        }
        c.close();

        return list;
    }
    
    public static void deleteAllDiio(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_ALL_DIIO);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
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
            hayReg = c.moveToNext();
        }

        return g;
    }
    
    public static void deleteAllSecado(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_ALL_SECADO);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    public static void deleteGanadoSecado(SqLiteTrx trx, Integer id) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_GANADO_SECADO);
    	statement.clearBindings();
    	statement.bindLong(1, id);
    	statement.executeUpdateDelete();
    }
    
    public static boolean existsGanado(SqLiteTrx trx, Integer ganadoId) throws SQLException {
        boolean exists = false;
        boolean hayReg;
        
        String[] args = {Integer.toString(ganadoId)};
        Cursor c = trx.getDB().rawQuery(SQL_EXISTS_GANADO, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	exists = true;
            hayReg = c.moveToNext();
        }
        c.close();

        return exists;
    }
    
}
