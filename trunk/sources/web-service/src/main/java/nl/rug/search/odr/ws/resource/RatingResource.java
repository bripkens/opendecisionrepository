package nl.rug.search.odr.ws.resource;

import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.Rating;
import nl.rug.search.odr.ws.PathParameter;
import nl.rug.search.odr.ws.RequestAttribute;
import nl.rug.search.odr.ws.assembler.RatingAssembler;
import nl.rug.search.odr.ws.dto.RatingDTO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import nl.rug.search.odr.entities.Concern;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.ws.SessionBeanRepository;
import nl.rug.search.odr.ws.exception.BadRequestException;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Path("/projects/{" + PathParameter.PROJECT_ID + "}/ratings/")
public class RatingResource {
    
    private ProjectLocal projectLocal = SessionBeanRepository
            .getInstance()
            .lookup(ProjectLocal.class);
    
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes(MediaType.APPLICATION_XML)
    public RatingDTO addRating(@Context HttpServletRequest request,
            RatingDTO dto) {
        final Project project;
        project = (Project) request.getAttribute(RequestAttribute.PROJECT);
        
        Rating rating = RatingAssembler.disassemble(dto);
        entityDisassemble(project, rating, dto);
        projectLocal.addRating(project.getId(), rating);
        
        dto.setId(rating.getId());
        return dto;
    }
    
    private void entityDisassemble(Project project, Rating entity,
            RatingDTO dto) {
        Concern concern = project.getConcern(dto.getConcernId());
        if (concern == null) {
            throw new BadRequestException("Illegal concern id.");
        }
        entity.setConcern(concern);
        
        Decision decision = project.getDecision(dto.getDecisionId());
        if (decision == null) {
            throw new BadRequestException("Illegal decision id.");
        }
        entity.setDecision(decision);
    }
}
