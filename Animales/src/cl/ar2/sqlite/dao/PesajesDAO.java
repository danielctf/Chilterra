package cl.ar2.sqlite.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Pesaje;

public class PesajesDAO {
	
	private static final String SQL_INSERT_PESAJE = ""
			+ "INSERT INTO pesaje (ganadoId, ganadoDiio, fundoId, mangada, fecha, peso, gpd, sincronizado) "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";
	
	private static final String SQL_SELECT_PESAJE_NO_SYNC = ""
			+ "SELECT id, ganadoId, ganadoDiio, fundoId, mangada, fecha, peso, gpd, sincronizado "
			+ " FROM pesaje "
			+ " WHERE sincronizado = 'N' ";
	
	private static final String SQL_SELECT_PESAJE_FUNDO_NO_SYNC = ""
			+ "SELECT id, ganadoId, ganadoDiio, fundoId, mangada, fecha, peso, gpd, sincronizado "
			+ " FROM pesaje "
			+ " WHERE sincronizado = 'N' "
			+ " AND fundoId = ? "
			+ " ORDER BY id DESC ";
	
	private static final String SQL_SELECT_GAN_PESAJE = ""
			+ "SELECT id, ganadoId, ganadoDiio, fundoId, mangada, fecha, peso, gpd, sincronizado "
			+ " FROM pesaje "
			+ " WHERE ganadoId = ? ";
	
	private static final String SQL_DELETE_GAN_PESAJE = ""
			+ "DELETE FROM pesaje "
			+ " WHERE id = ? ";
	
	private static final String SQL_DELETE_PESAJE_SYNCED = ""
			+ "DELETE FROM pesaje "
			+ " WHERE sincronizado = 'Y' ";
	
	private static final String SQL_DELETE_PESAJE = ""
			+ "DELETE FROM pesaje ";
	
	private static final String SQL_EXISTS_GANADO = ""
			+ "SELECT id "
			+ " FROM pesaje "
			+ " WHERE ganadoId = ? "
			+ " AND sincronizado = 'N' ";
	
	private static final String SQL_MANGADA_ACTUAL = ""
			+ "SELECT max(mangada) mangada "
			+ " FROM pesaje "
			+ " WHERE sincronizado = 'N' ";
	
    public static void insertPesaje(SqLiteTrx trx, List<Pesaje> list) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_PESAJE);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        
        for (Pesaje p : list){
        	statement.clearBindings();
        	statement.bindLong(1, p.getGan().getId());
        	statement.bindLong(2, p.getGan().getDiio());
        	statement.bindLong(3, p.getGan().getPredio());
        	if (p.getGan().getMangada() != null){
        		statement.bindLong(4, p.getGan().getMangada());
        	} else {
        		statement.bindNull(4);
        	}
        	statement.bindString(5, df.format(p.getFecha()));
        	statement.bindDouble(6, p.getPeso());
        	statement.bindDouble(7, p.getGpd());
        	statement.bindString(8, p.getSincronizado());
        	statement.executeInsert();
        }
    }
    
    public static List selectPesajeNoSync(SqLiteTrx trx) throws SQLException {
    	List list = new ArrayList();
    	
        boolean hayReg;
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_PESAJE_NO_SYNC, null);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	Pesaje p = new Pesaje();
        	Ganado g = new Ganado();
        	p.setId(c.getInt(c.getColumnIndex("id")));
        	g.setId(c.getInt(c.getColumnIndex("ganadoId")));
        	g.setDiio(c.getInt(c.getColumnIndex("ganadoDiio")));
        	g.setPredio(c.getInt(c.getColumnIndex("fundoId")));
        	try {
				p.setFecha(df.parse(c.getString(c.getColumnIndex("fecha"))));
				System.out.println(p.getFecha());
			} catch (ParseException e) {}
        	if (!c.isNull(c.getColumnIndex("mangada"))){
        		g.setMangada(c.getInt(c.getColumnIndex("mangada")));
        	}
        	p.setPeso(c.getDouble(c.getColumnIndex("peso")));
        	p.setGpd(c.getDouble(c.getColumnIndex("gpd")));
        	p.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
        	p.setGan(g);
        	
        	list.add(p);
        	hayReg = c.moveToNext();
        }
        return list;
    }
    
    public static List selectPesajeFundoNoSync(SqLiteTrx trx, Integer fundoId) throws SQLException {
    	List list = new ArrayList();
    	
        boolean hayReg;
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String[] args = {Integer.toString(fundoId)};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_PESAJE_FUNDO_NO_SYNC, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	Pesaje p = new Pesaje();
        	Ganado g = new Ganado();
        	p.setId(c.getInt(c.getColumnIndex("id")));
        	g.setId(c.getInt(c.getColumnIndex("ganadoId")));
        	g.setDiio(c.getInt(c.getColumnIndex("ganadoDiio")));
        	g.setPredio(c.getInt(c.getColumnIndex("fundoId")));
        	try {
				p.setFecha(df.parse(c.getString(c.getColumnIndex("fecha"))));
			} catch (ParseException e) {}
        	if (!c.isNull(c.getColumnIndex("mangada"))){
        		g.setMangada(c.getInt(c.getColumnIndex("mangada")));
        	}
        	p.setPeso(c.getDouble(c.getColumnIndex("peso")));
        	p.setGpd(c.getDouble(c.getColumnIndex("gpd")));
        	p.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
        	p.setGan(g);
        	
        	list.add(p);
        	hayReg = c.moveToNext();
        }
        return list;
    }
    
    public static Pesaje selectGanPesaje(SqLiteTrx trx, Integer ganadoId) throws SQLException {
    	Pesaje p = null;
    	
        boolean hayReg;
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String[] args = {Integer.toString(ganadoId)};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_GAN_PESAJE, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	p = new Pesaje();
        	Ganado g = new Ganado();
        	p.setId(c.getInt(c.getColumnIndex("id")));
        	g.setId(c.getInt(c.getColumnIndex("ganadoId")));
        	g.setDiio(c.getInt(c.getColumnIndex("ganadoDiio")));
        	g.setPredio(c.getInt(c.getColumnIndex("fundoId")));
        	try {
				p.setFecha(df.parse(c.getString(c.getColumnIndex("fecha"))));
			} catch (ParseException e) {}
        	if (!c.isNull(c.getColumnIndex("mangada"))){
        		g.setMangada(c.getInt(c.getColumnIndex("mangada")));
        	}
        	p.setPeso(c.getDouble(c.getColumnIndex("peso")));
        	p.setGpd(c.getDouble(c.getColumnIndex("gpd")));
        	p.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
        	p.setGan(g);
        	
        	hayReg = c.moveToNext();
        }
        return p;
    }
    
    public static void deleteGanPesaje(SqLiteTrx trx, Integer id) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_GAN_PESAJE);
    	statement.clearBindings();
    	statement.bindLong(1, id);
    	statement.executeUpdateDelete();
    }
    
    public static void deletePesajeSynced(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_PESAJE_SYNCED);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    public static void deletePesaje(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_PESAJE);
    	statement.clearBindings();
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
        return exists;
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
	
}
