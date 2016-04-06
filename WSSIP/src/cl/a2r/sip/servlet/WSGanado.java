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
import cl.a2r.sip.model.Traslado;
import cl.a2r.sip.service.GanadoService;
import cl.a2r.sip.service.TrasladosService;

public class WSGanado extends HttpServlet{

	/**
	 * 
	 */
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

            if (servicio.equals("traeGanado") ) {

                Integer diio = (Integer) params.getParam("diio");
                List list = GanadoService.traeGanado(diio);
                retorno = list;

            } else if (servicio.equals("traeGanadoBaston") ){
                long eid = Long.parseLong(((String) params.getParam("eid")).trim());
                List list = GanadoService.traeGanadoBaston(eid);
                retorno = list;
            } else if (servicio.equals("traeDiio")){
            	Integer p_diio = (Integer) params.getParam("diio");
            	Integer diio = GanadoService.traeDiio(p_diio);
            	retorno = diio;
            } else if (servicio.equals("traeDiioBaston")){
                long eid = Long.parseLong(((String) params.getParam("eid")).trim());
                Integer diio = GanadoService.traeDiioBaston(eid);
                retorno = diio;
            } else if (servicio.equals("reajustaGanado")){
            	List<Traslado> trasList = (List<Traslado>) params.getParam("trasList");
            	for (Traslado traslado : trasList){
	            	Integer g_movimiento_id = TrasladosService.insertaMovimiento(traslado);
	            	traslado.setG_movimiento_id(g_movimiento_id);
	            	TrasladosService.insertaMovtoConfirm(traslado);
            	}
            	Integer i = 1;
            	retorno = i;
            } else if (servicio.equals("traeGanadoBusqueda")){
            	List list = GanadoService.traeGanadoBusqueda();
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
