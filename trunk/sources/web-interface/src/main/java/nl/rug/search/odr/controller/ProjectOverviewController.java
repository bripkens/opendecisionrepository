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
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.project.IterationLocal;
import nl.rug.search.odr.project.ProjectLocal;

/**
 *
 * @author Stefan
 */
@RequestScoped
@ManagedBean
public class ProjectOverviewController {

    @EJB
    private ProjectLocal pl;
    @EJB
    private IterationLocal il;
    @EJB
    private VersionLocal vl;
    private String pid;
    private long id;
    private Project pr;

    public boolean isValid() {

        if (!AuthenticationUtil.isAuthtenticated()) {
            return false;
        }

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (request.getParameter(RequestParameter.ID) != null) {
            pid = request.getParameter(RequestParameter.ID);
            try {
                id = Long.parseLong(pid);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        if (pl.getById(id) != null && memberIsInProject() != false) {
            return true;
        }
        return false;
    }

    private boolean memberIsInProject() {
        getProject();
        for (ProjectMember pm : pr.getMembers()) {
            if (pm.getPerson().getId().equals(AuthenticationUtil.getUserId())) {
                return true;
            }
        }
        return false;
    }

    public String getDescription() {
        return pr.getDescription();
    }

    public Project getProject() {
        pr = pl.getById(Long.parseLong(pid));
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

//    public Collection<Iteration> getIterations() {
//        return il.getAllITerationsByProjectId(Long.parseLong(pid));
//    }
}
