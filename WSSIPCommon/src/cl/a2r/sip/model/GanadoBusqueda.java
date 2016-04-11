package cl.a2r.sip.model;

import java.io.Serializable;

public class GanadoBusqueda implements Serializable{

	private static final long serialVersionUID = 1L;
	private Ganado gan;
	
	public GanadoBusqueda(){
		gan = new Ganado();
	}

	public Ganado getGan() {
		return gan;
	}

	public void setGan(Ganado gan) {
		this.gan = gan;
	}
	
	public String toString(){
		if (gan.getFlag().intValue() == 1 && gan.getVenta().intValue() == 1){
			return "DIIO: " + this.gan.getDiio() + "  Pomo: Si" + "  Venta: Si";
		} else if (gan.getFlag().intValue() == 1 && gan.getVenta().intValue() == 0){
			return "DIIO: " + this.gan.getDiio() + "  Pomo: Si" + "  Venta: No";
		} else if (gan.getFlag().intValue() == 0 && gan.getVenta().intValue() == 1){
			return "DIIO: " + this.gan.getDiio() + "  Pomo: No" + "  Venta: Si";
		} else {
			return "DIIO: " + this.gan.getDiio() + "  Pomo: No" + "  Venta: No";
		}
	}
	
}
