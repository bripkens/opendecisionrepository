package nl.rug.search.odr.ws.connection;

import java.io.IOException;
import java.util.List;
import nl.rug.search.odr.ws.dto.DecisionStateDTO;
import nl.rug.search.odr.ws.dto.DecisionStateDTOList;
import nl.rug.search.odr.ws.dto.RelationshipTypeDTO;
import nl.rug.search.odr.ws.dto.RelationshipTypeDTOList;

/**
 * Use this class to access functionality of the Open Decision Repository which
 * does not require authentication.
 * 
 * Please note that all methods may throw a {@link WebServiceException} when
 * you provide invalid data, e.g., invalid decision states or references
 * entities which don't exist in the ODR.
 * 
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class UnauthorizedWebServiceFacade extends AbstractWebServiceFacade {

    /*
     * Constructor used by subclasses
     */
    UnauthorizedWebServiceFacade(String baseURL, WebServiceConnection connection) {
        super(baseURL, connection);
    }

    /**
     * Create a new instance. Please note that no connection will be
     * established on object creation. Instead, this will be done on 
     * subsequent method calls.
     * 
     * This class is threadsafe and may be reused.
     * 
     * @param baseUrl URL of the ODR, e.g., http://www.decisionrepository.com
     *   or http://localhost:8080. 
     */
    public UnauthorizedWebServiceFacade(String baseUrl) {
        this(baseUrl, new WebServiceConnection());
    }

    /**
     * Request common decision states from the ODR. Decision states are states
     * a decision can be in, e.g., approved or rejected.
     * 
     * Common decision states are shared among all projects.
     * 
     * @return A list of all common decision states.
     * @throws IOException if an I/O exception occurs
     */
    public List<DecisionStateDTO> getCommonDecisionStates() throws IOException {
        String url = buildUrl(URL.COMMON_DECISION_STATES);
        return getConnection().getAndUnmarshal(url,
                DecisionStateDTOList.class).getList();
    }

    /**
     * Request common relationship types from the ODR. Such types may be
     * depends on, replaces and others.
     * 
     * Common relationship types are shared among all projects.
     * 
     * @return A list of all common relationship types.
     * @throws IOException if an I/O exception occurs
     */
    public List<RelationshipTypeDTO> getCommonRelationshipTypes()
            throws IOException {
        String url = buildUrl(URL.COMMON_RELATIONSHIP_TYPES);
        return getConnection().getAndUnmarshal(url,
                RelationshipTypeDTOList.class).getList();
    }

    private interface URL {

        String COMMON_DECISION_STATES = "decisionstates/";

        String COMMON_RELATIONSHIP_TYPES = "relationshiptypes/";

    }
}
