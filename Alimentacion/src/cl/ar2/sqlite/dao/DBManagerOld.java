/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.ar2.sqlite.dao;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import java.io.File;

/**
 *
 * @author Miguel Vega Brante
 */
public class DBManagerOld {
    private static SQLiteDatabase cobertura = null;


    public static SQLiteDatabase getDB() {

    String DBPath = Environment.getDataDirectory().getName()+"/data/cl.ar2.sqlite/databases/";

    String myPath = DBPath + "cobertura.db";

        try {
            System.out.println("ANTES DE CATCH !!!");
            if (cobertura == null) {
                File file = new File(DBPath);
                if (!file.exists()) {
                    file.mkdirs();
                }

                cobertura  = SQLiteDatabase.openOrCreateDatabase(myPath, null);

            }
        } catch ( Exception ex ) {
            Log.e("ERROR", ex.getMessage(), ex);
            System.out.println("CATCH !!!");
            System.out.println(ex.getStackTrace());
            ex.printStackTrace();
        }
        return cobertura;
    }


}
