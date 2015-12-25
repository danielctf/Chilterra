package cl.a2r.sap.servlet;

import java.util.Date;

import cl.a2r.common.AppException;
import cl.a2r.sap.model.Medicion;
import cl.a2r.sap.wsservice.WSMedicionCliente;

public class main {

	public static void main(String[] args) {
		
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
			System.out.println(e.getMessage());
		}
	}

}
