package nl.rug.search.odr.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import nl.rug.search.odr.RequestAnalyser;
import nl.rug.search.odr.RequestAnalyser.RequestAnalyserDto;

/**
 *
 * @author Stefan
 */
@ManagedBean
@ViewScoped
public class IterationController {

    @EJB
    private ProjectLocal projectLocal;

    @EJB
    private IterationLocal il;

    private long projectId;

    private Project project;

    private long iterationId;

    private String str_iterationId;

    private Iteration iteration = null;

    private ProjectMember member;

    // <editor-fold defaultstate="collapsed" desc="Iteration attributes">
    private String iterationName = "";

    private String iterationDescription = "";

    private Date startDate = null;

    private Date endDate = null;

    private long days;

    private long hours;

    private long minutes;
// </editor-fold>

    private boolean isUpdate = false;

    private NavigationBuilder navi;

    private boolean validRequest;




    @PostConstruct
    public void postConstruct() {
        navi = new NavigationBuilder();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().
                getExternalContext().
                getRequest();


        RequestAnalyser analyser = new RequestAnalyser(request, projectLocal);
        RequestAnalyserDto result = analyser.analyse();


        if (result.isValid()) {
            JsfUtil.flashScope().put("projetId", projectId);
            member = result.getMember();
            project = result.getProject();
            if (request.getParameter(RequestParameter.ITERATION_ID) != null) {
                isUpdate = true;
                str_iterationId = request.getParameter(RequestParameter.ITERATION_ID);
            } else {
                isUpdate = false;
            }

            if (isUpdate) {
                try {
                    iterationId = Long.parseLong(str_iterationId);

                } catch (NumberFormatException e) {
                    ErrorUtil.showInvalidIdError();
                    validRequest = false;
                    return;
                }
                iteration = il.getById(iterationId);
                if (!project.getIterations().contains(iteration)) {
                    ErrorUtil.showInvalidIdError();
                    validRequest = false;
                    return;
                } else if (str_iterationId != null && iteration == null) {
                    ErrorUtil.showInvalidIdError();
                    validRequest = false;
                    return;
                }
            }

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
            validRequest = true;
        } else {
            result.executeErrorAction();
            validRequest = false;
        }
    }




    /**
     * adds the specific attributes to the navigationbuilder and returns a list with all information that
     * are neccessary to create the breadcrumbtrail
     * @return the list of navigationlink for the breadcrumbtrail
     */
    public List<NavigationBuilder.NavigationLink> getNavigationBar() {
        if (!validRequest) {
            return Collections.EMPTY_LIST;
        }
        navi.setNavigationSite(FacesContext.getCurrentInstance().getViewRoot().getViewId());
        navi.setProject(project);
        navi.setIteration(iteration);
        if (isUpdate) {
            navi.setOption(Action.EDIT);
        } else {
            navi.setOption(Action.CREATE);
        }
        return navi.getNavigationBar();
    }




    /**
     * checks if the user that is logged in, is part of the project
     * @return the projectmember which is logged in and part of the project.
     *         if he isn't part of the project, it returns null
     */
    public ProjectMember getProjectMember() {
        return member;
    }




    public long getProjectId() {
        return projectId;
    }




    /**
     * Get the JSON Code which includes all iterations
     * @return the String with the JSON code for the iteration calendar
     */
    public String getDataRequestUrl() {
        return Filename.ITERATION_DATA_PROVIDER;
    }




    /**
     *  gets the iteration with the provided requestparameter(iterationId) from the database
     * @return the iteration
     */
    private Iteration getIterationFromDb() {
        iteration = il.getById(iterationId);
        return iteration;
    }




    /**
     * checks if the request is valid. the request is valid,
     * if the project and the iteration are not null
     * @return boolean (true if it is valid)
     */
    public boolean isValid() {
        return iteration != null;
    }




    public Iteration getIteration() {
        return iteration;
    }




    /**
     * set all entered attributes to the iteration
     * checks if the startdate is earlier then the enddate
     * checks if the entered timespan has an intersection with an other iteration timespan
     * the iteration will be stored in the database
     * forwarding to the detailspage of the new/updated iteration
     */
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
                projectLocal.merge(project);
            }

            JsfUtil.redirect(new QueryStringBuilder().setUrl(Filename.ITEARTION_DETAILS_WITH_LEADING_SLASH).
                    append(RequestParameter.ID, project.getId()).
                    append(RequestParameter.ITERATION_ID, iteration.getId()).
                    toString());
        }
    }




    /**
     * forwarding to the projectDetails page
     */
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




    /**
     * get all ErrorMessages which are in the FacesContext
     * @return the list of all messages
     */
    public List<FacesMessage> getCurrentMessages() {
        List<FacesMessage> result = new ArrayList<FacesMessage>();
        Iterator<FacesMessage> iter = FacesContext.getCurrentInstance().getMessages();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }




    public String getupdateLink() {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_ITERATION).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.ITERATION_ID, iteration.getId()).
                toString();
    }




    public String getCreateLink() {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_ITERATION).
                append(RequestParameter.ID, project.getId()).
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




    /**
     * calculate the time between the start- and the enddate in days, hours and minutes
     * and saves it in the variables days, hours and minutes
     */
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
    }




}



