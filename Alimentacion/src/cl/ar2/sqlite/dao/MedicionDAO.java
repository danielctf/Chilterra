package cl.ar2.sqlite.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import cl.a2r.common.wsutils.Util;
import cl.a2r.sap.model.Calificacion;
import cl.a2r.sap.model.Medicion;
import cl.a2r.sap.model.Potrero;
import cl.a2r.sap.model.TipoMedicion;
public class MedicionDAO {
	
	private static final String SQL_INSERT_TIPO_MEDICION = ""
			+ "INSERT INTO tipo_medicion (a_tipo_medicion_id, codigo, nombre) "
			+ " VALUES (?, ?, ?)";
	
	private static final String SQL_EXISTS_TIPO_MEDICION = ""
			+ " SELECT a_tipo_medicion_id FROM tipo_medicion LIMIT 1";
	
	private static final String SQ_INSERT_POTRERO = ""
			+ " INSERT INTO potrero (a_potrero_id, numero, superficie, g_fundo_id, "
			+ " a_tipo_siembra_id, calificacion, sincronizado) "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	private static final String SQL_EXISTS_POTREROS = ""
			+ " SELECT a_potrero_id FROM potrero LIMIT 1";
	
	private static final String SQL_SELECT_SUPERFICIE = ""
			+ " SELECT superficie FROM potrero WHERE a_potrero_id = ? ";
	
    private static final String SQL_INSERT_MEDICION = ""
            + "INSERT INTO medicion "
            + " (a_medicion_id, isactive, fecha_medicion, inicial, final, "
            + " muestra, materia_seca, medidor, a_tipo_medicion_id, a_potrero_id, "
            + " animales, sincronizado) "
            + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_INSERT_ACTUALIZADO = ""
    		+ "INSERT INTO actualizado (fecha_actualizado) VALUES (?)";
    
    private static final String SQL_DELETE_ACTUALIZADO = ""
    		+ "DELETE FROM actualizado";
    
    private static final String SQL_SELECT_ACTUALIZADO = ""
    		+ "SELECT fecha_actualizado FROM actualizado";

    /*
    private static final String SQL_SELECT_MEDICION = ""
            + "SELECT (a_medicion_id, isactive, fecha_medicion, inicial, final, "
            + " muestra, materia_seca, medidor, a_tipo_medicion_id, a_potrero_id, "
            + " animales, sincronizado) FROM medicion";
    */
    
    private static final String SQL_DELETE_MEDICION = ""
    		+ "DELETE FROM medicion WHERE a_medicion_id = ?";
    
    private static final String SQL_DELETE_ALL_MEDICIONES = ""
    		+ "DELETE FROM medicion";
    
    private static final String SQL_SELECT_MEDICION_FUNDO = ""
    		+ "SELECT m.a_medicion_id, m.fecha_medicion, m.inicial, m.final, "
            + " m.muestra, m.materia_seca, m.medidor, m.a_tipo_medicion_id, c.a_potrero_id, " 
            + " m.sincronizado, m.animales, c.numero, c.superficie, t.nombre "
            + " FROM "
            + " (SELECT max(a.a_medicion_id) med_id, b.a_potrero_id, max(b.numero) numero, "
            + " max(b.superficie) superficie "
            + " FROM potrero b "
            + " LEFT JOIN medicion a ON a.a_potrero_id = b.a_potrero_id "
            + " WHERE b.g_fundo_id = ? "
            + " GROUP BY b.a_potrero_id) c "
            + " LEFT JOIN medicion m ON m.a_medicion_id = c.med_id "
            + " LEFT JOIN tipo_medicion t ON m.a_tipo_medicion_id = t.a_tipo_medicion_id " 
            + " AND (m.isactive = 'Y' or m.isactive is null) " 
            + " ORDER BY m.materia_seca DESC ";

    private static final String SQL_SELECT_MEDICION_POTRERO = ""
    		+ "SELECT m.a_medicion_id, m.fecha_medicion, m.inicial, m.final, "
            + " m.muestra, m.materia_seca, m.medidor, m.a_tipo_medicion_id, m.a_potrero_id, "
            + " m.animales, m.sincronizado, p.numero, t.nombre "
            + " FROM medicion m, potrero p, tipo_medicion t "
            + " WHERE m.a_potrero_id = p.a_potrero_id AND m.a_tipo_medicion_id = t.a_tipo_medicion_id "
            + " AND m.isactive = 'Y' "
            + " AND p.a_potrero_id = ?";
    
    private static final String SQL_INSERT_CALIFICACION = ""
    		+ "INSERT INTO calificacion "
    		+ " (fundo_id, numero, calificacion, sincronizado) "
    		+ " VALUES (?, ?, ?, ?)";
    
    private static final String SQL_SELECT_CALIFICACION = ""
            + "SELECT * FROM calificacion ";
    
    private static final String SQL_DELETE_CALIFICACION = ""
    		+ "DELETE FROM calificacion";
    
    private static final String SQL_SELECT_CRECIMIENTO_MEDS = ""
    		+ " SELECT a.a_medicion_id, a.inicial, a.final, a.muestra, a.fecha_medicion, "
    		+ " a.a_potrero_id, b.superficie "
    		+ " FROM medicion a, potrero b "
    		+ " WHERE a.a_potrero_id = b.a_potrero_id "
    		+ " AND a.a_tipo_medicion_id = 3 "
    		+ " AND b.g_fundo_id = ? ";
    
    public static void insertTipoMedicion(SqLiteTrx trx, List<TipoMedicion> list) throws SQLException {
        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_TIPO_MEDICION);
       
        for (TipoMedicion t : list){
	        statement.clearBindings();
	        statement.bindLong(1, t.getId());
	        statement.bindString(2, t.getCodigo());
	        statement.bindString(3, t.getNombre());
	        statement.executeInsert();
        }

    }
    
    public static boolean existsTipoMedicion(SqLiteTrx trx) throws SQLException {
    	boolean exists = false;
        boolean hayReg;

        Cursor c = trx.getDB().rawQuery(SQL_EXISTS_TIPO_MEDICION, null);
        hayReg = c.moveToFirst();
        if ( hayReg ) {
        	
        	exists = true;
        	
            hayReg = c.moveToNext();
        }

        return exists;
    }
    
    public static void insertPotrero(SqLiteTrx trx, List<Potrero> list) throws SQLException {
        SQLiteStatement statement = trx.getDB().compileStatement(SQ_INSERT_POTRERO);
        
        for (Potrero p : list){
        	statement.clearBindings();
        	statement.bindLong(1, p.getId());
        	statement.bindLong(2, p.getNumero());
        	statement.bindDouble(3, p.getSuperficie());
        	statement.bindLong(4, p.getG_fundo_id());
        	statement.bindLong(5, p.getA_tipo_siembra_id());
        	statement.bindLong(6, p.getCalificacion());
        	statement.bindString(7, p.getSincronizado());
        	statement.executeInsert();
        }

    }
    
    public static boolean existsPotrero(SqLiteTrx trx) throws SQLException {
    	boolean exists = false;
        boolean hayReg;

        Cursor c = trx.getDB().rawQuery(SQL_EXISTS_POTREROS, null);
        hayReg = c.moveToFirst();
        if ( hayReg ) {
        	
        	exists = true;
        	
            hayReg = c.moveToNext();
        }

        return exists;
        

    }
    
    public static double selectSuperficie(SqLiteTrx trx, Integer a_potrero_id) throws SQLException {
    	double superficie = 0;
        boolean hayReg;

        String[] args = {Integer.toString(a_potrero_id)};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_SUPERFICIE, args);
        hayReg = c.moveToFirst();
        if ( hayReg ) {
        	
        	superficie = c.getDouble(c.getColumnIndex("superficie"));
        	
            hayReg = c.moveToNext();
        }

        return superficie;
        

    }

    public static void insertMedicion(SqLiteTrx trx, List<Medicion> medList, boolean comesFromCloud) throws SQLException {
        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_MEDICION);
       
        for (Medicion med : medList){
	        statement.clearBindings();
	        statement.bindLong(1, med.getId());
	        statement.bindString(2, med.getActiva());
	        statement.bindString(3, Util.dateToString(med.getFecha(), "dd-MM-yyyy HH:mm:ss"));
	        statement.bindLong(4, med.getClickInicial());
	        statement.bindLong(5, med.getClickFinal());
	        statement.bindLong(6, med.getMuestras());
	        statement.bindLong(7, med.getMateriaSeca());
	        statement.bindNull(8);
	        statement.bindLong(9, med.getTipoMuestraId());
	        statement.bindLong(10, med.getPotreroId());
	        statement.bindLong(11, med.getAnimales());
	        statement.bindString(12, med.getSincronizado());
	        statement.executeInsert();
        }
        
        if (comesFromCloud){
        	statement = trx.getDB().compileStatement(SQL_DELETE_ACTUALIZADO);
        	statement.clearBindings();
        	statement.executeUpdateDelete();
        	statement = trx.getDB().compileStatement(SQL_INSERT_ACTUALIZADO);
        	statement.clearBindings();
        	statement.bindString(1, Util.dateToString(medList.get(0).getActualizado(), "dd-MM-yyyy HH:mm"));
        	statement.executeInsert();
        }

    }
    
    public static Date selectFechaActualizado(SqLiteTrx trx) throws SQLException {
    	Date date = null;
        boolean hayReg;

        Cursor c = trx.getDB().rawQuery(SQL_SELECT_ACTUALIZADO, null);
        hayReg = c.moveToFirst();
        if ( hayReg ) {
        	
        	String fecha = c.getString(c.getColumnIndex("fecha_actualizado"));
        	date = Util.stringToDate(fecha, "dd-MM-yyyy HH:mm");
        	
            hayReg = c.moveToNext();
        }

        return date;

    }

    /*
    public static List selectMediciones(SqLiteTrx trx) throws SQLException {
        List<Medicion> medList = new ArrayList<Medicion>();
        boolean hayReg;

        Cursor c = trx.getDB().rawQuery(SQL_SELECT_MEDICION, null);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	
            Medicion med = new Medicion();
        	med.setId(c.getInt(c.getColumnIndex("a_medicion_id")));
        	med.setActiva(c.getString(c.getColumnIndex("isactive")));
        	String fecha = c.getString(c.getColumnIndex("fecha_medicion"));
        	med.setFecha(Util.stringToDate(fecha, "dd-MM-yyyy HH:mm"));
        	med.setClickInicial(c.getInt(c.getColumnIndex("inicial")));
        	med.setClickFinal(c.getInt(c.getColumnIndex("final")));
        	med.setMuestras(c.getInt(c.getColumnIndex("muestra")));
        	med.setMateriaSeca(c.getInt(c.getColumnIndex("materia_seca")));
        	med.setMedidorId(null);
        	med.setTipoMuestraId(c.getInt(c.getColumnIndex("a_tipo_medicion_id")));
        	med.setPotreroId(c.getInt(c.getColumnIndex("a_potrero_id")));
        	med.setAnimales(c.getInt(c.getColumnIndex("animales")));
        	med.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
        	
            medList.add(med);

            hayReg = c.moveToNext();
        }

        return medList;

    }
    */
    
    public static void deleteMedicion(SqLiteTrx trx, Integer a_medicion_id) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_MEDICION);
    	statement.clearBindings();
    	statement.bindLong(1, a_medicion_id);
    	statement.executeUpdateDelete();
    }
    
    public static void deleteAllMediciones(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_ALL_MEDICIONES);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    
    public static List selectMedicionFundo(SqLiteTrx trx, Integer g_fundo_id) throws SQLException {
        List<Medicion> medList = new ArrayList<Medicion>();
        boolean hayReg;

        String[] args = {Integer.toString(g_fundo_id)};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_MEDICION_FUNDO, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {

        	if (!c.isNull(c.getColumnIndex("a_medicion_id"))){
                Medicion med = new Medicion();
            	med.setId(c.getInt(c.getColumnIndex("a_medicion_id")));
            	String fecha = c.getString(c.getColumnIndex("fecha_medicion"));
            	med.setFecha(Util.stringToDate(fecha, "dd-MM-yyyy HH:mm"));
            	med.setClickInicial(c.getInt(c.getColumnIndex("inicial")));
            	med.setClickFinal(c.getInt(c.getColumnIndex("final")));
            	med.setMuestras(c.getInt(c.getColumnIndex("muestra")));
				double click = ((double) med.getClickFinal().intValue()
						- (double) med.getClickInicial().intValue()) 
						/ (double) med.getMuestras().intValue();
				med.setClick(click);
            	med.setMateriaSeca(c.getInt(c.getColumnIndex("materia_seca")));
            	med.setMedidorId(null);
            	med.setTipoMuestraId(c.getInt(c.getColumnIndex("a_tipo_medicion_id")));
            	med.setPotreroId(c.getInt(c.getColumnIndex("a_potrero_id")));
            	med.setAnimales(c.getInt(c.getColumnIndex("animales")));
            	med.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
            	med.setNumeroPotrero(c.getInt(c.getColumnIndex("numero")));
            	med.setSuperficie(c.getDouble(c.getColumnIndex("superficie")));
            	med.setTipoMuestraNombre(c.getString(c.getColumnIndex("nombre")));
            	
                medList.add(med);	
        	} else {
        		Medicion med = new Medicion();
        		med.setPotreroId(c.getInt(c.getColumnIndex("a_potrero_id")));
        		med.setNumeroPotrero(c.getInt(c.getColumnIndex("numero")));
        		medList.add(med);
        	}

            hayReg = c.moveToNext();
        }

        return medList;

    }
    
    public static List selectMedicionPotrero(SqLiteTrx trx, Integer a_potrero_id) throws SQLException {        
        List<Medicion> medList = new ArrayList<Medicion>();
        boolean hayReg;

        String[] args = {Integer.toString(a_potrero_id)};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_MEDICION_POTRERO, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {

            Medicion med = new Medicion();
        	med.setId(c.getInt(c.getColumnIndex("a_medicion_id")));
        	String fecha = c.getString(c.getColumnIndex("fecha_medicion"));
        	med.setFecha(Util.stringToDate(fecha, "dd-MM-yyyy HH:mm"));
        	med.setClickInicial(c.getInt(c.getColumnIndex("inicial")));
        	med.setClickFinal(c.getInt(c.getColumnIndex("final")));
        	med.setMuestras(c.getInt(c.getColumnIndex("muestra")));
        	med.setMateriaSeca(c.getInt(c.getColumnIndex("materia_seca")));
        	med.setMedidorId(null);
        	med.setTipoMuestraId(c.getInt(c.getColumnIndex("a_tipo_medicion_id")));
        	med.setPotreroId(c.getInt(c.getColumnIndex("a_potrero_id")));
        	med.setAnimales(c.getInt(c.getColumnIndex("animales")));
        	med.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
        	med.setNumeroPotrero(c.getInt(c.getColumnIndex("numero")));
        	med.setTipoMuestraNombre(c.getString(c.getColumnIndex("nombre")));
        	
            medList.add(med);

            hayReg = c.moveToNext();
        }

        return medList;
    }
    
    public static void insertaCalificacion(SqLiteTrx trx, List<Calificacion> calList) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_CALIFICACION);

        for (Calificacion cal : calList){
	        statement.clearBindings();
	        statement.bindLong(1, cal.getG_fundo_id());
	        statement.bindLong(2, cal.getNumero());
	        statement.bindLong(3, cal.getCalificacion());
	        statement.bindString(4, cal.getSincronizado());
	        statement.executeInsert();
        }

    }
    
    public static List selectCalificacion(SqLiteTrx trx) throws SQLException {
        List<Calificacion> list = new ArrayList<Calificacion>();
        boolean hayReg;

        Cursor c = trx.getDB().rawQuery(SQL_SELECT_CALIFICACION, null);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	
        	Calificacion cal = new Calificacion();
        	cal.setG_fundo_id(c.getInt(c.getColumnIndex("fundo_id")));
        	cal.setNumero(c.getInt(c.getColumnIndex("numero")));
        	cal.setCalificacion(c.getInt(c.getColumnIndex("calificacion")));
        	cal.setSincronizado(c.getString(c.getColumnIndex("sincronizado")));
            list.add(cal);

            hayReg = c.moveToNext();
        }
		return list;
    }
    
    public static void deleteCalificacion(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_CALIFICACION);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    public static List selectCrecimientoMeds(SqLiteTrx trx, Integer g_fundo_id) throws SQLException {
    	List<Medicion> list = new ArrayList<Medicion>();
        boolean hayReg;

        String[] args = {Integer.toString(g_fundo_id)};
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_CRECIMIENTO_MEDS, args);
        hayReg = c.moveToFirst();
        while ( hayReg ) {
        	
        	Medicion med = new Medicion();
        	med.setId(c.getInt(c.getColumnIndex("a_medicion_id")));
        	med.setClickInicial(c.getInt(c.getColumnIndex("inicial")));
        	med.setClickFinal(c.getInt(c.getColumnIndex("final")));
        	med.setMuestras(c.getInt(c.getColumnIndex("muestra")));
			double click = ((double) med.getClickFinal().intValue()
					- (double) med.getClickInicial().intValue()) 
					/ (double) med.getMuestras().intValue();
			med.setClick(click);
        	String fecha = c.getString(c.getColumnIndex("fecha_medicion"));
        	med.setFecha(Util.stringToDate(fecha, "dd-MM-yyyy HH:mm"));
        	med.setPotreroId(c.getInt(c.getColumnIndex("a_potrero_id")));
        	med.setSuperficie(c.getDouble(c.getColumnIndex("superficie")));
        	list.add(med);
        	
            hayReg = c.moveToNext();
        }

        return list;

    }
    
}
