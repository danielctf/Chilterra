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
		30399359,
		30401773,
		30400387,
		30399831,
		30400913,
		30399952,
		30401586,
		30399602,
		30401615,
		30401975,
		30400790,
		30400460,
		30399861,
		30402744,
		30401237,
		30399087,
		30401033,
		30403259,
		30401771,
		30400408,
		30400919,
		30402742,
		30402761,
		30402011,
		30400903,
		30401049,
		30400658,
		30401611,
		30401050,
		30401133,
		30400492,
		30399367,
		30400884,
		30400010,
		30401236,
		30400012,
		30399967,
		30400027,
		30399750,
		30400892,
		30399680,
		30402743,
		30402753,
		30400900,
		30400006,
		30401055,
		30401043,
		30401605,
		30400447,
		30402767,
		30399573,
		30400287,
		30401238,
		30401137,
		30401038,
		30401240,
		30400675,
		30401130,
		30402771,
		30400888,
		30401013,
		30400859,
		30401761,
		30401056

 };
		
		Traslado t = new Traslado();
		
		
		t.setUsuarioId(1);
		t.setG_movimiento_id(181);
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
