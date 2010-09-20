/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author Stefan
 */
@Entity
public class ArchitecturalDecision implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ArchitecturalDecisionId;
    @Column(length = 30, nullable = false)
    private String name;
    @Column(length = 30)
    private String oprId;
    @Column(length = 30)
    private String arguments;
    @Column(length = 30)
    private String decision;
    @Column(length = 30)
    private String problem;

//    @OneToMany
//    private Collection<Version> versions;

//    public ArchitecturalDecision() {
//        versions = new ArrayList<Version>();
//    }

//    public void addVerison(Version version) {
//        if (version == null) {
//            throw new BusinessException("version is null.");
//        }
//        versions.add(version);
//    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        StringValidator.isValid(name);
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

    
//    public Collection<Version> getVersions() {
//        return versions;
//    }

    public Long getId() {
        return ArchitecturalDecisionId;
    }

    public void setId(Long id) {
        if (id == null) {
            throw new BusinessException("Id is null.");
        }
        this.ArchitecturalDecisionId = id;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(ArchitecturalDecisionId).append(name).append(problem).append(arguments).
                append(decision).append(problem).append(oprId).toHashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (object.getClass() != getClass()) {
            return false;
        }

        ArchitecturalDecision ac = (ArchitecturalDecision) object;
        return new EqualsBuilder().
                append(ArchitecturalDecisionId, ac.ArchitecturalDecisionId).
                append(name, ac.name).
                append(arguments, ac.arguments).
                append(decision, ac.decision).
                append(problem, ac.problem).
                append(oprId, ac.oprId).
                isEquals();
    }

    @Override
    public String toString() {
        return "ArchitecturalDecision{" + "id=" + ArchitecturalDecisionId + "type=" + name + '}';
    }
}
