package cl.ar2.sqlite.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import cl.a2r.alimentacion.Aplicaciones;
import cl.a2r.common.wsutils.Util;
import cl.a2r.sap.model.Calificacion;
import cl.a2r.sap.model.Medicion;
import cl.ar2.sqlite.cobertura.RegistroMedicion;
import cl.ar2.sqlite.cobertura.StockM;

public class MedicionDAO {
	
    private static final String SQL_INSERT_MEDICION = ""
            + "INSERT INTO medicion "
            + " (a_medicion_id, isactive, createdby, fecha_medicion, inicial, final, "
            + " muestra, materia_seca, medidor, a_tipo_medicion_id, a_potrero_id, "
            + " animales, sincronizado) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_INSERT_ACTUALIZADO = ""
    		+ "INSERT INTO actualizado (fecha_actualizado) VALUES (?)";
    
    private static final String SQL_DELETE_ACTUALIZADO = ""
    		+ "DELETE FROM actualizado";
    
    private static final String SQL_SELECT_ACTUALIZADO = ""
    		+ "SELECT fecha_actualizado FROM actualizado";

    private static final String SQL_SELECT_MEDICION = ""
            + "SELECT * FROM registro_medicion";
    
    private static final String SQL_DELETE_MEDICION = ""
    		+ "DELETE FROM registro_medicion WHERE registro_medicion_id = ?";
    
    private static final String SQL_INSERT_STOCK = ""
            + "INSERT INTO stock "
            + "       (medicion, actualizado) "
            + "VALUES ( ?, ? )";
    
    private static final String SQL_DELETE_STOCK = ""
            + "DELETE FROM stock ";
    
    private static final String SQL_SELECT_STOCK = ""
            + "SELECT * FROM stock ";
    
    private static final String SQL_INSERT_CALIFICACION = ""
    		+ "INSERT INTO calificacion "
    		+ " (fundo_id, numero, calificacion, sincronizado) "
    		+ " VALUES (?, ?, ?, ?)";
    
    private static final String SQL_SELECT_CALIFICACION = ""
            + "SELECT * FROM calificacion ";
    
    private static final String SQL_DELETE_CALIFICACION = ""
    		+ "DELETE FROM calificacion";

    public static void insertaMedicion(SqLiteTrx trx, List<Medicion> medList, boolean comesFromCloud) throws SQLException {
        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_MEDICION);
       
        for (Medicion med : medList){
	        statement.clearBindings();
	        statement.bindLong(1, med.getId());
	        statement.bindString(2, med.getActiva());
	        statement.bindLong(3, med.getUsuarioId());
	        statement.bindString(4, Util.dateToString(med.getFecha(), "dd-MM-yyyy HH:mm"));
	        statement.bindLong(5, med.getClickInicial());
	        statement.bindLong(6, med.getClickFinal());
	        statement.bindLong(7, med.getMuestras());
	        statement.bindLong(8, med.getMateriaSeca());
	        statement.bindNull(9);
	        statement.bindLong(10, med.getTipoMuestraId());
	        statement.bindLong(11, med.getPotreroId());
	        statement.bindLong(12, med.getAnimales());
	        statement.bindString(13, med.getSincronizado());
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
    
    public static void insertaStock(SqLiteTrx trx, List<Medicion> list) throws SQLException {

        SQLiteStatement statement = trx.getDB().compileStatement(SQL_INSERT_STOCK);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        
        for (Medicion m : list){
        	statement.clearBindings();
        	byte[] bytes = Util.serializa(m);
        	statement.bindBlob(1, bytes);
        	statement.bindString(2, df.format(m.getActualizado()));
        	statement.executeInsert();
        }
    }
    
    public static void deleteStock(SqLiteTrx trx) throws SQLException {
    	SQLiteStatement statement = trx.getDB().compileStatement(SQL_DELETE_STOCK);
    	statement.clearBindings();
    	statement.executeUpdateDelete();
    }
    
    public static List selectStockTotal(SqLiteTrx trx) throws SQLException {
        List<StockM> list = new ArrayList<StockM>();
        boolean hayReg;

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Cursor c = trx.getDB().rawQuery(SQL_SELECT_STOCK, null);
        hayReg = c.moveToFirst();
        while ( hayReg ) {

        	StockM s = new StockM();
        	s.setId(c.getInt(c.getColumnIndex("stock_id")));
        	byte[] bytes = c.getBlob(c.getColumnIndex("medicion"));
        	Medicion med = (Medicion) Util.desSerializa(bytes);
        	s.setMed(med);
        	
        	try {
				s.setActualizado(df.parse(c.getString(c.getColumnIndex("actualizado"))));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
            list.add(s);

            hayReg = c.moveToNext();
        }
		return list;
    }
    
    public static List selectStock(SqLiteTrx trx, List<StockM> list, Integer g_fundo_id) throws SQLException {
        //Filtra por el predio que eligio
        List<StockM> listFiltrada = new ArrayList<StockM>();
        for (StockM sm : list){
        	if (sm.getMed().getFundoId().intValue() == g_fundo_id.intValue()){
        		listFiltrada.add(sm);
        	}
        }
        
        //Trae la medicion mas actualizada de cada potrero
        //<numeroPotrero, StockM (del maximo getMed().getId()>
        HashMap<Integer, StockM> mp = new HashMap<Integer, StockM>();
        List<StockM> listUpdated = new ArrayList<StockM>();
        for (StockM sf : listFiltrada){
        	if (mp.containsKey(sf.getMed().getPotreroId())){
        		if (mp.get(sf.getMed().getPotreroId()).getMed().getId().intValue() < 
        				sf.getMed().getId().intValue()){
        			mp.remove(sf.getMed().getPotreroId());
        			mp.put(sf.getMed().getPotreroId(), sf);
        		}
        	} else {
    			mp.put(sf.getMed().getPotreroId(), sf);
    		}
        }
        
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            listUpdated.add((StockM) pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }

        //Agrega los potreros que no esten solo para q se desplieguen en la lista
        for (int i = 0; i < Aplicaciones.predioWS.getPotreros().intValue(); i++){
        	boolean isInList = false;
        	for (StockM sm : listUpdated){
        		if (sm.getMed().getPotreroId().intValue() == (i + 1)){
        			isInList = true;
        			break;
        		}
        	}
        	if (!isInList){
        		StockM s = new StockM();
        		Medicion m = new Medicion();
        		m.setPotreroId(i+1);
        		s.setMed(m);
        		listUpdated.add(s);
        	}
        	
        }
        
        //Ordena de mayor a menor por materia seca
        for (int i = 0; i < listUpdated.size(); i++){
        	for (int j = 0; j < listUpdated.size(); j++){
        		if (listUpdated.get(i).getMed().getMateriaSeca() != null && 
        				listUpdated.get(j).getMed().getMateriaSeca() != null &&
        				listUpdated.get(j).getMed().getMateriaSeca().intValue() < listUpdated.get(i).getMed().getMateriaSeca().intValue()){
        			StockM temp = listUpdated.get(i);
        			listUpdated.set(i, listUpdated.get(j));
        			listUpdated.set(j, temp);
        		}
        	}
        }

        return listUpdated;

    }
    
    public static List selectStockPotrero(SqLiteTrx trx, List<StockM> list, Integer g_fundo_id, Integer a_potrero_id) throws SQLException {        
        //Filtra por el predio que eligio
        List<StockM> listFiltrada = new ArrayList<StockM>();
        for (StockM sm : list){
        	if (sm.getMed().getFundoId().intValue() == g_fundo_id.intValue()){
        		listFiltrada.add(sm);
        	}
        }
        
        //Filtra por el potrero que eligio
        List<StockM> listUpdated = new ArrayList<StockM>();
        for (StockM sm : listFiltrada){
        	if (sm.getMed().getPotreroId().intValue() == a_potrero_id.intValue()){
        		listUpdated.add(sm);
        	}
        }
        
        //Ordena de mayor a menor por Id (no por fecha por q a simple viste los usuarios tienen
        //la embarrada en su celular y hasta la fecha la tienen desconfigurada)
        for (int i = 0; i < listUpdated.size(); i++){
        	for (int j = 0; j < listUpdated.size(); j++){
        		if (listUpdated.get(j).getMed().getId().intValue() < listUpdated.get(i).getMed().getId().intValue()){
        			StockM temp = listUpdated.get(i);
        			listUpdated.set(i, listUpdated.get(j));
        			listUpdated.set(j, temp);
        		}
        	}
        }
        
        return listUpdated;
    }
    
    public static List selectStockCrecimiento(SqLiteTrx trx, List<StockM> list, Integer g_fundo_id, Integer numero) throws SQLException {        
    	//Devuelve el stock de un fundo o potrero para calcular el crecimiento promedio
    	
        //Filtra por el predio que eligio
        List<StockM> listFiltrada = new ArrayList<StockM>();
        for (StockM sm : list){
        	if (sm.getMed().getFundoId().intValue() == g_fundo_id.intValue()){
        		listFiltrada.add(sm);
        	}
        }
        
        if (numero.intValue() != 0){
            //Filtra por el potrero que eligio
            List<StockM> listUpdated = new ArrayList<StockM>();
            for (StockM sm : listFiltrada){
            	if (sm.getMed().getPotreroId().intValue() == numero.intValue()){
            		listUpdated.add(sm);
            	}
            }
            return listUpdated;
        }
        
		return listFiltrada;
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
    
}
