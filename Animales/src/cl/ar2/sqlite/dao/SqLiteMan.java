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

    private static final String dbName="animales";
    private static final int version = 1;

    public SqLiteMan(Context context) {
        super(context, dbName, null, version);
      
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( ""
            + "CREATE TABLE diio ( "
        	+ " id INTEGER,"
            + " diio INTEGER,"
            + " eid TEXT,"
            + " fundoId INTEGER)");
        
        db.execSQL( ""
            + "CREATE TABLE predio_libre ( "
        	+ " ganadoId INTEGER,"
        	+ " fundoId INTEGER,"
        	+ " instancia INTEGER,"
            + " ganadoDiio INTEGER,"
            + " tuboPPDId INTEGER,"
            + " sincronizado TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aqui dependiendo de los numeros de version se deben crear o modificar las nuevas tablas
        // Las actualizaciones debieran borrar la tabla anterior y crear las nuevas
        // Siempre debieran estar todos los pasos de cambios desde la version 1 
    	

    }


}
