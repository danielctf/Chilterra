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
import cl.a2r.sip.model.Auditoria;
import cl.a2r.sip.model.Bang;
import cl.a2r.sip.model.Ganado;
import cl.a2r.sip.model.Traslado;
import cl.a2r.sip.model.VRB51;
import cl.a2r.sip.service.AuditoriaService;
import cl.a2r.sip.service.RB51Service;
import cl.a2r.sip.service.TrasladosService;

public class WSAuditoria extends HttpServlet{

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
            
            if (servicio.equals("insertaAuditoria")){
            	Auditoria a = (Auditoria) params.getParam("auditoria");
            	Integer usuarioId = (Integer) params.getParam("usuarioId");
            	AuditoriaService.insertaAuditoria(a, usuarioId);
            	retorno = i;
            } else if (servicio.equals("traeAuditoria")){
            	Integer fundoId = (Integer) params.getParam("fundoId");
            	List list = AuditoriaService.traeAuditoria(fundoId);
            	retorno = list;
            } else if (servicio.equals("cerrarAuditoria")){
            	Auditoria a = (Auditoria) params.getParam("auditoria");
            	Integer usuarioId = (Integer) params.getParam("usuarioId");
            	List<Ganado> reubicaList = AuditoriaService.cerrarAuditoria(a, usuarioId);
            	Traslado t = new Traslado();
            	t.setGanado(reubicaList);
            	t.setUsuarioId(usuarioId);
            	t.setFundoOrigenId(a.getFundoId());
            	t.setFundoDestinoId(28);
            	t.setDescripcion("AUDITORIA " + Integer.toString(a.getId()));
            	Integer g_movimiento_id = TrasladosService.insertaMovimiento(t);
            	t.setG_movimiento_id(g_movimiento_id);
            	TrasladosService.insertaMovtoConfirm(t);
            	retorno = i;
            } else if (servicio.equals("borrarAuditoria")){
            	Auditoria a = (Auditoria) params.getParam("auditoria");
            	Integer usuarioId = (Integer) params.getParam("usuarioId");
            	AuditoriaService.borrarAuditoria(a, usuarioId);
            	retorno = i;
            } else if (servicio.equals("insertaGanado")){
            	List<Auditoria> auList = (List<Auditoria>) params.getParam("auList");
            	Integer usuarioId = (Integer) params.getParam("usuarioId");
            	AuditoriaService.insertaGanado(auList, usuarioId);
            	retorno = i;
            } else if (servicio.equals("traeGanado")){
            	Integer instancia = (Integer) params.getParam("instancia");
            	Auditoria a = AuditoriaService.traeGanado(instancia);
            	retorno = a;
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