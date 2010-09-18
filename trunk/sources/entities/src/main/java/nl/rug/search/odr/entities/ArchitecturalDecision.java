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
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Stefan
 */
//@Entity
public class ArchitecturalDecision implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 30, nullable = false)
    private String name;
    @Column(length = 30, nullable = true)
    private String oprId;
    @Column(length = 30, nullable = false)
    private String arguments;
    @Column(length = 30, nullable = false)
    private String decision;
    @Column(length = 30, nullable = false)
    private String problem;
    @OneToMany
    private Version version;
    @OneToMany
    private Collection<Version> versions;

    public ArchitecturalDecision() {
        versions = new ArrayList<Version>();
    }

    public void addVerison(Version version) {
        if (version == null) {
            throw new NullPointerException("version is null");
        }
        versions.add(version);
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        if (arguments == null) {
            throw new NullPointerException("please provide arguments");
        }
        this.arguments = arguments;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        if (decision == null) {
            throw new NullPointerException("please provide a decision");
        }
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
        if (oprId == null) {
            throw new NullPointerException("please provide a oprId");
        }
        this.oprId = oprId;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        if (problem == null) {
            throw new NullPointerException("please provide a problem");
        }
        this.problem = problem;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        if (version == null) {
            throw new NullPointerException("please provide an version");
        }
        this.version = version;
    }

    public Collection<Version> getVersions() {
        return versions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (id == null) {
            throw new NullPointerException("please provide an id");
        }
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ArchitecturalDecision)) {
            return false;
        }
        ArchitecturalDecision other = (ArchitecturalDecision) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ArchitecturalDecision{" + "id=" + id + "name=" + name + "oprId=" + oprId + "arguments=" + arguments + "decision=" + decision + "problem=" + problem + "version=" + version + "versions=" + versions + '}';
    }
}
