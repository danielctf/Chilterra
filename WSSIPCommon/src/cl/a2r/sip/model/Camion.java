package cl.a2r.sip.model;

import java.io.Serializable;

public class Camion implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String activa;
	private String nombre;
	
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
	
	public String toString(){
		return this.nombre;
	}
	
}
