package nl.rug.search.odr.controller;

import com.icesoft.faces.component.ext.RowSelectorEvent;
import com.icesoft.faces.context.effects.BlindUp;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.EffectQueue;
import com.icesoft.faces.context.effects.Fade;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.util.JsfUtil;
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
                JsfUtil.redirect(RequestParameter.PROJECT_PATH_SHORT.concat(members.get(i).getProject().getName()));
            }
        }
    }




    @PostConstruct
    public void init() {
        members = pl.getAllProjectsFromUser(AuthenticationUtil.getUserId());
    }




    /**
     * @return array of projects data.
     */
    public List<ProjectMember> getProjects() {
        // members = pl.getAllProjectsFromUser(AuthenticationUtil.getUserId());
        return members;
    }




    public boolean isActive() {
        members = pl.getAllProjectsFromUser(AuthenticationUtil.getUserId());
        if (members.isEmpty()) {
            return false;
        }
        return true;
    }




    public void newProjectLink() {
        JsfUtil.redirect(RequestParameter.PROJECT_PATH_SHORT.concat(RequestParameter.CREATE));
    }




    public Effect getEffect() {
        EffectQueue blind = new EffectQueue("effectBlind");
        blind.add(new Fade());
//        blind.add(new BlindDown());
        blind.setFired(false);

        return blind;
    }




    public void changeEffectAction(ActionEvent event) {
        getEffect().setFired(true);
    }
}
