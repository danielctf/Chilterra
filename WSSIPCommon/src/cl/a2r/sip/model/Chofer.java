package cl.a2r.sip.model;

import java.io.Serializable;

public class Chofer implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String activa;
	private String nombre;
	private Integer transportistaId;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getActiva() {
		return activa;
	}
	
	public void setActiva(String activa) {
		this.activa = activa;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Integer getTransportistaId() {
		return transportistaId;
	}

	public void setTransportistaId(Integer transportistaId) {
		this.transportistaId = transportistaId;
	}

	public String toString(){
		return this.nombre;
	}
	
}
