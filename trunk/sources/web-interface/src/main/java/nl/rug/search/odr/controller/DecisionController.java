package nl.rug.search.odr.controller;


import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import nl.rug.search.odr.decision.DecisionLocal;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.entities.Decision;
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
    private DecisionLocal architecturalDecisionLocal;
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

        Decision ad = new Decision();
        ad.setName(name);
//        ad.setProblem(problem);
//        ad.setDecision(decision);
//        ad.setArguments(arguments);
//        ad.setOprId(oprId);

//        architecturalDecisionLocal.persistDecision(ad);

        return true;
    }

    public DecisionLocal getArchitecturalDecisionLocal() {
        return architecturalDecisionLocal;
    }

    public void setArchitecturalDecisionLocal(DecisionLocal architecturalDecisionLocal) {
        this.architecturalDecisionLocal = architecturalDecisionLocal;
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
