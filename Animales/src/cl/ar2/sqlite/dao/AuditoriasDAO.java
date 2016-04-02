package cl.ar2.sqlite.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import cl.a2r.sip.model.Auditoria;
import cl.a2r.sip.model.Ganado;

public class AuditoriasDAO {

	private static final String SQL_INSERT_GANADO = ""
			+ "INSERT INTO auditoria (ganadoId, ganadoDiio, fundoId, mangada, instancia, "
			+ " fecha, sincronizado) "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?) ";
	
	private static final String SQL_SELECT_INSTANCIA_GANADO = ""
			+ "SELECT id, ganadoId, ganadoDiio, fundoId, mangada, instancia, "
			+ " fecha, sincronizado "
			+ " FROM auditoria "
			+ " WHERE instancia = ? "
			+ " ORDER BY id DESC ";
	
	private static final String SQL_SELECT_GANADO_NO_SYNC = ""
			+ "SELECT id, ganadoId, ganadoDiio, fundoId, mangada, instancia, "
			+ " fecha, sincronizado "
			+ " FROM auditoria "
			+ " WHERE sincronizado = 'N' "
			+ " ORDER BY id DESC ";
	
	private static final String SQL_DELETE_GANADO = ""
			+ "DELETE FROM auditoria";
	
	private static final String SQL_DELETE_GAN_GANADO = ""
			+ "DELETE FROM auditoria"
			+ " WHERE ganadoId = ? "
			+ " AND instancia = ? ";
	
	private static final String SQL_DELETE_GANADO_SYNCED = ""
			+ "DELETE FROM auditoria "
			+ " WHERE sincronizado = 'Y' ";
	
	private static final String SQL_SELECT_CANDIDATOS_FALTANTES = ""
			+ "SELECT id, diio, eid, fundoId "
			+ " FROM diio "
			+ " WHERE fundoId = ? "
			+ " AND id NOT IN (SELECT ganadoId "
			+ "                FROM auditoria"
			+ "                WHERE instancia = ?) ";
	
	private static final String SQL_EXISTS_GANADO = ""
			+ "SELECT id "
			+ " FROM auditoria "
			+ " WHERE ganadoId = ? "
			+ " AND instancia = ? ";
	
	private static final String SQL_SELECT_INSTANCIA_GANADO_NO_SYNC = ""
			+ "SELECT id, ganadoId, ganadoDiio, fundoId, mangada, instancia, "
			+ " fecha, sincronizado "
			+ " FROM auditoria "
			+ " WHERE sincronizado = 'N' "
			+ " AND instancia = ? "
			+ " ORDER BY id DESC ";
	
	private static final String SQL_MANGADA_ACTUAL = ""
			+ "SELECT max(mangada) mangada "
			+ " FROM auditoria "
			+ " WHERE sincronizado = 'N' "
			+ " AND instancia = ? ";
	
    public static void insertAuditoria(SqLiteTrx trx, Auditoria a) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_GANADO);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        
        for (Ganado g : a.getGanList()){
        	statement.clearBindings();
        	statement.bindLong(1, g.getId());
        	statement.bindLong(2, g.getDiio());
        	statement.bindLong(3, g.getPredio());
        	if (g.getMangada() != null){
        		statement.bindLong(4, g.getMangada());
        	} else {
        		statement.bindNull(4);
        	}
        	statement.bindLong(5, a.getId());
        	statement.bindString(6, df.format(g.getFecha()));
        	statement.bindString(7, g.getSincronizado());
        	statement.executeInsert();
        }
    }
    
    public static Auditoria selectInstanciaGanado(SqLiteTrx trx, Integer instancia) throws SQLException {
    	Auditoria a = new Auditoria();
    	a.setId(instancia);
        boolean hayReg;
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String[] args = {Integer.toString(instancia)};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_INSTANCIA_GANADO, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	Ganado g = new Ganado();
        	g.setId(c.getInt(c.getColumnIndex("ganadoId")));
        	g.setDiio(c.getInt(c.getColumnIndex("ganadoDiio")));
        	g.setPredio(c.getInt(c.getColumnIndex("fundoId")));
        	if (!c.isNull(c.getColumnIndex("mangada"))){
        		g.setMangada(c.getInt(c.getColumnIndex("mangada")));	
        	}
        	try {
				g.setFecha(df.parse(c.getString(c.getColumnIndex("fecha"))));
			} catch (ParseException e) {}
        	g.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
        	a.getGanList().add(g);

        	hayReg = c.moveToNext();
        }
        return a;
    }
    
    public static List selectGanadoNoSync(SqLiteTrx trx) throws SQLException {
        List list = new ArrayList();
        boolean hayReg;
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        Cursor c = trx.getDB().rawQuery(SQL_SELECT_GANADO_NO_SYNC, null);
        hayReg = c.moveToFirst();
        
        Auditoria a = new Auditoria();
        int instanciaActual = 0;
        while ( hayReg ) {
        	if (instanciaActual != c.getInt(c.getColumnIndex("instancia"))){
        		if (instanciaActual != 0){
        			list.add(a);
        		}
        		a = new Auditoria();
        		a.setId(c.getInt(c.getColumnIndex("instancia")));
        		instanciaActual = c.getInt(c.getColumnIndex("instancia"));
        	} 
        	Ganado g = new Ganado();
        	g.setId(c.getInt(c.getColumnIndex("ganadoId")));
        	g.setDiio(c.getInt(c.getColumnIndex("ganadoDiio")));
        	g.setPredio(c.getInt(c.getColumnIndex("fundoId")));
        	if (!c.isNull(c.getColumnIndex("mangada"))){
        		g.setMangada(c.getInt(c.getColumnIndex("mangada")));	
        	}
        	try {
				g.setFecha(df.parse(c.getString(c.getColumnIndex("fecha"))));
			} catch (ParseException e) {}
        	g.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
        	a.getGanList().add(g);

        	hayReg = c.moveToNext();
        	if (!hayReg){
        		list.add(a);
        	}
        }
        
        return list;
    }
    
    public static void deleteGanado(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_GANADO);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    public static void deleteGanGanado(SqLiteTrx trx, Integer ganadoId, Integer instancia) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_GAN_GANADO);
    	statement.clearBindings();
    	statement.bindLong(1, ganadoId);
    	statement.bindLong(2, instancia);
    	statement.executeUpdateDelete();
    }
    
    public static void deleteGanadoSynced(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_GANADO_SYNCED);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    public static Auditoria selectCandidatosFaltantes(SqLiteTrx trx, Integer fundoId, Integer instancia) throws SQLException {
        Auditoria a = new Auditoria();
        boolean hayReg;
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String[] args = {Integer.toString(fundoId), Integer.toString(instancia)};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_CANDIDATOS_FALTANTES, args);
        hayReg = c.moveToFirst();

        while ( hayReg ) {
        	Ganado g = new Ganado();
        	g.setId(c.getInt(c.getColumnIndex("id")));
        	g.setDiio(c.getInt(c.getColumnIndex("diio")));
        	g.setPredio(c.getInt(c.getColumnIndex("fundoId")));
        	a.getGanList().add(g);

        	hayReg = c.moveToNext();
        }
        
        return a;
    }
    
    public static boolean existsGanado(SqLiteTrx trx, Integer ganadoId, Integer instancia) throws SQLException {
    	boolean exists = false;
        boolean hayReg;

        String[] args = {Integer.toString(ganadoId), Integer.toString(instancia)};
        Cursor c = trx.getDB().rawQuery(SQL_EXISTS_GANADO, args);
        hayReg = c.moveToFirst();

        while ( hayReg ) {
        	exists = true;
        	hayReg = c.moveToNext();
        }
        
        return exists;
    }
    
    public static Auditoria selectInstanciaGanadoNoSync(SqLiteTrx trx, Integer instancia) throws SQLException {
    	Auditoria a = new Auditoria();
    	a.setId(instancia);
        boolean hayReg;
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String[] args = {Integer.toString(instancia)};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_INSTANCIA_GANADO_NO_SYNC, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	Ganado g = new Ganado();
        	g.setId(c.getInt(c.getColumnIndex("ganadoId")));
        	g.setDiio(c.getInt(c.getColumnIndex("ganadoDiio")));
        	g.setPredio(c.getInt(c.getColumnIndex("fundoId")));
        	if (!c.isNull(c.getColumnIndex("mangada"))){
        		g.setMangada(c.getInt(c.getColumnIndex("mangada")));	
        	}
        	try {
				g.setFecha(df.parse(c.getString(c.getColumnIndex("fecha"))));
			} catch (ParseException e) {}
        	g.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
        	a.getGanList().add(g);

        	hayReg = c.moveToNext();
        }
        return a;
    }
    
    public static Integer mangadaActual(SqLiteTrx trx, Integer instancia) throws SQLException {
    	Integer mangadaActual = null;
        boolean hayReg;
        String[] args = {Integer.toString(instancia)};
        Cursor c = trx.getDB().rawQuery(SQL_MANGADA_ACTUAL, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	if (!c.isNull(c.getColumnIndex("mangada"))){
        		mangadaActual = c.getInt(c.getColumnIndex("mangada"));	
        	}
        	hayReg = c.moveToNext();
        }
        return mangadaActual;
    }
	
}
