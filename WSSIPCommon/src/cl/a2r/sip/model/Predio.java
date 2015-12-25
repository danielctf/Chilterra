package cl.a2r.sip.model;

import java.io.Serializable;

public class Predio implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String codigo;
	private String nombre;
	private String rup;
	private Integer potreros;
	
    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
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
