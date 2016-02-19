package cl.ar2.sqlite.dao;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Traslado;

public class TrasladosDAO {

	private static final String SQL_REUBICA_GANADO = ""
			+ "INSERT INTO reubicacion (ganadoId, fundoOrigenId, fundoDestinoId) "
			+ " VALUES (?, ?, ?) ";
	
	private static final String SQL_SELECT_REUBICACIONES = ""
			+ "SELECT id, ganadoId, fundoOrigenId, fundoDestinoId "
			+ " FROM reubicacion ";
	
	private static final String SQL_DELETE_REUBICACIONES = ""
			+ "DELETE FROM reubicacion";
	
	private static final String SQL_UPDATE_GANADO_FUNDO = ""
			+ "UPDATE diio "
			+ " SET fundoId = ? "
			+ " WHERE id = ? ";
	
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
	
}
