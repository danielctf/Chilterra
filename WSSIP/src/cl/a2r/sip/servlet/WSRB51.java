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
import cl.a2r.sip.model.Bang;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Secado;
import cl.a2r.sip.model.VRB51;
import cl.a2r.sip.service.AreteosService;
import cl.a2r.sip.service.BajasService;
import cl.a2r.sip.service.RB51Service;
import cl.a2r.sip.service.SecadosService;

public class WSRB51 extends HttpServlet{

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
        	Integer i = 1;
            params = (ParamServlet) inputFromApplet.readObject();

            String servicio = (String) params.getParam("servicio");

            if (servicio.equals("traeBang")){
            	List list = RB51Service.traeBang();
            	retorno = list;
            } else if (servicio.equals("traeCandidatos")){
            	Integer g_fundo_id = (Integer) params.getParam("g_fundo_id");
            	List list = RB51Service.traeCandidatos(g_fundo_id);
            	retorno = list;
            } else if (servicio.equals("traeGanadoRB51")){
            	List list = RB51Service.traeGanadoRB51();
            	retorno = list;
            } else if (servicio.equals("traeNumeroVacuna")){
            	Integer numeroVacuna = RB51Service.traeNumeroVacuna();
            	retorno = numeroVacuna;
            } else if (servicio.equals("traeMedicamentos")){
            	Integer appId = (Integer) params.getParam("appId");
            	List list = RB51Service.traeMedicamentos(appId);
            	retorno = list;
            } else if (servicio.equals("deleteBang")){
            	List<Bang> list = (List<Bang>) params.getParam("bangList");
            	Integer usuarioId = (Integer) params.getParam("usuarioId");
            	RB51Service.deleteBang(list, usuarioId);
            	retorno = i;
            } else if (servicio.equals("insertaRB51")){
            	List<VRB51> list = (List<VRB51>) params.getParam("rbList");
            	Integer usuarioId = (Integer) params.getParam("usuarioId");
            	RB51Service.insertaRB51(list, usuarioId);
            	retorno = i;
            } else if (servicio.equals("traeGanadoRB51Anterior")){
            	List list = RB51Service.traeGanadoRB51Anterior();
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