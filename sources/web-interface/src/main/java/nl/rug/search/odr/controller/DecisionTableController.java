package nl.rug.search.odr.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
import nl.rug.search.odr.decision.DecisionLocal;
import nl.rug.search.odr.decision.VersionLocal;

import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Version;
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
public class DecisionTableController {

    @EJB
    private VersionLocal versionLocal;

    @EJB
    private DecisionLocal decisionLocal;

    @EJB
    private ProjectLocal projectLocal;

    private List<Item> allDecisions;

    private Project project;

    private Version versionToDelete;

    private Item itemToDelete;

    private boolean validRequest;




    @PostConstruct
    public void getDecisionsFromDb() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().
                getRequest();

        RequestAnalyser analyser = new RequestAnalyser(request, projectLocal);
        RequestAnalyserDto result = analyser.analyse();

        if (result.isValid()) {
            this.project = result.getProject();
            allDecisions = new ArrayList<Item>();

            List<Decision> temp = new ArrayList<Decision>(project.getDecisions());
            Collections.sort(temp, Collections.reverseOrder(new Decision.DocumentedWhenComparator()));

            for (Decision decision : temp) {
                if (!decision.isRemoved()) {
                    Item currentItem = new Item(decision);
                    currentItem.setCurrentVersion(decision.getCurrentVersion());
                    currentItem.setSubVersions();
                    allDecisions.add(currentItem);
                }
            }
            validRequest = true;
        } else {
            result.executeErrorAction();
            validRequest = false;
        }
    }


    public List<Item> getList() {
        return allDecisions;
    }




    public <T> List<T> makeModifiable(Collection<T> original) {
        return new ArrayList<T>(original);
    }




    public String getShowLink(Version version) {
        return new QueryStringBuilder().setUrl(Filename.DECISION_DETAILS).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.VERSION_ID, version.getId()).
                append(RequestParameter.DECISION_ID, version.getDecision().getId()).
                toString();
    }




    public String getUpdateLink(Version version) {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_DECISION).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.VERSION_ID, version.getId()).
                append(RequestParameter.DECISION_ID, version.getDecision().getId()).
                toString();
    }




    public String getCreateDecisionLink() {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_DECISION).
                append(RequestParameter.ID, project.getId()).
                toString().concat("&").concat(RequestParameter.CREATE);
    }




    public void showDeleteVersionConfirmation(ActionEvent e) {
        Version vo = (Version) e.getComponent().getAttributes().get("version");
        versionToDelete = vo;

        Item it = (Item) e.getComponent().getAttributes().get("item");
        itemToDelete = it;

        JsfUtil.addJavascriptCall("odr.showVersionDeleteForm();");
    }




    public void setItemToDelete(Item itemToDelete) {
        this.itemToDelete = itemToDelete;
    }




    public Item getItemToDelete() {
        return itemToDelete;
    }




    public Version getVersionToDelete() {
        return versionToDelete;
    }




    public void setVersionToDelete(Version versionToDelete) {
        this.versionToDelete = versionToDelete;
    }




    public void deleteVersion() {
        versionToDelete.setRemoved(true);

        if (itemToDelete.getDecision().isRemoved()) {
            allDecisions.remove(itemToDelete);
        }
        projectLocal.merge(project);
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

        private static final Integer DECISON_LENGTH = JsfUtil.evaluateExpressionGet("#{form['decisionTable.decision.name.length']}", Integer.class);

        private static final Integer INITIATOR_LENGTH = JsfUtil.evaluateExpressionGet("#{form['decisionTable.initiator.name.length']}", Integer.class);

        private static final String SEPERATOR = JsfUtil.evaluateExpressionGet("#{form['decisionTable.initiator.seperator']}", String.class);

        private static final String ENDING = JsfUtil.evaluateExpressionGet("#{form['decisionTable.name.tolong']}", String.class);

        private List<Version> subVersions = new ArrayList<Version>();

        private Decision decision;

        private Version current;




        public Item() {
        }




        public String getInitiators(Version v) {
            String initiators = "";
            for (ProjectMember member : v.getInitiators()) {
                initiators = initiators.concat(member.getPerson().getName()).concat(SEPERATOR + " ");
            }
            if (initiators.length() > INITIATOR_LENGTH) {
                initiators = initiators.substring(0, INITIATOR_LENGTH).concat(ENDING);
            } else {
                initiators = initiators.substring(0, initiators.length() - 2);
            }
            return initiators;
        }




        public void removeSubVersion(Version ver) {
            for (Version v : subVersions) {
                if (v.equals(ver)) {
                    ver.setRemoved(true);
                    subVersions.remove(v);
                }
            }
        }




        public String getAllInitiators(Version v) {
            String ini = "";
            for (ProjectMember member : v.getInitiators()) {
                ini = ini.concat(member.getPerson().getName()).concat(SEPERATOR + " ");
            }
            return ini.substring(0, ini.length() - 2);
        }




        public String getDecisionName() {
            String name;
            if (decision.getName().length() > DECISON_LENGTH) {
                name = decision.getName().substring(0, DECISON_LENGTH);
                return name.concat(ENDING);
            }
            return decision.getName();
        }




        public Version getCurrentVersion() {
            return current;
        }




        public void setCurrentVersion(Version version) {
            this.current = version;
        }




        public Collection<Version> getSubVersions() {
            Iterator<Version> versionIterator = subVersions.iterator();
            while (versionIterator.hasNext()) {
                if (versionIterator.next().isRemoved()) {
                    versionIterator.remove();
                }
            }
            Collections.sort(subVersions, new Version.DecidedWhenComparator());
            return subVersions;
        }




        public Item(Decision decision) {
            this.decision = decision;

        }




        public void setSubVersions() {
            Collection<Version> temp = new ArrayList<Version>(decision.getVersions());
            for (Version v : temp) {
                if (!v.equals(current)) {
                    subVersions.add(v);
                }
            }
        }




        public Decision getDecision() {
            return this.decision;
        }




        public void setDecision(Decision decision) {
            this.decision = decision;
        }




        public static class NameComparator implements Comparator<Item> {

            @Override
            public int compare(Item o1, Item o2) {
                return o1.decision.getName().compareToIgnoreCase(o2.decision.getName());
            }




        }




    }




}



