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
    private static final int version = 16;

    public SqLiteMan(Context context) {
        super(context, dbName, null, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL( ""
            + "CREATE TABLE diio ( "
        	+ " id INTEGER PRIMARY KEY, "
            + " diio INTEGER,"
            + " eid TEXT,"
            + " fundoId INTEGER, "
            + " g_estado_leche_id INTEGER )");
        
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
        
        db.execSQL( ""
        	+ "CREATE TABLE reubicacion ( "
        	+ " id INTEGER PRIMARY KEY, "
        	+ " ganadoId INTEGER, "
        	+ " fundoOrigenId INTEGER, "
        	+ " fundoDestinoId INTEGER )");
        
        db.execSQL( ""
        	+ "CREATE TABLE secado ( "
        	+ " id INTEGER PRIMARY KEY, "
        	+ " ganadoId INTEGER, "
        	+ " ganadoDiio INTEGER, "
        	+ " fundoId INTEGER, "
        	+ " mangada INTEGER, "
        	+ " med_control_id INTEGER, "
        	+ " serie TEXT, "
        	+ " venta INTEGER, "
        	+ " sincronizado TEXT )");
        
        db.execSQL( ""
        	+ "CREATE TABLE rb51 ( "
        	+ " id INTEGER PRIMARY KEY, "
        	+ " ganadoId INTEGER, "
        	+ " ganadoDiio INTEGER, "
        	+ " fundoId INTEGER, "
        	+ " mangada INTEGER, "
        	+ " bang_id INTEGER, "
        	+ " bang TEXT, "
        	+ " med_control_id INTEGER, "
        	+ " serie INTEGER, "
        	+ " fecha TEXT, "
        	+ " sincronizado TEXT )");
        
        db.execSQL( ""
        	+ "CREATE TABLE rb51_anterior ( "
        	+ " id INTEGER PRIMARY KEY, "
        	+ " ganadoId INTEGER )");
        
        db.execSQL( ""
        	+ "CREATE TABLE bang ( "
        	+ " id INTEGER PRIMARY KEY, "
        	+ " bang TEXT, "
        	+ " borrar TEXT )");
        
        db.execSQL( ""
        	+ "CREATE TABLE auditoria ( "
        	+ " id INTEGER PRIMARY KEY, "
        	+ " ganadoId INTEGER, "
        	+ " ganadoDiio INTEGER, "
        	+ " fundoId INTEGER, "
        	+ " mangada INTEGER, "
        	+ " instancia INTEGER, "
        	+ " fecha TEXT, "
        	+ " sincronizado TEXT )");
        
        db.execSQL( ""
        	+ "CREATE TABLE busqueda ( "
        	+ " id INTEGER PRIMARY KEY, "
        	+ " ganadoId INTEGER, "
        	+ " ganadoDiio INTEGER,"
        	+ " flag INTEGER,"
        	+ " venta INTEGER )");
        
        db.execSQL( ""
        	+ "CREATE TABLE pesaje ( "
        	+ " id INTEGER PRIMARY KEY, "
        	+ " ganadoId INTEGER, "
        	+ " ganadoDiio INTEGER, "
        	+ " fundoId INTEGER, "
        	+ " mangada INTEGER, "
        	+ " fecha TEXT, "
        	+ " peso REAL, "
        	+ " gpd REAL, "
        	+ " sincronizado TEXT )");
        
        db.execSQL( ""
        	+ "CREATE TABLE traslado_salida ( "
        	+ " id INTEGER PRIMARY KEY, "
        	+ " ganadoId INTEGER, "
        	+ " ganadoDiio INTEGER, "
        	+ " mangada INTEGER, "
        	+ " instancia INTEGER )");
        
        db.execSQL( ""
        	+ "CREATE TABLE traslado_entrada ( "
        	+ " id INTEGER PRIMARY KEY, "
        	+ " ganadoId INTEGER, "
        	+ " ganadoDiio INTEGER, "
        	+ " instancia INTEGER )");        
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
    	db.execSQL("DROP TABLE IF EXISTS salvataje");
    	db.execSQL("DROP TABLE IF EXISTS salvataje_diio");
    	db.execSQL("DROP TABLE IF EXISTS reubicacion");
    	db.execSQL("DROP TABLE IF EXISTS secado");
    	db.execSQL("DROP TABLE IF EXISTS rb51");
    	db.execSQL("DROP TABLE IF EXISTS rb51_anterior");
    	db.execSQL("DROP TABLE IF EXISTS bang");
    	db.execSQL("DROP TABLE IF EXISTS auditoria");
    	db.execSQL("DROP TABLE IF EXISTS busqueda");
    	
    	onCreate(db);
        
    }

}
