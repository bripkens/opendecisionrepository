package nl.rug.search.odr.ws.assembler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import nl.rug.search.odr.entities.Concern;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Rating;
import nl.rug.search.odr.ws.dto.ConcernDTO;
import nl.rug.search.odr.ws.dto.IterationDTO;
import nl.rug.search.odr.ws.dto.ProjectDTO;
import nl.rug.search.odr.ws.dto.ProjectMemberDTO;
import nl.rug.search.odr.ws.dto.ProjectOverviewDTO;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public final class ProjectAssembler {

    private ProjectAssembler() {
        throw new UnsupportedOperationException("Utility Class");
    }
    
    public static List<ProjectOverviewDTO> assembleOverview(
            Collection<ProjectMember> memberships) {
        List<ProjectOverviewDTO> result =
                new ArrayList<ProjectOverviewDTO>(memberships.size());
        
        for(ProjectMember pm : memberships) {
            result.add(assembleOverview(pm.getProject()));
        }
        
        return result;
    }
    
    public static ProjectOverviewDTO assembleOverview(Project p) {
        ProjectOverviewDTO dto = new ProjectOverviewDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        return dto;
    }
    
    public static ProjectDTO assembleDetail(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        
        for(Rating r : project.getRatings()) {
            dto.getRatings().add(RatingAssembler.assemble(r));
        }
        
        for(Concern c : project.getDestinctConcerns()) {
            ConcernDTO target = new ConcernDTO();
            target.setId(c.getId());
            target.setCreatedWhen(c.getCreatedWhen());
            target.setDescription(c.getDescription());
            target.setExternalId(c.getExternalId());
            target.setInitiatorId(c.getInitiators().getPerson().getId());
            target.setName(c.getName());
            target.setTags(c.getTags());
            dto.getConcerns().add(target);
        }
        
        for(Decision d : project.getDecisions()) {
            dto.getDecisions().add(DecisionAssembler.assemble(d));
        }
        
        for(Iteration i : project.getIterations()) {
            IterationDTO iDTO = new IterationDTO();
            iDTO.setId(i.getId());
            iDTO.setName(i.getName());
            iDTO.setDescription(i.getDescription());
            iDTO.setDocumentedWhen(i.getDocumentedWhen());
            iDTO.setStartDate(i.getStartDate());
            iDTO.setEndDate(i.getEndDate());
            dto.getIterations().add(iDTO);
        }
        
        for(ProjectMember pm : project.getMembers()) {
            ProjectMemberDTO mDTO = new ProjectMemberDTO();
            
            mDTO.setName(pm.getPerson().getName());
            mDTO.setEmail(pm.getPerson().getEmail());
            mDTO.setRole(pm.getRole().getName());
            
            dto.getMembers().add(mDTO);
        }
        
        return dto;
    }
}
