package cl.ar2.sqlite.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import cl.a2r.sap.model.Medicion;
import cl.ar2.sqlite.cobertura.RegistroMedicion;
import cl.ar2.sqlite.cobertura.Util;

public class MedicionDAO {
	
    private static final String SQL_INSERT_MEDICION = ""
            + "INSERT INTO registro_medicion "
            + "       (fecha_hora, medicion, sincronizado) "
            + "VALUES ( ?, ?, ? )";

    private static final String SQL_SELECT_MEDICION = ""
            + "SELECT * FROM registro_medicion";
    
    private static final String SQL_DELETE_MEDICION = ""
    		+ "DELETE FROM registro_medicion WHERE registro_medicion_id = ?";

    public static void insertaMedicion(SqLiteTrx trx, Medicion med) throws SQLException {

        byte[] bytes = Util.serializa(med);

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_MEDICION);

        statement.clearBindings();
        statement.bindLong(1, new Date().getTime() );
        statement.bindBlob(2, bytes);
        statement.bindString(3, "N");
        statement.executeInsert();

    }

    public static List selectMediciones(SqLiteTrx trx) throws SQLException {
        List<RegistroMedicion> list = new ArrayList<RegistroMedicion>();
        boolean hayReg;

        Cursor c = trx.getDB().rawQuery(SQL_SELECT_MEDICION, null);
        hayReg = c.moveToFirst();
        while ( hayReg ) {

            RegistroMedicion regMed = new RegistroMedicion();

            regMed.setId(c.getInt(c.getColumnIndex("registro_medicion_id")));
            regMed.setFechaHora(new Date(c.getInt(c.getColumnIndex("fecha_hora"))));

            byte[] bytes = c.getBlob(c.getColumnIndex("medicion"));
            Medicion med = (Medicion) Util.desSerializa(bytes);
            regMed.setMedicion(med);

            regMed.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));

            list.add(regMed);

            hayReg = c.moveToNext();
        }

        return list;

    }
    
    public static void deleteMedicion(SqLiteTrx trx, Integer registro_medicion_id) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_MEDICION);
    	statement.clearBindings();
    	statement.bindLong(1, registro_medicion_id);
    	statement.executeUpdateDelete();
    }
    
}
