package nl.rug.search.odr;

import javax.servlet.annotation.WebServlet;
import nl.rug.search.odr.servlet.IterationCalendarDataProvider;
import nl.rug.search.odr.servlet.TeXExportServlet;
import nl.rug.search.odr.servlet.ViewpointDataProvider;
import nl.rug.search.odr.servlet.ViewpointDataReceiver;

/**
 *
 * @author Stefan
 */
public interface Filename {

    String ERROR = "error.html";
    String INDEX = "index.html";
    String ERROR_WITH_LEADING_SLASH = "/".concat(ERROR);
    String PROJECT_OVERVIEW = "projects.html";
    String PROJECT_OVERVIEW_LEADING_SLASH = "/".concat(PROJECT_OVERVIEW);
    String CONCERN_DETAILS = "concernDetails.html";
    String CONCERN_DETAILS_WITH_LEADING_SLASH = "/".concat(CONCERN_DETAILS);
    String ITERATION_DETAILS = "iterationDetails.html";
    String ITEARTION_DETAILS_WITH_LEADING_SLASH = "/".concat(ITERATION_DETAILS);
    String MANAGE_CONCERNS = "manageConcern.html";
    String MANAGE_CONCERNS_WITH_LEADING_SLASH = "/".concat(MANAGE_CONCERNS);
    String MANAGE_ITERATION = "manageIteration.html";
    String MANAGE_ITERATION_WITH_LEADING_SLASH = "/".concat(MANAGE_ITERATION);
    String MANAGE_RELATIONSHIPTYPE = "manageRelationshipType.html";
    String MANAGE_RELATIONSHIPTYPE_WITH_LEADING_SLASH = "/".concat(MANAGE_RELATIONSHIPTYPE);
    String DECISION_DETAILS = "decisionDetails.html";
    String DECISION_DETAILS_WITH_LEADING_SLASH = "/".concat(DECISION_DETAILS);
    String PROJECT_DETAILS_WITH_LEADING_SLASH = "/".concat("projectDetails.html");
    String MANAGE_DECISION = "manageDecision.html";
    String MANAGE_DECISION_WITH_LEADING_SLASH = "/".concat(MANAGE_DECISION);
    String CREATE_PROJECT_WITH_LEADING_SLASH = "/createProject.html";
    String UPDATE_PROJECT_WITH_LEADING_SLASH = "/updateProject.html";
    String DELETE_PROJECT_WITH_LEADING_SLASH = "/deleteProject.html";
    String CONCERNS_TABLE_WITH_LEADING_SLASH = "/concernsTable.html";
    String CONCERNS_TABLE = "concernsTable.html";
    String MEMBERS_TABLE_WITH_LEADING_SLASH = "/membersTable.html";
    String MEMBERS_TABLE = "membersTable.html";
    String ITERATIONS_TABLE_WITH_LEADING_SLASH = "/iterationsTable.html";
    String ITERATION_TABLE = "iterationsTable.html";
    String DECISIONS_TABLE_WITH_LEADING_SLASH = "/decisionsTable.html";
    String DECISIONS_TABLE = "decisionsTable.html";
    String REGISTER_USER_WITH_LEADING_SLASH = "/register.html";
    String DIAGRAM = "diagram.htm";
    String DIAGRAM_WITH_LEADING_SLASH = "/".concat(DIAGRAM);
    String VIEWPOINT_DATA_PROVIDER = ((WebServlet) ViewpointDataProvider.class.getAnnotations()[0]).urlPatterns()[0].substring(1);
    String VIEWPOINT_DATA_PROVIDER_WITH_LEADING_SLASH = "/".concat(VIEWPOINT_DATA_PROVIDER);
    String ITERATION_DATA_PROVIDER = ((WebServlet) IterationCalendarDataProvider.class.getAnnotations()[0]).urlPatterns()[0].substring(1);
    String VIEWPOINT_DATA_RECEIVER = ((WebServlet) ViewpointDataReceiver.class.getAnnotations()[0]).urlPatterns()[0].substring(1);
    String VIEWPOINT_DATA_RECEIVER_WITH_LEADING_SLASH = "/".concat(VIEWPOINT_DATA_PROVIDER);
    String TEX_EXPORT = ((WebServlet) TeXExportServlet.class.getAnnotations()[0]).urlPatterns()[0].substring(1);
    String TEX_EXPORT_WITH_LEADING_SLASH = "/".concat(TEX_EXPORT);
}
