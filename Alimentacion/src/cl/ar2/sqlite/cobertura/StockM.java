package cl.ar2.sqlite.cobertura;

import java.util.Date;

import cl.a2r.sap.model.Medicion;

public class StockM {
	private Integer id;
	private Medicion med;
	private Date actualizado;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Medicion getMed() {
		return med;
	}
	
	public void setMed(Medicion med) {
		this.med = med;
	}
	
	public Date getActualizado() {
		return actualizado;
	}
	
	public void setActualizado(Date actualizado) {
		this.actualizado = actualizado;
	}
	
	public String toString(){
		return this.med.toString();
	}
}
