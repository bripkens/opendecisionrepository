package nl.rug.search.odr.ws.resource;

import java.io.IOException;
import java.io.OutputStream;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.export.TeXExport;
import nl.rug.search.odr.ws.PathParameter;
import nl.rug.search.odr.ws.RequestAttribute;
import nl.rug.search.odr.ws.assembler.ProjectAssembler;
import nl.rug.search.odr.ws.dto.ProjectDTO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Path("/projects/{" + PathParameter.PROJECT_ID + "}")
public class ProjectResource {

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ProjectDTO getProjectDetails(@Context HttpServletRequest request) {
        Project project;
        project = (Project) request.getAttribute(RequestAttribute.PROJECT);
        return ProjectAssembler.assembleDetail(project);
    }

    @GET
    @Path("/export/tex")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response exportProject(@Context HttpServletRequest request) {
        final Project project;
        project = (Project) request.getAttribute(RequestAttribute.PROJECT);

        return Response.ok(new StreamingOutput() {

            @Override
            public void write(OutputStream output) throws IOException {
                new TeXExport(output, project).export();
            }
        }).header("Content-disposition", "attachment; filename=texExport.zip")
                .build();
    }
}
