package cl.a2r.sip.model;

import java.io.Serializable;

public class MedicamentoControl implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer serie;
	private Integer lote;
	private Integer cantidad;
	private Medicamento med;
	
	public MedicamentoControl(){
		med = new Medicamento();
	}
	
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
	
	public Integer getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	public Medicamento getMed() {
		return med;
	}
	
	public void setMed(Medicamento med) {
		this.med = med;
	}
	
	public String toString(){
		if (this.med.getNombre() != null){
			return this.med.getNombre() + " Serie " + Integer.toString(this.serie);
		} else {
			return "";
		}
	}
	
}
