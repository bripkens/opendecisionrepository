package nl.rug.search.odr.ws.resource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.ws.MimeType;
import nl.rug.search.odr.ws.PathParameter;
import nl.rug.search.odr.ws.RequestAttribute;
import nl.rug.search.odr.ws.SessionBeanRepository;
import nl.rug.search.odr.ws.assembler.DecisionAssembler;
import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.DecisionStateDTO;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */


@Path("/projects/{" + PathParameter.PROJECT_ID + "}/decisions/")
public class DecisionResource {

    private ProjectLocal projectLocal = SessionBeanRepository
            .getInstance()
            .getProjectLocal();
    
    @GET
    @Path("/{" + PathParameter.DECISION_ID + "}")
    @Produces({MimeType.JSON, MimeType.XML})
    public DecisionDTO getDecisionOverview(@Context HttpServletRequest request, @PathParam(PathParameter.DECISION_ID) long decisonId) {
        Project project;
        project = (Project) request.getAttribute(RequestAttribute.PROJECT);
        return DecisionAssembler.ASSEMBLE(project.getDecision(decisonId));
    }
    
    @POST
    @Consumes(MimeType.XML)
    public Response setDecisionDTO(@Context HttpServletRequest request, DecisionDTO decision){
        Project project;
        project = (Project) request.getAttribute((RequestAttribute.PROJECT));
        
        String result = "Product created : " + decision.getName();
        return Response.status(201).entity(result).build();
    }
}