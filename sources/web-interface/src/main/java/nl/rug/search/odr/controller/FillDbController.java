package nl.rug.search.odr.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
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
import nl.rug.search.odr.decision.DecisionLocal;
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
import nl.rug.search.odr.entities.Relationship;
import nl.rug.search.odr.entities.StakeholderRole;
import nl.rug.search.odr.entities.State;
import nl.rug.search.odr.entities.TemplateComponent;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.project.ConcernLocal;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.project.RelationshipTypeLocal;
import nl.rug.search.odr.project.StakeholderRoleLocal;
import nl.rug.search.odr.project.StateLocal;
import nl.rug.search.odr.user.UserLocal;
import nl.rug.search.odr.viewpoint.Handle;
import nl.rug.search.odr.viewpoint.relationship.InitRelationshipView;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewAssociation;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewNode;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewVisualization;

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
    private ConcernLocal cl;

    @EJB
    private RelationshipTypeLocal rtl;

    @EJB
    private DecisionLocal dl;

    private boolean clearDone, rolesDone, statesDone,
            templatesDone, personsDone, projectsDone, iterationsDone,
            decisionsDone, concernsDone, relationshipTypesDone, addAllDone, relationshipsDone, relationshipViewDone;




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
                statesDone = templatesDone = relationshipTypesDone = relationshipViewDone = false;
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

        Date previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Tcl");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("xowiki");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Java Enterprise Edition");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Glassfish");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);


        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("OPR technology stack");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("JavaServer Faces");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Icefaces");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Java Persistence API");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("MySQL");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Eclipselink");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Enterprise Java Beans");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Icefaces 1.8");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Icefaces 2.0-beta1");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Icefaces 2.0-beta2");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("JavaServer Faces 1.2");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("JavaServer Faces 2");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Glassfish 2");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Glassfish 3");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Enterprise Java Beans 3");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Enterprise Java Beans 3.1");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Java Enterprise Edition 5");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Java Enterprise Edition 6");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Client side image generation");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Apache FOP");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Apache Batik");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Scalable Vector Graphics");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("approved"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Server side image generation");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Image generation in Java");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("HTML 5 Canvas Element");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        previousDate = v.getDecidedWhen();
        d = new Decision();
        d.setName("Graphviz");
        d.setTemplate(getTemplate("Quick add form"));
        v = new Version();
        v.setDecidedWhen(new Date(previousDate.getTime() + 1));
        v.setDocumentedWhen(new Date());
        v.setInitiators(p.getMembers());
        v.setState(getState("rejected"));
        d.addVersion(v);
        p.addDecision(d);

        pl.merge(p);

        clearDone = false;
        decisionsDone = true;
    }




    public void addIterations() {
        Project p = pl.getByName("OpenDecisionRepository");

        Iteration it1 = new Iteration();
        it1.setName("Analysis and design");
        it1.setProjectMember(p.getMembers().iterator().next());
        it1.setDocumentedWhen(new Date());
        it1.setStartDate(getDate(2010, 9, 1));
        it1.setEndDate(getDate(2010, 9, 25));

        Iteration it2 = new Iteration();
        it2.setName("Sprint 1");
        it2.setProjectMember(p.getMembers().iterator().next());
        it2.setDocumentedWhen(new Date(it1.getEndDate().getTime() + 1));
        it2.setStartDate(getDate(2010, 9, 27));
        it2.setEndDate(getDate(2010, 10, 9));

        p.addIteration(it1);
        p.addIteration(it2);

        it2 = new Iteration();
        it2.setName("Sprint 2");
        it2.setProjectMember(p.getMembers().iterator().next());
        it2.setDocumentedWhen(new Date(it1.getEndDate().getTime() + 2));
        it2.setStartDate(getDate(2010, 10, 18));
        it2.setEndDate(getDate(2010, 10, 30));

        p.addIteration(it2);

        it2 = new Iteration();
        it2.setName("Sprint 3");
        it2.setProjectMember(p.getMembers().iterator().next());
        it2.setDocumentedWhen(new Date(it1.getEndDate().getTime() + 3));
        it2.setStartDate(getDate(2010, 11, 1));
        it2.setEndDate(getDate(2010, 11, 13));

        p.addIteration(it2);

        it2 = new Iteration();
        it2.setName("Sprint 4");
        it2.setProjectMember(p.getMembers().iterator().next());
        it2.setDocumentedWhen(new Date(it1.getEndDate().getTime() + 4));
        it2.setStartDate(getDate(2010, 11, 15));
        it2.setEndDate(getDate(2010, 11, 27));

        p.addIteration(it2);

        it2 = new Iteration();
        it2.setName("Sprint 5");
        it2.setProjectMember(p.getMembers().iterator().next());
        it2.setDocumentedWhen(new Date(it1.getEndDate().getTime() + 5));
        it2.setStartDate(getDate(2010, 11, 29));
        it2.setEndDate(getDate(2010, 12, 11));

        p.addIteration(it2);

        it2 = new Iteration();
        it2.setName("Sprint 6");
        it2.setProjectMember(p.getMembers().iterator().next());
        it2.setDocumentedWhen(new Date(it1.getEndDate().getTime() + 6));
        it2.setStartDate(getDate(2010, 12, 13));
        it2.setEndDate(getDate(2010, 12, 25));

        p.addIteration(it2);

        it2 = new Iteration();
        it2.setName("Sprint 7 / release sprint");
        it2.setProjectMember(p.getMembers().iterator().next());
        it2.setDocumentedWhen(new Date(it1.getEndDate().getTime() + 7));
        it2.setStartDate(getDate(2011, 1, 10));
        it2.setEndDate(getDate(2011, 1, 29));

        p.addIteration(it2);

        pl.merge(p);

        clearDone = false;
        iterationsDone = true;
    }




    private Date getDate(int year, int month, int day) {
        return new GregorianCalendar(year, month - 1, day).getTime();
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
        r.setInitiator(p.getMembers().iterator().next());
        r.setCreatedWhen(new Date());
        r.setExternalId("NFR-1");
        p.addConcern(r);
        cl.persist(r);
        r.setGroup(r.getId());


        r = new Concern();
        r.setName("Data integrity");
        r.setInitiator(p.getMembers().iterator().next());
        r.setCreatedWhen(new Date());
        r.setExternalId("NFR-2");
        p.addConcern(r);
        cl.persist(r);
        r.setGroup(r.getId());

        r = new Concern();
        r.setName("Well tested (> 70% test coverage)");
        r.setInitiator(p.getMembers().iterator().next());
        r.setCreatedWhen(new Date());
        r.setExternalId("NFR-10");
        p.addConcern(r);
        cl.persist(r);
        r.setGroup(r.getId());

        r = new Concern();
        r.setName("Portable to any major OS");
        r.setInitiator(p.getMembers().iterator().next());
        r.setCreatedWhen(new Date());
        r.setExternalId("NFR-11");
        p.addConcern(r);
        cl.persist(r);
        r.setGroup(r.getId());

        r = new Concern();
        r.setName("Password encryption");
        r.setInitiator(p.getMembers().iterator().next());
        r.setCreatedWhen(new Date());
        r.setExternalId("NFR-12");
        p.addConcern(r);
        cl.persist(r);
        r.setGroup(r.getId());

        r = new Concern();
        r.setName("OPR corporate layout");
        r.setInitiator(p.getMembers().iterator().next());
        r.setCreatedWhen(new Date());
        r.setExternalId("NFR-4");
        p.addConcern(r);
        cl.persist(r);
        r.setGroup(r.getId());

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




    public RelationshipType getRelationshipType(String name) {
        for (RelationshipType r : rtl.getAll()) {
            if (r.getName().equalsIgnoreCase(name)) {
                return r;
            }
        }

        throw new RuntimeException();
    }




    public void addRelationships() {
        Project p = pl.getByName("OpenDecisionRepository");
        Decision oprTech = getDecision(p.getDecisions(), "OPR technology stack");
        Decision java = getDecision(p.getDecisions(), "Java Programming language");
        Decision tcl = getDecision(p.getDecisions(), "Tcl");
        Decision xowiki = getDecision(p.getDecisions(), "xowiki");
        Decision javaee = getDecision(p.getDecisions(), "Java Enterprise Edition");
        Decision glassfish = getDecision(p.getDecisions(), "Glassfish");
        Decision jsf = getDecision(p.getDecisions(), "JavaServer Faces");
        Decision icefaces = getDecision(p.getDecisions(), "Icefaces");
        Decision jpa = getDecision(p.getDecisions(), "Java Persistence API");
        Decision mysql = getDecision(p.getDecisions(), "MySQL");
        Decision eclipselink = getDecision(p.getDecisions(), "Eclipselink");
        Decision ejb = getDecision(p.getDecisions(), "Enterprise Java Beans");
        Decision ice18 = getDecision(p.getDecisions(), "Icefaces 1.8");
        Decision ice201 = getDecision(p.getDecisions(), "Icefaces 2.0-beta1");
        Decision ice202 = getDecision(p.getDecisions(), "Icefaces 2.0-beta2");
        Decision jsf12 = getDecision(p.getDecisions(), "JavaServer Faces 1.2");
        Decision jsf2 = getDecision(p.getDecisions(), "JavaServer Faces 2");
        Decision glassfish2 = getDecision(p.getDecisions(), "Glassfish 2");
        Decision glassfish3 = getDecision(p.getDecisions(), "Glassfish 3");
        Decision ejb3 = getDecision(p.getDecisions(), "Enterprise Java Beans 3");
        Decision ejb31 = getDecision(p.getDecisions(), "Enterprise Java Beans 3.1");
        Decision jee5 = getDecision(p.getDecisions(), "Java Enterprise Edition 5");
        Decision jee6 = getDecision(p.getDecisions(), "Java Enterprise Edition 6");
        Decision client = getDecision(p.getDecisions(), "Client side image generation");
        Decision fop = getDecision(p.getDecisions(), "Apache FOP");
        Decision batik = getDecision(p.getDecisions(), "Apache Batik");
        Decision svg = getDecision(p.getDecisions(), "Scalable Vector Graphics");
        Decision server = getDecision(p.getDecisions(), "Server side image generation");
        Decision javaImg = getDecision(p.getDecisions(), "Image generation in Java");
        Decision html5 = getDecision(p.getDecisions(), "HTML 5 Canvas Element");
        Decision graphviz = getDecision(p.getDecisions(), "Graphviz");

        RelationshipType causedBy = getRelationshipType("caused by");
        RelationshipType alternative = getRelationshipType("is alternative for");
        RelationshipType replaces = getRelationshipType("replaces");
        RelationshipType depends = getRelationshipType("depends on");

        createRelationship(java, tcl, alternative);
        createRelationship(java, oprTech, causedBy);
        createRelationship(xowiki, tcl, depends);
        createRelationship(javaee, oprTech, causedBy);
        createRelationship(javaee, java, depends);
        createRelationship(jee5, javaee, causedBy);
        createRelationship(jee5, glassfish2, depends);
        createRelationship(jee6, javaee, causedBy);
        createRelationship(jee6, jee5, replaces);
        createRelationship(jee6, glassfish3, depends);
        createRelationship(glassfish, oprTech, causedBy);
        createRelationship(glassfish2, glassfish, causedBy);
        createRelationship(glassfish2, ice18, causedBy);
        createRelationship(glassfish3, glassfish, causedBy);
        createRelationship(glassfish3, glassfish2, replaces);
        createRelationship(glassfish3, ice202, causedBy);
        createRelationship(jsf, oprTech, causedBy);
        createRelationship(jsf12, jsf, causedBy);
        createRelationship(jsf12, ice18, causedBy);
        createRelationship(jsf2, jsf, causedBy);
        createRelationship(jsf2, jsf12, replaces);
        createRelationship(jsf2, ice202, causedBy);
        createRelationship(icefaces, oprTech, causedBy);
        createRelationship(ice18, icefaces, causedBy);
        createRelationship(ice201, icefaces, causedBy);
        createRelationship(ice201, ice18, replaces);
        createRelationship(ice202, icefaces, causedBy);
        createRelationship(ice202, ice201, replaces);
        createRelationship(ejb, oprTech, causedBy);
        createRelationship(ejb3, ejb, causedBy);
        createRelationship(ejb3, glassfish2, depends);
        createRelationship(ejb31, ejb, causedBy);
        createRelationship(ejb31, ejb3, replaces);
        createRelationship(ejb31, glassfish3, depends);
        createRelationship(jpa, oprTech, causedBy);
        createRelationship(jpa, java, depends);
        createRelationship(eclipselink, oprTech, causedBy);
        createRelationship(eclipselink, java, depends);
        createRelationship(mysql, oprTech, causedBy);
        createRelationship(javaImg, server, causedBy);
        createRelationship(graphviz, server, causedBy);
        createRelationship(client, server, replaces);
        createRelationship(html5, client, causedBy);
        createRelationship(svg, client, causedBy);
        createRelationship(svg, javaImg, replaces);
        createRelationship(svg, graphviz, replaces);
        createRelationship(svg, html5, replaces);
        createRelationship(fop, java, depends);
        createRelationship(fop, svg, causedBy);
        createRelationship(batik, java, depends);
        createRelationship(batik, svg, causedBy);


        pl.merge(p);

        relationshipsDone = true;
        clearDone = false;
    }




    private void createRelationship(Decision source, Decision target, RelationshipType type) {
        Relationship r = new Relationship();
        r.setSource(source.getCurrentVersion());
        r.setTarget(target.getCurrentVersion());
        r.setType(type);
    }




    private Relationship getRelationship(Version source, Version target, RelationshipType type) {
        for (Relationship r : source.getOutgoingRelationships()) {
            if (r.getTarget().equals(target) && r.getType().equals(type)) {
                return r;
            }
        }
        return null;
    }




    private Decision getDecision(Collection<Decision> allDecisions, String name) {
        for (Decision d : allDecisions) {
            if (d.getName().equalsIgnoreCase(name)) {
                return d;
            }
        }

        throw new RuntimeException();
    }




    public void addRelationshipView() {
        Project p = pl.getByName("OpenDecisionRepository");
        Decision oprTech = getDecision(p.getDecisions(), "OPR technology stack");
        Decision java = getDecision(p.getDecisions(), "Java Programming language");
        Decision tcl = getDecision(p.getDecisions(), "Tcl");
        Decision xowiki = getDecision(p.getDecisions(), "xowiki");
        Decision javaee = getDecision(p.getDecisions(), "Java Enterprise Edition");
        Decision glassfish = getDecision(p.getDecisions(), "Glassfish");
        Decision jsf = getDecision(p.getDecisions(), "JavaServer Faces");
        Decision icefaces = getDecision(p.getDecisions(), "Icefaces");
        Decision jpa = getDecision(p.getDecisions(), "Java Persistence API");
        Decision mysql = getDecision(p.getDecisions(), "MySQL");
        Decision eclipselink = getDecision(p.getDecisions(), "Eclipselink");
        Decision ejb = getDecision(p.getDecisions(), "Enterprise Java Beans");
        Decision ice18 = getDecision(p.getDecisions(), "Icefaces 1.8");
        Decision ice201 = getDecision(p.getDecisions(), "Icefaces 2.0-beta1");
        Decision ice202 = getDecision(p.getDecisions(), "Icefaces 2.0-beta2");
        Decision jsf12 = getDecision(p.getDecisions(), "JavaServer Faces 1.2");
        Decision jsf2 = getDecision(p.getDecisions(), "JavaServer Faces 2");
        Decision glassfish2 = getDecision(p.getDecisions(), "Glassfish 2");
        Decision glassfish3 = getDecision(p.getDecisions(), "Glassfish 3");
        Decision ejb3 = getDecision(p.getDecisions(), "Enterprise Java Beans 3");
        Decision ejb31 = getDecision(p.getDecisions(), "Enterprise Java Beans 3.1");
        Decision jee5 = getDecision(p.getDecisions(), "Java Enterprise Edition 5");
        Decision jee6 = getDecision(p.getDecisions(), "Java Enterprise Edition 6");
        Decision client = getDecision(p.getDecisions(), "Client side image generation");
        Decision fop = getDecision(p.getDecisions(), "Apache FOP");
        Decision batik = getDecision(p.getDecisions(), "Apache Batik");
        Decision svg = getDecision(p.getDecisions(), "Scalable Vector Graphics");
        Decision server = getDecision(p.getDecisions(), "Server side image generation");
        Decision javaImg = getDecision(p.getDecisions(), "Image generation in Java");
        Decision html5 = getDecision(p.getDecisions(), "HTML 5 Canvas Element");
        Decision graphviz = getDecision(p.getDecisions(), "Graphviz");

        InitRelationshipView init = new InitRelationshipView(p);
        RelationshipViewVisualization vis = init.getView();

        RelationshipViewNode node = vis.getNode(server.getCurrentVersion());
        node.setX(370);
        node.setY(80);
        node.setVisible(true);

        node = vis.getNode(graphviz.getCurrentVersion());
        node.setX(740);
        node.setY(80);
        node.setVisible(true);

        node = vis.getNode(client.getCurrentVersion());
        node.setX(850);
        node.setY(80);
        node.setVisible(true);

        node = vis.getNode(oprTech.getCurrentVersion());
        node.setX(180);
        node.setY(690);
        node.setVisible(true);

        node = vis.getNode(javaee.getCurrentVersion());
        node.setX(300);
        node.setY(430);
        node.setVisible(true);

        node = vis.getNode(svg.getCurrentVersion());
        node.setX(690);
        node.setY(220);
        node.setVisible(true);

        node = vis.getNode(javaImg.getCurrentVersion());
        node.setX(390);
        node.setY(220);
        node.setVisible(true);

        node = vis.getNode(batik.getCurrentVersion());
        node.setX(580);
        node.setY(330);
        node.setVisible(true);

        node = vis.getNode(fop.getCurrentVersion());
        node.setX(830);
        node.setY(330);
        node.setVisible(true);

        node = vis.getNode(html5.getCurrentVersion());
        node.setX(1130);
        node.setY(220);
        node.setVisible(true);

        node = vis.getNode(tcl.getCurrentVersion());
        node.setX(220);
        node.setY(220);
        node.setVisible(true);

        node = vis.getNode(xowiki.getCurrentVersion());
        node.setX(50);
        node.setY(220);

        node = vis.getNode(java.getCurrentVersion());
        node.setX(300);
        node.setY(430);
        node.setVisible(true);

        node = vis.getNode(jee5.getCurrentVersion());
        node.setX(760);
        node.setY(460);
        node.setVisible(true);

        node = vis.getNode(jee6.getCurrentVersion());
        node.setX(1170);
        node.setY(430);
        node.setVisible(true);

        node = vis.getNode(glassfish.getCurrentVersion());
        node.setX(520);
        node.setY(580);
        node.setVisible(true);

        node = vis.getNode(glassfish2.getCurrentVersion());
        node.setX(800);
        node.setY(530);
        node.setVisible(true);

        node = vis.getNode(glassfish3.getCurrentVersion());
        node.setX(1210);
        node.setY(580);
        node.setVisible(true);

        node = vis.getNode(jpa.getCurrentVersion());
        node.setX(180);
        node.setY(560);
        node.setVisible(true);

        node = vis.getNode(eclipselink.getCurrentVersion());
        node.setX(60);
        node.setY(560);
        node.setVisible(true);

        node = vis.getNode(ejb.getCurrentVersion());
        node.setX(480);
        node.setY(790);
        node.setVisible(true);

        node = vis.getNode(ejb3.getCurrentVersion());
        node.setX(760);
        node.setY(740);
        node.setVisible(true);

        node = vis.getNode(ejb31.getCurrentVersion());
        node.setX(900);
        node.setY(740);
        node.setVisible(true);

        node = vis.getNode(jsf.getCurrentVersion());
        node.setX(2000);
        node.setY(2000);
        node.setVisible(true);

        Relationship rel = getRelationship(html5.getCurrentVersion(), client.getCurrentVersion(), getRelationshipType("caused by"));
        RelationshipViewAssociation relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1220).setY(100));

        rel = getRelationship(svg.getCurrentVersion(), client.getCurrentVersion(), getRelationshipType("caused by"));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(850).setY(230));
        relVieAss.addHandle(new Handle().setX(960).setY(230));

         rel = getRelationship(client.getCurrentVersion(), server.getCurrentVersion(), getRelationshipType("replaces"));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(960).setY(50));
        relVieAss.addHandle(new Handle().setX(480).setY(50));


        rel = getRelationship(fop.getCurrentVersion(), svg.getCurrentVersion(), getRelationshipType("caused by"));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(880).setY(290));
        relVieAss.addHandle(new Handle().setX(780).setY(290));

        rel = getRelationship(batik.getCurrentVersion(), svg.getCurrentVersion(), getRelationshipType("caused by"));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(640).setY(290));
        relVieAss.addHandle(new Handle().setX(780).setY(290));

        rel = getRelationship(java.getCurrentVersion(), tcl.getCurrentVersion(), getRelationshipType("is alternative for"));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(360).setY(440));
        relVieAss.addHandle(new Handle().setX(360).setY(240));


        rel = getRelationship(batik.getCurrentVersion(), java.getCurrentVersion(), getRelationshipType("depends on"));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(400).setY(350));

        rel = getRelationship(eclipselink.getCurrentVersion(), java.getCurrentVersion(), getRelationshipType("depends on"));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(110).setY(450));

        rel = getRelationship(fop.getCurrentVersion(), java.getCurrentVersion(), getRelationshipType("depends on"));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(880).setY(390));
        relVieAss.addHandle(new Handle().setX(400).setY(390));

        rel = getRelationship(jee6.getCurrentVersion(), jee5.getCurrentVersion(), getRelationshipType("replaces"));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1210).setY(460));
        relVieAss.addHandle(new Handle().setX(1210).setY(480));

        rel = getRelationship(jpa.getCurrentVersion(), java.getCurrentVersion(), getRelationshipType("depends on"));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(260).setY(450));


        rel = getRelationship(glassfish2.getCurrentVersion(), glassfish.getCurrentVersion(), getRelationshipType("caused by"));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(560).setY(550));

        rel = getRelationship(glassfish3.getCurrentVersion(), glassfish2.getCurrentVersion(), getRelationshipType("replaces"));
        relVieAss = vis.getAssociation(rel);
        relVieAss.addHandle(new Handle().setX(1230).setY(590));
        relVieAss.addHandle(new Handle().setX(1230).setY(550));

        p.addRelationshipView(vis);
        pl.merge(p);

        relationshipViewDone = true;
        clearDone = false;
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




    public void doEverything() {
        DatabaseCleaner.bruteForceCleanup();

        SessionUtil.resetSession();

        addStakeholderRoles();
        addStates();
        addDecisionTemplates();
        addRelationshipTypes();
        addPersons();
        addProject();
        addIterations();
        addDecisions();
        addConcerns();
        addRelationships();
        addRelationshipView();

        clearDone = false;
        addAllDone = true;

        login();
    }




    public boolean isAddAllDone() {
        return addAllDone;
    }




    public boolean isRelationshipsDone() {
        return relationshipsDone;
    }




    public void setRelationshipsDone(boolean relationshipsDone) {
        this.relationshipsDone = relationshipsDone;
    }




    public boolean isRelationshipViewDone() {
        return relationshipViewDone;
    }




}



