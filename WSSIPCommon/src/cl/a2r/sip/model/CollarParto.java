package cl.a2r.sip.model;

import java.io.Serializable;

public class CollarParto implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer predioId;
	private String codigo;
	private String nombre;
	
	public Integer getPredioId() {
		return predioId;
	}
	
	public void setPredioId(Integer predioId) {
		this.predioId = predioId;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String toString() {
		return this.nombre;
	}
	
}
