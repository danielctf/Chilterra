
import cl.a2r.common.AppException;
import cl.a2r.sip.model.Aplicacion;
import cl.a2r.sip.model.Baja;
import cl.a2r.sip.model.CausaBaja;
import cl.a2r.sip.model.CollarParto;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.MotivoBaja;
import cl.a2r.sip.model.Parto;
import cl.a2r.sip.model.Predio;
import cl.a2r.sip.wsservice.WSAutorizacionCliente;
import cl.a2r.sip.wsservice.WSBajasCliente;
import cl.a2r.sip.wsservice.WSGanadoCliente;
import cl.a2r.sip.wsservice.WSPartosCliente;

import java.awt.Button;
import java.math.BigInteger;
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

        	
            WSPartosCliente.confirmaParto(1);
            /*
            for (Ganado mb : motivos) {
            	System.out.println(mb.getId());
            	System.out.println(mb.getDiio());
            }
            */
            
        } catch (AppException ex) {
            //Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
        
    }
    

}
