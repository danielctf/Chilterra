/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.ar2.sqlite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 *
 * @author Miguel Vega Brante
 */
public class SqLiteTrx {
    private static SqLiteMan sqLiteMan = null;
    private SQLiteDatabase db = null;

    public static void Inicializa(Context context) {
        sqLiteMan = new SqLiteMan(context);
    }

    public SqLiteTrx(boolean isTrx ) {
        if (isTrx) {
            db = sqLiteMan.getWritableDatabase();
            db.beginTransaction();
        } else {
            db = sqLiteMan.getReadableDatabase();
        }
    }


    public SQLiteDatabase getDB() {
        return db;
    }

    public void commit() {
        getDB().setTransactionSuccessful();
    }

    public void rollback() {
//        getDB().execSQL("ROLLBACK");
    }

    public void close() {
    	try {
	        if (getDB().inTransaction()) {
	            getDB().endTransaction();
	        }
	        getDB().close();
    	} catch (IllegalStateException e){
    		
    	}
    }

    public static SqLiteTrx getTrx(boolean isTrx) throws Exception {
        if (sqLiteMan == null) {
            throw new Exception("Base de datos no inicializada.");
        }
        SqLiteTrx trx = new SqLiteTrx(isTrx);
        if (trx.getDB() == null)
            return null;
        return trx;
    }

}
