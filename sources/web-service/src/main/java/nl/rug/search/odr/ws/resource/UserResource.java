package nl.rug.search.odr.ws.resource;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import nl.rug.search.odr.entities.Person;
import nl.rug.search.odr.ws.MimeType;
import nl.rug.search.odr.ws.RequestAttribute;
import nl.rug.search.odr.ws.assembler.ProjectAssembler;
import nl.rug.search.odr.ws.dto.ProjectOverviewDTOList;
import nl.rug.search.odr.ws.security.SkipGroupVerification;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Path("/user/")
public class UserResource {
    @GET
    @Path("projects")
    @Produces({MimeType.JSON, MimeType.XML})
    @SkipGroupVerification
    public ProjectOverviewDTOList getProjectOverview(@Context HttpServletRequest request) {
        Person p = (Person) request.getAttribute(RequestAttribute.PERSON);
        return new ProjectOverviewDTOList(ProjectAssembler.ASSEMBLE_OVERVIEW(p.getMemberships()));
    }
    
    
}
