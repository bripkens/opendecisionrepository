package nl.rug.search.odr.ws.resource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.ws.MimeType;
import nl.rug.search.odr.ws.PathParameter;
import nl.rug.search.odr.ws.RequestAttribute;
import nl.rug.search.odr.ws.assembler.DecisionAssembler;
import nl.rug.search.odr.ws.dto.DecisionDTO;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Path("/projects/{" + PathParameter.PROJECT_ID + "}/decisions/{" + PathParameter.DECISION_ID + "}")
public class DecisionResource {

    @GET
    @Produces({MimeType.JSON, MimeType.XML})
    public DecisionDTO getDecisionOverview(@Context HttpServletRequest request, @PathParam(PathParameter.DECISION_ID) long decisonId) {
        Project project;
        project = (Project) request.getAttribute(RequestAttribute.PROJECT);
        return DecisionAssembler.ASSEMBLE(project.getDecision(decisonId));
    }
}