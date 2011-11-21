package nl.rug.search.odr.ws.security;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nl.rug.search.odr.entities.Person;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.user.UserLocal;
import nl.rug.search.odr.ws.PathParameter;
import nl.rug.search.odr.ws.RequestAttribute;
import nl.rug.search.odr.ws.SessionBeanRepository;

import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.interception.SecurityPrecedence;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.AcceptedByMethod;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
import org.jboss.resteasy.util.Base64;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Provider
@ServerInterceptor
@SecurityPrecedence
public class AuthenticationInterceptor implements PreProcessInterceptor,
        AcceptedByMethod {

    private static final Logger LOGGER =
            Logger.getLogger(AuthenticationInterceptor.class.getName());
    private static final Pattern AUTH_PATTERN = Pattern.compile("^Basic (.*)$",
            Pattern.CASE_INSENSITIVE);
    private final UserLocal userLocal;
    private final ProjectLocal projectLocal;

    public AuthenticationInterceptor() throws NamingException {
        SessionBeanRepository beanRepository;
        beanRepository = SessionBeanRepository.getInstance();
        
        userLocal = beanRepository.getUserLocal();
        projectLocal = beanRepository.getProjectLocal();
    }

    @Override
    public ServerResponse preProcess(HttpRequest hr, ResourceMethod rm)
            throws Failure, WebApplicationException {
        HttpHeaders httpHeaders = hr.getHttpHeaders();

        List<String> authHeaders =
                httpHeaders.getRequestHeader("Authorization");

        if (authHeaders == null || authHeaders.size() != 1) {
            return generateUnauthorizedError();
        }

        String authHeader = authHeaders.get(0);
        Matcher authHeaderMatcher = AUTH_PATTERN.matcher(authHeader);

        if (!authHeaderMatcher.matches()) {
            return generateBadRequestError("Invalid auth header.");
        }

        String authPart;
        try {
            authPart = new String(Base64.decode(authHeaderMatcher.group(1)));
        } catch (IOException ex) {
            return generateInternalServerError();
        }

        String[] authCredentials = authPart.split(":", 2);

        if (authCredentials.length != 2) {
            return generateBadRequestError("Invalid auth header.");
        }

        Person person = null;
        try {
            person = userLocal.getByEmailOrNull(authCredentials[0]);
        } catch (EJBException ejbe) {
            return generateInternalServerError();
        }

        if (person == null || !person.validatePassword(authCredentials[1])) {
            return generateUnauthorizedError();
        }

        hr.setAttribute(RequestAttribute.PERSON, person);
        
        if (rm.getMethod().isAnnotationPresent(SkipGroupVerification.class)) {
            return null;
        } else {
            return checkGroupMembership(hr, rm, person);
        }
    }
    
    private ServerResponse checkGroupMembership(HttpRequest hr,
            ResourceMethod rm, Person person) {
        MultivaluedMap<String, String> pathParameters = hr.getUri()
                .getPathParameters();
        if (pathParameters == null) {
            logMissingPathParam(rm.getMethod(), PathParameter.PROJECT_ID);
            return generateNotFoundError();
        }
        
        List<String> projectIdParameters =
                pathParameters.get(PathParameter.PROJECT_ID);

        if (projectIdParameters == null || projectIdParameters.size() != 1) {
            logMissingPathParam(rm.getMethod(), PathParameter.PROJECT_ID);
            return generateBadRequestError("No project id parameter available"
                    + " (see server log).");
        }

        long projectId;
        try {
            projectId = Long.parseLong(projectIdParameters.get(0));
        } catch (NumberFormatException ex) {
            return generateBadRequestError("Project id is not a number.");
        }

        Project project = projectLocal.getById(projectId);

        if (project == null) {
            return generateNotFoundError();
        }

        if (!projectLocal.isMember(person.getId(), projectId)) {
            return generateForbiddenError();
        }

        hr.setAttribute(RequestAttribute.PROJECT, project);

        return null;
    }

    private ServerResponse generateBadRequestError(Object entity) {
        return generateResponse(400, true, entity);
    }
    
    private ServerResponse generateUnauthorizedError() {
        return generateResponse(401, true, null);
    }
    
    private ServerResponse generateForbiddenError() {
        return generateResponse(403, false, null);
    }

    private ServerResponse generateNotFoundError() {
        return generateResponse(404, false, null);
    }
    
    private ServerResponse generateInternalServerError() {
        return generateResponse(500, true, null);
    }
    
    private ServerResponse generateResponse(int statusCode, boolean authHeader,
            Object entity) {
        ServerResponse result = new ServerResponse();
        result.setStatus(statusCode);
        
        if (authHeader) {
            result.getMetadata().putSingle("WWW-Authenticate",
                "Basic realm=\"ODR web service (use email for authentication)\"");
        }
        
        if (entity != null) {
            result.setEntity(entity);
        }
        
        return result;
    }
    
    private void logMissingPathParam(Method method, String pathParam) {
        LOGGER.log(Level.WARNING, "Security constraint (Group member "
                + "verification) was added to method '{0}' but no path "
                + "parameter '{1}' was found.",
                new Object[]{method, pathParam});
    }

    @Override
    public boolean accept(Class type, Method method) {
        return !method.isAnnotationPresent(SkipAuthentication.class);
    }
}
