package cl.ar2.sqlite.dao;

import java.util.ArrayList;
import java.util.List;
import cl.a2r.sip.model.VRB51;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;

public class RB51DAO {

	private static final String SQL_INSERT_RB51 = ""
			+ "INSERT INTO rb51 (ganadoId, ganadoDiio, fundoId, mangada, bang, med_control_id, "
			+ " serie, sincronizado) "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?) ";
	
	private static final String SQL_SELECT_RB51 = ""
			+ "SELECT id, ganadoId, ganadoDiio, fundoId, mangada, bang, med_control_id, "
			+ " serie, sincronizado "
			+ " FROM rb51 ";
	
	private static final String SQL_DELETE_GAN_RB51 = ""
			+ "DELETE FROM rb51 "
			+ " WHERE id = ? ";
	
	private static final String SQL_DELETE_RB51 = ""
			+ "DELETE FROM rb51";
	
    public static void insertRB51(SqLiteTrx trx, List<VRB51> list) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_RB51);
        
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
        	statement.bindString(5, rb.getBang());
        	statement.bindLong(6, rb.getMed().getId());
        	statement.bindLong(7, rb.getMed().getSerie());
        	statement.bindString(8, rb.getSincronizado());
        	statement.executeInsert();	
        }
    }
    
    public static List selectRB51(SqLiteTrx trx) throws SQLException {
        List list = new ArrayList();
        boolean hayReg;
        
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
        	rb.setBang(c.getString(c.getColumnIndex("bang")));
        	rb.getMed().setId(c.getInt(c.getColumnIndex("med_control_id")));
        	rb.getMed().setSerie(c.getInt(c.getColumnIndex("serie")));
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
    
}
