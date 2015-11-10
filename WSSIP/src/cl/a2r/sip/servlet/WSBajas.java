/*
 * WSSIP
 * Todos los derechos reservados.
 */

package cl.a2r.sip.servlet;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.model.Baja;
import cl.a2r.sip.service.BajasService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Miguel Vega Brante
 */
public class WSBajas extends HttpServlet {
    private static final long serialVersionUID = 1L;
   
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

            String servicio = (String) params.getParam("servicio");

            if (servicio.equals("traeMotivos") ) {

//                Integer idUsuario = (Integer) params.getParam("idUsuario");
                List list = BajasService.traeMotivos();
                retorno = list;
            } else if (servicio.equals("traeCausas")) {
                List list = BajasService.traeCausas();
                retorno = list;
            } else if (servicio.equals("insertaBaja")){
            	Baja baja = (Baja) params.getParam("baja");
            	BajasService.insertaBaja(baja);
            } else if (servicio.equals("traeBajas")){
            	Integer userId = (Integer) params.getParam("userId");
            	Integer fundoId = (Integer) params.getParam("fundoId");
            	List list = BajasService.traeBajas(userId, fundoId);
            	retorno = list;
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