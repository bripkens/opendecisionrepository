package nl.rug.search.odr.controller;


import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import nl.rug.search.odr.decision.ArchitecturalDecisionLocal;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.entities.ArchitecturalDecision;
import nl.rug.search.odr.entities.Person;
import nl.rug.search.odr.entities.StakeholderRole;
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

        architecturalDecisionLocal.addDecision(ad);

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
        StakeholderRole role = new StakeholderRole();
        role.setName("Architect");
        role.setCommon(true);
        srl.persistRole(role);

        role = new StakeholderRole();
        role.setName("Manager");
        role.setCommon(true);
        srl.persistRole(role);

        role = new StakeholderRole();
        role.setName("Customer");
        role.setCommon(true);
        srl.persistRole(role);

        Person p = new Person();
        p.setName("Ben Ripkens");
        p.setPlainPassword("12345");
        p.setEmail("ben@ben.de");
        ul.register(p);

        p = new Person();
        p.setName("Stefan Arians");
        p.setPlainPassword("12345");
        p.setEmail("stefan@stefan.de");
        ul.register(p);

        p = new Person();
        p.setName("Uwe van Heesch");
        p.setPlainPassword("12345");
        p.setEmail("uwe@uwe.de");
        ul.register(p);

        p = new Person();
        p.setName("Paris Avgeriou");
        p.setPlainPassword("12345");
        p.setEmail("paris@paris.de");
        ul.register(p);

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
