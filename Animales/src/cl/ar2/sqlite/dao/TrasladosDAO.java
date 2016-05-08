package cl.ar2.sqlite.dao;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import cl.a2r.animales.Login;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Instancia;

public class TrasladosDAO {

	private static final String SQL_INSERT_REUBICACION = ""
			+ "INSERT INTO reubicacion (ganadoId, fundoDestinoId) "
			+ " VALUES (?, ?) ";
	
	private static final String SQL_SELECT_REUBICACIONES = ""
			+ "SELECT id, ganadoId, fundoDestinoId "
			+ " FROM reubicacion ";
	
	private static final String SQL_DELETE_REUBICACIONES = ""
			+ "DELETE FROM reubicacion";
	
	private static final String SQL_UPDATE_GANADO_FUNDO = ""
			+ "UPDATE diio "
			+ " SET fundoId = ? "
			+ " WHERE id = ? ";
	
	private static final String SQL_INSERTA_TRASLADO = ""
			+ "INSERT INTO traslado (ganadoId, ganadoDiio, mangada, tipo_ganado, instancia) "
			+ " VALUES (?, ?, ?, ?, ?) ";
	
	private static final String SQL_SELECT_GAN_TRASLADO = ""
			+ "SELECT id, ganadoId, ganadoDiio, mangada, tipo_ganado, instancia "
			+ " FROM traslado "
			+ " ORDER BY id DESC ";
	
	private static final String SQL_EXISTS_GANADO = ""
			+ "SELECT id "
			+ " FROM traslado "
			+ " WHERE ganadoId = ? ";
	
	private static final String SQL_DELETE_TRASLADO = ""
			+ "DELETE FROM traslado ";
	
	private static final String SQL_DELETE_GANADO_TRASLADO = ""
			+ "DELETE FROM traslado "
			+ " WHERE id = ? ";
	
	private static final String SQL_CHECK_INSTANCE = ""
			+ "SELECT id "
			+ " FROM traslado "
			+ " WHERE instancia != ? "
			+ " LIMIT 1 ";
	
	private static final String SQL_MANGADA_ACTUAL = ""
			+ "SELECT max(mangada) mangada "
			+ " FROM traslado ";
	
	/*
    public static void insertReubicacion(SqLiteTrx trx, Traslado t) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_REUBICA_GANADO);

        for (Ganado g : t.getGanado()){
	    	statement.clearBindings();
	    	statement.bindLong(1, g.getId());
	    	statement.bindLong(2, t.getFundoOrigenId());
	    	statement.bindLong(3, t.getFundoDestinoId());
	    	statement.executeInsert();
        }
    }
    
    public static List selectReubicaciones(SqLiteTrx trx) throws SQLException {
        List list = new ArrayList();
        boolean hayReg;
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_REUBICACIONES, null);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	Traslado t = new Traslado();
        	Ganado g = new Ganado();
        	g.setId(c.getInt(c.getColumnIndex("ganadoId")));
        	t.setFundoOrigenId(c.getInt(c.getColumnIndex("fundoOrigenId")));
        	t.setFundoDestinoId(c.getInt(c.getColumnIndex("fundoDestinoId")));
        	t.getGanado().add(g);
        	list.add(t);
            hayReg = c.moveToNext();
        }
        return list;
    }
    */
	
    public static void deleteReubicaciones(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_REUBICACIONES);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
	
    public static void updateGanadoFundo(SqLiteTrx trx, Integer nuevoFundoId, Integer ganadoId) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_UPDATE_GANADO_FUNDO);
    	statement.clearBindings();
    	statement.bindLong(1, nuevoFundoId);
    	statement.bindLong(2, ganadoId);
    	statement.executeUpdateDelete();
    }
    
    //----------------------------- TRASLADOS V2 -------------------------------
    
    public static void insertaTraslado(SqLiteTrx trx, Instancia superInstancia) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERTA_TRASLADO);
        
        for (Ganado g : superInstancia.getInstancia().getGanList()){
	    	statement.clearBindings();
	    	statement.bindLong(1, g.getId());
	    	statement.bindLong(2, g.getDiio());
	    	if (g.getMangada() != null){
	    		statement.bindLong(3, g.getMangada());
	    	} else {
	    		statement.bindNull(3);
	    	}
	    	statement.bindLong(4, g.getTipoGanadoId());
	    	statement.bindLong(5, superInstancia.getId());
	    	statement.executeInsert();
        }
    }
    
    public static List selectGanTraslado(SqLiteTrx trx) throws SQLException {
    	List list = new ArrayList();
        boolean hayReg;
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_GAN_TRASLADO, null);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	Ganado g = new Ganado();
        	g.setSqlId(c.getInt(c.getColumnIndex("id")));
        	g.setId(c.getInt(c.getColumnIndex("ganadoId")));
        	g.setDiio(c.getInt(c.getColumnIndex("ganadoDiio")));
        	if (!c.isNull(c.getColumnIndex("mangada"))){
        		g.setMangada(c.getInt(c.getColumnIndex("mangada")));
        	}
        	g.setTipoGanadoId(c.getInt(c.getColumnIndex("tipo_ganado")));
        	list.add(g);
            hayReg = c.moveToNext();
        }
        return list;
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
    
    public static void deleteTraslado(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_TRASLADO);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    public static void deleteGanadoTraslado(SqLiteTrx trx, Integer id) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_GANADO_TRASLADO);
    	statement.clearBindings();
    	statement.bindLong(1, id);
    	statement.executeUpdateDelete();
    }
    
    public static boolean checkInstance(SqLiteTrx trx, Integer superInstanciaId) throws SQLException {
    	boolean replace = false;
        boolean hayReg;
        String[] args = {Integer.toString(superInstanciaId)};
        Cursor c = trx.getDB().rawQuery(SQL_CHECK_INSTANCE, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	replace = true;
            hayReg = c.moveToNext();
        }
        return replace;
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
	
    //---------------------------- REUBICACION V2 -------------------------------------
    
    public static void insertReubicacion(SqLiteTrx trx, Instancia instancia) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_REUBICACION);

        for (Ganado g : instancia.getGanList()){
	    	statement.clearBindings();
	    	statement.bindLong(1, g.getId());
	    	statement.bindLong(2, instancia.getFundoId());
	    	statement.executeInsert();
        }
    }
    
    public static List selectReubicaciones(SqLiteTrx trx) throws SQLException {
        List<Instancia> instList = new ArrayList<Instancia>();
        
        boolean hayReg;
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_REUBICACIONES, null);
        hayReg = c.moveToFirst();
        
        while ( hayReg ) {
        	Ganado g = new Ganado();
        	g.setId(c.getInt(c.getColumnIndex("ganadoId")));
        	Integer fundoDestinoId = c.getInt(c.getColumnIndex("fundoDestinoId"));
        	
        	boolean exists = false;
        	for (Instancia i : instList){
        		if (i.getFundoId().intValue() == fundoDestinoId.intValue()){
        			i.getGanList().add(g);
        			exists = true;
        			break;
        		}
        	}
        	if (!exists){
        		Instancia instancia = new Instancia();
        		List<Ganado> ganList = new ArrayList<Ganado>();
        		ganList.add(g);
        		instancia.setGanList(ganList);
        		instancia.setFundoId(fundoDestinoId);
        		instancia.setUsuarioId(Login.user);
        		instList.add(instancia);
        	}
        	
            hayReg = c.moveToNext();
        }
        return instList;
    }
    
}
