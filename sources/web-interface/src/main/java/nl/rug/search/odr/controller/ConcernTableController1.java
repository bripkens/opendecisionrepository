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
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.Filename;
import nl.rug.search.odr.QueryStringBuilder;
import nl.rug.search.odr.RequestParameter;

import nl.rug.search.odr.entities.Concern;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ConcernLocal;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.util.ErrorUtil;

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




    @PostConstruct
    public void getConcernsFromDb() {

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

        if (project == null) {
            ErrorUtil.showIdNotRegisteredError();
            return;
        } else if (project != null && !memberIsInProject()) {
            ErrorUtil.showNoMemberError();
            return;
        }

        parentConcerns = new ArrayList<Item>();
        List<Concern> con = new ArrayList<Concern>();
        con = concernLocal.getAll();

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
    public List<Item> getList() {
        return parentConcerns;
    }




    public Collection<Item> getSubconcern(Item it) {
        if (it == null) {
            return Collections.EMPTY_LIST;
        }
//        if (it.hasSub) {
//            System.out.println("hat subitems :" + it.getSubItems().size());
//        }
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




    public static class Item {

        private boolean selected;

        private List<Item> subItems = new ArrayList<Item>();

        private Concern concern;

        private boolean subConcern = false;

        private boolean hasSub = false;

        private boolean colored = false;




        public boolean isColored() {
            return colored;
        }




        public void setColored(boolean colored) {
            this.colored = colored;
        }




        public Item() {
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
            System.out.println("added nen subItem");
            subItems.add(item);
        }




        public boolean isSelected() {
            return selected;
        }




        public void setSelected(boolean selected) {
            if (this.selected == true) {
                this.selected = false;
            } else {
                this.selected = selected;
            }
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




    }




}



