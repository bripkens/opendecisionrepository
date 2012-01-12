package nl.rug.search.odr.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import nl.rug.search.odr.Filename;
import nl.rug.search.odr.NavigationBuilder;
import nl.rug.search.odr.QueryStringBuilder;
import nl.rug.search.odr.RequestAnalyser;
import nl.rug.search.odr.RequestAnalyser.RequestAnalyserDto;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.RelationshipType;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.project.RelationshipTypeLocal;
import nl.rug.search.odr.util.JsfUtil;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Stefan
 */
@ManagedBean
@ViewScoped
public class RelationshipTypeTableController {

    @EJB
    private ProjectLocal projectLocal;
    @EJB
    private RelationshipTypeLocal relationshipTypeLocal;
    private List<Item> relationshipTypes;
    private Project project;
    private Item itemToDelete;
    private NavigationBuilder navi;
    private RelationshipType relationshipType;
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
            relationshipTypes = new ArrayList<Item>();
            validRequest = true;

            //get the public types
            for (RelationshipType relationship : relationshipTypeLocal.getPublicTypes()) {
                Item current = new Item(relationship);
                relationshipTypes.add(current);
            }

            //get the project specific types
            for (RelationshipType relationtypes : project.getRelationshipTypes()) {
                Item currentItem = new Item(relationtypes);
                relationshipTypes.add(currentItem);
            }

        } else {
            result.executeErrorAction();
            validRequest = false;
        }
    }

    /**
     * return the list with the newest version of every concern
     * @return
     */
    public Collection<Item> getList() {
        return relationshipTypes;
    }

    public String getShowLink(Item item) {
        return new QueryStringBuilder().setUrl(Filename.ITERATION_DETAILS).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.RELATIONSHIPTYPE_ID, item.relationshipType.getId()).
                toString();
    }

    public String getUpdateLink(Item item) {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_RELATIONSHIPTYPE).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.RELATIONSHIPTYPE_ID, item.relationshipType.getId()).
                toString();
    }

    public String getCreateRelationshipTypeLink() {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_RELATIONSHIPTYPE).
                append(RequestParameter.ID, project.getId()).toString();
    }

    public void showDeleteRelationshipTypeConfirmation(ActionEvent e) {
        Item it = (Item) e.getComponent().getAttributes().get("relationshipTypeItem");
        itemToDelete = it;
        JsfUtil.addJavascriptCall("odr.showIterationDeleteForm();");
    }

    public Item getRelationshipTypeToDelete() {
        return itemToDelete;
    }

    public void setRelationshipTypeToDelete(Item relationshipTypeToDelete) {
        this.itemToDelete = relationshipTypeToDelete;
    }

    public void deleteRelationshipType() {
        relationshipTypes.remove(itemToDelete);
        project.removeRelationshipType(itemToDelete.relationshipType);

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

        private RelationshipType relationshipType;
        private boolean colored = false;

        public Item() {
        }

        public void setRelationshipType(RelationshipType relationshipType) {
            this.relationshipType = relationshipType;
        }

        public RelationshipType getRelationshipType() {
            return relationshipType;
        }

        public boolean isColored() {
            return colored;
        }

        public void setColored(boolean colored) {
            this.colored = colored;
        }

        public Item(RelationshipType relationshipType) {
            this.relationshipType = relationshipType;
        }
    }
}
