/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.odr.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.rug.search.odr.MalformedJsonException;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.project.VisualizationLocal;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.util.GsonUtil;
import nl.rug.search.odr.viewpoint.Association;
import nl.rug.search.odr.viewpoint.Node;
import nl.rug.search.odr.viewpoint.Visualization;

/**
 *
 * @author ben
 */
@WebServlet(name="ViewpointDataReceiver", urlPatterns={"/ViewpointDataReceiver"})
public class ViewpointDataReceiver extends HttpServlet {

    @EJB
    private ProjectLocal pl;

    @EJB
    private VisualizationLocal vl;

    private static final Logger logger = Logger.getLogger(ViewpointDataReceiver.class.getName());

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

        String projectIdParameter = request.getParameter(RequestParameter.ID);
        String data = request.getParameter(RequestParameter.DATA);

        if (data == null || projectIdParameter == null) {
            // TODO: Inform user that request parameter are missing
            // This only happens when the user is abusing the system
            return;
        }

        long projectId = -1l;

        try {
            projectId = Long.parseLong(projectIdParameter);
        } catch (NumberFormatException ex) {
            // TODO: Inform user that the project id is invalid
            // This only happens when the user is abusing the system
            return;
        }


        Project p = pl.getById(projectId);

        if (p == null) {
            // TODO: Inform the user that the project is not existing? This only happens when the user is abusing the system
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
            return;
        }



        Gson gson = GsonUtil.getDefaultGson();

        Visualization v = null;

        try {
            v = gson.fromJson(data, Visualization.class);
        } catch (JsonParseException ex) {
            // TODO: Inform user that malformed data has been sent to the system.
            // This only happens when the user is abusing the system
            logger.log(Level.WARNING, "Malformed data has been sent to the server which can't be parsed.", ex);
            return;
        }

        Visualization visualizationFromDatabase = null;

        for (Visualization eachVisualization : p.getVisualizations()) {
            if (eachVisualization.getId().equals(v.getId())) {
                visualizationFromDatabase = eachVisualization;
                break;
            }
        }

        if (visualizationFromDatabase == null) {
            // TODO: Inform the user that the visualization does not belong to the project?
            // Most of the time this happens when the user is abusing the system but it could
            // also happen if some other user deletes the visualization
            logger.log(Level.WARNING, "Visualization is not part of the project.");
            return;
        }

        try {
            copyModifiedData(v, visualizationFromDatabase);
        } catch (MalformedJsonException ex) {
            // TODO: Inform the user that something went really wrong. This error might occur with two users
            // are editing the data of a project at the same time.
            logger.log(Level.WARNING, "Malformed data has been sent to the server which could be parsed but "
                    + "contains content-related errors.", ex);
            return;
        }
        

        // TODO return something that clearly indicates that everything went fine?
    } 

    private void copyModifiedData(Visualization source, Visualization target) {
        target.setName(source.getName());
        target.setDocumentedWhen(new Date());

        copyNodeInformation(source, target);
        copyAssociationInformation(source, target);

        vl.merge(target);
    }

    private void copyNodeInformation(Visualization source, Visualization target) {
        for(Node eachNodeInSource : source.getNodes()) {
            Node nodeInTarget = getNode(eachNodeInSource.getId(), target.getNodes());
            
            nodeInTarget.setVisible(eachNodeInSource.isVisible());
            nodeInTarget.setX(eachNodeInSource.getX());
            nodeInTarget.setY(eachNodeInSource.getY());
        }
    }

    private Node getNode(long id, Collection<Node> nodes) {
        for(Node n : nodes) {
            if (n.getId().equals(id)) {
                return n;
            }
        }

        throw new MalformedJsonException("Could not find node with id: " + id);
    }

    private void copyAssociationInformation(Visualization source, Visualization target) {
        for (Association eachAssociationInSource : source.getAssociations()) {
            Association associationInTarget = getAssociation(eachAssociationInSource.getId(), target.getAssociations());

            if (!eachAssociationInSource.getHandles().isEmpty()) {
                System.out.println("Handles: " + eachAssociationInSource.getHandles().size());
            }

            associationInTarget.setHandles(eachAssociationInSource.getHandles());
        }
    }

    private Association getAssociation(long id, Collection<Association> associations) {
        for (Association a : associations) {
            if (a.getId().equals(id)) {
                return a;
            }
        }

        throw new MalformedJsonException("Could not find association with id: " + id);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
