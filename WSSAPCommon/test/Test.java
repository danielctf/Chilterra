
import cl.a2r.common.AppException;
import cl.a2r.sap.model.Medicion;
import cl.a2r.sap.model.Potrero;
import cl.a2r.sap.model.TipoMedicion;
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

    	
    	try {
			List<TipoMedicion> list = WSMedicionCliente.traeTipoMedicion();
			int i = 1;
			for (TipoMedicion m : list){
				System.out.println(m.getId());
				i++;
			}
		} catch (AppException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
        
    }
    

}
