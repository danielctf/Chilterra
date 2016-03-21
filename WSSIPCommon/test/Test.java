
import cl.a2r.common.AppException;
import cl.a2r.sip.model.Aplicacion;
import cl.a2r.sip.model.Areteo;
import cl.a2r.sip.model.Baja;
import cl.a2r.sip.model.Brucelosis;
import cl.a2r.sip.model.Camion;
import cl.a2r.sip.model.CausaBaja;
import cl.a2r.sip.model.Chofer;
import cl.a2r.sip.model.CollarParto;
import cl.a2r.sip.model.DctoAdem;
import cl.a2r.sip.model.Ecografia;
import cl.a2r.sip.model.Ecografista;
import cl.a2r.sip.model.EstadoLeche;
import cl.a2r.sip.model.FMA;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.GanadoLogs;
import cl.a2r.sip.model.Inseminacion;
import cl.a2r.sip.model.InyeccionTB;
import cl.a2r.sip.model.Medicamento;
import cl.a2r.sip.model.MedicamentoControl;
import cl.a2r.sip.model.MotivoBaja;
import cl.a2r.sip.model.Movimiento;
import cl.a2r.sip.model.PPD;
import cl.a2r.sip.model.Parto;
import cl.a2r.sip.model.Persona;
import cl.a2r.sip.model.Predio;
import cl.a2r.sip.model.PredioLibre;
import cl.a2r.sip.model.Raza;
import cl.a2r.sip.model.TipoGanado;
import cl.a2r.sip.model.Transportista;
import cl.a2r.sip.model.Traslado;
import cl.a2r.sip.wsservice.WSAreteosCliente;
import cl.a2r.sip.wsservice.WSAutorizacionCliente;
import cl.a2r.sip.wsservice.WSBajasCliente;
import cl.a2r.sip.wsservice.WSEcografiasCliente;
import cl.a2r.sip.wsservice.WSGanadoCliente;
import cl.a2r.sip.wsservice.WSPartosCliente;
import cl.a2r.sip.wsservice.WSPredioLibreCliente;
import cl.a2r.sip.wsservice.WSSecadosCliente;
import cl.a2r.sip.wsservice.WSTrasladosCliente;

import java.awt.Button;
import java.math.BigInteger;
import java.util.ArrayList;
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

    		List<MedicamentoControl> list = WSSecadosCliente.traeMedicamentos(14);
    		for (MedicamentoControl i : list){
    			System.out.println("se "+i.getSerie());
    			System.out.println("n "+i.getMed().getNombre());
    		}
    		
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    

}
