package cl.a2r.sip.model;

import java.io.Serializable;
import java.util.Date;

public class VRB51 implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Ganado gan;
	private MedicamentoControl med;
	private Bang bang;
	private Date fecha;
	private String sincronizado;
	
	public VRB51(){
		gan = new Ganado();
		med = new MedicamentoControl();
		bang = new Bang();
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
	
	public Bang getBang() {
		return bang;
	}

	public void setBang(Bang bang) {
		this.bang = bang;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getSincronizado() {
		return sincronizado;
	}
	
	public void setSincronizado(String sincronizado) {
		this.sincronizado = sincronizado;
	}
	
	public String toString(){
		if (this.bang.getBang() != null){
			return "DIIO: " + Integer.toString(this.gan.getDiio())
					+ "  Bang: " + this.bang.getBang()
					+ "  Serie: " + Integer.toString(this.med.getSerie());
		} else {
			return "DIIO: " + Integer.toString(this.gan.getDiio())
					+ "  Bang:" 
					+ "  Serie: " + Integer.toString(this.med.getSerie());
		}
	}
	
}
