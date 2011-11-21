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
@XmlRootElement(name="decision")
@DTO
public class DecisionDTO {
    private long id;
    private String name;
    private List<RelationshipDTO> relationships;
    private List<HistoryDTO> history;
    private List<TextualDescriptionDTO> description;

    public DecisionDTO() {
        relationships = new LinkedList<RelationshipDTO>();
        history = new LinkedList<HistoryDTO>();
        description = new LinkedList<TextualDescriptionDTO>();
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

    public List<RelationshipDTO> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<RelationshipDTO> relationships) {
        this.relationships = relationships;
    }

    public List<HistoryDTO> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryDTO> history) {
        this.history = history;
    }

    public List<TextualDescriptionDTO> getDescription() {
        return description;
    }

    public void setDescription(List<TextualDescriptionDTO> description) {
        this.description = description;
    }
}
