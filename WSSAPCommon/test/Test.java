
import cl.a2r.common.AppException;
import cl.a2r.sap.model.Medicion;
import cl.a2r.sap.wsservice.WSMedicionCliente;

import java.awt.Button;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Miguel Vega Brante
 */
public class Test {

    public static void main(String[] arg) {
    	
    	Medicion m = new Medicion();
		m.setCorreo("dsantamaria@chilterra.com");
		m.setClickInicial(23);
		m.setClickFinal(65);
		m.setMuestras(12);
		m.setFecha(new Date());
		m.setFundoId(9);
		m.setMateriaSeca(22);
		m.setPotreroId(2);
		m.setTipoMuestraId(1);
    	
    	try {
			WSMedicionCliente.insertaMedicion(m);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
        
    }
    

}
