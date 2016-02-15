package cl.ar2.sqlite.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Salvataje;

public class SalvatajesDAO {
	
	private static final String SQL_INSERT_GRUPO = ""
			+ "INSERT INTO salvataje (nombre, fecha) "
			+ " VALUES (?, ?) ";
	
	private static final String SQL_SELECT_GRUPOS = ""
			+ "SELECT id, nombre, fecha "
			+ " FROM salvataje ";
	
	private static final String SQL_DELETE_GRUPO = ""
			+ "DELETE FROM salvataje "
			+ " WHERE id = ? ";
	
	private static final String SQL_INSERT_DIIOEID = ""
			+ "INSERT INTO salvataje_diio (diioeid, observacion, s_id) "
			+ " VALUES (?, ?, ?) ";
	
	private static final String SQL_SELECT_DIIOEID = ""
			+ "SELECT id, diioeid, observacion "
			+ " FROM salvataje_diio "
			+ " WHERE s_id = ? "
			+ " ORDER BY id DESC ";
	
	private static final String SQL_DELETE_DIIOEID = ""
			+ "DELETE FROM salvataje_diio "
			+ " WHERE id = ? ";
	
	private static final String SQL_DELETE_GRUPO_DIIOEID = ""
			+ "DELETE FROM salvataje_diio "
			+ " WHERE s_id = ? ";
	
    public static void insertGrupo(SqLiteTrx trx, Salvataje s) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_GRUPO);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        
    	statement.clearBindings();
    	statement.bindString(1, s.getNombreGrupo());
    	statement.bindString(2, df.format(s.getFecha()));
    	statement.executeInsert();
    }
    
    public static List selectGrupos(SqLiteTrx trx) throws SQLException {
        List list = new ArrayList();
        boolean hayReg;

        Cursor c = trx.getDB().rawQuery(SQL_SELECT_GRUPOS, null);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        hayReg = c.moveToFirst();
        
        while ( hayReg ) {
        	try {
            	Salvataje s = new Salvataje();
            	s.setGrupoId(c.getInt(c.getColumnIndex("id")));
            	s.setNombreGrupo(c.getString(c.getColumnIndex("nombre")));
				s.setFecha(df.parse(c.getString(c.getColumnIndex("fecha"))));
				list.add(s);
			} catch (ParseException e) {
				e.printStackTrace();
			} finally {
				hayReg = c.moveToNext();
			}
        }

        return list;
    }
    
    public static void deleteGrupo(SqLiteTrx trx, Integer grupoId) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_GRUPO);
    	statement.clearBindings();
    	statement.bindLong(1, grupoId);
    	statement.executeUpdateDelete();
    }
    
    public static void insertDiio(SqLiteTrx trx, Salvataje s) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_DIIOEID);
        
        for (Ganado g : s.getGanado()){
        	statement.clearBindings();
        	statement.bindString(1, g.getEid());
        	statement.bindString(2, g.getObservacion());
        	statement.bindLong(3, s.getGrupoId());
        	statement.executeInsert();	
        }
    }
    
    public static List selectDiio(SqLiteTrx trx, Integer grupoId) throws SQLException {
        List list = new ArrayList();
        boolean hayReg;

        String[] args = {Integer.toString(grupoId)};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_DIIOEID, args);
        hayReg = c.moveToFirst();
        
        while ( hayReg ) {
        	Ganado g = new Ganado();
        	//El id no es en realidad el ganadoId, es el Id de la tabla...
        	g.setId(c.getInt(c.getColumnIndex("id")));
        	g.setEid(c.getString(c.getColumnIndex("diioeid")));
        	g.setObservacion(c.getString(c.getColumnIndex("observacion")));
        	list.add(g);
        	hayReg = c.moveToNext();
        }

        return list;
    }
    
    public static void deleteDiio(SqLiteTrx trx, Integer id) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_DIIOEID);
    	statement.clearBindings();
    	statement.bindLong(1, id);
    	statement.executeUpdateDelete();
    }
    
    public static void deleteGrupoDiio(SqLiteTrx trx, Integer grupoId) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_GRUPO_DIIOEID);
    	statement.clearBindings();
    	statement.bindLong(1, grupoId);
    	statement.executeUpdateDelete();
    }

}
