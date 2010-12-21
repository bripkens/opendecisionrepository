package nl.rug.search.odr.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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

import nl.rug.search.odr.entities.Concern;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ConcernLocal;
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
public class ConcernTableController1 {

    @EJB
    private ConcernLocal concernLocal;

    @EJB
    private ProjectLocal projectLocal;

    private List<Item> parentConcerns;

    private Project project;

    private Item concernToDelete;

    private boolean validRequest;




    @PostConstruct
    public void postConstruct() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().
                getRequest();

        RequestAnalyser analyser = new RequestAnalyser(request, projectLocal);
        RequestAnalyserDto result = analyser.analyse();

        if (result.isValid()) {

            this.project = result.getProject();

            parentConcerns = new ArrayList<Item>();
            List<Concern> con = new ArrayList<Concern>(project.getConcerns());

            Collections.sort(con, new Concern.GroupDateComparator());

            Item parent = null;

            int i = 0;
            for (Concern concern : con) {
                Item currentItem = new Item(concern);

                if (parent == null || !parent.concern.getGroup().equals(currentItem.concern.getGroup())) {
                    parent = currentItem;
                    parentConcerns.add(currentItem);
                    if (i % 2 == 0) {
                        currentItem.setColored(true);

                    }
                    i++;

                } else if (parent.concern.getGroup().equals(currentItem.concern.getGroup())) {
                    parent.setHasSub(true);
                    parent.addSubItem(currentItem);
                    if (i % 2 == 0) {
                        currentItem.setColored(true);
                    }
                }

            }
            validRequest = true;
        } else {
            result.executeErrorAction();
            validRequest = false;
        }
    }




    /**
     * return the list with the newest version of every concern
     * @return
     */
    public List<Item> getList() {
        Collections.sort(parentConcerns, Collections.reverseOrder(new Item.CreationDateComparator()));
        return parentConcerns;
    }




    public Collection<Item> getSubconcern(Item it) {
        if (it == null) {
            return Collections.EMPTY_LIST;
        }
        return it.getSubItems();
    }




    public String getDate(Item item) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
        return sdf.format(item.getConcer().getCreatedWhen());
    }




    public String getShowLink(Item item) {
        return new QueryStringBuilder().setUrl(Filename.CONCERN_DETAILS).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.CONCERN_ID, item.concern.getId()).
                toString();
    }




    public String getUpdateLink(Item item) {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_CONCERNS).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.CONCERN_ID, item.concern.getId()).
                toString();
    }




    public String getCreateConcernLink() {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_CONCERNS).
                append(RequestParameter.ID, project.getId()).toString();
    }




    public void showDeleteConcernConfirmation(ActionEvent e) {
        Item co = (Item) e.getComponent().getAttributes().get("item");

        concernToDelete = co;

        JsfUtil.addJavascriptCall("odr.showConcernDeleteForm();");
    }




    public Item getConcernToDelete() {
        return concernToDelete;
    }




    public void setConcernToDelete(Item concernToDelete) {
        this.concernToDelete = concernToDelete;
    }




    public void deleteConcern() {
        parentConcerns.remove(concernToDelete);
        project.removeConcern(concernToDelete.concern);

        projectLocal.merge(project);

        JsfUtil.addJavascriptCall("odr.colorrow();");

    }




    public boolean isValid() {
        return validRequest;
    }




    public List<NavigationBuilder.NavigationLink> getNavigationBar() {
        NavigationBuilder navi = new NavigationBuilder();
        navi.setNavigationSite(FacesContext.getCurrentInstance().getViewRoot().getViewId());
        navi.setProject(project);
        return navi.getNavigationBar();
    }




    public static class Item {

        private List<Item> subItems = new ArrayList<Item>();

        private Concern concern;

        private boolean subConcern = false;

        private boolean hasSub = false;

        private boolean colored = false;




        public Item() {
        }




        public boolean isColored() {
            return colored;
        }




        public void setColored(boolean colored) {
            this.colored = colored;
        }




        public Item(Concern concer) {
            this.concern = concer;
        }




        public boolean isHasSub() {
            return hasSub;
        }




        public void setHasSub(boolean hasSub) {
            this.hasSub = hasSub;
        }




        public List<Item> getSubItems() {
            return subItems;
        }




        public void setSubItems(List<Item> subItems) {
            this.subItems = subItems;
        }




        public void addSubItem(Item item) {
            subItems.add(item);
        }




        public boolean isSubConcern() {
            return subConcern;
        }




        public void setSubConcern(boolean subConcern) {
            this.subConcern = subConcern;
        }




        public Concern getConcer() {
            return concern;
        }




        public void setConcer(Concern concer) {
            this.concern = concer;
        }




        public static class NameComparator implements Comparator<Item> {

            @Override
            public int compare(Item o1, Item o2) {
                return o1.concern.getName().compareToIgnoreCase(o2.concern.getName());
            }




        }




        public static class ExternalIdComparator implements Comparator<Item> {

            @Override
            public int compare(Item o1, Item o2) {
                return o1.concern.getExternalId().compareTo(o2.concern.getExternalId());
            }




        }




        public static class CreationDateComparator implements Comparator<Item> {

            @Override
            public int compare(Item o1, Item o2) {
                return o1.concern.getCreatedWhen().compareTo(o2.concern.getCreatedWhen());
            }




        }




    }




}



