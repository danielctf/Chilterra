package cl.a2r.sap.model;

import java.io.Serializable;

public class Potrero implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer numero;
	private double superficie;
	private Integer g_fundo_id;
	private Integer a_tipo_siembra_id;
	private Integer calificacion;
	private String sincronizado;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getNumero() {
		return numero;
	}
	
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
	public double getSuperficie() {
		return superficie;
	}
	
	public void setSuperficie(double superficie) {
		this.superficie = superficie;
	}
	
	public Integer getG_fundo_id() {
		return g_fundo_id;
	}
	
	public void setG_fundo_id(Integer g_fundo_id) {
		this.g_fundo_id = g_fundo_id;
	}
	
	public Integer getA_tipo_siembra_id() {
		return a_tipo_siembra_id;
	}
	
	public void setA_tipo_siembra_id(Integer a_tipo_siembra_id) {
		this.a_tipo_siembra_id = a_tipo_siembra_id;
	}
	
	public Integer getCalificacion() {
		return calificacion;
	}
	
	public void setCalificacion(Integer calificacion) {
		this.calificacion = calificacion;
	}
	
	public String getSincronizado() {
		return sincronizado;
	}
	
	public void setSincronizado(String sincronizado) {
		this.sincronizado = sincronizado;
	}
	
}
