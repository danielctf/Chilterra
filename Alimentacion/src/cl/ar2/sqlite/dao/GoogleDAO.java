package cl.ar2.sqlite.dao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import cl.a2r.sap.model.Persona;

public class GoogleDAO {
	
    private static final String SQL_SELECT_PERSONA = ""
            + "SELECT * FROM google_info WHERE correo = ?";
    
    private static final String SQL_INSERT_PERSONA = ""
            + "INSERT INTO google_info "
            + "       (nombre, correo, photo) "
            + "VALUES ( ?, ?, ? )";
    
    private static final String SQL_UPDATE_PHOTO = ""
    		+ "UPDATE google_info "
    		+ " SET photo = ? "
    		+ " WHERE correo = ?";
    
    public static Persona selectPersona(SqLiteTrx trx, String correo) throws SQLException {
        Persona p = new Persona();
        boolean hayReg;

        String[] args = {correo};
        
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_PERSONA, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	
        	p.setId(c.getInt(c.getColumnIndex("person_id")));
        	p.setNombre(c.getString(c.getColumnIndex("nombre")));
        	p.setCorreo(c.getString(c.getColumnIndex("correo")));
        	p.setPhoto(c.getBlob(c.getColumnIndex("photo")));
        	
            hayReg = c.moveToNext();
        }

        return p;
    }
    
    public static void insertaPersona(SqLiteTrx trx, Persona p) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_PERSONA);

        statement.clearBindings();
        statement.bindString(1, p.getNombre());
        statement.bindString(2, p.getCorreo());
        statement.bindBlob(3, p.getPhoto());
        statement.executeInsert();

    }
    
    public static void updatePhoto(SqLiteTrx trx, Persona newP) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_UPDATE_PHOTO);
    	statement.clearBindings();
    	statement.bindBlob(1, newP.getPhoto());
    	statement.bindString(2, newP.getCorreo());
    	statement.executeUpdateDelete();
    }
    
}
