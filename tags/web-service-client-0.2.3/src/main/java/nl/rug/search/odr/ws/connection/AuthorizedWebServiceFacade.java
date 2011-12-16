package nl.rug.search.odr.ws.connection;

import java.io.IOException;
import java.util.List;
import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.EditDecisionDTO;
import nl.rug.search.odr.ws.dto.ProjectDTO;
import nl.rug.search.odr.ws.dto.ProjectOverviewDTO;
import nl.rug.search.odr.ws.dto.ProjectOverviewDTOList;
import nl.rug.search.odr.ws.dto.RatingDTO;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

/**
 * Use this class to access functionality of the Open Decision Repository which
 * requires authentication.
 * 
 * Please note that all methods may throw a {@link WebServiceException} when
 * you provide invalid data, e.g., invalid decision states or references
 * entities which don't exist in the ODR.
 * 
 * @author Stefan
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class AuthorizedWebServiceFacade extends UnauthorizedWebServiceFacade {

    /**
     * Create a new instance. Please note that no connection will be
     * established on object creation. Instead, this will be done on 
     * subsequent method calls.
     * 
     * This class is threadsafe and may be reused.
     * 
     * The user credentials will be verified once a method is used which
     * requires authentication.
     * 
     * @param baseUrl URL of the ODR, e.g., http://www.decisionrepository.com
     *   or http://localhost:8080.
     * @param email The user's email address which will be used for
     *   authentication purposes.
     * @param password  The user's password which will be used for
     *   authentication purposes.
     */
    public AuthorizedWebServiceFacade(String baseUrl, String email,
            String password) {
        super(baseUrl, new WebServiceConnection(email, password));
    }

    /**
     * Retrieve an overview over all the projects in which the user is
     * involved in.
     * 
     * @return A list of project overviews, i.e., a short summary of each
     *   project.
     * @throws IOException if an I/O exception occurs
     */
    public List<ProjectOverviewDTO> getProjectOverview() throws IOException {
        String url = buildUrl(URL.PROJECT_OVERVIEW);
        return getConnection().getAndUnmarshal(url,
                        ProjectOverviewDTOList.class)
                .getList();
    }

    /**
     * Retrieve details about a specific project. This includes information
     * about project members, concerns, iterations and decisions.
     * 
     * @param projectId The project id identifies the project for which
     *   information should be retrieved. Project IDs may be retrieved
     *   using the {@link #getProjectOverview()} method.
     * @return Details about the project.
     * @throws IOException if an I/O exception occurs
     */
    public ProjectDTO getProject(long projectId) throws IOException {
        String url = buildUrl(URL.PROJECT_DETAILS, projectId);
        return getConnection().getAndUnmarshal(url, ProjectDTO.class);
    }

    /**
     * Retrieve information about a specific decisions including all the
     * decision's histories.
     * 
     * @param projectId The project id identifies the project for which
     *   information should be retrieved. Project IDs may be retrieved
     *   using the {@link #getProjectOverview()} method.
     * @param decisionId A decision's ID. The decision must belong to the
     *   project.
     * @return Details about the decision.
     * @throws IOException if an I/O exception occurs
     */
    public DecisionDTO getDecision(long projectId, long decisionId)
            throws IOException {
        String url = buildUrl(URL.DECISION_DETAILS, projectId, decisionId);
        return getConnection().getAndUnmarshal(url, DecisionDTO.class);
    }

    /**
     * Add a rating to the project. Decisions can be rated regarding certain
     * concerns. For instance, you might defined that Java has a very positive
     * effect on a portability concern.
     * 
     * To update a rating, simply call this method again.
     * 
     * @param projectId The project to which the rating should be added.
     * @param rating The actual rating information which will be send to the
     *   server.
     * @throws IOException if an I/O exception occurs
     */
    public void addRating(long projectId, RatingDTO rating)
            throws IOException {
        String url = buildUrl(URL.ADD_RATING, projectId);
        HttpPost request = new HttpPost(url);
        request.setEntity(getConnection().marshal(rating));

        RatingDTO ratingFromServer = getConnection()
                .executeAndUnmarshal(request, RatingDTO.class);
        
        rating.setId(ratingFromServer.getId());
    }

    /**
     * Add a decision to the project.
     * 
     * @param projectId The project to which the decision should be added.
     * @param decision Information about the decision which should be added to
     *   the project.
     * @return A complete DecisionDTO which can be used for further processing.
     * @throws IOException if an I/O exception occurs
     */
    public DecisionDTO addDecision(long projectId, EditDecisionDTO decision)
            throws IOException {
        String url = buildUrl(URL.ADD_DECISION, projectId);
        HttpPost request = new HttpPost(url);
        request.setEntity(getConnection().marshal(decision));
        return getConnection().executeAndUnmarshal(request, DecisionDTO.class);
    }

    /**
     * Update a decision through the web service. If you change the decision's
     * state, then a new history entry will be created for this decision.
     * All referenced decisions should already be stored in the ODR otherwise
     * an exception will be thrown.
     * 
     * @param projectId The project to which the decision should be added.
     * @param decision Information about the decision which should be
     *   updated.
     * @throws IOException if an I/O exception occurs
     */
    public void updateDecision(long projectId, EditDecisionDTO decision)
            throws IOException {
        String url = buildUrl(URL.UPDATE_DECISION, projectId, decision.getId());
        HttpPut request = new HttpPut(url);
        request.setEntity(getConnection().marshal(decision));
        getConnection().consume(getConnection().execute(request).getEntity());
    }

    private interface URL {

        String COMMON_DECISION_STATES = "decisionstates/";

        String COMMON_RELATIONSHIP_TYPES = "relationshiptypes/";

        String PROJECT_OVERVIEW = "user/projects";

        String PROJECT_DETAILS = "projects/%s/";

        String DECISION_DETAILS = PROJECT_DETAILS + "decisions/%s/";

        String ADD_RATING = PROJECT_DETAILS + "ratings/";

        String UPDATE_RATING = ADD_RATING + "%s/";

        String ADD_DECISION = PROJECT_DETAILS + "decisions/";

        String UPDATE_DECISION = ADD_DECISION + "%s/";

    }
}
