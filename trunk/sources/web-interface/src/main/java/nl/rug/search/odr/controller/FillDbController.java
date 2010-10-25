package nl.rug.search.odr.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.AuthenticationUtil;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.DatabaseCleaner;
import nl.rug.search.odr.DecisionTemplateLocal;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.SessionUtil;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.DecisionTemplate;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Person;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Requirement;
import nl.rug.search.odr.entities.StakeholderRole;
import nl.rug.search.odr.entities.State;
import nl.rug.search.odr.entities.TemplateComponent;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.project.StakeholderRoleLocal;
import nl.rug.search.odr.project.StateLocal;
import nl.rug.search.odr.user.UserLocal;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@RequestScoped
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
        p5.setName("No project member");
        p5.setPlainPassword("12345");
        p5.setEmail("not@not.de");
        ul.register(p5);
    }

    public void addProject() {
        Project pro = new Project();
        pro.setName("OpenDecisionRepository");
        pro.setDescription("...part of search...");

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

        pl.createProject(pro);

    }

    public void login() {
        Person p = null;

        try {
            p = ul.tryLogin("ben@ripkens.de", "12345");
        } catch (BusinessException ex) {
            throw new RuntimeException(ex);
        }


        AuthenticationUtil.authenticate(p);

        try {
            JsfUtil.redirect("/projects.html");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void clearDatabase() {
        DatabaseCleaner.bruteForceCleanup();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        SessionUtil.resetSession();

        JsfUtil.refreshPage();
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
    }

    public void addStakeholderRoles() {
        StakeholderRole role1 = new StakeholderRole();
        role1.setName("Architect");
        role1.setCommon(true);
        srl.persistRole(role1);

        StakeholderRole role2 = new StakeholderRole();
        role2.setName("Manager");
        role2.setCommon(true);
        srl.persistRole(role2);

        StakeholderRole role3 = new StakeholderRole();
        role3.setName("Customer");
        role3.setCommon(true);
        srl.persistRole(role3);
    }

    private StakeholderRole getStakeholderRole(String roleName) {
        Collection<StakeholderRole> roles = srl.getAll();

        for(StakeholderRole role : roles) {
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
        state.setInitialState(false);
        state.setCommon(true);
        sl.persist(state);

        state = new State();
        state.setActionName("propose");
        state.setStatusName("considered");
        state.setInitialState(true);
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
    }

    private State getState(String statusName) {
        Collection<State> states = sl.getAll();

        for(State state : states) {
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
    }

    private DecisionTemplate getTemplate(String name) {
        for(DecisionTemplate template : dtl.getAll()) {
            if (template.getName().equalsIgnoreCase(name)) {
                return template;
            }
        }

        throw new RuntimeException("Can't find template.");
    }

    public void addRequirements() {
        Project p = pl.getByName("OpenDecisionRepository");

        Requirement r = new Requirement();
        r.setName("First requirement");
        r.setInitiators(p.getMembers());
        p.addRequirement(r);

        r = new Requirement();
        r.setName("Second requirement");
        r.setInitiators(p.getMembers());
        p.addRequirement(r);

        r = new Requirement();
        r.setName("Third requirement");
        r.setInitiators(p.getMembers());
        p.addRequirement(r);

        pl.merge(p);
    }
}
