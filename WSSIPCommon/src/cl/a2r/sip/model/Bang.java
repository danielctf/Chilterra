package cl.a2r.sip.model;

import java.io.Serializable;

public class Bang implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String bang;
	private String borrar;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getBang() {
		return bang;
	}
	
	public void setBang(String bang) {
		this.bang = bang;
	}
	
	public String getBorrar() {
		return borrar;
	}

	public void setBorrar(String borrar) {
		this.borrar = borrar;
	}

	public String toString(){
		return this.bang;
	}
	
}
