/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Stefan
 */
//@Entity
public class ArchitecturalDecision extends BaseEntity<ArchitecturalDecision> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
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

    @OneToMany(mappedBy="decision", cascade=CascadeType.ALL, orphanRemoval=true)
    private Collection<Version> versions;


    public ArchitecturalDecision() {
        versions = new ArrayList<Version>();
    }



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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (id == null) {
            throw new BusinessException("Id is null.");
        }
        this.id = id;
    }

    @Override
    public String toString() {
        return "ArchitecturalDecision{" + "id=" + id + "type=" + name + '}';
    }



   public Collection<Version> getVersions() {
        return Collections.unmodifiableCollection(versions);
    }

    public void setVersions(Collection<Version> versions) {
        if (versions == null) {
            throw new BusinessException("Collection of members is null");
        }

        this.versions = versions;
    }

    public void addVersion(Version version){
        if(version == null){
            throw new BusinessException("Version is null.");
        }
        version.setArchitecturalDecision(this);
    }

    public void removeAllVersions() {
        versions.clear();
    }

    public void removeVersion(Version version) {
        if(version == null){
            throw new BusinessException("Version is null.");
        }

        version.setArchitecturalDecision(this);
    }

    void internalAddVersion(Version version) {
        versions.add(version);
    }

    void internalRemoveVersion(Version version) {
        versions.remove(version);
    }

    @Override
    protected Object[] getCompareData() {
        return new Object[] {name, oprId, arguments, decision, problem};
    }

    @Override
    public boolean isPersistable() {
        if(name == null){
            return false;
        }
        return true;
    }

}
