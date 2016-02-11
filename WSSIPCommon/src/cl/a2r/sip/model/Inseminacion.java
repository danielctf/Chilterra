package cl.a2r.sip.model;

import java.io.Serializable;
import java.util.Date;

public class Inseminacion implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Date fecha;
	private Ganado gan;
	private String sincronizado;
	
	public Inseminacion(){
		gan = new Ganado();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Ganado getGan() {
		return gan;
	}

	public String getSincronizado() {
		return sincronizado;
	}

	public void setSincronizado(String sincronizado) {
		this.sincronizado = sincronizado;
	}
	
}
