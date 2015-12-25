/*
 * Desarrollado por : Miguel Vega B.
 */

package cl.a2r.common.wsutils;

import cl.a2r.common.AppException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Miguelon
 */
public class ParamServlet implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List params = new ArrayList();

    public void add( String nombre, Object obj ) {
        params.add( new Param(nombre, obj) );
    }

    public List getParams() {
        return params;
    }

    public Object getParam( String nombre ) throws AppException {
        Object ret = null;
        boolean encontro = false;

        for ( Iterator it = params.iterator(); it.hasNext(); ) {
            Param p = (Param) it.next();
            if ( p.getNombre().equals(nombre)) {
                ret = p.getObj();
                encontro = true;
                break;
            }
        }
        if ( !encontro ) {
            throw new AppException("Error en servlet: Parámetro \"" + nombre + "\" no existe.", null);
        }
        return ret;
    }

    public Object getParamOpc( String nombre, Object retSiNoEncontro ) throws AppException {
        Object ret = null;
        boolean encontro = false;

        for ( Iterator it = params.iterator(); it.hasNext(); ) {
            Param p = (Param) it.next();
            if ( p.getNombre().equals(nombre)) {
                ret = p.getObj();
                encontro = true;
                break;
            }
        }
        if ( !encontro ) {
//            throw new AppException("Error en servlet: Parámetro \"" + nombre + "\" no existe.", null);
            ret = retSiNoEncontro;
        }
        return ret;
    }

}
