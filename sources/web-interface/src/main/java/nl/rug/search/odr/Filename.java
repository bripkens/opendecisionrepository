
package nl.rug.search.odr;

/**
 *
 * @author Stefan
 */
public interface Filename {

    String ERROR = "error.html";
    String ERROR_WITH_LEADING_SLASH = "/".concat(ERROR);



    String PROJECT_OVERVIEW = "projects.html";
    String PROJECT_OVERVIEW_LEADING_SLASH = "/".concat(PROJECT_OVERVIEW);




    String ITERATION_DETAILS = "iterationDetails.html";
    String ITEARTION_DETAILS_WITH_LEADING_SLASH = "/".concat(ITERATION_DETAILS);

    String MANAGE_ITERATION = "manageIteration.html";
    String MANAGE_ITERATION_WITH_LEADING_SLASH = "/".concat(MANAGE_ITERATION);



    String DECISION_DETAILS = "decisionDetails.html";
    String DECISION_DETAILS_WITH_LEADING_SLASH = "/".concat(DECISION_DETAILS);

    String MANAGE_DECISION = "manageDecision.html";
    String MANAGE_DECISION_WITH_LEADING_SLASH = "/".concat(MANAGE_DECISION);
}
