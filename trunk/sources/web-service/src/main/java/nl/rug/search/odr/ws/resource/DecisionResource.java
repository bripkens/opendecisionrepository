package nl.rug.search.odr.ws.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import nl.rug.search.odr.DecisionTemplateLocal;
import nl.rug.search.odr.decision.DecisionLocal;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Person;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Relationship;
import nl.rug.search.odr.entities.RelationshipType;
import nl.rug.search.odr.entities.State;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.project.RelationshipTypeLocal;
import nl.rug.search.odr.project.StateLocal;
import nl.rug.search.odr.ws.PathParameter;
import nl.rug.search.odr.ws.RequestAttribute;
import nl.rug.search.odr.ws.SessionBeanRepository;
import nl.rug.search.odr.ws.assembler.DecisionAssembler;
import nl.rug.search.odr.ws.dto.DecisionDTO;
import nl.rug.search.odr.ws.dto.EditDecisionDTO;
import nl.rug.search.odr.ws.dto.RelationshipDTO;
import nl.rug.search.odr.ws.exception.BadRequestException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import nl.rug.search.odr.decision.VersionLocal;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Path("/projects/{" + PathParameter.PROJECT_ID + "}/decisions/")
public class DecisionResource {

    private final DecisionLocal decisionLocal;
    private final StateLocal stateLocal;
    private final RelationshipTypeLocal relationshipTypeLocal;
    private final DecisionTemplateLocal templateLocal;
    private final ProjectLocal projectLocal;
    private final VersionLocal versionLocal;

    public DecisionResource() {
        SessionBeanRepository repository = SessionBeanRepository.getInstance();
        decisionLocal = repository.lookup(DecisionLocal.class);
        stateLocal = repository.lookup(StateLocal.class);
        relationshipTypeLocal = repository.lookup(RelationshipTypeLocal.class);
        templateLocal = repository.lookup(DecisionTemplateLocal.class);
        projectLocal = repository.lookup(ProjectLocal.class);
        versionLocal = repository.lookup(VersionLocal.class);
    }

    @GET
    @Path("/{" + PathParameter.DECISION_ID + "}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DecisionDTO getDecisionOverview(@Context HttpServletRequest request,
            @PathParam(PathParameter.DECISION_ID) long decisonId) {
        Project project;
        project = (Project) request.getAttribute(RequestAttribute.PROJECT);
        return DecisionAssembler.assemble(project.getDecision(decisonId));
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DecisionDTO addDecision(@Context HttpServletRequest request,
            EditDecisionDTO dto) {
        Project project;
        project = (Project) request.getAttribute(RequestAttribute.PROJECT);
        Person person;
        person = (Person) request.getAttribute(RequestAttribute.PERSON);

        if (isNameUsed(project, dto.getName())) {
            throw new BadRequestException("Decision name (" + dto.getName()
                    + ") is already used within this project");
        }

        Decision decision = new Decision();
        Version version = new Version();
        deassemble(dto, decision, project, person, version);
        decision.addVersion(version);
        decision.setTemplate(templateLocal.getSmallestTemplate());

        decisionLocal.persist(decision);
        project.addDecision(decision);
        projectLocal.merge(project);

        return DecisionAssembler.assemble(decision);
    }

    @PUT
    @Path("/{" + PathParameter.DECISION_ID + "}")
    @Consumes(MediaType.APPLICATION_XML)
    public void updateDecision(@Context HttpServletRequest request,
            EditDecisionDTO dto,
            @PathParam(PathParameter.DECISION_ID) long decisionId) {

        System.out.println(dto.getId());
        System.out.println("------");
        for (RelationshipDTO rel : dto.getRelationshipDTOs()) {
            System.out.println(rel.getId());
        }

        if (decisionId != dto.getId()) {
            throw new BadRequestException("pathparameter does not equal id"
                    + "of request entity.");
        }

        Project project;
        project = (Project) request.getAttribute(RequestAttribute.PROJECT);
        Person person;
        person = (Person) request.getAttribute(RequestAttribute.PERSON);

        Decision decision = decisionLocal.getById(dto.getId());

        if (!project.getDecisions().contains(decision)) {
            throw new BadRequestException("This decision is not part of the"
                    + "project");
        }

        if (!decision.getName().equalsIgnoreCase(dto.getName())
                && isNameUsed(project, dto.getName())) {

            throw new BadRequestException("Decision name is already used within"
                    + "this project");
        }

        Version currentVersion = decision.getCurrentVersion();

        if (!currentVersion.getState().getStatusName().equals(dto.getState())) {
            currentVersion = new Version();
            currentVersion.setState(getState(dto.getState()));
            decision.addVersion(currentVersion);
        } else {

            for (Relationship r : currentVersion.getOutgoingRelationships()) {
                if (!currentVersion.getOutgoingRelationships().contains(r)) {
                    Version oldTarget = r.getTarget();
                    r.setTarget(null);
                    versionLocal.merge(oldTarget);
                }
            }
        }

        deassemble(dto, decision, project, person, currentVersion);
        decisionLocal.merge(decision);
    }

    private boolean isNameUsed(Project project, String decisionName) {
        for (Decision decision : project.getDecisions()) {
            if (decision.getName().equalsIgnoreCase(decisionName)) {
                return true;
            }
        }

        return false;
    }

    private void deassemble(EditDecisionDTO dto, Decision decision, Project project,
            Person person, Version currentVersion) {
        decision.setName(dto.getName());


        if (!currentVersion.getInitiators().contains(getMember(project, person))) {
            currentVersion.addInitiator(getMember(project, person));
        }

        currentVersion.setState(getState(dto.getState()));
        currentVersion.setDecidedWhen(getCurrentDateIfNull(dto.getDecidedWhen()));
        currentVersion.setDocumentedWhen(getCurrentDateIfNull(dto.getDocumentedWhen()));

        for (RelationshipDTO rs : dto.getRelationshipDTOs()) {
            Relationship relationship = new Relationship();
            relationship.setTarget(getTarget(project, rs.getTargetId()));
            relationship.setSource(currentVersion);
            relationship.setType(getRelationshipType(rs.getRelationshipType()));
        }
    }

    private State getState(String name) {
        State state = stateLocal.getByNameOrNull(name);

        if (state == null) {
            throw new BadRequestException("Decision state name '" + name
                    + "'is invalid.");
        }

        return state;
    }

    private ProjectMember getMember(Project project, Person person) {
        for (ProjectMember member : project.getMembers()) {
            if (member.getPerson().equals(person)) {
                return member;
            }
        }

        throw new IllegalArgumentException("Person isn't a member of the"
                + " project.");
    }

    private Date getCurrentDateIfNull(Date date) {
        return (date != null) ? date : new Date();
    }

    private Version getTarget(Project project, long targetDecisionId) {
        for (Decision decision : project.getDecisions()) {
            if (decision.getId().equals(targetDecisionId)) {
                return decision.getCurrentVersion();
            }
        }

        throw new BadRequestException("Relationship target id '"
                + targetDecisionId + "'is invalid.");
    }

    private RelationshipType getRelationshipType(String name) {
        RelationshipType rt = relationshipTypeLocal.getByNameOrNull(name);

        if (rt == null) {
            throw new BadRequestException("Relationship type '" + name
                    + "' is invalid.");
        }

        return rt;
    }
}
