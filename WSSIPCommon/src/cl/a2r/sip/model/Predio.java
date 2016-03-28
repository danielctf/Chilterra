package cl.a2r.sip.model;

import java.io.Serializable;

public class Predio implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String codigo;
	private String nombre;
	private String rup;
	private Integer potreros;
	
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

	public String getRup() {
		return rup;
	}

	public void setRup(String rup) {
		this.rup = rup;
	}
	
    public Integer getPotreros() {
		return potreros;
	}

	public void setPotreros(Integer potreros) {
		this.potreros = potreros;
	}

	public String toString() {
        return this.getNombre();
    }
	
}
