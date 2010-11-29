package nl.rug.search.odr.servlet;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.viewpoint.InitRelationshipView;
import nl.rug.search.odr.viewpoint.Viewpoint;
import nl.rug.search.odr.viewpoint.ViewpointExclusionStrategy;
import nl.rug.search.odr.viewpoint.Visualization;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@WebServlet(name = "ViewpointDataProvider",
            urlPatterns = {"/ViewpointDataProvider"})
public class ViewpointDataProvider extends HttpServlet {

    @EJB
    private ProjectLocal pl;


    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (!AuthenticationUtil.isAuthenticated(request.getSession())) {
            // TODO inform the user that he is not logged in? This only happens when the user is abusing the system
            return;
        }
        
        long userId = AuthenticationUtil.getUserId(request.getSession());

        long projectId;

        try {
            projectId = Long.parseLong(request.getParameter(RequestParameter.ID));
        } catch (NumberFormatException ex) {
            out.println("Invalid id");
            return;
        }

        Project p = pl.getById(projectId);

        if (p == null) {
            // TODO: Inform the user that the project is not existing? This only happens when the user is abusing the system
            return;
        }

        boolean isMember = false;

        for(ProjectMember pm : p.getMembers()) {
            if (pm.getPerson().getId().equals(userId)) {
                isMember = true;
                break;
            }
        }

        if (!isMember) {
            // TODO: Inform the user that he is not a member of the project? This only happens when the user is abusing the system
            return;
        }

        String stakeholderParam = request.getParameter(RequestParameter.STAKEHOLDER_VIEWPOINT);
        String relationshipParam = request.getParameter(RequestParameter.RELATIONSHIP_VIEWPOINT);
        String chronologicalParam = request.getParameter(RequestParameter.CHRONOLOGICAL_VIEWPOINT);

        int paramCounter = 0;
        paramCounter += stakeholderParam != null ? 1 : 0;
        paramCounter += relationshipParam != null ? 1 : 0;
        paramCounter += chronologicalParam != null ? 1 : 0;

        try {
            if (paramCounter != 1) {
                out.println("Illegal amount of parameters");
                return;
            }

            Viewpoint point =  (stakeholderParam != null) ? Viewpoint.STAKEHOLDER_INVOLVEMENT : Viewpoint.CHRONOLOGICAL;
            point = (relationshipParam != null) ? Viewpoint.RELATIONSHIP : point;

            Gson gson = new GsonBuilder().setExclusionStrategies(new ViewpointExclusionStrategy(point)).
                    serializeNulls().
                    setDateFormat("yyyy-MM-dd HH:mm z").
                    setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).
                    create();

            InitRelationshipView relationshipView = new InitRelationshipView(point, p);

            Visualization v = relationshipView.getView();

            out.print(gson.toJson(v));
        } finally {
            out.close();
        }
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
