package nl.rug.search.odr.controller;

import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import nl.rug.search.odr.project.IterationLocal;
import nl.rug.search.odr.entities.Iteration;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.Filename;
import nl.rug.search.odr.QueryStringBuilder;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.util.ErrorUtil;
import nl.rug.search.odr.util.JsfUtil;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.Action;
import nl.rug.search.odr.NavigationBuilder;

/**
 *
 * @author Stefan
 */
@ManagedBean
@ViewScoped
public class IterationController {

    @EJB
    private ProjectLocal pl;

    @EJB
    private IterationLocal il;

    private long projectId;

    private String str_projectId;

    private Project project;

    private long iterationId;

    private String str_iterationId;

    private Iteration iteration = null;

    // <editor-fold defaultstate="collapsed" desc="Iteration attributes">
    private String iterationName = "";

    private String iterationDescription = "";

    private ProjectMember member;

    private Date startDate = null;

    private Date endDate = null;

    private long days;

    private long hours;

    private long minutes;
// </editor-fold>

    private boolean isUpdate = false;

    private NavigationBuilder navi;

    private String url;




    @PostConstruct
    public void postConstruct() {
        if (!AuthenticationUtil.isAuthtenticated()) {
            ErrorUtil.showNotAuthenticatedError();
            return;
        }
        navi = new NavigationBuilder();
        getRequestURL();
        navi.setNavigationSite(url);

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().
                getRequest();

        // <editor-fold defaultstate="collapsed" desc="get Project Id">

        if (request.getParameter(RequestParameter.ID) != null) {
            str_projectId = request.getParameter(RequestParameter.ID);

            try {
                projectId = Long.parseLong(str_projectId);
            } catch (NumberFormatException e) {
                ErrorUtil.showInvalidIdError();
                return;
            }
        }

        // </editor-fold>

        getProject();
        navi.setProject(project);

        if (project == null) {
            ErrorUtil.showIdNotRegisteredError();
            return;
        } else if (project != null && !memberIsInProject()) {
            ErrorUtil.showNoMemberError();
            return;
        }
        JsfUtil.flashScope().put("projetId", projectId);

        // <editor-fold defaultstate="collapsed" desc="get Iteration Id">
        if (request.getParameter(RequestParameter.ITERATION_ID) != null) {
            isUpdate = true;
            str_iterationId = request.getParameter(RequestParameter.ITERATION_ID);
        } else {
            isUpdate = false;
            navi.setOption(Action.CREATE);
        }

        if (isUpdate) {
            try {
                iterationId = Long.parseLong(str_iterationId);
                getIterationFromDb();
                navi.setIteration(iteration);
                navi.setOption(Action.EDIT);
            } catch (NumberFormatException e) {
                ErrorUtil.showInvalidIdError();
                return;
            }
        }
        // </editor-fold>



        if (iteration == null) {
            iteration = new Iteration();
            Date currentDate = new Date();
            iteration.setStartDate(currentDate);
            iteration.setEndDate(new Date(currentDate.getTime() + 86400000l));
            startDate = iteration.getStartDate();
            endDate = iteration.getEndDate();
        } else {
            iterationName = iteration.getName();
            iterationDescription = iteration.getDescription();
            startDate = iteration.getStartDate();
            endDate = iteration.getEndDate();
            calculateDuration();
        }
    }




    public void getRequestURL() {
        this.url = FacesContext.getCurrentInstance().getViewRoot().getViewId();
    }




    public List<NavigationBuilder.NavigationLink> getNavigationBar() {
        return navi.getNavigationBar();
    }




    private Project getProject() {
        project = pl.getById(projectId);
        return project;
    }




    private boolean memberIsInProject() {
        return getProjectMember() != null;
    }




    public ProjectMember getProjectMember() {
        long userId = AuthenticationUtil.getUserId();
        for (ProjectMember pm : project.getMembers()) {
            if (pm.getPerson().getId().equals(userId)) {
                member = pm;
                return pm;
            }
        }
        return null;
    }




    public long getProjectId() {
        return projectId;
    }




    public String getDataRequestUrl() {
        return Filename.ITERATION_DATA_PROVIDER;
    }




    private Iteration getIterationFromDb() {
        iteration = il.getById(iterationId);
        return iteration;
    }




    public boolean isValid() {
        if (project != null && iteration != null) {
            return true;
        }
        return false;
    }




    public Iteration getIteration() {
        return iteration;
    }




    public void submitForm() {



        if (getCurrentMessages().isEmpty()) {
            iteration.setName(iterationName);
            iteration.setDescription(iterationDescription);

            if (iteration.getEndDate().before(startDate)) {
                iteration.setEndDate(endDate);
            }
            iteration.setStartDate(startDate);
            iteration.setEndDate(endDate);

            if (il.isIntersection(iteration, projectId)) {
                FacesContext.getCurrentInstance().addMessage("manageIteration",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        JsfUtil.evaluateExpressionGet("#{form['iteration.error.intersection']}", String.class),
                        null));
                return;
            }
            //TODO IF THE MEMBER ALSO BE SAVED

            if (isUpdate) {
                il.merge(iteration);
            } else {
                iteration.setDocumentedWhen(new Date());
                iteration.setProjectMember(member);
                project.addIteration(iteration);
                il.persist(iteration);
                pl.merge(project);
            }

            JsfUtil.redirect(new QueryStringBuilder().setUrl(Filename.ITEARTION_DETAILS_WITH_LEADING_SLASH).
                    append(RequestParameter.ID, project.getId()).
                    append(RequestParameter.ITERATION_ID, iteration.getId()).
                    toString());
        }
    }




    public void abortForm() {
        JsfUtil.redirect(RequestParameter.PROJECT_PATH_SHORT + project.getName());
    }

    // <editor-fold defaultstate="collapsed" desc="comboBox values">



    public SelectItem[] getHourItems() {
        return arrayBuilder(24);
    }




    public SelectItem[] getMinuteItems() {
        return arrayBuilder(60);
    }




    private SelectItem[] arrayBuilder(int count) {
        SelectItem[] components = new SelectItem[count];
        for (int i = 0; i < components.length; i++) {
            components[i] = new SelectItem(i);
        }
        return components;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getter-setter">



    public String getName() {
        return iterationName;
    }




    public void setName(String name) {
        iterationName = name;
    }




    public String getDescription() {
        return iterationDescription;
    }




    public void setDescription(String desciption) {
        iterationDescription = desciption;
    }




    public Date getEndDate() {
        return endDate;
    }




    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }




    public Date getStartDate() {
        return startDate;
    }




    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    // </editor-fold>




    public List<FacesMessage> getCurrentMessages() {
        List<FacesMessage> result = new ArrayList<FacesMessage>();
        Iterator<FacesMessage> iter = FacesContext.getCurrentInstance().getMessages();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }




    public String getupdateLink() {
        return new QueryStringBuilder().setUrl("manageIteration.html").
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.ITERATION_ID, iteration.getId()).
                toString();
    }




    public String getDays() {
        return String.valueOf(days);
    }




    public String getHours() {
        return String.valueOf(hours);
    }




    public String getMinutes() {
        return String.valueOf(minutes);
    }




    public void calculateDuration() {
        long millisPerMinute = 1000 * 60;
        long millisPerHour = millisPerMinute * 60;
        long millisPerDay = millisPerHour * 24;

        long end = iteration.getEndDate().getTime();
        long start = iteration.getStartDate().getTime();
        long timeDiff = end - start;
        days = timeDiff / millisPerDay;
        hours = (timeDiff % millisPerDay) / millisPerHour;
        minutes = (timeDiff % millisPerHour) / millisPerMinute;
        System.out.println(days + " : " + hours + " : " + minutes);
    }




}



