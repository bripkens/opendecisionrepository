
package nl.rug.search.odr;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public interface RequestParameter {
    String PROJECT_PATH_LONG = "/project/";
    String PROJECT_PATH_SHORT = "/p/";

    String CREATE = "create";
    String UPDATE = "update";
    String DELETE = "delete";
    String CONFIRM = "confirm";
    String ID = "id";
    
    String ITERATION_ID = "iterationId";
    String DECISION_ID = "decisionId";
    String VERSION_ID = "versionId";
}
