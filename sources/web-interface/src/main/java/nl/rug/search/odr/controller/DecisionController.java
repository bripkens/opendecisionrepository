package nl.rug.search.odr.controller;


import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import nl.rug.search.odr.decision.ArchitecturalDecisionLocal;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.entities.ArchitecturalDecision;
import nl.rug.search.odr.entities.Person;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.StakeholderRole;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.project.StakeholderRoleLocal;
import nl.rug.search.odr.user.UserLocal;


/**
 *
 * @author Stefan
 */
@RequestScoped
@ManagedBean
public class DecisionController extends AbstractController {

    @EJB
    private UserLocal ul;

    @EJB
    private StakeholderRoleLocal srl;

    @EJB
    private ProjectLocal pl;

    @EJB
    private ArchitecturalDecisionLocal architecturalDecisionLocal;
    private String name, problem, decision, arguments, oprId;

    @Override
    protected String getSuccessMessage() {
        return JsfUtil.evaluateExpressionGet("#{form['Decision.success']}", String.class);
    }

    @Override
    protected String getFailMessage() {
        return JsfUtil.evaluateExpressionGet("#{form['Decision.fail']}", String.class);
    }

    @Override
    protected void reset() {
        name = problem = decision = arguments = oprId = null;
    }

    @Override
    protected boolean execute() {

        ArchitecturalDecision ad = new ArchitecturalDecision();
        ad.setName(name);
        ad.setProblem(problem);
        ad.setDecision(decision);
        ad.setArguments(arguments);
        ad.setOprId(oprId);

        architecturalDecisionLocal.persistDecision(ad);

        return true;
    }

    public ArchitecturalDecisionLocal getArchitecturalDecisionLocal() {
        return architecturalDecisionLocal;
    }

    public void setArchitecturalDecisionLocal(ArchitecturalDecisionLocal architecturalDecisionLocal) {
        this.architecturalDecisionLocal = architecturalDecisionLocal;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public String getDecision() {
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

        Person p1 = new Person();
        p1.setName("Ben Ripkens");
        p1.setPlainPassword("12345");
        p1.setEmail("ben@ben.de");
        ul.register(p1);


        Person p2 = new Person();
        p2.setName("Stefan Arians");
        p2.setPlainPassword("12345");
        p2.setEmail("stefan@stefan.de");
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

        Project pro = new Project();
        pro.setName("OpenDecisionRepository");
        pro.setDescription("...part of search...");

        ProjectMember member = new ProjectMember();
        member.setPerson(p1);
        member.setRole(role1);
        pro.addMember(member);

        member = new ProjectMember();
        member.setPerson(p2);
        member.setRole(role1);
        pro.addMember(member);

        member = new ProjectMember();
        member.setPerson(p3);
        member.setRole(role2);
        pro.addMember(member);

        member = new ProjectMember();
        member.setPerson(p4);
        member.setRole(role3);
        pro.addMember(member);

        pl.createProject(pro);

        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
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
}
