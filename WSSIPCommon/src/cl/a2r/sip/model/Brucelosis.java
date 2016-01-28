package cl.a2r.sip.model;

import java.io.Serializable;
import java.util.Date;

public class Brucelosis implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer usuarioId;
	private Ganado ganado;
	private String codBarra;
	private Integer instancia;
	private Date fecha_muestra;
	private String sincronizado;
	
	public Brucelosis(){
		ganado = new Ganado();
	}
	
	public Ganado getGanado(){
		return this.ganado;
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getCodBarra() {
		return codBarra;
	}

	public void setCodBarra(String codBarra) {
		this.codBarra = codBarra;
	}

	public Integer getInstancia() {
		return instancia;
	}

	public void setInstancia(Integer instancia) {
		this.instancia = instancia;
	}

	public Date getFecha_muestra() {
		return fecha_muestra;
	}

	public void setFecha_muestra(Date fecha_muestra) {
		this.fecha_muestra = fecha_muestra;
	}

	public String getSincronizado() {
		return sincronizado;
	}

	public void setSincronizado(String sincronizado) {
		this.sincronizado = sincronizado;
	}

	public String toString(){
		return "DIIO: " + Integer.toString(getGanado().getDiio())
				+ "   Codigo: " + this.codBarra;
	}
	
}
