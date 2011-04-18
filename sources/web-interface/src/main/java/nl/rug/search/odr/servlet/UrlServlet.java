
package nl.rug.search.odr.servlet;

import java.io.IOException;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.util.url.Binding;
import nl.rug.search.odr.util.url.ProjectBinding;
import nl.rug.search.odr.util.url.UrlTarget;
import java.util.Hashtable;
import java.util.Map.Entry;
import javax.ejb.EJB;
import nl.rug.search.odr.EjbUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@WebServlet(name="UrlServlet", urlPatterns={"/p/*"})
public class UrlServlet extends HttpServlet {

    private static final Binding root;

    static {
        ProjectLocal pl;
        pl = EjbUtil.lookUp(ProjectLocalHelper.JNDI_NAME, ProjectLocal.class);
        
        root = new Binding("^[/]?").to("/projects.html");

        Binding projectDetailView;
        projectDetailView = new ProjectBinding("^(\\w{2,100})[/]?", pl);
        root.bind(projectDetailView).to("/projectDetails.html?id=%s");
        
        Binding iterationOverview = projectDetailView.bind("^iterations[/]?");
        Binding iterationAddView = iterationOverview.bind("^new[/]?");
        Binding iterationDetailView = iterationOverview.bind("^\\d+[/]?");
        Binding iterationEditView = iterationDetailView.bind("^edit[/]?");
        Binding iterationDeleteView = iterationDetailView.bind("^delete[/]?");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String path = request.getPathInfo();

        if (path == null) {
            path = "";
        }

        System.out.println(root.check(path, request, response));
    } 

    private static void projectOverview(List<String> args) {
        
    }

    private static void iterationOverview(List<String> args) {
        
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

    @EJB(name = ProjectLocalHelper.JNDI_NAME, beanInterface = ProjectLocal.class)
    private class ProjectLocalHelper {

        public static final String JNDI_NAME = "nl.rug.search.odr.servlet.UrlServlet.Helper";
    }
}
