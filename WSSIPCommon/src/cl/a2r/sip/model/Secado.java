package cl.a2r.sip.model;

import java.io.Serializable;

public class Secado implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private MedicamentoControl med;
	private Ganado gan;
	private String sincronizado;

	public Secado(){
		gan = new Ganado();
		setMed(new MedicamentoControl());
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Ganado getGan() {
		return gan;
	}

	public void setGan(Ganado gan) {
		this.gan = gan;
	}

	public MedicamentoControl getMed() {
		return med;
	}

	public void setMed(MedicamentoControl med) {
		this.med = med;
	}

	public String getSincronizado() {
		return sincronizado;
	}

	public void setSincronizado(String sincronizado) {
		this.sincronizado = sincronizado;
	}

	public String toString(){
		if (this.med.getId() != null){
			return "DIIO: " + Integer.toString(this.gan.getDiio()) 
					+ "   Serie: " + Integer.toString(this.med.getSerie());
		} else {
			return "DIIO: " + Integer.toString(this.gan.getDiio()) 
					+ "   Serie: ";
		}
	}

}
