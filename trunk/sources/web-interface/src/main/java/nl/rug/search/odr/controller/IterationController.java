package nl.rug.search.odr.controller;

import com.sun.faces.context.flash.ELFlash;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import nl.rug.search.odr.project.IterationLocal;
import nl.rug.search.odr.entities.Iteration;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.QueryStringBuilder;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.util.ErrorUtil;
import nl.rug.search.odr.util.JsfUtil;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ProjectLocal;

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
            Date currentDate = new Date();
            iteration.setStartDate(currentDate);
            iteration.setEndDate(new Date(currentDate.getTime() + 86400000l));
            startDate = iteration.getStartDate();
            endDate = iteration.getEndDate();
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

        if (getCurrentMessages().isEmpty()) {
            iteration.setName(iterationName);
            iteration.setDescription(iterationDescription);

            if(iteration.getEndDate().before(startDate)){
                iteration.setEndDate(endDate);
            }
            iteration.setStartDate(startDate);
            iteration.setEndDate(endDate);

            //TODO IF THE MEMBER ALSO BE SAVED

            if (isUpdate) {
                il.merge(iteration);
            } else {
                iteration.setDocumentedWhen(new Date());
                iteration.setProjectMember(member);
                project.addIteration(iteration);
                il.persist(iteration);
                System.out.println("erfolgreich persistiert");
                pl.merge(project);
            }

             JsfUtil.redirect(new QueryStringBuilder().setUrl("/iterationDetails.html").
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.ITERATION_ID, iteration.getId()).
                toString());
        }
    }

    public boolean isValidDate(){
        System.out.println("checkt");
        return true;
    }




    public void abortForm() {
        JsfUtil.redirect("/p/"+ project.getName());
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




    public List<FacesMessage> getCurrentMessages() {
        List<FacesMessage> result = new ArrayList<FacesMessage>();
        Iterator<FacesMessage> iter = FacesContext.getCurrentInstance().getMessages();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }
}
