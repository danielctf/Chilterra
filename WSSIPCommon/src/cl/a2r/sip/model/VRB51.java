package cl.a2r.sip.model;

import java.io.Serializable;

public class VRB51 implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Ganado gan;
	private MedicamentoControl med;
	private String bang;
	private String sincronizado;
	
	public VRB51(){
		gan = new Ganado();
		med = new MedicamentoControl();
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Ganado getGan() {
		return gan;
	}
	
	public void setGan(Ganado gan) {
		this.gan = gan;
	}
	
	public MedicamentoControl getMed() {
		return med;
	}
	
	public void setMed(MedicamentoControl med) {
		this.med = med;
	}
	
	public String getBang() {
		return bang;
	}
	
	public void setBang(String bang) {
		this.bang = bang;
	}
	
	public String getSincronizado() {
		return sincronizado;
	}
	
	public void setSincronizado(String sincronizado) {
		this.sincronizado = sincronizado;
	}
	
	public String toString(){
		return "DIIO: " + Integer.toString(this.getGan().getDiio())
				+ "  Bang: " + this.bang 
				+ "  Serie: " + Integer.toString(this.getMed().getSerie());
	}
	
}
