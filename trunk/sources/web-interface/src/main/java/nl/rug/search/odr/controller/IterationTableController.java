package nl.rug.search.odr.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
import nl.rug.search.odr.RequestAnalyser;
import nl.rug.search.odr.RequestAnalyser.RequestAnalyserDto;
import nl.rug.search.odr.RequestParameter;

import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.util.JsfUtil;

/**
 *
 * @author Stefan
 */
@ManagedBean
@ViewScoped
public class IterationTableController {

    
    @EJB
    private ProjectLocal projectLocal;

    private List<Item> iterations;

    private Project project;

    private Item itemToDelete;

    private NavigationBuilder navi;

    private Iteration iterationToDelete;

    private boolean validRequest;




    @PostConstruct
    public void postConstruct() {
        navi = new NavigationBuilder();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().
                getRequest();

        RequestAnalyser analyser = new RequestAnalyser(request, projectLocal);
        RequestAnalyserDto result = analyser.analyse();

        if (result.isValid()) {
            this.project = result.getProject();
            iterations = new ArrayList<Item>();
            validRequest = true;

            for (Iteration iteration : project.getIterations()) {
                Item currentItem = new Item(iteration);
                iterations.add(currentItem);
            }

        } else {
            result.executeErrorAction();
            validRequest = false;
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
        validRequest = false;
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
        Item it = (Item) e.getComponent().getAttributes().get("iterationitem");
        Iteration iter = (Iteration) e.getComponent().getAttributes().get("iteration");


        itemToDelete = it;
        iterationToDelete = iter;

        JsfUtil.addJavascriptCall("odr.showIterationDeleteForm();");
    }




    public Item getIterationToDelete() {
        return itemToDelete;
    }




    public void setIterationToDelete(Item iterationToDelete) {
        this.itemToDelete = iterationToDelete;
    }




    public void deleteIteration() {
        iterations.remove(itemToDelete);
        project.removeIteration(iterationToDelete);

        projectLocal.merge(project);

        JsfUtil.addJavascriptCall("odr.colorrow();");
    }




    public boolean isValid() {
        return validRequest;
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



