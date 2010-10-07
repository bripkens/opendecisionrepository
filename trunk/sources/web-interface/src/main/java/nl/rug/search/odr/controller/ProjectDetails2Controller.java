package nl.rug.search.odr.controller;

import com.icesoft.faces.component.ext.RowSelectorEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import nl.rug.search.odr.AuthenticationUtil;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ProjectLocal;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ViewScoped
@ManagedBean
public class ProjectDetails2Controller {

    @EJB
    private ProjectLocal pl;
    private Project project;

    private String projectId;

    public void addIteration() {
        System.out.println("+++additeration");
    }

    private boolean loadRequiredData() {
        if (!AuthenticationUtil.isAuthtenticated()) {
            return false;
        } else if (project != null && memberIsInProject()) {
            return true;
        }

        String projectIdParameter = projectId;

        long projectIdLong = -1;

        try {
            projectIdLong = Long.parseLong(projectIdParameter);
        } catch (NumberFormatException e) {
            return false;
        }

        project = pl.getById(projectIdLong);

        if (project != null && memberIsInProject()) {
            return true;
        }
        return false;
    }

    private boolean memberIsInProject() {
        long userId = AuthenticationUtil.getUserId();
        for (ProjectMember pm : project.getMembers()) {
            if (pm.getPerson().getId().equals(userId)) {
                return true;
            }
        }

        return false;
    }

    public Collection<Iteration> getIterations() {
        Collection<Iteration> unmodifiableCollection = project.getIterations();

        if (unmodifiableCollection.isEmpty()) {
            return Collections.emptyList();
        }

        List<Iteration> iterations = new ArrayList(unmodifiableCollection.size());

        for (Iteration it : unmodifiableCollection) {
            iterations.add(it);
        }

        Collections.sort(iterations, new Iteration.EndDateComparator());

        return iterations;
    }

    public void rowIterationSelectionListener(RowSelectorEvent event) {
        System.out.println(event.getRow());
    }

    // <editor-fold defaultstate="collapsed" desc="getter">
    public Project getProject() {
        return project;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        System.out.println("+++setProjectId");

        this.projectId = projectId;

        if (!loadRequiredData()) {
            throw new RuntimeException("Can't find project / your not allowed!");
        }
    }
    // </editor-fold>
}
