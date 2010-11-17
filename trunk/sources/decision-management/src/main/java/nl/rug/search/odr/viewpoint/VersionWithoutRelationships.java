package nl.rug.search.odr.viewpoint;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Relationship;
import nl.rug.search.odr.entities.Requirement;
import nl.rug.search.odr.entities.State;
import nl.rug.search.odr.entities.Version;

public class VersionWithoutRelationships {

    private static final long serialVersionUID = 1L;

    private final Version v;

    @RequiredFor(value = {Viewpoint.CHRONOLOGICAL, Viewpoint.RELATIONSHIP})
    private Long id;

    private Date documentedWhen;

    @RequiredFor(value = Viewpoint.CHRONOLOGICAL)
    private Date decidedWhen;

    @RequiredFor(value = {Viewpoint.CHRONOLOGICAL, Viewpoint.RELATIONSHIP})
    private boolean removed;

    @RequiredFor(value = {Viewpoint.CHRONOLOGICAL, Viewpoint.RELATIONSHIP})
    private State state;

    private Collection<Requirement> requirements;

    @RequiredFor(value = Viewpoint.RELATIONSHIP)
    private Collection<Relationship> relationships;

    private Collection<ProjectMember> initiators;




    public VersionWithoutRelationships(Version v) {
        this.v = v;
        id = v.getId();
        documentedWhen = v.getDocumentedWhen();
        decidedWhen = v.getDecidedWhen();
        removed = v.isRemoved();
        state = v.getState();
        requirements = v.getRequirements();
        relationships = Collections.emptyList();
        initiators = v.getInitiators();
    }




    
    public void addInitiator(ProjectMember initiator) {
        v.addInitiator(initiator);
    }




    
    public void addRelationship(Relationship relationship) {
        v.addRelationship(relationship);
    }




    
    public void addRequirement(Requirement requirement) {
        v.addRequirement(requirement);
    }




    
    public Date getDecidedWhen() {
        return v.getDecidedWhen();
    }




    
    public Date getDocumentedWhen() {
        return v.getDocumentedWhen();
    }




    
    public Long getId() {
        return v.getId();
    }




    
    public Collection<ProjectMember> getInitiators() {
        return v.getInitiators();
    }




    
    public Collection<Relationship> getRelationships() {
        return v.getRelationships();
    }




    
    public Collection<Requirement> getRequirements() {
        return v.getRequirements();
    }




    
    public State getState() {
        return v.getState();
    }




    
    public boolean isPersistable() {
        return v.isPersistable();
    }




    
    public boolean isRemoved() {
        return v.isRemoved();
    }




    
    public void removeAllInitiators() {
        v.removeAllInitiators();
    }




    
    public void removeAllRelationships() {
        v.removeAllRelationships();
    }




    
    public void removeAllRequirements() {
        v.removeAllRequirements();
    }




    
    public void removeInitiator(ProjectMember initiator) {
        v.removeInitiator(initiator);
    }




    
    public void removeRelationship(Relationship relationship) {
        v.removeRelationship(relationship);
    }




    
    public void removeRequirement(Requirement requirement) {
        v.removeRequirement(requirement);
    }




    
    public void setDecidedWhen(Date decidedWhen) {
        v.setDecidedWhen(decidedWhen);
    }




    
    public void setDocumentedWhen(Date documentedWhen) {
        v.setDocumentedWhen(documentedWhen);
    }




    
    public void setId(Long id) {
        v.setId(id);
    }




    
    public void setInitiators(Collection<ProjectMember> initiators) {
        v.setInitiators(initiators);
    }




    
    public void setRelationships(Collection<Relationship> relationships) {
        v.setRelationships(relationships);
    }




    
    public void setRemoved(boolean removed) {
        v.setRemoved(removed);
    }




    
    public void setRequirements(Collection<Requirement> requirements) {
        v.setRequirements(requirements);
    }




    
    public void setState(State state) {
        v.setState(state);
    }





}
