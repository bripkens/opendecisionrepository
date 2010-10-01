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
public class ProjectOverviewController {

    @EJB
    private ProjectLocal pl;
    @EJB
    private IterationLocal il;
    private String pid;
    private long id;

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
        boolean auth = false;
        for (ProjectMember pm : getProject().getMembers()) {
            if (pm.getPerson().getId() == AuthenticationUtil.getUserId()) {
                auth = true;
            } else {
                auth = false;
            }
        }
        return auth;
    }

    public Project getProject() {
        return pl.getById(Long.parseLong(pid));
    }

    public Collection<ProjectMember> getProjectMembers() {
        Collection<ProjectMember> copy = new ArrayList<ProjectMember>();

        for (ProjectMember pm : getProject().getMembers()) {
            if (!pm.isRemoved()) {
                copy.add(pm);
            }
        }
        return copy;
    }

    public String getProjectName() {
        return getProject().getName();
    }

    public Collection<Iteration> getIterations() {
        return il.getAllITerationsByProjectId(Long.parseLong(pid));
    }
}
