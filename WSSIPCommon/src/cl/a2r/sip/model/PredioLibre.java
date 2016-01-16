package cl.a2r.sip.model;

import java.io.Serializable;
import java.util.Date;

public class PredioLibre implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String nombreFundo;
	private Date fecha_inicio;
	private Date fecha_termino;
	private String estado;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNombreFundo() {
		return nombreFundo;
	}
	
	public void setNombreFundo(String nombreFundo) {
		this.nombreFundo = nombreFundo;
	}
	
	public Date getFecha_inicio() {
		return fecha_inicio;
	}
	
	public void setFecha_inicio(Date fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}
	
	public Date getFecha_termino() {
		return fecha_termino;
	}
	
	public void setFecha_termino(Date fecha_termino) {
		this.fecha_termino = fecha_termino;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String toString(){
		return "Fundo: " + this.getNombreFundo() + "\n" + "Id: " + this.getId(); 
	}
	
}
