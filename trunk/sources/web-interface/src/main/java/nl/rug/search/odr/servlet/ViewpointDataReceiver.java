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
import nl.rug.search.odr.project.RelationshipViewVisualizationLocal;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.util.GsonUtil;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewAssociation;
import nl.rug.search.odr.viewpoint.Handle;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewNode;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewVisualization;

/**
 *
 * @author ben
 */
@WebServlet(name="ViewpointDataReceiver", urlPatterns={"/ViewpointDataReceiver"})
public class ViewpointDataReceiver extends HttpServlet {

    @EJB
    private ProjectLocal pl;

    @EJB
    private RelationshipViewVisualizationLocal vl;

    private static final Logger logger = Logger.getLogger(ViewpointDataReceiver.class.getName());

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

        String projectIdParameter = request.getParameter(RequestParameter.ID);
        String data = request.getParameter(RequestParameter.DATA);

        if (data == null || projectIdParameter == null) {
            // TODO: Inform user that request parameter are missing
            // This only happens when the user is abusing the system
            response.sendError(ERROR_CODE);
            return;
        }

        long projectId = -1l;

        try {
            projectId = Long.parseLong(projectIdParameter);
        } catch (NumberFormatException ex) {
            // TODO: Inform user that the project id is invalid
            // This only happens when the user is abusing the system
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



        Gson gson = GsonUtil.getDefaultGson();

        RelationshipViewVisualization v = null;

        try {
            v = gson.fromJson(data, RelationshipViewVisualization.class);
        } catch (JsonParseException ex) {
            // TODO: Inform user that malformed data has been sent to the system.
            // This only happens when the user is abusing the system
            logger.log(Level.WARNING, "Malformed data has been sent to the server which can't be parsed.", ex);
            response.sendError(ERROR_CODE);
            return;
        }

        RelationshipViewVisualization visualizationFromDatabase = null;

        for (RelationshipViewVisualization eachVisualization : p.getRelationshipViews()) {
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
            response.sendError(ERROR_CODE);
            return;
        }

        try {
            copyModifiedData(v, visualizationFromDatabase);
        } catch (MalformedJsonException ex) {
            // TODO: Inform the user that something went really wrong. This error might occur with two users
            // are editing the data of a project at the same time.
            logger.log(Level.WARNING, "Malformed data has been sent to the server which could be parsed but "
                    + "contains content-related errors.", ex);
            response.sendError(ERROR_CODE);
            return;
        }
    } 

    private void copyModifiedData(RelationshipViewVisualization source, RelationshipViewVisualization target) {
        target.setName(source.getName());
        target.setDocumentedWhen(new Date());

        copyNodeInformation(source, target);
        copyAssociationInformation(source, target);

        vl.merge(target);
    }

    private void copyNodeInformation(RelationshipViewVisualization source, RelationshipViewVisualization target) {
        for(RelationshipViewNode eachNodeInSource : source.getNodes()) {
            RelationshipViewNode nodeInTarget = getNode(eachNodeInSource.getId(), target.getNodes());
            
            nodeInTarget.setVisible(eachNodeInSource.isVisible());
            nodeInTarget.setX(eachNodeInSource.getX());
            nodeInTarget.setY(eachNodeInSource.getY());
        }
    }

    private RelationshipViewNode getNode(long id, Collection<RelationshipViewNode> nodes) {
        for(RelationshipViewNode n : nodes) {
            if (n.getId().equals(id)) {
                return n;
            }
        }

        throw new MalformedJsonException("Could not find node with id: " + id);
    }

    private void copyAssociationInformation(RelationshipViewVisualization source, RelationshipViewVisualization target) {
        for (RelationshipViewAssociation eachAssociationInSource : source.getAssociations()) {
            RelationshipViewAssociation associationInTarget = getAssociation(eachAssociationInSource.getId(), target.getAssociations());

            associationInTarget.setLabelX(eachAssociationInSource.getLabelX());
            associationInTarget.setLabelY(eachAssociationInSource.getLabelY());
            associationInTarget.setHandles(eachAssociationInSource.getHandles());
        }
    }

    private RelationshipViewAssociation getAssociation(long id, Collection<RelationshipViewAssociation> associations) {
        for (RelationshipViewAssociation a : associations) {
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
