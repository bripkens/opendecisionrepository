package nl.rug.search.odr.controller;

import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.AuthenticationUtil;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.decision.VersionLocal;
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
    @EJB
    private VersionLocal vl;
    private long id;
    private Project pr;

    public boolean isValid() {

        if (!AuthenticationUtil.isAuthtenticated()) {
            return false;
        }

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (request.getParameter(RequestParameter.ID) != null) {
            String projectId = request.getParameter(RequestParameter.ID);
            try {
                id = Long.parseLong(projectId);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        getProject();
        if (pr != null && memberIsInProject()) {
            return true;
        }
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
        return "updateProject.html?".concat(RequestParameter.UPDATE).concat("&").concat(RequestParameter.ID).concat("=") + pr.getId();
    }

    public String getDeleteLink() {
        return "deleteProject.html?".concat(RequestParameter.DELETE).concat("&").concat(RequestParameter.ID).concat("=") + pr.getId();
    }

//    public Collection<Iteration> getIterations() {
//        return il.getAllITerationsByProjectId(Long.parseLong(pid));
//    }
}
