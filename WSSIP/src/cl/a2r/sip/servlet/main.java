package cl.a2r.sip.servlet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.common.wsservice.WSAdempiereCliente;
import cl.a2r.sip.common.Util;
import cl.a2r.sip.model.Baja;
import cl.a2r.sip.model.DctoAdem;
import cl.a2r.sip.model.FMA;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.InyeccionTB;
import cl.a2r.sip.model.ResultadoBrucelosis;
import cl.a2r.sip.model.Traslado;
import cl.a2r.sip.service.BajasService;
import cl.a2r.sip.service.PredioLibreService;
import cl.a2r.sip.service.TrasladosService;

public class main {

	public static void main(String[] args) {

		
	int[] ganado = {
			30399266,
			2872530,
			11711041,
			2871094,
			30391074,
			2872770,
			30391291,
			2870549,
			2870934,
			2871368,
			2871023


	 };

		
		 
		Traslado t = new Traslado();
		
		
		t.setUsuarioId(11);
    	t.setFundoOrigenId(4);
    	t.setFundoDestinoId(1);
		t.setTransportistaId(1000063);
		t.setChoferId(1000175);
    	t.setCamionId(1000235);
    	t.setAcopladoId(null);
    	t.setDescripcion("TRASLADO");
		
    	for (int i = 0; i < ganado.length; i++){
    		Ganado g = new Ganado();
    		g.setId(ganado[i]);
    		t.getGanado().add(g);
    	}
    	
    	try {
        	Integer g_movimiento_id = TrasladosService.insertaMovimiento(t);
        	DctoAdem d = TrasladosService.insertaMovtoAdem(g_movimiento_id);
        	WSAdempiereCliente.completaDocto(d.getIddocto(), d.getIdtipodocto());
        	
	    	FMA fma = new FMA();
	    	fma.setUsuarioId(11);
	    	fma.setG_movimiento_id(Integer.parseInt(d.getNrodocto()));
	    	fma.setFundoOrigenId(4);
	    	fma.setFundoDestinoId(1);
	    	TrasladosService.generaXMLTraslado(fma);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	/*
    	try {
    		TrasladosService.insertaMovtoConfirm(t);
    		//Integer g_movimiento_id = TrasladosService.insertaMovimiento(t);
    		//DctoAdem d = TrasladosService.insertaMovtoAdem(g_movimiento_id);
			
    		WSAdempiereCliente.completaDocto(d.getIddocto(), d.getIdtipodocto());
			
	    	FMA fma = new FMA();
	    	fma.setUsuarioId(16);
	    	fma.setG_movimiento_id(Integer.parseInt(d.getNrodocto()));
	    	fma.setFundoOrigenId(8);
	    	fma.setFundoDestinoId(15);
	    	TrasladosService.generaXMLTraslado(fma);
	    	
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
				

	}

}
