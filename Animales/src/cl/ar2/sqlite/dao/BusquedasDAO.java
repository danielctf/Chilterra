package cl.ar2.sqlite.dao;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.GanadoBusqueda;

public class BusquedasDAO {

	private static final String SQL_INSERT_BUSQUEDA = ""
			+ "INSERT INTO busqueda (ganadoId, ganadoDiio, flag, venta) "
			+ " VALUES (?, ?, ?, ?) ";
	
	private static final String SQL_SELECT_BUSQUEDA = ""
			+ "SELECT id, ganadoId, ganadoDiio, flag, venta "
			+ " FROM busqueda "
			+ " ORDER BY id DESC ";
	
	private static final String SQL_EXISTS_GANADO = ""
			+ "SELECT id "
			+ " FROM busqueda "
			+ " WHERE ganadoId = ? ";
	
    public static void insertBusqueda(SqLiteTrx trx, Ganado g) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_BUSQUEDA);
        
    	statement.clearBindings();
    	statement.bindLong(1, g.getId());
    	statement.bindLong(2, g.getDiio());
    	statement.bindLong(3, g.getFlag());
    	statement.bindLong(4, g.getVenta());
    	statement.executeInsert();
        
    }
    
    public static List selectGanBusqueda(SqLiteTrx trx) throws SQLException {
    	List list = new ArrayList();
    	
        boolean hayReg;
        
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_BUSQUEDA, null);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	GanadoBusqueda g = new GanadoBusqueda();
        	g.getGan().setSqlId(c.getInt(c.getColumnIndex("id")));
        	g.getGan().setId(c.getInt(c.getColumnIndex("ganadoId")));
        	g.getGan().setDiio(c.getInt(c.getColumnIndex("ganadoDiio")));
        	g.getGan().setFlag(c.getInt(c.getColumnIndex("flag")));
        	g.getGan().setVenta(c.getInt(c.getColumnIndex("venta")));

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
	
}
