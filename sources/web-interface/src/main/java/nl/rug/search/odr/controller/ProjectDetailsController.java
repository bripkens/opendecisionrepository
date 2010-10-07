package nl.rug.search.odr.controller;

import com.icesoft.faces.component.ext.RowSelectorEvent;
import com.icesoft.faces.context.effects.JavascriptContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.AuthenticationUtil;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.IterationLocal;
import nl.rug.search.odr.project.ProjectLocal;

/**
 *
 * @author Stefan
 */
@RequestScoped
@ManagedBean
public class ProjectDetailsController {

    @EJB
    private ProjectLocal pl;
    @EJB
    private IterationLocal il;
    private long id;
    private Project pr;
    private String iterationName;
    private String iterationDescription;
    private String projectId;
    private String iterationToDeleteId;
    private String iterationToDeleteName;

    public boolean isRedirectIfInvalidRequest() {
        if (!isValid()) {
            try {
                JsfUtil.redirect("/error.html");
                return false;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        return true;
    }

    public void iterationAddCanceled(ActionEvent e) {
        iterationName = iterationDescription = null;
    }

    public boolean isValid() {
        System.out.println("### 1");

        if (!AuthenticationUtil.isAuthtenticated()) {
            System.out.println("### 2");
            return false;
        } else if (pr != null) {
            System.out.println("### 3");
            return true;
        }

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();


        if (request.getParameter(RequestParameter.ID) != null) {
            System.out.println("### 4");
            projectId = request.getParameter(RequestParameter.ID);
        }

        try {
            System.out.println("### 5");
            id = Long.parseLong(projectId);
        } catch (NumberFormatException e) {
            System.out.println("### 6");
            return false;
        }

        System.out.println("### 7");
        getProject();
        if (pr != null && memberIsInProject()) {
            System.out.println("### 8");
            return true;
        }
        System.out.println("### 9");
        return false;
    }

    private boolean memberIsInProject() {
        long userId = AuthenticationUtil.getUserId();
        for (ProjectMember pm : pr.getMembers()) {
            if (pm.getPerson().getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    public void rowMemberSelectionListener(RowSelectorEvent event) {
        // TODO: empty until now, just to get the css tag right
    }

    public void rowIterationSelectionListener(RowSelectorEvent event) {
        System.out.println(event.getRow());
    }


    public void addIteration() {
        if (!isValid()) {
            try {
                JsfUtil.redirect("/error.html");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        Iteration i = new Iteration();
        i.setName(iterationName);
        i.setDescription(iterationDescription);
        i.setStartDate(new Date());

        il.addIteration(pr, i);

        iterationName = iterationDescription = null;

        JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "hideIterationAddForm();");
    }

    public void deleteIteration() {
        JavascriptContext.addJavascriptCall(FacesContext.getCurrentInstance(), "hideModalPopup();");
    }

    public String getDescription() {
        return pr.getDescription();
    }

    public Project getProject() {
        pr = pl.getById(id);
        return pr;
    }

    public Collection<ProjectMember> getProjectMembers() {
        Collection<ProjectMember> copy = new ArrayList<ProjectMember>();

        for (ProjectMember pm : pr.getMembers()) {
            if (!pm.isRemoved()) {
                copy.add(pm);
            }
        }
        return copy;
    }

    public String getProjectName() {
        return pr.getName();
    }

    public String getUpdateLink() {
        return "project/".concat(pr.getName()).concat("/update");
    }

    public String getDeleteLink() {
        return "project/".concat(pr.getName()).concat("/delete");
    }

    public Collection<Iteration> getIterations() {
        System.out.println("### 8");
        
        if (pr == null) {
            System.out.println("### 9");
            return null;
        }

        Collection<Iteration> unmodifiableCollection = pr.getIterations();

        if (unmodifiableCollection.isEmpty()) {
            System.out.println("### 10");
            return Collections.emptyList();
        }

        List<Iteration> iterations = new ArrayList(unmodifiableCollection.size());

        for (Iteration it : unmodifiableCollection) {
            iterations.add(it);
        }

        Collections.sort(iterations, new Iteration.EndDateComparator());

        System.out.println("### 11");

        return iterations;
    }

    public String getIterationDescription() {
        return iterationDescription;
    }

    public void setIterationDescription(String iterationDescription) {
        this.iterationDescription = iterationDescription;
    }

    public String getIterationName() {
        return iterationName;
    }

    public void setIterationName(String iterationName) {
        this.iterationName = iterationName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getIterationToDeleteId() {
        return iterationToDeleteId;
    }

    public void setIterationToDeleteId(String iterationToDeleteId) {
        this.iterationToDeleteId = iterationToDeleteId;
    }

    public String getIterationToDeleteName() {
        return iterationToDeleteName;
    }

    public void setIterationToDeleteName(String iterationToDeleteName) {
        this.iterationToDeleteName = iterationToDeleteName;
    }
}
