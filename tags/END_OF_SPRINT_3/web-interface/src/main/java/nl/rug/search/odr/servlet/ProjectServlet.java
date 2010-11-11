package nl.rug.search.odr.servlet;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.project.ProjectLocal;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@WebServlet(name = "ProjectServlet", urlPatterns = {"/project/*", "/p/*"})
public class ProjectServlet extends HttpServlet {

    @EJB
    private ProjectLocal projectLocal;

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String[] parts = request.getPathInfo().split("/");


        String target = null;

        if (parts.length == 2) {
            if (RequestParameter.CREATE.equalsIgnoreCase(parts[1])) {
                target = "/createProject.html?create=true";
                request.getRequestDispatcher(target).forward(request, response);
                return;
            } else {
                target = "/projectDetails.html?".concat(RequestParameter.ID).concat("=");
            }
        } else if (parts.length > 2) {
            if (parts[2].equalsIgnoreCase(RequestParameter.UPDATE)) {
                target = "/updateProject.html?".concat(RequestParameter.UPDATE).
                        concat("=true&").concat(RequestParameter.ID).concat("=");
            } else if (parts[2].equalsIgnoreCase(RequestParameter.DELETE)) {
                target = "/deleteProject.html?".concat(RequestParameter.DELETE).
                        concat("=true&").concat(RequestParameter.ID).concat("=");
            }
        } else {
            // TODO: handle error, user used some path that should not exist
            return;
        }


        Project p = null;
        p = projectLocal.getByName(parts[1]);

        if (p == null) {
            target += -1;
        } else {
            target += p.getId();
        }

        request.getRequestDispatcher(target).forward(request, response);
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
