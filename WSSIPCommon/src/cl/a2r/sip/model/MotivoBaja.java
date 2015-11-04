/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.a2r.sip.model;

import java.io.Serializable;

/**
 *
 * @author Miguel Vega Brante
 */
public class MotivoBaja implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String codigo;
    private String nombre;

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

    @Override
    public String toString() {
        return this.getNombre();
    }

}
