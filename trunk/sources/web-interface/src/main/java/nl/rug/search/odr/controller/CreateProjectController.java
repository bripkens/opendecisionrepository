
package nl.rug.search.odr.controller;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import nl.rug.search.odr.ProjectLocal;
import nl.rug.search.odr.entities.Project;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@RequestScoped
public class CreateProjectController {

    @EJB
    private ProjectLocal pl;

    private String name;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void submit() {
        Project p = new Project();
        p.setName(name);
        p.setDescription(description);
        
        pl.createProject(p);

        reset();
    }

    public void reset() {
        name = null;
        description = null;
    }

    public void reset(ActionEvent e) {
        reset();
    }
}
