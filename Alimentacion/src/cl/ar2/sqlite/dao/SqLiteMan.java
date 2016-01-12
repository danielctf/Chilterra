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
    private static final int version = 2;

    public SqLiteMan(Context context) {
        super(context, dbName, null, version);
      
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( ""
            + "CREATE TABLE registro_medicion ( "
            + " registro_medicion_id INTEGER PRIMARY KEY, "
            + " fecha_hora INTEGER, "
            + " medicion BLOB, "
            + " sincronizado TEXT )" );
        
        db.execSQL(""
        	+ "CREATE TABLE google_info ( "
        	+ " person_id INTEGER PRIMARY KEY,"
        	+ " nombre TEXT,"
        	+ " correo TEXT,"
        	+ " photo BLOB)");
        
        db.execSQL(""
            	+ "CREATE TABLE stock ( "
            	+ " stock_id INTEGER PRIMARY KEY,"
            	+ " medicion BLOB, "
            	+ " actualizado TEXT )");
        
        db.execSQL(""
        		+ "CREATE TABLE calificacion ("
        		+ " cal_id INTEGER PRIMARY KEY,"
        		+ " fundo_id INTEGER, "
        		+ " numero INTEGER, "
        		+ " calificacion INTEGER, "
        		+ " sincronizado TEXT)");
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aqui dependiendo de los numeros de version se deben crear o modificar las nuevas tablas
        // Las actualizaciones debieran borrar la tabla anterior y crear las nuevas
        // Siempre debieran estar todos los pasos de cambios desde la version 1 
    	
    	//----------------- VERSION 2 -----------------
        db.execSQL(""
            	+ "CREATE TABLE stock ( "
            	+ " stock_id INTEGER PRIMARY KEY,"
            	+ " medicion BLOB, "
            	+ " actualizado TEXT )");
        
        db.execSQL(""
        		+ "CREATE TABLE calificacion ("
        		+ " cal_id INTEGER PRIMARY KEY,"
        		+ " fundo_id INTEGER, "
        		+ " numero INTEGER, "
        		+ " calificacion INTEGER, "
        		+ " sincronizado TEXT)");
        //---------------------------------------------

    }


}
