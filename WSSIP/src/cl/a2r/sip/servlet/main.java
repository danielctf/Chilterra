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
			2871458,
			690,
			3717,
			168666,
			68313,
			2872673,
			666,
			2869936,
			6481,
			2871295,
			2873229,
			169123,
			2871894,
			2872493,
			30391218,
			2873162,
			1808957,
			2871096,
			30392462,
			2872387,
			2870512

	 };
		
	Traslado t = new Traslado();
	t.setUsuarioId(1);
	t.setFundoDestinoId(8);
	
	for (int i = 0; i < ganado.length; i++){
		Ganado g = new Ganado();
		g.setId(ganado[i]);
		t.getGanado().add(g);
	}
	
	try {
		TrasladosService.reubicaGanado(t);
	} catch (AppException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
/*
		List<InyeccionTB> list = new ArrayList<InyeccionTB>();
		Date date = new Date();
		long yesterday = date.getTime() - 72*60*60*1000;
		date.setTime(yesterday);
		
		for (int i = 0; i < ganado.length; i++){
			InyeccionTB tb = new InyeccionTB();
			tb.setUsuarioId(1);
			tb.setInstancia(66);
			tb.setGanadoID(ganado[i]);
			tb.setTuboPPDId(1);
			tb.setFecha_dosis(date);
			list.add(tb);
		}
		
		try {
			PredioLibreService.insertaGanadoTuberculina(list);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}





		/*
		 
		Traslado t = new Traslado();
		
		
		t.setUsuarioId(1);
		t.setG_movimiento_id(208);
    	t.setFundoOrigenId(null);
    	t.setFundoDestinoId(15);
		t.setTransportistaId(null);
		t.setChoferId(null);
    	t.setCamionId(null);
    	t.setAcopladoId(null);
		
    	for (int i = 0; i < ganado.length; i++){
    		Ganado g = new Ganado();
    		g.setId(ganado[i]);
    		t.getGanado().add(g);
    	}
    	
    	try {
			TrasladosService.insertaMovtoConfirm(t);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	
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
