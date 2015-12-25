/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.ar2.sqlite.cobertura;

import cl.a2r.sap.model.Medicion;
import java.util.Date;

/**
 *
 * @author Miguel Vega Brante
 */
public class RegistroMedicion {
    private Integer id;
    private Date fechaHora;
    private Medicion medicion;
    private String sincronizado;

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
     * @return the fechaHora
     */
    public Date getFechaHora() {
        return fechaHora;
    }

    /**
     * @param fechaHora the fechaHora to set
     */
    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    /**
     * @return the medicion
     */
    public Medicion getMedicion() {
        return medicion;
    }

    /**
     * @param medicion the medicion to set
     */
    public void setMedicion(Medicion med) {
        this.medicion = med;
    }

    /**
     * @return the sincronizado
     */
    public String getSincronizado() {
        return sincronizado;
    }

    /**
     * @param sincronizado the sincronizado to set
     */
    public void setSincronizado(String sincronizado) {
        this.sincronizado = sincronizado;
    }
    
    public String toString(){
    	return this.medicion.toString();
    }
}
