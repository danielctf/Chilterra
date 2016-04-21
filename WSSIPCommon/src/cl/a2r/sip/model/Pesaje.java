package cl.a2r.sip.model;

import java.io.Serializable;
import java.util.Date;

public class Pesaje implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Ganado gan;
	private Date fecha;
	private double peso;
	private double gpd;
	private String sincronizado;
	
	public Pesaje(){
		gan = new Ganado();
	}
	
	public Ganado getGan(){
		return this.gan;
	}
	
	public void setGan(Ganado gan){
		this.gan = gan;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public double getPeso() {
		return peso;
	}
	
	public void setPeso(double peso) {
		this.peso = peso;
	}
	
	public double getGpd() {
		return gpd;
	}

	public void setGpd(double gpd) {
		this.gpd = gpd;
	}

	public String getSincronizado() {
		return sincronizado;
	}
	
	public void setSincronizado(String sincronizado) {
		this.sincronizado = sincronizado;
	}
	
	public String toString(){
		return "DIIO: " + Integer.toString(this.gan.getDiio()) + "\n"
				+ "Peso: " + Double.toString(peso) + " Kg" + "\n"
				+ "Ganancia: " + Double.toString(gpd) + " Kg/día" + "\n";
	}
}
