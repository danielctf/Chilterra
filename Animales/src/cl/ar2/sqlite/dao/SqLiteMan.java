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
    private static final int version = 6;

    public SqLiteMan(Context context) {
        super(context, dbName, null, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL( ""
            + "CREATE TABLE diio ( "
        	+ " id INTEGER,"
            + " diio INTEGER,"
            + " eid TEXT,"
            + " fundoId INTEGER)");
        
        db.execSQL( ""
            + "CREATE TABLE predio_libre ( "
        	+ " id INTEGER PRIMARY KEY,"
        	+ " ganadoId INTEGER,"
        	+ " fundoId INTEGER,"
        	+ " instancia INTEGER,"
            + " ganadoDiio INTEGER,"
        	+ " mangada INTEGER,"
            + " tuboPPDId INTEGER,"
        	+ " tuboPPDSerie INTEGER,"
            + " lecturaTB TEXT,"
            + " sincronizado TEXT)");
        
        db.execSQL( ""
            + "CREATE TABLE predio_libre_brucelosis ( "
            + " id INTEGER PRIMARY KEY,"
        	+ " ganadoId INTEGER,"
        	+ " fundoId INTEGER,"
        	+ " instancia INTEGER,"
            + " ganadoDiio INTEGER,"
            + " mangada INTEGER,"
            + " codBarra TEXT,"
            + " sincronizado TEXT)");
        
        db.execSQL( ""
        	+ "CREATE TABLE ecografia ( "
        	+ " id INTEGER PRIMARY KEY, "
        	+ " ganadoId INTEGER, "
        	+ " ganadoDiio INTEGER, "
        	+ " fundoId INTEGER, "
        	+ " fecha TEXT, "
        	+ " dias_prenez INTEGER, "
        	+ " ecografista_id INTEGER, "
        	+ " inseminacion_id INTEGER, "
        	+ " estado_id INTEGER, "
        	+ " problema_id INTEGER, "
        	+ " nota_id INTEGER, "
        	+ " mangada INTEGER, "
        	+ " sincronizado TEXT )");
        
        db.execSQL( ""
        	+ "CREATE TABLE inseminacion ( "
        	+ " id INTEGER PRIMARY KEY, "
        	+ " ganadoId INTEGER, "
        	+ " ganadoDiio INTEGER, "
        	+ " fundoId INTEGER, "
        	+ " fecha TEXT, "
        	+ " sincronizado )");
        
        db.execSQL( ""
        	+ "CREATE TABLE salvataje ( "
        	+ " id INTEGER PRIMARY KEY, "
        	+ " nombre TEXT, "
        	+ " fecha TEXT )");
        
        db.execSQL( ""
        	+ "CREATE TABLE salvataje_diio ( "
        	+ " id INTEGER PRIMARY KEY, "
        	+ " diioeid TEXT, "
        	+ " observacion TEXT, "
        	+ " s_id INTEGER, "
        	+ " FOREIGN KEY (s_id) REFERENCES salvataje (id) )");
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aqui dependiendo de los numeros de version se deben crear o modificar las nuevas tablas
        // Las actualizaciones debieran borrar la tabla anterior y crear las nuevas
        // Siempre debieran estar todos los pasos de cambios desde la version 1 
        
    	db.execSQL("DROP TABLE IF EXISTS diio");
    	db.execSQL("DROP TABLE IF EXISTS predio_libre");
    	db.execSQL("DROP TABLE IF EXISTS predio_libre_brucelosis");
    	db.execSQL("DROP TABLE IF EXISTS ecografia");
    	db.execSQL("DROP TABLE IF EXISTS inseminacion");
    	
        db.execSQL( ""
            + "CREATE TABLE diio ( "
        	+ " id INTEGER,"
            + " diio INTEGER,"
            + " eid TEXT,"
            + " fundoId INTEGER)");
        
        db.execSQL( ""
            + "CREATE TABLE predio_libre ( "
        	+ " id INTEGER PRIMARY KEY,"
        	+ " ganadoId INTEGER,"
        	+ " fundoId INTEGER,"
        	+ " instancia INTEGER,"
            + " ganadoDiio INTEGER,"
        	+ " mangada INTEGER,"
            + " tuboPPDId INTEGER,"
        	+ " tuboPPDSerie INTEGER,"
            + " lecturaTB TEXT,"
            + " sincronizado TEXT)");
        
        db.execSQL( ""
            + "CREATE TABLE predio_libre_brucelosis ( "
            + " id INTEGER PRIMARY KEY,"
        	+ " ganadoId INTEGER,"
        	+ " fundoId INTEGER,"
        	+ " instancia INTEGER,"
            + " ganadoDiio INTEGER,"
            + " mangada INTEGER,"
            + " codBarra TEXT,"
            + " sincronizado TEXT)");
        
        db.execSQL( ""
        	+ "CREATE TABLE ecografia ( "
        	+ " id INTEGER PRIMARY KEY, "
        	+ " ganadoId INTEGER, "
        	+ " ganadoDiio INTEGER, "
        	+ " fundoId INTEGER, "
        	+ " fecha TEXT, "
        	+ " dias_prenez INTEGER, "
        	+ " ecografista_id INTEGER, "
        	+ " inseminacion_id INTEGER, "
        	+ " estado_id INTEGER, "
        	+ " problema_id INTEGER, "
        	+ " nota_id INTEGER, "
        	+ " mangada INTEGER, "
        	+ " sincronizado TEXT )");
        
        db.execSQL( ""
        	+ "CREATE TABLE inseminacion ( "
        	+ " id INTEGER PRIMARY KEY, "
        	+ " ganadoId INTEGER, "
        	+ " ganadoDiio INTEGER, "
        	+ " fundoId INTEGER, "
        	+ " fecha TEXT, "
        	+ " sincronizado )");
        
        db.execSQL( ""
        	+ "CREATE TABLE salvataje ( "
        	+ " id INTEGER PRIMARY KEY, "
        	+ " nombre TEXT, "
        	+ " fecha TEXT )");
            
        db.execSQL( ""
        	+ "CREATE TABLE salvataje_diio ( "
        	+ " id INTEGER PRIMARY KEY, "
        	+ " diioeid TEXT, "
        	+ " observacion TEXT, "
        	+ " s_id INTEGER, "
        	+ " FOREIGN KEY (s_id) REFERENCES salvataje (id) )");
        
    }

}
