package cl.a2r.sip.servlet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.common.wsservice.WSAdempiereCliente;
import cl.a2r.sip.common.Util;
import cl.a2r.sip.model.Baja;
import cl.a2r.sip.model.Brucelosis;
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
		List<Brucelosis> list = new ArrayList<Brucelosis>();
		Brucelosis b = new Brucelosis();
		b.setUsuarioId(1);
		b.setCodBarra("1000000006391");
		b.setFecha_muestra(new Date());
		b.setInstancia(133246);
		b.getGanado().setId(2870677);
		list.add(b);
		try {
			PredioLibreService.insertaGanadoBrucelosis(list);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				

	}

}
