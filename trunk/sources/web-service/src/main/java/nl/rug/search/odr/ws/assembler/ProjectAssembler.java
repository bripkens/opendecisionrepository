package nl.rug.search.odr.ws.assembler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.ws.dto.IterationDTO;
import nl.rug.search.odr.ws.dto.ProjectDTO;
import nl.rug.search.odr.ws.dto.ProjectMemberDTO;
import nl.rug.search.odr.ws.dto.ProjectOverviewDTO;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class ProjectAssembler {

    public ProjectAssembler() {
        throw new UnsupportedOperationException("Utility Class");
    }
    
    public static List<ProjectOverviewDTO> ASSEMBLE_OVERVIEW(
            Collection<ProjectMember> memberships) {
        List<ProjectOverviewDTO> result =
                new ArrayList<ProjectOverviewDTO>(memberships.size());
        
        for(ProjectMember pm : memberships) {
            result.add(ASSEMBLE_OVERVIEW(pm.getProject()));
        }
        
        return result;
    }
    
    public static ProjectOverviewDTO ASSEMBLE_OVERVIEW(Project p) {
        ProjectOverviewDTO dto = new ProjectOverviewDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        return dto;
    }
    
    public static ProjectDTO ASSEMBLE_DETAIL(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        
        for(Decision d : project.getDecisions()) {
            dto.getDecisions().add(DecisionAssembler.ASSEMBLE(d));
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
