package nl.rug.search.odr.controller;

import java.io.IOException;
import java.util.GregorianCalendar;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import nl.rug.search.odr.project.IterationLocal;
import nl.rug.search.odr.entities.Iteration;

import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.AuthenticationUtil;
import nl.rug.search.odr.ErrorUtil;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.project.ProjectMemberLocal;

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

    private String iterationName = "";

    private String iterationDescription = "";

    private ProjectMember member;
    //

    private Date startDate = null;

    private String startHour = "";

    private String startMinute = "";
    //

    private Date endDate = null;

    private String endHour = "";

    private String endMinute = "";

    private boolean isUpdate = false;

private Object dummy;




    public Object getDummy() {
        return dummy;
    }




    public void setDummy(Object dummy) {
        this.dummy = dummy;
    }




    @PostConstruct
    public void postConstruct() {
        if (!AuthenticationUtil.isAuthtenticated()) {
            ErrorUtil.showNotAuthenticatedError();
            return;
        }

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().
                getRequest();

        // <editor-fold defaultstate="collapsed" desc="get Project Id">
        System.out.println("Project Id ist : " + request.getParameter(RequestParameter.ID));

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
        }

        if (isUpdate) {
            try {
                iterationId = Long.parseLong(str_iterationId);
                getIterationFromDb();
            } catch (NumberFormatException e) {
                ErrorUtil.showInvalidIdError();
                return;
            }
        }
        // </editor-fold>



        if (iteration == null) {
            iteration = new Iteration();
        } else {
            iterationName = iteration.getName();
            iterationDescription = iteration.getDescription();

            startDate = iteration.getStartDate();
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(startDate);
            startHour = String.valueOf(cal.get(Calendar.HOUR));
            startMinute = String.valueOf(cal.get(Calendar.MINUTE));

            endDate = iteration.getEndDate();
            cal.setTime(endDate);
            endHour = String.valueOf(cal.get(Calendar.HOUR));
            endMinute = String.valueOf(cal.get(Calendar.MINUTE));
        }

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
        iteration.setName(iterationName);
        iteration.setDescription(iterationDescription);

        iteration.setStartDate(startDate);
        iteration.setEndDate(endDate);


        //TODO IF THE MEMBER ALSO BE SAVED

        if (isUpdate) {
            il.updateIteration(iteration);
        } else {
            iteration.setDocumentedWhen(new Date());
            iteration.setProjectMember(member);
            project.addIteration(iteration);
            pl.updateProject(project);
        }
    }




    public void abortForm() {
        JsfUtil.redirect("/projects.html");
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




    public String getEndHour() {
        return endHour;
    }




    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }




    public String getEndMinute() {
        return endMinute;
    }




    public void setEndMinute(String endMinute) {
        this.endMinute = endMinute;
    }




    public String getStartHour() {
        return startHour;
    }




    public void setStartHour(String hour) {
        startHour = hour;
    }




    public String getStartMinute() {
        return startMinute;
    }




    public void setStartMinute(String minute) {
        startMinute = minute;
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
}
