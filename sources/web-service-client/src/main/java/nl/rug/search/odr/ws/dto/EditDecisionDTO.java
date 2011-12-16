/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.ws.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlRootElement;
import nl.rug.search.odr.ws.DTO;

/**
 *
 * @author fragger
 */
@DTO
@XmlRootElement(name = "editdecision")
public class EditDecisionDTO {

    private long id;

    private String name;

    private String state;

    private Date decidedWhen;

    private Date documentedWhen;

    private List<RelationshipDTO> relationshipDTOs;

    public EditDecisionDTO() {
        relationshipDTOs = new ArrayList<RelationshipDTO>();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getDecidedWhen() {
        return decidedWhen;
    }

    public void setDecidedWhen(Date decidedWhen) {
        this.decidedWhen = decidedWhen;
    }

    public Date getDocumentedWhen() {
        return documentedWhen;
    }

    public void setDocumentedWhen(Date documentedWhen) {
        this.documentedWhen = documentedWhen;
    }

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

    public List<RelationshipDTO> getRelationshipDTOs() {
        return relationshipDTOs;
    }

    public void setRelationshipDTOs(List<RelationshipDTO> relationshipDTOs) {
        this.relationshipDTOs = relationshipDTOs;
    }

    @Override
    @Generated("")
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EditDecisionDTO other = (EditDecisionDTO) obj;
        if (this.id != other.id) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) :
                !this.name.equals(other.name)) {
            return false;
        }
        if ((this.state == null) ? (other.state != null) :
                !this.state.equals(other.state)) {
            return false;
        }
        if (this.decidedWhen != other.decidedWhen && (this.decidedWhen == null
                || !this.decidedWhen.equals(other.decidedWhen))) {
            return false;
        }
        if (this.documentedWhen != other.documentedWhen && (this.documentedWhen
                == null || !this.documentedWhen.equals(other.documentedWhen))) {
            return false;
        }
        if (this.relationshipDTOs != other.relationshipDTOs &&
                (this.relationshipDTOs == null || !this.relationshipDTOs.
                equals(other.relationshipDTOs))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 97 * hash + (this.state != null ? this.state.hashCode() : 0);
        hash = 97 * hash + (this.decidedWhen != null ? this.decidedWhen
                .hashCode() : 0);
        hash = 97 * hash + (this.documentedWhen != null ? this.documentedWhen
                .hashCode() : 0);
        hash = 97 * hash + (this.relationshipDTOs != null ?
                this.relationshipDTOs.hashCode() : 0);
        return hash;
    }
}
