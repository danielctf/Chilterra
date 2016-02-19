package cl.ar2.sqlite.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import cl.a2r.sip.model.Ecografia;
import cl.a2r.sip.model.Inseminacion;

public class EcografiasDAO {

	private static final String SQL_INSERT_INSEMINACION = ""
			+ "INSERT INTO inseminacion (id, ganadoId, fecha, sincronizado) "
			+ " VALUES (?, ?, ?, ?) ";
	
	private static final String SQL_SELECT_INSEMINACION_GANADO = ""
			+ "SELECT id, ganadoId, fecha, sincronizado "
			+ " FROM inseminacion "
			+ " WHERE ganadoId = ? ";
	
	private static final String SQL_DELETE_INSEMINACION = ""
			+ "DELETE FROM inseminacion";
	
	private static final String SQL_DELETE_INSEMINACION_SYNCED = ""
			+ "DELETE FROM inseminacion WHERE sincronizado = 'Y' ";
	
	private static final String SQL_INSERT_ECOGRAFIA = ""
			+ "INSERT INTO ecografia (ganadoId, ganadoDiio, fundoId, fecha, "
			+ " dias_prenez, ecografista_id, inseminacion_id, estado_id, problema_id, "
			+ " nota_id, mangada, sincronizado) "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	
	private static final String SQL_SELECT_ECOGRAFIA = ""
			+ "SELECT id, ganadoId, ganadoDiio, fundoId, fecha, "
			+ " dias_prenez, ecografista_id, inseminacion_id, estado_id, problema_id, nota_id, "
			+ " mangada "
			+ " FROM ecografia "
			+ " WHERE sincronizado = 'N' "
			+ " ORDER BY id DESC";
	
	private static final String SQL_DELETE_ECOGRAFIA_SYNCED = ""
			+ "DELETE FROM ecografia WHERE sincronizado = 'Y' ";
	
	private static final String SQL_DELETE_ECOGRAFIA = ""
			+ "DELETE FROM ecografia ";
	
	private static final String SQL_DELETE_ECOGRAFIA_ID = ""
			+ "DELETE FROM ecografia WHERE id = ? ";
	
	private static final String SQL_SELECT_ECOGRAFIA_GANADO = ""
			+ "SELECT id, ganadoId, ganadoDiio, fundoId, fecha, "
			+ " dias_prenez, ecografista_id, inseminacion_id, estado_id, problema_id, "
			+ " nota_id, mangada, sincronizado "
			+ " FROM ecografia "
			+ " WHERE ganadoId = ? ";
	
	private static final String SQL_SELECT_INSEMINACION_ID = ""
			+ "SELECT id, fecha "
			+ " FROM inseminacion "
			+ " WHERE id = ? ";
	
	private static final String SQL_SELECT_MANGADA_ACTUAL = ""
			+ "SELECT max(mangada) mangada "
			+ " FROM ecografia ";
	
	private static final String SQL_UPDATE_ECO_FUNDO = ""
			+ "UPDATE ecografia "
			+ " SET fundoId = ? "
			+ " WHERE ganadoId = ? ";
	
    public static void insertInseminacion(SqLiteTrx trx, List<Inseminacion> list) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_INSEMINACION);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        
        for (Inseminacion i : list){
        	statement.clearBindings();
        	statement.bindLong(1, i.getId());
        	statement.bindLong(2, i.getGan().getId());
        	statement.bindString(3, df.format(i.getFecha()));
        	statement.bindString(4, i.getSincronizado());
        	statement.executeInsert();
        }
    }
    
    public static List selectInseminacionGanado(SqLiteTrx trx, Integer ganadoId) throws SQLException {
        List list = new ArrayList();
        boolean hayReg;
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String[] args = {Integer.toString(ganadoId)};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_INSEMINACION_GANADO, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	
        	try {
            	Inseminacion i = new Inseminacion();
            	i.setId(c.getInt(c.getColumnIndex("id")));
            	i.getGan().setId(c.getInt(c.getColumnIndex("ganadoId")));
				i.setFecha(df.parse(c.getString(c.getColumnIndex("fecha"))));
				i.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
				list.add(i);
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	hayReg = c.moveToNext();
        	
        }

        return list;
    }
    
    public static void deleteInseminaciones(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_INSEMINACION);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    public static void deleteInseminacionesSincronizadas(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_INSEMINACION_SYNCED);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    public static void insertEcografia(SqLiteTrx trx, List<Ecografia> list) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_ECOGRAFIA);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		
        for (Ecografia e : list){
        	statement.clearBindings();
        	statement.bindLong(1, e.getGan().getId());
        	
        	if (e.getGan().getDiio() != null){
        		statement.bindLong(2, e.getGan().getDiio());
        	} else {
        		statement.bindNull(2);
        	}
        	
        	if (e.getFundoId() != null){
        		statement.bindLong(3, e.getFundoId());
        	} else {
        		statement.bindNull(3);
        	}
        	statement.bindString(4, df.format(e.getFecha()));
        	
        	if (e.getDias_prenez() != null){
        		statement.bindLong(5, e.getDias_prenez());
        	} else {
        		statement.bindNull(5);
        	}
        	
        	if (e.getEcografistaId() != null){
        		statement.bindLong(6, e.getEcografistaId());
        	} else {
        		statement.bindNull(6);
        	}
        	
        	if (e.getInseminacionId() != null){
        		statement.bindLong(7, e.getInseminacionId());
        	} else {
        		statement.bindNull(7);
        	}
        	
        	if (e.getEstadoId() != null){
        		statement.bindLong(8, e.getEstadoId());
        	} else {
        		statement.bindNull(8);
        	}
        	
        	if (e.getProblemaId() != null){
        		statement.bindLong(9, e.getProblemaId());
        	} else {
        		statement.bindNull(9);
        	}
        	
        	if (e.getNotaId() != null){
        		statement.bindLong(10, e.getNotaId());
        	} else {
        		statement.bindNull(10);
        	}
        	
        	if (e.getMangada() != null){
        		statement.bindLong(11, e.getMangada());
        	} else {
        		statement.bindNull(11);
        	}
        	statement.bindString(12, e.getSincronizado());
        	statement.executeInsert();
        }
    }
    
    public static List selectEcografias(SqLiteTrx trx) throws SQLException {
        List list = new ArrayList();
        boolean hayReg;
        
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_ECOGRAFIA, null);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	try {
            	Ecografia e = new Ecografia();
            	e.setId(c.getInt(c.getColumnIndex("id")));
            	e.getGan().setId(c.getInt(c.getColumnIndex("ganadoId")));
            	e.getGan().setDiio(c.getInt(c.getColumnIndex("ganadoDiio")));
            	e.getGan().setPredio(c.getInt(c.getColumnIndex("fundoId")));
				e.setFecha(df.parse(c.getString(c.getColumnIndex("fecha"))));
				if (!c.isNull(c.getColumnIndex("dias_prenez"))){
					e.setDias_prenez(c.getInt(c.getColumnIndex("dias_prenez")));
				}
				e.setEcografistaId(c.getInt(c.getColumnIndex("ecografista_id")));
				if (!c.isNull(c.getColumnIndex("inseminacion_id"))){
					e.setInseminacionId(c.getInt(c.getColumnIndex("inseminacion_id")));	
				}
				if (!c.isNull(c.getColumnIndex("estado_id"))){
					e.setEstadoId(c.getInt(c.getColumnIndex("estado_id")));	
				}
				if (!c.isNull(c.getColumnIndex("problema_id"))){
					e.setProblemaId(c.getInt(c.getColumnIndex("problema_id")));
				}
				if (!c.isNull(c.getColumnIndex("nota_id"))){
					e.setNotaId(c.getInt(c.getColumnIndex("nota_id")));	
				}
				if (!c.isNull(c.getColumnIndex("mangada"))){
					e.setMangada(c.getInt(c.getColumnIndex("mangada")));
				}
				list.add(e);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
        	hayReg = c.moveToNext();
        }

        return list;
    }
    
    public static void deleteEcografiasSincronizadas(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_ECOGRAFIA_SYNCED);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    public static void deleteEcografias(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_ECOGRAFIA);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    public static void deleteEcografiaId(SqLiteTrx trx, Integer id) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_ECOGRAFIA_ID);
    	statement.clearBindings();
    	statement.bindLong(1, id);
    	statement.executeUpdateDelete();
    }
    
    public static List selectEcografiaGanado(SqLiteTrx trx, Integer ganadoId) throws SQLException {
        List list = new ArrayList();
        boolean hayReg;
        
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String[] args = {Integer.toString(ganadoId)};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_ECOGRAFIA_GANADO, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	try {
            	Ecografia e = new Ecografia();
            	e.setId(c.getInt(c.getColumnIndex("id")));
            	e.getGan().setId(c.getInt(c.getColumnIndex("ganadoId")));
            	e.getGan().setDiio(c.getInt(c.getColumnIndex("ganadoDiio")));
            	e.getGan().setPredio(c.getInt(c.getColumnIndex("fundoId")));
				e.setFecha(df.parse(c.getString(c.getColumnIndex("fecha"))));
				if (!c.isNull(c.getColumnIndex("dias_prenez"))){
					e.setDias_prenez(c.getInt(c.getColumnIndex("dias_prenez")));
				}
				e.setEcografistaId(c.getInt(c.getColumnIndex("ecografista_id")));
				if (!c.isNull(c.getColumnIndex("inseminacion_id"))){
					e.setInseminacionId(c.getInt(c.getColumnIndex("inseminacion_id")));	
				}
				if (!c.isNull(c.getColumnIndex("estado_id"))){
					e.setEstadoId(c.getInt(c.getColumnIndex("estado_id")));	
				}
				if (!c.isNull(c.getColumnIndex("problema_id"))){
					e.setProblemaId(c.getInt(c.getColumnIndex("problema_id")));
				}
				if (!c.isNull(c.getColumnIndex("nota_id"))){
					e.setNotaId(c.getInt(c.getColumnIndex("nota_id")));
				}
				if (!c.isNull(c.getColumnIndex("mangada"))){
					e.setMangada(c.getInt(c.getColumnIndex("mangada")));
				}
				e.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
				list.add(e);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
        	hayReg = c.moveToNext();
        }

        return list;
    }
    
    public static Inseminacion selectInseminacionId(SqLiteTrx trx, Integer insId) throws SQLException {
        Inseminacion i = new Inseminacion();
        boolean hayReg;
        
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String[] args = {Integer.toString(insId)};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_INSEMINACION_ID, args);
        hayReg = c.moveToFirst();
        if ( hayReg ) {
        	try {
            	i.setId(c.getInt(c.getColumnIndex("id")));
				i.setFecha(df.parse(c.getString(c.getColumnIndex("fecha"))));
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	
        }

        return i;
    }
    
    public static Integer selectMangadaActual(SqLiteTrx trx) throws SQLException {
        Integer mangadaActual = null;
        boolean hayReg;
        
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_MANGADA_ACTUAL, null);
        hayReg = c.moveToFirst();
        if ( hayReg ) {
        	if (!c.isNull(c.getColumnIndex("mangada"))){
        		mangadaActual = c.getInt(c.getColumnIndex("mangada"));
        	}
        }

        return mangadaActual;
    }
    
    public static void updateEcoFundo(SqLiteTrx trx, Integer nuevoFundoId, Integer ganadoId) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_UPDATE_ECO_FUNDO);
    	statement.clearBindings();
    	statement.bindLong(1, nuevoFundoId);
    	statement.bindLong(2, ganadoId);
    	statement.executeUpdateDelete();
    }
    
}
