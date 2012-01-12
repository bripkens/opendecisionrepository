package nl.rug.search.odr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nl.rug.search.odr.entities.Concern;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.util.JsfUtil;

/**
 *
 * @author Stefan
 */
public class NavigationBuilder {

//  ############### Navigation Strings############
    private static final String EDIT = JsfUtil.evaluateExpressionGet("#{page['navigation.edit']}", String.class);

    private static final String CREATE = JsfUtil.evaluateExpressionGet("#{page['navigation.create']}", String.class);

    private static final String DELETE = JsfUtil.evaluateExpressionGet("#{page['navigation.delete']}", String.class);

    private static final String ITERATIONS = JsfUtil.evaluateExpressionGet("#{page['navigation.iterations']}", String.class);

    private static final String CONCERNS = JsfUtil.evaluateExpressionGet("#{page['navigation.concerns']}", String.class);

    private static final String DECISIONS = JsfUtil.evaluateExpressionGet("#{page['navigation.decisions']}", String.class);

    private static final String MEMBERS = JsfUtil.evaluateExpressionGet("#{page['navigation.members']}", String.class);

    private static final String PROJECT = JsfUtil.evaluateExpressionGet("#{page['navigation.project']}", String.class);

    private static final String PROJECTS = JsfUtil.evaluateExpressionGet("#{page['navigation.projects']}", String.class);

    private static final String REGISTER = JsfUtil.evaluateExpressionGet("#{page['navigation.register']}", String.class);

    private static final String HOME = JsfUtil.evaluateExpressionGet("#{page['navigation.home']}", String.class);

    private static final String USER = JsfUtil.evaluateExpressionGet("#{page['navigation.user']}", String.class);

//  ##############################################
    private Project project;

    private Iteration iteration;

    private Concern concern;

    private Decision decision;

    private Version version;

    private Action option;

    private String navigationSite;



// <editor-fold defaultstate="collapsed" desc="getter / setter">

    public String getNavigationSite() {
        return navigationSite;
    }




    public void setNavigationSite(String navigationSite) {
        this.navigationSite = new StringBuilder(navigationSite).deleteCharAt(navigationSite.length() - 5).toString();
    }




    public Concern getConcern() {
        return concern;
    }




    public void setConcern(Concern concern) {
        this.concern = concern;
    }




    public Decision getDecision() {
        return decision;
    }




    public void setDecision(Decision decision) {
        this.decision = decision;
    }




    public Iteration getIteration() {
        return iteration;
    }




    public void setIteration(Iteration iteration) {
        this.iteration = iteration;
    }




    public Project getProject() {
        return project;
    }




    public void setProject(Project project) {
        if (project != null) {
            this.project = project;
        }
    }




    public Version getVersion() {
        return version;
    }




    public void setVersion(Version version) {
        this.version = version;
    }




    public Action getOption() {
        return option;
    }




    public void setOption(Action option) {
        this.option = option;
    }

// </editor-fold>



    public List<NavigationLink> getNavigationBar() {



        List<NavigationLink> navigation = new ArrayList<NavigationLink>();

        //Home
        navigation.add(new NavigationLink(HOME, Filename.INDEX));

        //projects
        if (!navigationSite.equals(Filename.PROJECT_OVERVIEW_LEADING_SLASH)
                && !navigationSite.equals(Filename.CREATE_PROJECT_WITH_LEADING_SLASH)
                && !navigationSite.equals(Filename.REGISTER_USER_WITH_LEADING_SLASH)) {

            if (project == null) {
                return Collections.EMPTY_LIST;
            } else {
                navigation.add(new NavigationLink(project.getName(), RequestParameter.PROJECT_PATH_WITH_ENDING_SLASH
                        + project.getName()));
            }

        }

        //Iteration
        if (navigationSite.equals(Filename.MANAGE_ITERATION_WITH_LEADING_SLASH)
                || navigationSite.equals(Filename.ITEARTION_DETAILS_WITH_LEADING_SLASH)) {
            iterationSpecific(navigation);
        } //Concern
        else if (navigationSite.equals(Filename.MANAGE_CONCERNS_WITH_LEADING_SLASH)
                || navigationSite.equals(Filename.CONCERN_DETAILS_WITH_LEADING_SLASH)) {
            concernSpecific(navigation);
        } // Decision
        else if (navigationSite.equals(Filename.MANAGE_DECISION_WITH_LEADING_SLASH)
                || navigationSite.equals(Filename.DECISION_DETAILS_WITH_LEADING_SLASH)) {
            decisionSpecific(navigation);
        } // Project
        else if (navigationSite.equals(Filename.CREATE_PROJECT_WITH_LEADING_SLASH)) {
            navigation.add(new NavigationLink(PROJECT, ""));
            navigation.add(new NavigationLink(CREATE, ""));
        } //Projects
        else if (navigationSite.equals(Filename.PROJECT_OVERVIEW_LEADING_SLASH)) {
            navigation.add(new NavigationLink(PROJECTS, ""));
        } //edit Project
        else if (navigationSite.equals(Filename.UPDATE_PROJECT_WITH_LEADING_SLASH)) {
            navigation.add(new NavigationLink(EDIT, ""));
        } //delete Project
        else if (navigationSite.equals(Filename.DELETE_PROJECT_WITH_LEADING_SLASH)) {
            navigation.add(new NavigationLink(DELETE, ""));
        } //register
        else if (navigationSite.equals(Filename.REGISTER_USER_WITH_LEADING_SLASH)) {
            navigation.add(new NavigationLink(USER, ""));
            navigation.add(new NavigationLink(REGISTER, ""));
        } //concernsTable
        else if (navigationSite.equals(Filename.CONCERNS_TABLE_WITH_LEADING_SLASH)) {
            navigation.add(new NavigationLink(CONCERNS, ""));
        } //iterationsTable
        else if (navigationSite.equals(Filename.ITERATIONS_TABLE_WITH_LEADING_SLASH)) {
            navigation.add(new NavigationLink(ITERATIONS, ""));
        } //decisionTable
        else if (navigationSite.equals(Filename.DECISIONS_TABLE_WITH_LEADING_SLASH)) {
            navigation.add(new NavigationLink(DECISIONS, ""));
        } //membersTable
        else if (navigationSite.equals(Filename.MEMBERS_TABLE_WITH_LEADING_SLASH)) {
            navigation.add(new NavigationLink(MEMBERS, ""));
        }
        return navigation;
    }

// <editor-fold defaultstate="collapsed" desc="Iteration">



    private void iterationSpecific(List<NavigationLink> navigation) {
        addIterationHeader(navigation);
        if (navigationSite.equals(Filename.MANAGE_ITERATION_WITH_LEADING_SLASH)) {
            if (option == Action.CREATE) {
                navigation.add(new NavigationLink(CREATE, ""));
            } else {
                navigation.add(new NavigationLink(iteration.getName(), iterationLink(Filename.ITERATION_DETAILS)));
                navigation.add(new NavigationLink(EDIT, ""));
            }
        } else if (navigationSite.equals(Filename.ITEARTION_DETAILS_WITH_LEADING_SLASH)) {
            navigation.add(new NavigationLink(iteration.getName(), ""));
        }
    }




    private void addIterationHeader(List<NavigationLink> list) {
        list.add(new NavigationLink(ITERATIONS, getTableLink(Filename.ITERATION_TABLE)));
    }




    private String iterationLink(String site) {
        return site + "?" + RequestParameter.ID + "=" + project.getId() + "&" + RequestParameter.ITERATION_ID + "=" + iteration.
                getId();
    }




    // </editor-fold>
// <editor-fold defaultstate="collapsed" desc="concern">
    private void concernSpecific(List<NavigationLink> navigation) {
        addConcernHeader(navigation);
        if (navigationSite.equals(Filename.MANAGE_CONCERNS_WITH_LEADING_SLASH)) {
            if (option == Action.CREATE) {
                navigation.add(new NavigationLink(CREATE, ""));
            } else {
                navigation.add(new NavigationLink(concern.getName(), concernLink(Filename.CONCERN_DETAILS)));
                navigation.add(new NavigationLink(EDIT, ""));
            }
        } else if (navigationSite.equals(Filename.CONCERN_DETAILS_WITH_LEADING_SLASH)) {
            navigation.add(new NavigationLink(concern.getName(), ""));
        }
    }




    private void addConcernHeader(List<NavigationLink> list) {
        list.add(new NavigationLink(CONCERNS, getTableLink(Filename.CONCERNS_TABLE)));
    }




    private String concernLink(String site) {
        return site + "?" + RequestParameter.ID + "=" + project.getId() + "&" + RequestParameter.CONCERN_ID + "=" + concern.
                getId();
    }


// </editor-fold>


// <editor-fold defaultstate="collapsed" desc="decision">
    private void decisionSpecific(List<NavigationLink> navigation) {
        addDecisionHeader(navigation);
        if (navigationSite.equals(Filename.MANAGE_DECISION_WITH_LEADING_SLASH)) {
            if (option == Action.CREATE) {
                navigation.add(new NavigationLink(CREATE, ""));
            } else {
                navigation.add(new NavigationLink(decision.getName(), decisionLink(Filename.DECISION_DETAILS)));
                navigation.add(new NavigationLink(EDIT, ""));
            }
        } else if (navigationSite.equals(Filename.DECISION_DETAILS_WITH_LEADING_SLASH)) {
            navigation.add(new NavigationLink(decision.getName(), ""));
        }
    }




    private void addDecisionHeader(List<NavigationLink> list) {
        list.add(new NavigationLink(DECISIONS, getTableLink(Filename.DECISIONS_TABLE)));
    }




    private String decisionLink(String site) {
        return site + "?" + RequestParameter.ID + "=" + project.getId() + "&" + RequestParameter.DECISION_ID + "=" + decision.
                getId();
    }

// </editor-fold>



    private String getTableLink(String site) {
        return site + "?" + RequestParameter.ID + "=" + project.getId();
    }


// <editor-fold defaultstate="collapsed" desc="NavigationLink class">


    public class NavigationLink {

        private String name;

        private String link;




        public NavigationLink(String name, String link) {
            this.name = name;
            this.link = link;
        }




        public String getLink() {
            return link;
        }




        public void setLink(String link) {
            this.link = link;
        }




        public String getName() {
            return name;
        }




        public void setName(String name) {
            this.name = name;
        }




    }

// </editor-fold>



}



