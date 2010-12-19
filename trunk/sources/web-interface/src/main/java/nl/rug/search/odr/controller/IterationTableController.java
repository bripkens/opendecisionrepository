package nl.rug.search.odr.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.Filename;
import nl.rug.search.odr.NavigationBuilder;
import nl.rug.search.odr.QueryStringBuilder;
import nl.rug.search.odr.RequestParameter;

import nl.rug.search.odr.entities.Concern;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ConcernLocal;
import nl.rug.search.odr.project.IterationLocal;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.util.ErrorUtil;
import nl.rug.search.odr.util.JsfUtil;

/**
 *
 * @author Stefan
 */
@ManagedBean
@ViewScoped
public class IterationTableController {

    @EJB
    private IterationLocal iterationLocal;

    @EJB
    private ProjectLocal projectLocal;

    private List<Item> iterations;

    private Project project;

    private Item iterationToDelete;

    private NavigationBuilder navi;




    @PostConstruct
    public void getIterationsFromDb() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().
                getRequest();

        // <editor-fold defaultstate="collapsed" desc="get Project Id">

        long projectId = 0l;
        if (request.getParameter(RequestParameter.ID) != null) {
            String str_projectId = request.getParameter(RequestParameter.ID);

            try {
                projectId = Long.parseLong(str_projectId);
            } catch (NumberFormatException e) {
                ErrorUtil.showInvalidIdError();
                return;
            }
        }

        project = projectLocal.getById(projectId);
        navi = new NavigationBuilder();

        if (project == null) {
            ErrorUtil.showIdNotRegisteredError();
            return;
        } else if (project != null && !memberIsInProject()) {
            ErrorUtil.showNoMemberError();
            return;
        }

        iterations = new ArrayList<Item>();

        int i = 0;
        for (Iteration iteration : project.getIterations()) {
            Item currentItem = new Item(iteration);
            iterations.add(currentItem);
        }
    }




    public String getDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
        return sdf.format(date);
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




    /**
     * return the list with the newest version of every concern
     * @return
     */
    public Collection<Item> getList() {
        Collections.sort(iterations, new Item.CreationDateComparator());
        return iterations;
    }





    public String getShowLink(Item item) {
        return new QueryStringBuilder().setUrl(Filename.ITERATION_DETAILS).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.ITERATION_ID, item.iteration.getId()).
                toString();
    }




    public String getUpdateLink(Item item) {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_ITERATION).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.ITERATION_ID, item.iteration.getId()).
                toString();
    }




    public String getCreateIterationLink() {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_ITERATION).
                append(RequestParameter.ID, project.getId()).toString();
    }




    public void showDeleteIterationConfirmation(ActionEvent e) {
        Item it = (Item) e.getComponent().getAttributes().get("iterationItem");

        iterationToDelete = it;

        JsfUtil.addJavascriptCall("odr.showIterationDeleteForm();");
    }




    public Item getIterationToDelete() {
        return iterationToDelete;
    }




    public void setIterationToDelete(Item iterationToDelete) {
        this.iterationToDelete = iterationToDelete;
    }




    public void deleteIteration() {
        project.removeIteration(iterationToDelete.iteration);
        iterations.remove(iterationToDelete);

        projectLocal.merge(project);

        JsfUtil.addJavascriptCall("odr.popup.hide();");

    }




    public boolean isValid() {
        return project != null;
    }




    public List<NavigationBuilder.NavigationLink> getNavigationBar() {
        navi.setNavigationSite(FacesContext.getCurrentInstance().getViewRoot().getViewId());
        navi.setProject(project);

        return navi.getNavigationBar();
    }




    public static class Item {

        private Iteration iteration;

        private boolean colored = false;




        public Item() {
        }




        public void setIteration(Iteration iteration) {
            this.iteration = iteration;
        }




        public Iteration getIteration() {
            return iteration;
        }




        public boolean isColored() {
            return colored;
        }




        public void setColored(boolean colored) {
            this.colored = colored;
        }




        public Item(Iteration iteration) {
            this.iteration = iteration;
        }




        public static class CreationDateComparator implements Comparator<Item> {

            @Override
            public int compare(Item o1, Item o2) {
                return o1.iteration.getDocumentedWhen().compareTo(o2.iteration.getDocumentedWhen());
            }




        }




    }




}



