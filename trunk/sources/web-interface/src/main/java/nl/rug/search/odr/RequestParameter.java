
package nl.rug.search.odr;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public interface RequestParameter {
    String EQUAL_SIGN = "=";
    String AMPERSAND = "&";
    String QUESTION_MARK = "?";

    String PROJECT_PATH_SHORT = "/p/";
    String PROJECT_PATH_WITH_ENDING_SLASH = "p/";

    String CREATE = "create";
    @Deprecated
    String UPDATE = "update";
    String EDIT = "edit";
    String DELETE = "delete";
    String CONFIRM = "confirm";

    String ID = "id";
    String ITERATION_ID = "iterationId";
    String DECISION_ID = "decisionId";
    String VERSION_ID = "versionId";
    String CONCERN_ID = "concernId";
    String CONCERN_GROUP_ID = "concernGroupId"; 
    String RELATIONSHIPTYPE_ID = "relationshipTypeId";

    String CHRONOLOGICAL_VIEWPOINT = "chronological";
    String RELATIONSHIP_VIEWPOINT = "relationship";
    String STAKEHOLDER_VIEWPOINT = "stakeholder";

    String FACES_REDIRECT = "faces-redirect";

    String FILENAME = "filename";
    String FORMAT = "format";
    String DATA = "data";
    String FORMAT_PNG = "png";
    String FORMAT_JPEG = "jpeg";
    String FORMAT_PDF = "pdf";
    String FORMAT_SVG = "svg";
}
