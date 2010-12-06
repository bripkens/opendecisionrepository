package nl.rug.search.odr;

import javax.servlet.annotation.WebServlet;
import nl.rug.search.odr.servlet.IterationCalendarDataProvider;
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

    String DECISION_DETAILS = "decisionDetails.html";

    String DECISION_DETAILS_WITH_LEADING_SLASH = "/".concat(DECISION_DETAILS);

    String PROJECT_DETAILS_WITH_LEADING_SLASH = "/".concat("projectDetails.html");

    String MANAGE_DECISION = "manageDecision.html";

    String MANAGE_DECISION_WITH_LEADING_SLASH = "/".concat(MANAGE_DECISION);

    String CREATE_PROJECT_WITH_LEADING_SLASH ="/createProject.html";
    String UPDATE_PROJECT_WITH_LEADING_SLASH ="/updateProject.html";
    String DELETE_PROJECT_WITH_LEADING_SLASH ="/deleteProject.html";

     String REGISTER_USER_WITH_LEADING_SLASH ="/register.html";


    String DRAWING = "drawing.html";

    String DRAWING_WITH_LEADING_SLASH = "/".concat(DRAWING);

    String VIEWPOINT_DATA_PROVIDER = ((WebServlet) ViewpointDataProvider.class.getAnnotations()[0]).urlPatterns()[0].
            substring(1);

    String VIEWPOINT_DATA_PROVIDER_WITH_LEADING_SLASH = "/".concat(VIEWPOINT_DATA_PROVIDER);

    String ITERATION_DATA_PROVIDER = ((WebServlet) IterationCalendarDataProvider.class.getAnnotations()[0]).urlPatterns()[0].
            substring(1);

    String VIEWPOINT_DATA_RECEIVER = ((WebServlet) ViewpointDataReceiver.class.getAnnotations()[0]).urlPatterns()[0].
            substring(1);

    String VIEWPOINT_DATA_RECEIVER_WITH_LEADING_SLASH = "/".concat(VIEWPOINT_DATA_PROVIDER);
}



