package cl.a2r.sip.servlet;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.model.GanadoLogs;
import cl.a2r.sip.model.Parto;
import cl.a2r.sip.service.AutorizacionService;
import cl.a2r.sip.service.PartosService;

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
public class WSPartos extends HttpServlet {
   
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

            if (servicio.equals("traeCollares") ) {

                Integer predioId = (Integer) params.getParam("predioId");
                List list = PartosService.traeCollares(predioId);
                retorno = list;

            } else if (servicio.equals("traeTipoPartos")){
	            List list = PartosService.traeTipoPartos();
	            retorno = list;
            } else if (servicio.equals("traeSubTipoPartos")){
                List list = PartosService.traeSubTipoPartos();
                retorno = list;
            } else if (servicio.equals("traePartoAnterior")){
            	Integer ganadoId = (Integer) params.getParam("ganadoId");
                List list = PartosService.traePartoAnterior(ganadoId);
                retorno = list;
            } else if (servicio.equals("traePartos")){
            	Integer userId = (Integer) params.getParam("userId");
            	Integer fundoId = (Integer) params.getParam("fundoId");
            	List list = PartosService.traePartos(userId, fundoId);
            	retorno = list;
            } else if (servicio.equals("traeCandidatosEncontrados")){
            	Integer fundoId = (Integer) params.getParam("fundoId");
            	List list = PartosService.traeCandidatosEncontrados(fundoId);
            	retorno = list;
            } else if (servicio.equals("traeCandidatosFaltantes")){
            	Integer fundoId = (Integer) params.getParam("fundoId");
            	List list = PartosService.traeCandidatosFaltantes(fundoId);
            	retorno = list;
            } else if (servicio.equals("insertaParto")){
            	Parto parto = (Parto) params.getParam("parto");
            	PartosService.insertaParto(parto);
            	Integer i = 1;
            	retorno = i;
            }else if (servicio.equals("confirmaParto")){
            	Integer ganadoId = (Integer) params.getParam("ganadoId");
            	Integer usuarioId = (Integer) params.getParam("usuarioId");
            	PartosService.confirmaParto(ganadoId, usuarioId);
            	Integer i = 1;
            	retorno = i;
            } else if (servicio.equals("traePartoPorConfirmar")){
            	Integer ganadoId = (Integer) params.getParam("ganadoId");
                List list = PartosService.traePartoPorConfirmar(ganadoId);
                retorno = list;
            } else if (servicio.equals("deshacerRegistroParto")){
            	GanadoLogs gl = (GanadoLogs) params.getParam("gl");
            	PartosService.deshacerRegistroParto(gl);
            	Integer i = 1;
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