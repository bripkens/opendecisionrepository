package nl.rug.search.odr.controller;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import nl.rug.search.odr.project.IterationLocal;
import nl.rug.search.odr.entities.Iteration;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.QueryStringBuilder;
import nl.rug.search.odr.RequestAnalyser;
import nl.rug.search.odr.RequestAnalyser.RequestAnalyserDto;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.util.ErrorUtil;
import nl.rug.search.odr.util.JsfUtil;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.project.ProjectLocal;

/**
 *
 * @author Stefan
 */
@ManagedBean
@ViewScoped
public class IterationDetailsController {

    @EJB
    private ProjectLocal pl;

    @EJB
    private IterationLocal il;

    private Project project;

    private long iterationId;

    private Iteration iteration = null;

    private String iterationDescription = "";

    private long days;

    private long hours;

    private long minutes;




    @PostConstruct
    public void postConstruct() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().
                getExternalContext().
                getRequest();

        RequestAnalyser analyser = new RequestAnalyser(request, pl);
        RequestAnalyserDto result = analyser.analyse();

        if (result.isValid()) {
            setUpIterationSpecific(result);
        } else if (project == null) {
            result.executeErrorAction();
        } else if (!pl.isMember(AuthenticationUtil.getUserId(), project.getId())) {
            iteration = null;
        }

    }




    private Iteration getIterationFromDb(long id) {
        iteration = il.getById(id);
        return iteration;
    }




    public Iteration getIteration() {
        return iteration;
    }




    public void submitForm() {
    }




    public void abortForm() {
        JsfUtil.redirect("/p/" + project.getName());
    }




    public String getDescription() {
        return iterationDescription;
    }




    public void setDescription(String desciption) {
        iterationDescription = desciption;
    }

    // </editor-fold>



    private void setUpIterationSpecific(RequestAnalyserDto requestAnalyser) {
        project = requestAnalyser.getProject();

        String iterationIdParameter = requestAnalyser.getRequest().
                getParameter(RequestParameter.ITERATION_ID);

        iterationId = Long.parseLong(iterationIdParameter);

        try {
            iterationId = Long.parseLong(iterationIdParameter);
        } catch (NumberFormatException ex) {
            ErrorUtil.showIterationIdNotRegisteredError();
            return;
        }

        getIterationFromDb(iterationId);
        calculateDuration();
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
    }
}
