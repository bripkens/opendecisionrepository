package nl.rug.search.odr.controller;

import com.icesoft.faces.component.ext.RowSelectorEvent;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import nl.rug.search.odr.AuthenticationUtil;
import nl.rug.search.odr.JsfUtil;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ProjectLocal;

@ManagedBean
@RequestScoped
public class ProjectsController {

    @EJB
    private ProjectLocal pl;
    private List<ProjectMember> members;

    public void rowSelectionListener(RowSelectorEvent event) {
        for (int i = 0; i < members.size(); i++) {
            if (i == event.getRow()) {
                try {
                    JsfUtil.redirect("/manageProject.html?" + RequestParameter.ID + "=" + members.get(i).getProject().getId());
                } catch (IOException ex) {
                    Logger.getLogger(ProjectsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @PostConstruct
    public void init() {
        members = pl.getAllProjectsFromUser(1);
    }

    /**
     * @return array of projects data.
     */
    public List<ProjectMember> getProjects() {
        // members = pl.getAllProjectsFromUser(AuthenticationUtil.getUserId());
        return members;
    }

    public boolean isActive() {
        members = pl.getAllProjectsFromUser(1);
        if(members.isEmpty()){
            return false;
        }
        return true;
    }

    public void newProjectLink() {
        try {
            JsfUtil.redirect("/manageProject.html?create");
        } catch (IOException ex) {
            Logger.getLogger(ProjectsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Inventory Item subclass stores data about a cars inventory data.  Properties
     * such a stock, model, description, odometer and price are stored.
     */
    public class project {
        // slock number

        String projectName;
        String stakeholderRole;
        boolean selected;

        public project(String name, String role) {
            this.projectName = name;
            this.stakeholderRole = role;
        }

        public String getProjectName() {
            return projectName;
        }

        public String getStakeholderRole() {
            return stakeholderRole;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
