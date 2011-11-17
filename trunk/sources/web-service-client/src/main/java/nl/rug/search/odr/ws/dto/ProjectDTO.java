package nl.rug.search.odr.ws.dto;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import nl.rug.search.odr.ws.DTO;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@XmlRootElement(name="project")
@DTO
public class ProjectDTO {
    private long id;
    private String name;
    private String description;
    private List<DecisionDTO> decisions;
    private List<IterationDTO> iterations;
    private List<ProjectMemberDTO> members;

    public ProjectDTO() {
        decisions = new LinkedList<DecisionDTO>();
        iterations = new LinkedList<IterationDTO>();
        members = new LinkedList<ProjectMemberDTO>();
    }

    public List<IterationDTO> getIterations() {
        return iterations;
    }

    public void setIterations(List<IterationDTO> iterations) {
        this.iterations = iterations;
    }
    
    public List<DecisionDTO> getDecisions() {
        return decisions;
    }

    public void setDecisions(List<DecisionDTO> decisions) {
        this.decisions = decisions;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProjectMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<ProjectMemberDTO> members) {
        this.members = members;
    }
    
    @XmlAttribute
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
