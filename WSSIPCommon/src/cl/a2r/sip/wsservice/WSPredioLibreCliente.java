package cl.a2r.sip.wsservice;

import java.util.List;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.common.wsutils.ServiceWS;
import cl.a2r.sip.model.InyeccionTB;

public class WSPredioLibreCliente {

    public static List traePredioLibre(Integer g_fundo_id) throws AppException {

        ParamServlet params = new ParamServlet();
        params.add("servicio", "traePredioLibre" );
        params.add("g_fundo_id", g_fundo_id);

        Object obj = ServiceWS.invocaWS("WSPredioLibre", params );
        if ( obj != null && obj.getClass().getName().contains("AppException")) {
            throw (AppException) obj;
        } else if (obj == null){
        	throw new AppException("Error de conexión a Internet", null);
        } else {
            return (List) obj;
        }

    }
	
    public static List traeAllDiio() throws AppException {

        ParamServlet params = new ParamServlet();
        params.add("servicio", "traeAllDiio" );

        Object obj = ServiceWS.invocaWS("WSPredioLibre", params );
        if ( obj != null && obj.getClass().getName().contains("AppException")) {
            throw (AppException) obj;
        } else if (obj == null){
        	throw new AppException("Error de conexión a Internet", null);
        } else {
            return (List) obj;
        }

    }
    
    public static List traeTuberculinaPPD() throws AppException {

        ParamServlet params = new ParamServlet();
        params.add("servicio", "traeTuberculinaPPD" );

        Object obj = ServiceWS.invocaWS("WSPredioLibre", params );
        if ( obj != null && obj.getClass().getName().contains("AppException")) {
            throw (AppException) obj;
        } else if (obj == null){
        	throw new AppException("Error de conexión a Internet", null);
        } else {
            return (List) obj;
        }

    }
    
    public static List traeGanadoTuberculina(Integer instancia) throws AppException {

        ParamServlet params = new ParamServlet();
        params.add("servicio", "traeGanadoTuberculina" );
        params.add("instancia", instancia );

        Object obj = ServiceWS.invocaWS("WSPredioLibre", params );
        if ( obj != null && obj.getClass().getName().contains("AppException")) {
            throw (AppException) obj;
        } else if (obj == null){
        	throw new AppException("Error de conexión a Internet", null);
        } else {
            return (List) obj;
        }

    }
    
    public static void insertaGanadoTuberculina(List<InyeccionTB> ganList) throws AppException {

        ParamServlet params = new ParamServlet();
        params.add("servicio", "insertaGanadoTuberculina" );
        params.add("ganList", ganList );

        Object obj = ServiceWS.invocaWS("WSPredioLibre", params );
        if ( obj != null && obj.getClass().getName().contains("AppException")) {
            throw (AppException) obj;
        } else if (obj == null){
        	throw new AppException("Error de conexión a Internet", null);
        }

    }
    
    public static void insertaPredioLibre(Integer p_usuario_id, Integer g_fundo_id) throws AppException {

        ParamServlet params = new ParamServlet();
        params.add("servicio", "insertaPredioLibre" );
        params.add("p_usuario_id", p_usuario_id );
        params.add("g_fundo_id", g_fundo_id );

        Object obj = ServiceWS.invocaWS("WSPredioLibre", params );
        if ( obj != null && obj.getClass().getName().contains("AppException")) {
            throw (AppException) obj;
        } else if (obj == null){
        	throw new AppException("Error de conexión a Internet", null);
        }

    }
    
}
