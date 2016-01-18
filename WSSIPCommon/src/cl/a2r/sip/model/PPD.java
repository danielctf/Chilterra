package cl.a2r.sip.model;

import java.io.Serializable;
import java.util.Date;

public class PPD implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer serie;
	private Integer lote;
	private Date vencimiento;
	private Integer cantidad;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getSerie() {
		return serie;
	}
	
	public void setSerie(Integer serie) {
		this.serie = serie;
	}
	
	public Integer getLote() {
		return lote;
	}
	
	public void setLote(Integer lote) {
		this.lote = lote;
	}
	
	public Date getVencimiento() {
		return vencimiento;
	}
	
	public void setVencimiento(Date vencimiento) {
		this.vencimiento = vencimiento;
	}
	
	public Integer getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	public String toString(){
		return Integer.toString(this.serie);
	}
	
}
