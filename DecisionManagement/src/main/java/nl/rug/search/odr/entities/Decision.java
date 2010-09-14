/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.odr.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Stefan
 */
@Entity
public class Decision implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long decisionId;

    private String name;
    private String problem;
    private String decision;
    private String arguments;
    private String oprId;

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public Long getDecisionId() {
        return decisionId;
    }

    public void setDecisionId(Long decisionId) {
        this.decisionId = decisionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOprId() {
        return oprId;
    }

    public void setOprId(String oprId) {
        this.oprId = oprId;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (decisionId != null ? decisionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Decision)) {
            return false;
        }
        Decision other = (Decision) object;
        if ((this.decisionId == null && other.decisionId != null) ||
                (this.decisionId != null && !this.decisionId.equals(other.decisionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Decision{" + "decisionId=" + decisionId + "; name=" + name + "; problem=" + problem + "; "
                + "decision=" + decision + '}';
    }

}
