package nl.rug.search.odr.servlet;

import com.google.gson.Gson;
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
import nl.rug.search.odr.project.RelationshipViewVisualizationLocal;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.util.GsonUtil;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewAssociation;
import nl.rug.search.odr.viewpoint.Handle;
import nl.rug.search.odr.viewpoint.relationship.InitRelationshipView;
import nl.rug.search.odr.viewpoint.Viewpoint;
import nl.rug.search.odr.viewpoint.ViewpointExclusionStrategy;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewVisualization;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@WebServlet(name = "ViewpointDataProvider",
            urlPatterns = {"/ViewpointDataProvider"})
public class ViewpointDataProvider extends HttpServlet {

    @EJB
    private ProjectLocal pl;

    @EJB
    private RelationshipViewVisualizationLocal vl;

    public static final int ERROR_CODE = 303;




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
            response.sendError(ERROR_CODE);
            return;
        }

        long userId = AuthenticationUtil.getUserId(request.getSession());

        long projectId;

        try {
            projectId = Long.parseLong(request.getParameter(RequestParameter.ID));
        } catch (NumberFormatException ex) {
            // TODO change to a more appropriate error value
            response.sendError(ERROR_CODE);
            return;
        }

        Project p = pl.getById(projectId);

        if (p == null) {
            // TODO: Inform the user that the project is not existing? This only happens when the user is abusing the system
            response.sendError(ERROR_CODE);
            return;
        }

        boolean isMember = false;

        for (ProjectMember pm : p.getMembers()) {
            if (pm.getPerson().getId().equals(userId)) {
                isMember = true;
                break;
            }
        }

        if (!isMember) {
            // TODO: Inform the user that he is not a member of the project? This only happens when the user is abusing the system
            response.sendError(ERROR_CODE);
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
                // TODO: Inform the user about the illegal amount of parameters?  This only happens when the user is abusing the system
                response.sendError(ERROR_CODE);
                return;
            }

            Viewpoint point = (stakeholderParam != null) ? Viewpoint.STAKEHOLDER_INVOLVEMENT : Viewpoint.CHRONOLOGICAL;
            point = (relationshipParam != null) ? Viewpoint.RELATIONSHIP : point;

            Gson gson = GsonUtil.getDefaultGson(new ViewpointExclusionStrategy(point));

            RelationshipViewVisualization v = getVisualization(p, point);

            out.print(gson.toJson(v));
        } finally {
            out.close();
        }
    }




    private RelationshipViewVisualization getVisualization(Project p, Viewpoint point) {
        if (point == Viewpoint.RELATIONSHIP) {
            return getRelationshipVisualization(p);
        }

        throw new UnsupportedOperationException("Only the relationship viewpoint is supported");
    }




    private RelationshipViewVisualization getRelationshipVisualization(Project p) {
        RelationshipViewVisualization existingVisualization = null;

        for (RelationshipViewVisualization v : p.getRelationshipViews()) {
            existingVisualization = v;

            // for now we just quit at this point. May be changed to support multiple relationship views
            break;
        }

        if (existingVisualization == null) {
            existingVisualization = initRelationshipView(p);
        } else {
            updateRelationshipView(p, existingVisualization);
        }

        return existingVisualization;
    }




    private RelationshipViewVisualization initRelationshipView(Project p) {
        InitRelationshipView relationshipView = new InitRelationshipView(p);

        RelationshipViewVisualization v = relationshipView.getView();

        p.addRelationshipView(v);
        vl.persist(v);
        pl.merge(p);

        // we are retrieving the visualization again from the database to get all IDs right
        v = vl.getById(v.getId());

        return v;
    }




    private void updateRelationshipView(Project p, RelationshipViewVisualization v) {
        InitRelationshipView relationshipView = new InitRelationshipView(p);

        relationshipView.updateView(v);

        vl.merge(v);

        // we are retrieving the visualization again from the database to get all IDs right
        v = vl.getById(v.getId());
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



