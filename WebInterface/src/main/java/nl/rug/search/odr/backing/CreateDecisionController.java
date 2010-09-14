/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.odr.backing;

import javax.ejb.EJB;
import nl.rug.search.odr.DecisionBeanLocal;
import nl.rug.search.odr.entities.Decision;

/**
 *
 * @author Stefan
 */
public class CreateDecisionController {

    @EJB
    private DecisionBeanLocal decisionBean;

    private String name;
    private String problem;
    private String decision;
    private String pId;
    private String arguments;



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

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public void create(){
        System.out.println("bin in der create");

        Decision newdecision = new Decision();
        newdecision.setName(name);
        newdecision.setProblem(problem);
        newdecision.setArguments(arguments);
        newdecision.setOprId(pId);
        newdecision.setDecision(decision);

        decisionBean.createDecision(newdecision);
    }
}
