package cl.a2r.sip.servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.a2r.common.AppException;
import cl.a2r.common.wsutils.ParamServlet;
import cl.a2r.sip.common.AppLog;
import cl.a2r.sip.model.Areteo;
import cl.a2r.sip.model.Baja;
import cl.a2r.sip.service.AreteosService;
import cl.a2r.sip.service.BajasService;

public class WSAreteos extends HttpServlet{

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

            if (servicio.equals("traeRaza") ) {
                List list = AreteosService.traeRaza();
                retorno = list;
            } else if (servicio.equals("traeCollarAreteo")){
            	Integer fundoId = (Integer) params.getParam("fundoId");
            	List list = AreteosService.traeCollarAreteo(fundoId);
            	retorno = list;
            } else if (servicio.equals("traeTipoGanado")){
            	List list = AreteosService.traeTipoGanado();
            	retorno = list;
            } else if (servicio.equals("traeAreteosEncontrados")){
            	Integer fundoId = (Integer) params.getParam("fundoId");
            	List list = AreteosService.traeAreteosEncontrados(fundoId);
            	retorno = list;
            } else if (servicio.equals("traeAreteosFaltantes")){
            	Integer fundoId = (Integer) params.getParam("fundoId");
            	List list = AreteosService.traeAreteosFaltantes(fundoId);
            	retorno = list;
            } else if (servicio.equals("liberaCollar")){
            	Integer collarId = (Integer) params.getParam("collarId");
            	Integer usuarioId = (Integer) params.getParam("usuarioId");
            	AreteosService.liberaCollar(collarId, usuarioId);
            	Integer i = 1;
            	retorno = i;
            } else if (servicio.equals("insertaAreteoAlta")){
            	Areteo alta = (Areteo) params.getParam("alta");
            	AreteosService.insertaAreteoAlta(alta);
            	Integer i = 1;
            	retorno = i;
            } else if (servicio.equals("insertaAreteoAparicion")){
            	Areteo aparicion = (Areteo) params.getParam("aparicion");
            	AreteosService.insertaAreteoAparicion(aparicion);
            	Integer i = 1;
            	retorno = i;
            } else if (servicio.equals("insertaAreteoCambioDiio")){
            	Areteo cambiodiio = (Areteo) params.getParam("cambiodiio");
            	AreteosService.insertaAreteoCambioDiio(cambiodiio);
            	Integer i = 1;
            	retorno = i;
            } else if (servicio.equals("traeApariciones")){
            	Integer usuarioId = (Integer) params.getParam("usuarioId");
            	Integer fundoId = (Integer) params.getParam("fundoId");
            	List list = AreteosService.traeApariciones(usuarioId, fundoId);
            	retorno = list;
            } else if (servicio.equals("traeCambioDiio")){
            	Integer usuarioId = (Integer) params.getParam("usuarioId");
            	Integer fundoId = (Integer) params.getParam("fundoId");
            	List list = AreteosService.traeCambioDiio(usuarioId, fundoId);
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
