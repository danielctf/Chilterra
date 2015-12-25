/*
 * Desarrollado por : Miguel Vega B.
 */

package cl.a2r.common.wsutils;

import java.io.Serializable;

/**
 *
 * @author Miguelon
 */
public class Param implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private Object obj;

    public Param( String nombre, Object obj ) {
        this.nombre = nombre;
        this.obj = obj;
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

    /**
     * @return the obj
     */
    public Object getObj() {
        return obj;
    }

    /**
     * @param obj the obj to set
     */
    public void setObj(Object obj) {
        this.obj = obj;
    }

}
