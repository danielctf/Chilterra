package cl.a2r.sip.servlet;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.model.Brucelosis;
import cl.a2r.sip.model.InyeccionTB;
import cl.a2r.sip.model.Sesion;
import cl.a2r.sip.service.AutorizacionService;
import cl.a2r.sip.service.PredioLibreService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Miguel Vega Brante
 */
public class WSPredioLibre extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        Object retorno = null;

        ObjectInputStream inputFromApplet = new ObjectInputStream(request.getInputStream());
        ParamServlet params = null;

        try {
            params = (ParamServlet) inputFromApplet.readObject();
            Integer i = 1;
            String servicio = (String) params.getParam("servicio");
            if (servicio.equals("traePredioLibre")){
            	Integer g_fundo_id = (Integer) params.getParam("g_fundo_id");
            	List list = PredioLibreService.traePredioLibre(g_fundo_id);
            	retorno = list;
            } else if (servicio.equals("traeAllDiio")){
            	List list = PredioLibreService.traeAllDiio();
            	retorno = list;
            } else if (servicio.equals("traeGanadoTuberculina")){
            	Integer instancia = (Integer) params.getParam("instancia");
            	List list = PredioLibreService.traeGanadoTuberculina(instancia);
            	retorno = list;
            } else if (servicio.equals("insertaGanadoTuberculina")){
            	List<InyeccionTB> list = (List<InyeccionTB>) params.getParam("ganList");
            	PredioLibreService.insertaGanadoTuberculina(list);
            	retorno = i;
            } else if (servicio.equals("traeTuberculinaPPD")){
            	List list = PredioLibreService.traeTuberculinaPPD();
            	retorno = list;
            } else if (servicio.equals("insertaPredioLibre")){
            	Integer p_usuario_id = (Integer) params.getParam("p_usuario_id");
            	Integer g_fundo_id = (Integer) params.getParam("g_fundo_id");
            	PredioLibreService.insertaPredioLibre(p_usuario_id, g_fundo_id);
            	retorno = i;
            } else if (servicio.equals("updateLecturaTB")){
            	List<InyeccionTB> list = (List<InyeccionTB>) params.getParam("list");
            	PredioLibreService.updateLecturaTB(list);
            	retorno = i;
            } else if (servicio.equals("traeGanadoBrucelosis")){
            	Integer instancia = (Integer) params.getParam("instancia");
            	List list = PredioLibreService.traeGanadoBrucelosis(instancia);
            	retorno = list;
            } else if (servicio.equals("insertaGanadoBrucelosis")){
            	List<Brucelosis> list = (List<Brucelosis>) params.getParam("list");
            	PredioLibreService.insertaGanadoBrucelosis(list);
            	retorno = i;
            } else if (servicio.equals("cerrarInstancia")){
            	Integer g_usuario_id = (Integer) params.getParam("g_usuario_id");
            	Integer instancia = (Integer) params.getParam("instancia");
            	PredioLibreService.cerrarInstancia(g_usuario_id, instancia);
            	retorno = i;
            } else {
            	retorno = new AppException("Servicio no válido.", null);
            }
        } catch (ClassNotFoundException ex) {
            retorno = new AppException("Error en parametros (ParamServlet).", ex);
            AppLog.logSevere(((AppException)retorno).getMessage(), ex);
        } catch (AppException ex) {
            AppLog.logSevere(ex.getMessage(), ex);
            retorno = ex;
        }

        response.setContentType("application/x-java-serialized-object");
        ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream());
        oos.writeObject(retorno);
        oos.flush();
        oos.close();
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}