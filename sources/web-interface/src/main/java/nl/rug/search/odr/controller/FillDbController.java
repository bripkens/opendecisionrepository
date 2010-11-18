package nl.rug.search.odr.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.DatabaseCleaner;
import nl.rug.search.odr.DecisionTemplateLocal;
import nl.rug.search.odr.util.JsfUtil;
import nl.rug.search.odr.util.SessionUtil;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.DecisionTemplate;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Person;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.RelationshipType;
import nl.rug.search.odr.entities.Concern;
import nl.rug.search.odr.entities.StakeholderRole;
import nl.rug.search.odr.entities.State;
import nl.rug.search.odr.entities.TemplateComponent;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.project.RelationshipTypeLocal;
import nl.rug.search.odr.project.StakeholderRoleLocal;
import nl.rug.search.odr.project.StateLocal;
import nl.rug.search.odr.user.UserLocal;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@ViewScoped
public class FillDbController {

    @EJB
    private StakeholderRoleLocal srl;
    @EJB
    private UserLocal ul;
    @EJB
    private ProjectLocal pl;
    @EJB
    private DecisionTemplateLocal dtl;
    @EJB
    private StateLocal sl;
    @EJB
    private RelationshipTypeLocal rtl;
    private boolean clearDone, rolesDone, statesDone,
            templatesDone, personsDone, projectsDone, iterationsDone,
            decisionsDone, concernsDone, relationshipTypesDone;




    public void addPersons() {
        Person p1 = new Person();
        p1.setName("Ben Ripkens");
        p1.setPlainPassword("12345");
        p1.setEmail("ben@ripkens.de");
        ul.register(p1);


        Person p2 = new Person();
        p2.setName("Stefan Arians");
        p2.setPlainPassword("12345");
        p2.setEmail("s@s.de");
        ul.register(p2);


        Person p3 = new Person();
        p3.setName("Uwe van Heesch");
        p3.setPlainPassword("12345");
        p3.setEmail("uwe@uwe.de");
        ul.register(p3);

        Person p4 = new Person();
        p4.setName("Paris Avgeriou");
        p4.setPlainPassword("12345");
        p4.setEmail("paris@paris.de");
        ul.register(p4);

        Person p5 = new Person();
        p5.setName("Christian Manteuffel");
        p5.setPlainPassword("12345");
        p5.setEmail("christian@christian.de");
        ul.register(p5);

        Person p6 = new Person();
        p6.setName("Martin Verspai");
        p6.setPlainPassword("12345");
        p6.setEmail("martin@martin.de");
        ul.register(p6);

        clearDone = false;
        personsDone = true;
    }




    public void addProject() {
        Project pro = new Project();
        pro.setName("OpenDecisionRepository");
        pro.setDescription("The Open Decision Repository (ODR) project was initiated as part of the doctoral research by Uwe van Heesch, supervised by Paris Avgeriou. It is a joint-venture project of the research group Software Engineering and Architecture (SEARCH), which is part of the Department of Mathematics and Computing Science at the University of Groningen/NL and the Software Engineering study programme at the Fontys University of Applied Sciences in Venlo/NL.");

        ProjectMember member = new ProjectMember();
        member.setPerson(ul.getByName("Ben Ripkens"));
        member.setRole(getStakeholderRole("Architect"));
        pro.addMember(member);

        member = new ProjectMember();
        member.setPerson(ul.getByName("Stefan Arians"));
        member.setRole(getStakeholderRole("Architect"));
        pro.addMember(member);

        member = new ProjectMember();
        member.setPerson(ul.getByName("Uwe van Heesch"));
        member.setRole(getStakeholderRole("Customer"));
        pro.addMember(member);

        member = new ProjectMember();
        member.setPerson(ul.getByName("Paris Avgeriou"));
        member.setRole(getStakeholderRole("Manager"));
        pro.addMember(member);

        pl.persist(pro);

        pro = new Project();
        pro.setName("OpenPatternRepository");
        pro.setDescription("The Open Pattern Repository is a publicly available and freely usable online repository for software engineering patterns and software technologies like frameworks and libraries.");

        member = new ProjectMember();
        member.setPerson(ul.getByName("Christian Manteuffel"));
        member.setRole(getStakeholderRole("Architect"));
        pro.addMember(member);

        member = new ProjectMember();
        member.setPerson(ul.getByName("Martin Verspai"));
        member.setRole(getStakeholderRole("Architect"));
        pro.addMember(member);

        member = new ProjectMember();
        member.setPerson(ul.getByName("Uwe van Heesch"));
        member.setRole(getStakeholderRole("Customer"));
        pro.addMember(member);

        member = new ProjectMember();
        member.setPerson(ul.getByName("Paris Avgeriou"));
        member.setRole(getStakeholderRole("Manager"));
        pro.addMember(member);

        pl.persist(pro);

        clearDone = false;
        projectsDone = true;
    }




    public void login() {
        Person p = null;

        try {
            p = ul.tryLogin("ben@ripkens.de", "12345");
        } catch (BusinessException ex) {
            throw new RuntimeException(ex);
        }


        AuthenticationUtil.authenticate(p);

        JsfUtil.redirect("/projects.html");
    }




    public void clearDatabase() {
        DatabaseCleaner.bruteForceCleanup();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().
                getRequest();

        SessionUtil.resetSession();

        JsfUtil.refreshPage();

        clearDone = true;
        personsDone = projectsDone = decisionsDone =
                iterationsDone = concernsDone = rolesDone =
                statesDone = templatesDone = relationshipTypesDone = false;
    }




    public void addDecisions() {
        Project p = pl.getByName("OpenDecisionRepository");

        Decision d = new Decision();
        d.setName("Java Programming language");
        d.setTemplate(getTemplate("Quick add form"));
        Version v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("Tcl");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("xowiki");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("Java Enterprise Edition");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("Glassfish");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("OPR technology tstack");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("JavaServer Faces");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("Icefaces");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("Java Persistence API");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("MySQL");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("Eclipselink");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("Enterprise Java Beans");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("Icefaces 1.8");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("Icefaces 2.0-beta1");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("JavaServer Faces 1.2");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("JavaServer Faces 2");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("Glassfish 2");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("Glassfish 3");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("Enterprise Java Beans 3");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("Enterprise Java Beans 3.1");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("Java Enterprise Edition 5");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        d = new Decision();
        d.setName("Java Enterprise Edition 6");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date());
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        pl.merge(p);

        clearDone = false;
        decisionsDone = true;
    }




    public void addIterations() {
        Project p = pl.getByName("OpenDecisionRepository");

        Iteration it1 = new Iteration();
        it1.setName("Milestone 1");
        it1.setDescription("The first milestone");
        it1.setProjectMember(p.getMembers().iterator().next());
        it1.setDocumentedWhen(new Date());
        it1.setStartDate(it1.getDocumentedWhen());
        it1.setEndDate(new Date(it1.getStartDate().getTime() + 6000000));

        Iteration it2 = new Iteration();
        it2.setName("Milestone 2");
        it2.setDescription("The second milestone");
        it2.setProjectMember(p.getMembers().iterator().next());
        it2.setDocumentedWhen(new Date(it1.getEndDate().getTime() + 1));
        it2.setStartDate(it2.getDocumentedWhen());
        it2.setEndDate(new Date(it2.getStartDate().getTime() + 6000000));

        p.addIteration(it1);
        p.addIteration(it2);

        pl.merge(p);

        clearDone = false;
        iterationsDone = true;
    }




    public void addStakeholderRoles() {
        StakeholderRole role1 = new StakeholderRole();
        role1.setName("Architect");
        role1.setCommon(true);
        srl.persist(role1);

        StakeholderRole role2 = new StakeholderRole();
        role2.setName("Manager");
        role2.setCommon(true);
        srl.persist(role2);

        StakeholderRole role3 = new StakeholderRole();
        role3.setName("Customer");
        role3.setCommon(true);
        srl.persist(role3);

        clearDone = false;
        rolesDone = true;
    }




    private StakeholderRole getStakeholderRole(String roleName) {
        Collection<StakeholderRole> roles = srl.getAll();

        for (StakeholderRole role : roles) {
            if (role.getName().equalsIgnoreCase(roleName)) {
                return role;
            }
        }

        throw new RuntimeException("Can't find the role");
    }




    public void addStates() {
        State state = new State();
        state.setActionName("formulate");
        state.setStatusName("formulated");
        state.setInitialState(true);
        state.setCommon(true);
        sl.persist(state);

        state = new State();
        state.setActionName("propose");
        state.setStatusName("considered");
        state.setInitialState(false);
        state.setCommon(true);
        sl.persist(state);

        state = new State();
        state.setActionName("validate");
        state.setStatusName("decided");
        state.setInitialState(false);
        state.setCommon(true);
        sl.persist(state);

        state = new State();
        state.setActionName("confirm");
        state.setStatusName("approved");
        state.setInitialState(false);
        state.setCommon(true);
        sl.persist(state);

        state = new State();
        state.setActionName("challenge");
        state.setStatusName("challenged");
        state.setInitialState(false);
        state.setCommon(true);
        sl.persist(state);

        state = new State();
        state.setActionName("validate");
        state.setStatusName("rejected");
        state.setInitialState(false);
        state.setCommon(true);
        sl.persist(state);

        clearDone = false;
        statesDone = true;
    }




    private State getState(String statusName) {
        Collection<State> states = sl.getAll();

        for (State state : states) {
            if (state.getStatusName().equalsIgnoreCase(statusName)) {
                return state;
            }
        }

        throw new RuntimeException("Can't find state");
    }




    public void addDecisionTemplates() {
        DecisionTemplate template = new DecisionTemplate();
        template.setName("Quick add form");
        dtl.persist(template);

        template = new DecisionTemplate();
        template.setName("Demystifying architecture");

        TemplateComponent templateComponent = new TemplateComponent();
        templateComponent.setLabel("Issue");
        templateComponent.setOrder(0);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Decision");
        templateComponent.setOrder(1);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Group");
        templateComponent.setOrder(2);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Assumptions");
        templateComponent.setOrder(3);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Constraints");
        templateComponent.setOrder(4);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Positions");
        templateComponent.setOrder(5);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Argument");
        templateComponent.setOrder(6);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Implications");
        templateComponent.setOrder(7);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Notes");
        templateComponent.setOrder(8);
        template.addComponent(templateComponent);

        dtl.persist(template);

        template = new DecisionTemplate();
        template.setName("Viewpoints for architectural decisions");

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Problem/Issue");
        templateComponent.setOrder(0);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Decision");
        templateComponent.setOrder(1);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Alternatives");
        templateComponent.setOrder(2);
        template.addComponent(templateComponent);

        templateComponent = new TemplateComponent();
        templateComponent.setLabel("Arguments");
        templateComponent.setOrder(3);
        template.addComponent(templateComponent);

        dtl.persist(template);

        clearDone = false;
        templatesDone = true;
    }




    private DecisionTemplate getTemplate(String name) {
        for (DecisionTemplate template : dtl.getAll()) {
            if (template.getName().equalsIgnoreCase(name)) {
                return template;
            }
        }

        throw new RuntimeException("Can't find template.");
    }




    public void addConcerns() {
        Project p = pl.getByName("OpenDecisionRepository");

        Concern r = new Concern();
        r.setName("Web application");
        r.setInitiators(p.getMembers());
        r.setCreatedWhen(new Date());
        p.addConcern(r);

        r = new Concern();
        r.setName("Rich Internet Application");
        r.setInitiators(p.getMembers());
        r.setCreatedWhen(new Date());
        p.addConcern(r);

        r = new Concern();
        r.setName("Well tested (> 70% test coverage)");
        r.setInitiators(p.getMembers());
        r.setCreatedWhen(new Date());
        p.addConcern(r);

        r = new Concern();
        r.setName("Portable to any major OS");
        r.setInitiators(p.getMembers());
        r.setCreatedWhen(new Date());
        p.addConcern(r);

        r = new Concern();
        r.setName("Password encryption");
        r.setInitiators(p.getMembers());
        r.setCreatedWhen(new Date());
        p.addConcern(r);

        pl.merge(p);

        clearDone = false;
        concernsDone = true;
    }




    public void addRelationshipTypes() {
        RelationshipType type = new RelationshipType();
        type.setCommon(true);
        type.setName("depends on");
        rtl.persist(type);

        type = new RelationshipType();
        type.setCommon(true);
        type.setName("replaces");
        rtl.persist(type);

        type = new RelationshipType();
        type.setCommon(true);
        type.setName("is alternative for");
        rtl.persist(type);

        type = new RelationshipType();
        type.setCommon(true);
        type.setName("caused by");
        rtl.persist(type);

        clearDone = false;
        relationshipTypesDone = true;
    }




    public boolean isClearDone() {
        return clearDone;
    }




    public void setClearDone(boolean clearDone) {
        this.clearDone = clearDone;
    }




    public boolean isDecisionsDone() {
        return decisionsDone;
    }




    public void setDecisionsDone(boolean decisionsDone) {
        this.decisionsDone = decisionsDone;
    }




    public boolean isIterationsDone() {
        return iterationsDone;
    }




    public void setIterationsDone(boolean iterationsDone) {
        this.iterationsDone = iterationsDone;
    }




    public boolean isPersonsDone() {
        return personsDone;
    }




    public void setPersonsDone(boolean personsDone) {
        this.personsDone = personsDone;
    }




    public boolean isProjectsDone() {
        return projectsDone;
    }




    public void setProjectsDone(boolean projectsDone) {
        this.projectsDone = projectsDone;
    }




    public boolean isConcernsDone() {
        return concernsDone;
    }




    public void setConcernsDone(boolean concernsDone) {
        this.concernsDone = concernsDone;
    }




    public boolean isRolesDone() {
        return rolesDone;
    }




    public void setRolesDone(boolean rolesDone) {
        this.rolesDone = rolesDone;
    }




    public boolean isStatesDone() {
        return statesDone;
    }




    public void setStatesDone(boolean statesDone) {
        this.statesDone = statesDone;
    }




    public boolean isTemplatesDone() {
        return templatesDone;
    }




    public void setTemplatesDone(boolean templatesDone) {
        this.templatesDone = templatesDone;
    }




    public boolean isRelationshipTypesDone() {
        return relationshipTypesDone;
    }




    public void setRelationshipTypesDone(boolean relationshipTypesDone) {
        this.relationshipTypesDone = relationshipTypesDone;
    }
}
