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
		30403135	,
		30403136	,
		30403481	,
		30399921	,
		30401515	,
		30403148	,
		30402085	,
		30399427	,
		30401936	,
		30403303	,
		71961015	,
		30403165	,
		30400115	,
		30402324	,
		71961022	,
		30402327	,
		30400598	,
		30403461	,
		71961005	,
		30402984	,
		30403173	,
		30402452	,
		30401961	,
		30402611	,
		30403770	,
		30399916	,
		30402205	,
		30399445	,
		30401951	,
		30402429	,
		30402467	,
		30400284	,
		30400944	,
		30403198	,
		30403596	,
		30403204	,
		30400810	,
		30401729	,
		30403559	,
		30401185	,
		30402269	,
		30401075	,
		30403246	,
		30402612	,
		30403112	,
		30402855	,
		30402474	,
		30402884	,
		30401378	,
		30402913	,
		30401834	,
		30402997	,
		30400821	,
		30399550	,
		30402907	,
		30403119	,
		30401732	,
		30399718	,
		30402431	,
		30401826	,
		30402992	,
		30403457	,
		30403210	,
		30403154	,
		30399881	,
		30401706	,
		30403143	,
		30401860	,
		30401207	,
		30402792	,
		30399756	,
		30400096	,
		30402090	,
		30403187	,
		30402473	,
		30403595	,
		30403482	,
		30399598	,
		30400952	,
		30402093	,
		30402989	,
		30403149	,
		30400518	,
		30399428	,
		30402926	,
		30403155	,
		30402925	,
		30401641	,
		30399767	,
		30401481	,
		30400265	,
		30403070	,
		30399605	,
		30402209	,
		30402996	,
		30401517	,
		30402874	,
		30400671	,
		30403050	,
		30401508	,
		30400411	,
		30403597	,
		71961028	,
		30400109	,
		30401859	,
		30401718	,
		30403350	,
		30401972	,
		30401503	,
		71961014	,
		30400661	,
		30400818	,
		30401943	,
		30400268	,
		30402272	,
		30401745	,
		30403359	,
		30403467	,
		30402971	,
		30403208	,
		30403097	,
		30401083	,
		30400836	,
		30403485	,
		30402610	,
		30402337	,
		30403376	,
		30403592	,
		30401073	,
		30402621	,
		30402394	,
		30402220	,
		30401824	,
		30400534	,
		30401723	,
		30403477	,
		30403594	,
		30401953	,
		30402387	,
		30403053	,
		30402919	,
		30403150	,
		30402322	,
		30399284	,
		74481049	,
		30403071	,
		30403579	,
		30400533	,
		30401098	,
		30403056	,
		30403069	,
		30402385	


 };
		
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
