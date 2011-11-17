package nl.rug.search.odr.ws.resource;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.export.TeXExport;
import nl.rug.search.odr.ws.MimeType;
import nl.rug.search.odr.ws.PathParameter;
import nl.rug.search.odr.ws.RequestAttribute;
import nl.rug.search.odr.ws.assembler.ProjectAssembler;
import nl.rug.search.odr.ws.dto.ProjectDTO;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Path("/projects/{" + PathParameter.PROJECT_ID + "}")
public class ProjectResource {

    @GET
    @Produces({MimeType.JSON, MimeType.XML})
    public ProjectDTO getProjectOverview(@Context HttpServletRequest request) {
        Project project;
        project = (Project) request.getAttribute(RequestAttribute.PROJECT);
        return ProjectAssembler.ASSEMBLE_DETAIL(project);
    }

    @GET
    @Path("/export/tex")
    @Produces("application/octet-stream")
    public Response exportProject(@Context HttpServletRequest request) {
        final Project project;
        project = (Project) request.getAttribute(RequestAttribute.PROJECT);

        return Response.ok(new StreamingOutput() {

            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                new TeXExport(output, project).export();
            }
        }).header("Content-disposition", "attachment; filename=texExport.zip")
                .build();
    }
}
