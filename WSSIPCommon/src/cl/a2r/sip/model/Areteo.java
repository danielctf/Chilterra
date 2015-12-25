package cl.a2r.sip.model;

import java.io.Serializable;

public class Areteo implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer userId;
	private Integer predioId;
	private Integer ganadoId;
	private long eid;
	private Integer diio;
	private Integer collarId;
	private String collarNombre;
	private String sexo;
	private Integer razaId;
	private Integer edadEnMeses;
	private Integer diioAnterior;
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getPredioId() {
		return predioId;
	}
	
	public void setPredioId(Integer predioId) {
		this.predioId = predioId;
	}
	
	public Integer getDiio() {
		return diio;
	}
	
	public void setDiio(Integer diio) {
		this.diio = diio;
	}
	
	public long getEid() {
		return eid;
	}
	
	public void setEid(long eid) {
		this.eid = eid;
	}
	
	public String getSexo() {
		return sexo;
	}
	
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	public Integer getCollarId() {
		return collarId;
	}
	
	public void setCollarId(Integer collarId) {
		this.collarId = collarId;
	}
	
	public Integer getRazaId() {
		return razaId;
	}
	
	public void setRazaId(Integer razaId) {
		this.razaId = razaId;
	}

	public Integer getDiioAnterior() {
		return diioAnterior;
	}

	public void setDiioAnterior(Integer diioAnterior) {
		this.diioAnterior = diioAnterior;
	}

	public Integer getEdadEnMeses() {
		return edadEnMeses;
	}

	public void setEdadEnMeses(Integer edadEnMeses) {
		this.edadEnMeses = edadEnMeses;
	}

	public Integer getGanadoId() {
		return ganadoId;
	}

	public void setGanadoId(Integer ganadoId) {
		this.ganadoId = ganadoId;
	}

	public String getCollarNombre() {
		return collarNombre;
	}

	public void setCollarNombre(String collarNombre) {
		this.collarNombre = collarNombre;
	}
	
	public String toString(){
		return "Collar: " + this.getCollarNombre() + "  DIIO: " + Integer.toString(this.diio);
	}

}
