package nl.rug.search.odr.ws.connection;

import java.io.IOException;
import java.util.List;
import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.DecisionStateDTO;
import nl.rug.search.odr.ws.dto.DecisionStateDTOList;
import nl.rug.search.odr.ws.dto.ProjectDTO;
import nl.rug.search.odr.ws.dto.ProjectOverviewDTO;
import nl.rug.search.odr.ws.dto.ProjectOverviewDTOList;
import nl.rug.search.odr.ws.dto.RelationshipTypeDTO;
import nl.rug.search.odr.ws.dto.RelationshipTypeDTOList;

/**
 *
 * @author Stefan
 */
public class WebServiceFacade {

    private static final String BASE_PATH = "/web-service/rest/";
    private static final String COMMON_DECISION_STATES = BASE_PATH + "decisionstates/";
    private static final String COMMON_RELATIONSHIP_TYPES = BASE_PATH + "relationshiptypes/";
    private static final String PROJECT_OVERVIEW = BASE_PATH + "user/projects";
    private static final String PROJECT_DETAILS = BASE_PATH + "projects/%s/";
    private static final String DECISION_DETAILS = PROJECT_DETAILS + "decisions/%s/";
    private final WebServiceConnection connection;
    private final String baseUrl;

    public WebServiceFacade(String baseUrl, String email,
            String password) {
        this.baseUrl = baseUrl;
        connection = new WebServiceConnection(email, password);
    }

    public List<ProjectOverviewDTO> getProjectOverview() throws IOException {
        return connection.retrieveAndUnmarshal(baseUrl + PROJECT_OVERVIEW,
                ProjectOverviewDTOList.class).getList();
    }

    public ProjectDTO getProject(long projectId) throws IOException {
        return connection.retrieveAndUnmarshal(baseUrl + 
                String.format(PROJECT_DETAILS, projectId), ProjectDTO.class);
    }
    
    public List<DecisionStateDTO> getCommonDecisionStates() throws IOException {
        return connection.retrieveAndUnmarshal(baseUrl + COMMON_DECISION_STATES,
                DecisionStateDTOList.class).getList();
    }
    
    public List<RelationshipTypeDTO> getCommonRelationshipTypes() throws IOException {
        return connection.retrieveAndUnmarshal(baseUrl + COMMON_RELATIONSHIP_TYPES,
                RelationshipTypeDTOList.class).getList();
    }
    
    public DecisionDTO getDecision(long projectId, long decisionId) throws IOException {
        return connection.retrieveAndUnmarshal(baseUrl + 
                String.format(DECISION_DETAILS, projectId, decisionId),
                DecisionDTO.class);
    }
}
