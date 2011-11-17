package nl.rug.search.odr.util.url;

import javax.ejb.EJB;
import nl.rug.search.odr.EjbUtil;
import nl.rug.search.odr.project.ProjectLocal;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public final class UrlBindings {

    private final ProjectLocal pl;

    // <editor-fold defaultstate="collapsed" desc="generic reg ex">
    private static final String[] CREATE = {"^new[/]?", "new/"};

    private static final String[] EDIT = {"^edit[/]?", "edit/"};

    private static final String[] DELETE = {"^delete[/]?", "delete/"};

    private static final String[] ID = {"^(\\d+)[/]?", "%s/"};

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="binding attributes">
    private Binding root;

    /*
     * used in bindUser()
     */    
    private Binding register;

    /*
     * used in bindUser()
     */
    private Binding logout;
    
    private Binding projectOverview;

    private Binding projectDetails;

    private Binding projectCreate;

    private Binding projectEdit;

    private Binding projectDelete;

    private Binding memberOverview;

    private Binding iterationOverview;

    private Binding iterationDetails;

    private Binding iterationCreate;

    private Binding iterationEdit;

    private Binding iterationDelete;

    private Binding concernOverview;

    private Binding concernDetails;

    private Binding concernCreate;

    private Binding concernEdit;

    private Binding concernDelete;

    private Binding decisionOverview;

    private Binding decisionDetails;

    private Binding decisionCreate;

    private Binding decisionEdit;

    private Binding decisionDelete;

    // </editor-fold>
    
    private UrlBindings() {
        pl = EjbUtil.lookUp(ProjectLocalHelper.JNDI_NAME, ProjectLocal.class);

        root = new Binding("^[/]?", "").to("/index.html");
        
        bindUser();
        bindProject();
        bindIteration();
        bindConcern();
        bindDecision();
    }

    // <editor-fold defaultstate="collapsed" desc="bindings">
    private void bindUser() {
        register = root.bind("^register[/]?").to("/register.html");
        // todo implement logout!
        logout = root.bind("^logout[/]?").to("/logout.do");
    }
    
    private void bindProject() {
        projectOverview = root.bind("^p[/]?", "").to("/projects.html");

        projectCreate = projectOverview.
                bind(CREATE).
                to("/createProject.html?create=true");

        projectDetails = new ProjectBinding("^(\\w{2,100})[/]?", "%s/", pl);
        projectOverview.
                bind(projectDetails).
                to("/projectDetails.html?id=%s");

        projectEdit = projectDetails.
                bind(EDIT).
                to("/updateProject.html?update=true&id=%s");

        projectDelete = projectDetails.
                bind(DELETE).
                to("/deleteProject.html?delete=true&id=%s");

        memberOverview = projectDetails.
                bind("^members[/]?", "members/").
                to("/membersTable.html?id=%s");
    }

    private void bindIteration() {
        iterationOverview = projectDetails.
                bind("^iterations[/]?", "iterations/").
                to("/iterationsTable.html?id=%s");
        iterationDetails = iterationOverview.
                bind(ID).
                to("/iterationDetails.html?id=%s&iterationId=%s");
        iterationCreate = iterationOverview.
                bind(CREATE).
                to("/manageIteration.html?id=%s");
        iterationEdit = iterationDetails.
                bind(EDIT).
                to("/manageIteration.html?id=%s&iterationId=%s");
        iterationDelete = iterationDetails.
                bind(DELETE);
    }

    private void bindConcern() {
        concernOverview = projectDetails.
                bind("^concerns[/]?", "concerns/").
                to("/concernsTable.html?id=%s");
        concernDetails = concernOverview.
                bind(ID).
                to("/concernDetails.html?id=%s&concernId=%s");
        concernCreate = concernOverview.
                bind(CREATE).
                to("/manageConcern.html?id=%s");
        concernEdit = concernDetails.
                bind(EDIT).
                to("/manageConcern.html?id=%s&concernId=%s");
        concernDelete = concernDetails.
                bind(DELETE);
    }

    private void bindDecision() {
        decisionOverview = projectDetails.
                bind("^decisions[/]?", "decisions/").
                to("/decisionsTable.html?id=%s");
        decisionDetails = decisionOverview.
                bind("^(\\d+)/(\\d+)[/]?", "%s/%s/").
                to("/decisionDetails.html?id=%s&decisionId=%s&versionId=%s");
        decisionCreate = decisionOverview.
                bind(CREATE).
                to("/manageDecision.html?id=%s&create");
        decisionEdit = decisionDetails.
                bind(EDIT).
                to("/manageDecision.html?id=%s&decisionId=%s&versionId=%s");
        decisionDelete = decisionDetails.
                bind(DELETE);
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="retrieve bindings">

    public Binding getConcernCreate() {
        return concernCreate;
    }

    public Binding getConcernDelete() {
        return concernDelete;
    }

    public Binding getConcernDetails() {
        return concernDetails;
    }

    public Binding getConcernEdit() {
        return concernEdit;
    }

    public Binding getConcernOverview() {
        return concernOverview;
    }

    public Binding getDecisionCreate() {
        return decisionCreate;
    }

    public Binding getDecisionDelete() {
        return decisionDelete;
    }

    public Binding getDecisionDetails() {
        return decisionDetails;
    }

    public Binding getDecisionEdit() {
        return decisionEdit;
    }

    public Binding getDecisionOverview() {
        return decisionOverview;
    }

    public Binding getIterationCreate() {
        return iterationCreate;
    }

    public Binding getIterationDelete() {
        return iterationDelete;
    }

    public Binding getIterationDetails() {
        return iterationDetails;
    }

    public Binding getIterationEdit() {
        return iterationEdit;
    }

    public Binding getIterationOverview() {
        return iterationOverview;
    }

    public Binding getMemberOverview() {
        return memberOverview;
    }

    public Binding getProjectCreate() {
        return projectCreate;
    }

    public Binding getProjectDelete() {
        return projectDelete;
    }

    public Binding getProjectDetails() {
        return projectDetails;
    }

    public Binding getProjectEdit() {
        return projectEdit;
    }

    public Binding getProjectOverview() {
        return projectOverview;
    }

    public Binding getRoot() {
        return root;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="singleton and EJB">
    public static UrlBindings getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @EJB(name = ProjectLocalHelper.JNDI_NAME,
         beanInterface = ProjectLocal.class)
    private class ProjectLocalHelper {

        public static final String JNDI_NAME =
                "nl.rug.search.odr.util.url.UrlBindings.Helper";
    }

    private static class SingletonHolder {

        private static final UrlBindings INSTANCE = new UrlBindings();
    }
    // </editor-fold>
}
