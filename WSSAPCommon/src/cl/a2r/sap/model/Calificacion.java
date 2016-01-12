package cl.a2r.sap.model;

import java.io.Serializable;

public class Calificacion implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer g_fundo_id;
	private Integer numero;
	private Integer calificacion;
	private String sincronizado;
	
	public String getSincronizado() {
		return sincronizado;
	}

	public void setSincronizado(String sincronizado) {
		this.sincronizado = sincronizado;
	}

	public Integer getG_fundo_id() {
		return g_fundo_id;
	}
	
	public void setG_fundo_id(Integer g_fundo_id) {
		this.g_fundo_id = g_fundo_id;
	}
	
	public Integer getNumero() {
		return numero;
	}
	
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
	public Integer getCalificacion() {
		return calificacion;
	}
	
	public void setCalificacion(Integer calificacion) {
		this.calificacion = calificacion;
	}
	
}
