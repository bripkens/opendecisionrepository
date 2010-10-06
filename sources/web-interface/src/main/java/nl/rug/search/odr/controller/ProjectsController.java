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
                    JsfUtil.redirect("/project/" + members.get(i).getProject().getName());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
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
        if(members.isEmpty()){
            return false;
        }
        return true;
    }

    public void newProjectLink() {
        try {
            JsfUtil.redirect("/project/create");
        } catch (IOException ex) {
            Logger.getLogger(ProjectsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public Effect getEffect() {
        EffectQueue blind = new EffectQueue("effectBlind");
        blind.add(new Fade());
//        blind.add(new BlindDown());
        blind.setFired(false);

        return blind;
    }

    public void changeEffectAction(ActionEvent event){
        getEffect().setFired(true);
    }
}