package cl.ar2.sqlite.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cl.a2r.sip.model.Bang;
import cl.a2r.sip.model.VRB51;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;

public class RB51DAO {

	private static final String SQL_INSERT_RB51 = ""
			+ "INSERT INTO rb51 (ganadoId, ganadoDiio, fundoId, mangada, bang_id, bang, "
			+ " med_control_id, serie, fecha, sincronizado) "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	
	private static final String SQL_SELECT_RB51 = ""
			+ "SELECT id, ganadoId, ganadoDiio, fundoId, mangada, bang_id, bang, "
			+ " med_control_id, serie, fecha, sincronizado "
			+ " FROM rb51 "
			+ " ORDER BY id DESC ";
	
	private static final String SQL_SELECT_RB51_NO_SYNC = ""
			+ "SELECT id, ganadoId, ganadoDiio, fundoId, mangada, bang_id, bang, "
			+ " med_control_id, serie, fecha, sincronizado "
			+ " FROM rb51 "
			+ " WHERE sincronizado = 'N' "
			+ " ORDER BY id DESC ";
	
	private static final String SQL_DELETE_GAN_RB51 = ""
			+ "DELETE FROM rb51 "
			+ " WHERE id = ? ";
	
	private static final String SQL_DELETE_RB51 = ""
			+ "DELETE FROM rb51";
	
	private static final String SQL_DELETE_RB51_SYNC = ""
			+ "DELETE FROM rb51 "
			+ " WHERE sincronizado = 'Y' ";
	
	private static final String SQL_SELECT_ENCONTRADOS = ""
			+ "SELECT id, ganadoId, ganadoDiio, fundoId, mangada, bang_id, bang, "
			+ " med_control_id, serie, fecha, sincronizado "
			+ " FROM rb51 "
			+ " WHERE fundoId = ? "
			+ " ORDER BY id DESC ";
	
	private static final String SQL_INSERT_BANG = ""
			+ "INSERT INTO bang (id, bang, borrar) "
			+ " VALUES (?, ?, ?) ";
	
	private static final String SQL_SELECT_BANG = ""
			+ "SELECT id, bang, borrar "
			+ " FROM bang "
			+ " WHERE borrar = 'N' "
			+ " AND id NOT IN (SELECT bang_id "
			+ "				   FROM rb51) ";
	
	private static final String SQL_DELETE_ALL_BANG = ""
			+ "DELETE FROM bang ";
	
	private static final String SQL_BORRAR_BANG = ""
			+ "UPDATE bang "
			+ " SET borrar = 'Y' "
			+ " WHERE id = ? ";
	
	private static final String SQL_SELECT_BANG_NO_SYNC = ""
			+ "SELECT id, bang, borrar "
			+ " FROM bang "
			+ " WHERE borrar = 'Y' ";
	
	private static final String SQL_EXISTS_GANADO = ""
			+ "SELECT count(ganadoId) cuenta "
			+ " FROM rb51 "
			+ " WHERE ganadoId = ? "
			+ " GROUP BY ganadoId ";
	
	private static final String SQL_SELECT_MANGADA_ACTUAL = ""
			+ "SELECT max(mangada) mangada "
			+ " FROM rb51 "
			+ " WHERE sincronizado = 'N' "
			+ " AND fundoId = ? ";
	
    public static void insertRB51(SqLiteTrx trx, List<VRB51> list) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_RB51);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        
        for (VRB51 rb : list){
        	statement.clearBindings();
        	statement.bindLong(1, rb.getGan().getId());
        	statement.bindLong(2, rb.getGan().getDiio());
        	statement.bindLong(3, rb.getGan().getPredio());
        	if (rb.getGan().getMangada() != null){
        		statement.bindLong(4, rb.getGan().getMangada());
        	} else {
        		statement.bindNull(4);
        	}
        	statement.bindLong(5, rb.getBang().getId());
        	statement.bindString(6, rb.getBang().getBang());
        	statement.bindLong(7, rb.getMed().getId());
        	statement.bindLong(8, rb.getMed().getSerie());
        	statement.bindString(9, df.format(rb.getFecha()));
        	statement.bindString(10, rb.getSincronizado());
        	statement.executeInsert();	
        }
    }
    
    public static List selectRB51(SqLiteTrx trx) throws SQLException {
        List list = new ArrayList();
        boolean hayReg;
        
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_RB51, null);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	VRB51 rb = new VRB51();
        	rb.setId(c.getInt(c.getColumnIndex("id")));
        	rb.getGan().setId(c.getInt(c.getColumnIndex("ganadoId")));
        	rb.getGan().setDiio(c.getInt(c.getColumnIndex("ganadoDiio")));
        	rb.getGan().setPredio(c.getInt(c.getColumnIndex("fundoId")));
        	if (!c.isNull(c.getColumnIndex("mangada"))){
        		rb.getGan().setMangada(c.getInt(c.getColumnIndex("mangada")));
        	}
        	rb.getBang().setId(c.getInt(c.getColumnIndex("bang_id")));
        	rb.getBang().setBang(c.getString(c.getColumnIndex("bang")));
        	rb.getMed().setId(c.getInt(c.getColumnIndex("med_control_id")));
        	rb.getMed().setSerie(c.getInt(c.getColumnIndex("serie")));
        	try {
				rb.setFecha(df.parse(c.getString(c.getColumnIndex("fecha"))));
			} catch (ParseException e) {}
        	rb.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
        	
        	list.add(rb);
            hayReg = c.moveToNext();
        }

        return list;
    }
    
    public static List selectNoSyncRB51(SqLiteTrx trx) throws SQLException {
        List list = new ArrayList();
        boolean hayReg;
        
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_RB51_NO_SYNC, null);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	VRB51 rb = new VRB51();
        	rb.setId(c.getInt(c.getColumnIndex("id")));
        	rb.getGan().setId(c.getInt(c.getColumnIndex("ganadoId")));
        	rb.getGan().setDiio(c.getInt(c.getColumnIndex("ganadoDiio")));
        	rb.getGan().setPredio(c.getInt(c.getColumnIndex("fundoId")));
        	if (!c.isNull(c.getColumnIndex("mangada"))){
        		rb.getGan().setMangada(c.getInt(c.getColumnIndex("mangada")));
        	}
        	rb.getBang().setId(c.getInt(c.getColumnIndex("bang_id")));
        	rb.getBang().setBang(c.getString(c.getColumnIndex("bang")));
        	rb.getMed().setId(c.getInt(c.getColumnIndex("med_control_id")));
        	rb.getMed().setSerie(c.getInt(c.getColumnIndex("serie")));
        	try {
				rb.setFecha(df.parse(c.getString(c.getColumnIndex("fecha"))));
			} catch (ParseException e) {}
        	rb.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
        	
        	list.add(rb);
            hayReg = c.moveToNext();
        }

        return list;
    }
    
    public static void deleteGanRB51(SqLiteTrx trx, Integer id) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_GAN_RB51);
    	statement.clearBindings();
    	statement.bindLong(1, id);
    	statement.executeUpdateDelete();
    }
    
    public static void deleteRB51(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_RB51);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    public static void deleteSyncedRB51(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_RB51_SYNC);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    public static List selectCandidatosEncontrados(SqLiteTrx trx, Integer fundoId) throws SQLException {
        List list = new ArrayList();
        boolean hayReg;
        
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String[] args = {Integer.toString(fundoId)};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_ENCONTRADOS, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	VRB51 rb = new VRB51();
        	rb.setId(c.getInt(c.getColumnIndex("id")));
        	rb.getGan().setId(c.getInt(c.getColumnIndex("ganadoId")));
        	rb.getGan().setDiio(c.getInt(c.getColumnIndex("ganadoDiio")));
        	rb.getGan().setPredio(c.getInt(c.getColumnIndex("fundoId")));
        	if (!c.isNull(c.getColumnIndex("mangada"))){
        		rb.getGan().setMangada(c.getInt(c.getColumnIndex("mangada")));
        	}
        	rb.getBang().setId(c.getInt(c.getColumnIndex("bang_id")));
        	rb.getBang().setBang(c.getString(c.getColumnIndex("bang")));
        	rb.getMed().setId(c.getInt(c.getColumnIndex("med_control_id")));
        	rb.getMed().setSerie(c.getInt(c.getColumnIndex("serie")));
        	try {
				rb.setFecha(df.parse(c.getString(c.getColumnIndex("fecha"))));
			} catch (ParseException e) {}
        	rb.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
        	
        	list.add(rb);
            hayReg = c.moveToNext();
        }

        return list;
    }
    
    public static void insertBang(SqLiteTrx trx, List<Bang> list) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_BANG);
        
        for (Bang b : list){
        	statement.clearBindings();
        	statement.bindLong(1, b.getId());
        	statement.bindString(2, b.getBang());
        	statement.bindString(3, b.getBorrar());
        	statement.executeInsert();	
        }
    }
    
    public static List selectBang(SqLiteTrx trx) throws SQLException {
        List list = new ArrayList();
        boolean hayReg;
        
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_BANG, null);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	Bang b = new Bang();
        	b.setId(c.getInt(c.getColumnIndex("id")));
        	b.setBang(c.getString(c.getColumnIndex("bang")));
        	b.setBorrar(c.getString(c.getColumnIndex("borrar")));
        	list.add(b);
            hayReg = c.moveToNext();
        }

        return list;
    }
    
    public static void deleteAllBang(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_ALL_BANG);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    public static void borrarBang(SqLiteTrx trx, Integer id) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_BORRAR_BANG);
    	statement.clearBindings();
    	statement.bindLong(1, id);
    	statement.executeUpdateDelete();
    }
    
    public static List selectNoSyncBang(SqLiteTrx trx) throws SQLException {
        List list = new ArrayList();
        boolean hayReg;
        
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_BANG_NO_SYNC, null);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	Bang b = new Bang();
        	b.setId(c.getInt(c.getColumnIndex("id")));
        	b.setBang(c.getString(c.getColumnIndex("bang")));
        	b.setBorrar(c.getString(c.getColumnIndex("borrar")));
        	list.add(b);
            hayReg = c.moveToNext();
        }

        return list;
    }
    
    public static boolean existsGanado(SqLiteTrx trx, Integer ganadoId, Integer numeroVacuna) throws SQLException {
        boolean exists = false;
        boolean hayReg;
        
        String[] args = {Integer.toString(ganadoId)};
        Cursor c = trx.getDB().rawQuery(SQL_EXISTS_GANADO, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	int cuenta = c.getInt(c.getColumnIndex("cuenta"));
        	if (numeroVacuna.intValue() == 1){
        		if (cuenta >= 1){
        			exists = true;
        		}
        	} else if (numeroVacuna.intValue() == 2){
        		if (cuenta >= 2){
        			exists = true;
        		}
        	}
            hayReg = c.moveToNext();
        }

        return exists;
    }
    
    public static Integer mangadaActual(SqLiteTrx trx, Integer fundoId) throws SQLException {
        Integer mangadaActual = null;
        boolean hayReg;
        
        String[] args = {Integer.toString(fundoId)};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_MANGADA_ACTUAL, args);
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
