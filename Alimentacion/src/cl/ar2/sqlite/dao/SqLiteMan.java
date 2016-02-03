/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.ar2.sqlite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * @author Miguel Vega Brante
 */
public class SqLiteMan extends SQLiteOpenHelper {

    private static final String dbName="alimentacion";
    private static final int version = 3;

    public SqLiteMan(Context context) {
        super(context, dbName, null, version);
    }

    public void onCreate(SQLiteDatabase db) {
    	
    	db.execSQL(""
            + "CREATE TABLE actualizado ( "
			+ " fecha_actualizado TEXT) ");
    	
    	db.execSQL(""
            + "CREATE TABLE tipo_medicion ( "
			+ " a_tipo_medicion_id INTEGER PRIMARY KEY, "
			+ " codigo TEXT, "
			+ " nombre TEXT ) ");
    	
    	db.execSQL(""
            + "CREATE TABLE potrero ( "
    		+ " a_potrero_id INTEGER PRIMARY KEY, "
    		+ " numero INTEGER, "
    		+ " superficie REAL, "
    		+ " g_fundo_id INTEGER, "
    		+ " a_tipo_siembra_id INTEGER, "
    		+ " calificacion INTEGER, "
    		+ " sincronizado TEXT ) ");
    	
    	db.execSQL(""
            + "CREATE TABLE medicion ( "
            + " a_medicion_id INTEGER PRIMARY KEY, "
            + " isactive TEXT, "
            + " fecha_medicion TEXT, "
            + " inicial INTEGER, "
            + " final INTEGER, "
            + " muestra INTEGER, "
            + " materia_seca INTEGER, "
            + " medidor INTEGER, "
            + " a_tipo_medicion_id INTEGER, "
            + " a_potrero_id INTEGER, "
            + " animales INTEGER, "
            + " sincronizado TEXT, "
            + " FOREIGN KEY (a_potrero_id) REFERENCES potrero (a_potrero_id) ) ");
        
        db.execSQL(""
        	+ "CREATE TABLE google_info ( "
        	+ " person_id INTEGER PRIMARY KEY,"
        	+ " nombre TEXT,"
        	+ " correo TEXT,"
        	+ " photo BLOB)");

        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aqui dependiendo de los numeros de version se deben crear o modificar las nuevas tablas
        // Las actualizaciones debieran borrar la tabla anterior y crear las nuevas
        // Siempre debieran estar todos los pasos de cambios desde la version 1 
    	
    	db.execSQL("DROP TABLE IF EXISTS registro_medicion");
    	db.execSQL("DROP TABLE IF EXISTS stock");
    	db.execSQL("DROP TABLE IF EXISTS calificacion");
    	
    	db.execSQL(""
                + "CREATE TABLE actualizado ( "
    			+ " fecha_actualizado TEXT) ");
        	
        	db.execSQL(""
                + "CREATE TABLE tipo_medicion ( "
    			+ " a_tipo_medicion_id INTEGER PRIMARY KEY, "
    			+ " codigo TEXT, "
    			+ " nombre TEXT ) ");
        	
        	db.execSQL(""
                + "CREATE TABLE potrero ( "
        		+ " a_potrero_id INTEGER PRIMARY KEY, "
        		+ " numero INTEGER, "
        		+ " superficie REAL, "
        		+ " g_fundo_id INTEGER, "
        		+ " a_tipo_siembra_id INTEGER, "
        		+ " calificacion INTEGER, "
        		+ " sincronizado TEXT ) ");
        	
        	db.execSQL(""
                + "CREATE TABLE medicion ( "
                + " a_medicion_id INTEGER PRIMARY KEY, "
                + " isactive TEXT, "
                + " fecha_medicion TEXT, "
                + " inicial INTEGER, "
                + " final INTEGER, "
                + " muestra INTEGER, "
                + " materia_seca INTEGER, "
                + " medidor INTEGER, "
                + " a_tipo_medicion_id INTEGER, "
                + " a_potrero_id INTEGER, "
                + " animales INTEGER, "
                + " sincronizado TEXT, "
                + " FOREIGN KEY (a_potrero_id) REFERENCES potrero (a_potrero_id) ) ");

    }

}
