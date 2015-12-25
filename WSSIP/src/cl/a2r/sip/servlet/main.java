package cl.a2r.sip.servlet;

import cl.a2r.common.AppException;
import cl.a2r.common.wsservice.WSAdempiereCliente;
import cl.a2r.sip.model.DctoAdem;
import cl.a2r.sip.model.FMA;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Traslado;
import cl.a2r.sip.service.TrasladosService;

public class main {

	public static void main(String[] args) {

int[] ganado = {
		30399097,
		30400250,
		30399898,
		30400523,
		30401276


 };
		/*
		Traslado t = new Traslado();
		t.setUsuarioId(1);
    	t.setFundoOrigenId(4);
    	t.setFundoDestinoId(9);
		t.setTransportistaId(1000016);
		t.setChoferId(1000042);
    	t.setCamionId(1000037);
    	t.setAcopladoId(null);
    	*/
		Traslado t = new Traslado();
		t.setUsuarioId(1);
		t.setNro_documento(9420);
		t.setFundoDestinoId(15);
		
    	for (int i = 0; i < ganado.length; i++){
    		Ganado g = new Ganado();
    		g.setId(ganado[i]);
    		t.getGanado().add(g);
    	}
		
    	
    	try {
    		TrasladosService.insertaMovtoConfirm(t);
    		//Integer g_movimiento_id = TrasladosService.insertaMovimiento(t);
    		//DctoAdem d = TrasladosService.insertaMovtoAdem(g_movimiento_id);
			/*
    		WSAdempiereCliente.completaDocto(d.getIddocto(), d.getIdtipodocto());
			
	    	FMA fma = new FMA();
	    	fma.setUsuarioId(16);
	    	fma.setG_movimiento_id(Integer.parseInt(d.getNrodocto()));
	    	fma.setFundoOrigenId(8);
	    	fma.setFundoDestinoId(15);
	    	TrasladosService.generaXMLTraslado(fma);
	    	*/
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
	}

}
