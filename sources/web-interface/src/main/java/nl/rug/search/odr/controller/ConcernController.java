package nl.rug.search.odr.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.Action;
import nl.rug.search.odr.Filename;

import nl.rug.search.odr.NavigationBuilder;
import nl.rug.search.odr.QueryStringBuilder;
import nl.rug.search.odr.RequestAnalyser;
import nl.rug.search.odr.RequestAnalyser.RequestAnalyserDto;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.util.ErrorUtil;
import nl.rug.search.odr.util.JsfUtil;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.entities.Concern;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ConcernLocal;
import nl.rug.search.odr.project.ProjectLocal;

/**
 *
 * @author Stefan
 */
@ManagedBean
@ViewScoped
public class ConcernController {

    @EJB
    private ProjectLocal projectLocal;

    @EJB
    private ConcernLocal concernLocal;

    private Project project;

    private Concern concern;

    private Collection<String> autoComplete;

    private ProjectMember member;

    // <editor-fold defaultstate="collapsed" desc="concern attributes">
    private String externalId = "";

    private String name = "";

    private String description = "";

    private Long group;

    private ArrayList<Item> tags;
    // </editor-fold>

    private boolean isUpdate = false;

    private NavigationBuilder navi;

    private String url;
    //final Strings

    public static final String EXTERNALID_ALREADY_IN_USE =
            "nl.rug.search.odr.validator.ConcernValidator.EXTERNALIDALREADYINUSE";

    public static final String NAME_ALREADY_IN_USE =
            "nl.rug.search.odr.validator.ConcernValidator.NAMEALREADYINUSE";

    public static final String TAG_ALREADY_IN_USE =
            "nl.rug.search.odr.validator.ConcernValidator.TAGALREADYINUSE";




    @PostConstruct
    public void postConstruct() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().
                getExternalContext().
                getRequest();

        navi = new NavigationBuilder();
        getRequestURL();
        navi.setNavigationSite(this.url);

        RequestAnalyser analyser = new RequestAnalyser(request, projectLocal);
        RequestAnalyserDto result = analyser.analyse();


        if (result.isValid()) {
            this.autoComplete = new ArrayList<String>();
            this.tags = new ArrayList<Item>();
            this.member = result.getMember();
            setUpConcernSpecific(result);
        } else if (this.project == null) {
            result.executeErrorAction();

        } else if (!this.projectLocal.isMember(AuthenticationUtil.getUserId(), project.getId())) {
            this.concern = null;
        }


    }




    private void setUpConcernSpecific(RequestAnalyserDto requestAnalyser) {
        this.project = requestAnalyser.getProject();
        this.navi.setProject(this.project);

        String concernIdParameter = requestAnalyser.getRequest().
                getParameter(RequestParameter.CONCERN_ID);

        String concernGroupIdParameter = requestAnalyser.getRequest().
                getParameter(RequestParameter.CONCERN_GROUP_ID);

        if (concernIdParameter != null) {
            this.concern = getConernIdParameter(concernIdParameter);
            this.isUpdate = true;
        } else if (concernGroupIdParameter != null) {
            this.concern = getGroupParameter(concernGroupIdParameter);
            this.isUpdate = true;
        } else {
            this.concern = new Concern();
            this.isUpdate = false;
            this.navi.setOption(Action.CREATE);

        }

        if (this.concern == null) {
            ErrorUtil.showInvalidIdError();
            return;
        }


        if (this.isUpdate) {
            this.navi.setConcern(concern);
            this.navi.setOption(Action.EDIT);
            this.name = concern.getName();
            this.description = concern.getDescription();
            this.externalId = concern.getExternalId();
            this.group = concern.getGroup();

            for (String s : this.concern.getTags()) {
                this.tags.add(new Item(s));
            }
        }

        int numberToAdd = 3 - (this.tags.size() % 3);
        for (int i = 0;
                i < numberToAdd;
                i++) {
            this.tags.add(new Item(""));
        }
    }




    public void getRequestURL() {
        this.url = FacesContext.getCurrentInstance().getViewRoot().getViewId();
    }




    public List<NavigationBuilder.NavigationLink> getNavigationBar() {
        return navi.getNavigationBar();
    }



    /*
     * searches the newest Concern with the group parameter
     */

    private Concern getGroupParameter(String groupIdParameter) {
        long groupId = -1l;

        try {
            groupId = Long.parseLong(groupIdParameter);
        } catch (NumberFormatException ex) {
            ErrorUtil.showIterationIdNotRegisteredError();
            return null;
        }

        Concern newestGroupConcern = new Concern();
        newestGroupConcern.setCreatedWhen(new Date(0L));
        for (Concern con : project.getConcerns()) {
            if (con.getGroup().equals(groupId) && con.getCreatedWhen().after(newestGroupConcern.getCreatedWhen())) {
                newestGroupConcern = con;
            }
        }

        return newestGroupConcern;
    }


    /*
     * returns the specific concern
     */


    private Concern getConernIdParameter(String concernIdParameter) {


        long concernId = -1;


        try {
            concernId = Long.parseLong(concernIdParameter);
        } catch (NumberFormatException ex) {
            ErrorUtil.showIterationIdNotRegisteredError();
            return null;
        }

        Concern temp = null;
        for (Concern con : project.getConcerns()) {
            if (con.getId().equals(concernId)) {
                temp = con;
                break;
            }
        }

        return temp;
    }




    public Concern getConcern() {
        return concern;
    }




    public boolean isValid() {
        return concern != null;
    }




    public void submitForm() {
        concern = new Concern();
        concern.setExternalId(externalId);
        concern.setName(name);
        concern.setDescription(description);

        HashSet set = new HashSet();

        for (Item item : tags) {
            if (!item.getValue().isEmpty()) {
                set.add(item);
            }
        }

        int counter = 0;
        for (Item item : tags) {
            if (!item.getValue().isEmpty()) {
                counter++;
            }
        }

        if (set.size() != counter) {
            FacesContext.getCurrentInstance().addMessage("manageConcern",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    JsfUtil.evaluateExpressionGet("#{form['concern.error.doubleEntrie']}", String.class),
                    null));
            return;
        }

        concern.removeAllTags();


        for (Item tag : tags) {
            if (!tag.getValue().isEmpty()) {
                concern.addTag(tag.getValue());
            }
        }
        if (isUpdate) {
            concern.setGroup(group);
        }
        concern.setCreatedWhen(new Date());
        concern.setInitiator(member);
        project.addConcern(concern);
        concernLocal.persist(concern);

        if (!isUpdate) {
            concern.setGroup(concern.getId());
        }
        projectLocal.merge(project);

        JsfUtil.redirect(new QueryStringBuilder().setUrl(Filename.CONCERN_DETAILS_WITH_LEADING_SLASH).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.CONCERN_ID, concern.getId()).
                toString());
    }




    public void abortForm() {
        JsfUtil.redirect(RequestParameter.PROJECT_PATH_SHORT + project.getName());
    }




    public String getupdateLink() {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_CONCERNS).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.CONCERN_ID, concern.getId()).
                toString();
    }




    public Collection<Item> getAllTags() {
        return tags;
    }




    public void tagValueChanged(ValueChangeEvent e) {
        if (e.getNewValue().equals(e.getOldValue())) {
            return;
        }

        this.autoComplete.clear();

        this.autoComplete = concernLocal.getPossibleStrings(e.getNewValue().toString());


        int counter = 0;


        for (int i = 0; i < tags.size(); i++) {
            if (tags.get(i).getValue().isEmpty()) {
                counter++;
            }
        }
        if (counter > 1) {
            return;


        } else if (counter == 0 || counter == 1 && e.getOldValue().toString().isEmpty()) {
            tags.add(new Item(""));
            tags.add(new Item(""));
            tags.add(new Item(""));
        }

    }




    public boolean getReadOnly() {
        if (isUpdate) {
            return true;
        }
        return false;


    }



// <editor-fold defaultstate="collapsed" desc="getter and setter">

    public String getDescription() {
        return description;


    }




    public void setDescription(String description) {
        this.description = description;


    }




    public String getExternalId() {
        return externalId;


    }




    public void setExternalId(String externalId) {
        this.externalId = externalId;


    }




    public String getName() {
        return name;


    }




    public void setName(String name) {
        this.name = name;


    }

    // </editor-fold>



// <editor-fold defaultstate="collapsed" desc="validators">
//    public void validateId(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
//
//        if (value == null) {
//            return;
//        }
//
//        String externalId = value.toString();
//        boolean inUse = false;
//        for (Concern concern : project.getConcerns()) {
//            if (concern.getExternalId() != null && concern.getExternalId().equals(externalId)) {
//                if (!concern.getId().equals(concernId)) {
//                    inUse = true;
//                    break;
//                }
//            }
//        }
//        if (inUse) {
//            throw new ValidatorException(MessageFactory.getMessage(
//                    fc,
//                    EXTERNALID_ALREADY_IN_USE,
//                    new Object[]{
//                        MessageFactory.getLabel(fc, uic)
//                    }));
//        }
//    }
//
//
//
//
//    public void validateName(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
//        String name = value.toString();
//
//
//
//        boolean inUse = false;
//
//
//        for (Concern concern : project.getConcerns()) {
//            if (concern.getName().equals(name) && !name.isEmpty()) {
//                if (!concern.getId().equals(concernId)) {
//                    inUse = true;
//
//
//                    break;
//
//
//                }
//            }
//        }
//        if (inUse) {
//            throw new ValidatorException(MessageFactory.getMessage(
//                    fc,
//                    NAME_ALREADY_IN_USE,
//                    new Object[]{
//                        MessageFactory.getLabel(fc, uic)
//                    }));
//
//
//        }
//    }
// </editor-fold>
    public Collection<SelectItem> getTagPossibilities() {
        Collection<SelectItem> items = new ArrayList<SelectItem>();


        for (String s : this.autoComplete) {
            if (!tags.contains(new Item(s))) {
                items.add(new SelectItem(s, s));
            }
        }
        return items;











    }




    public class Item {

        private String value;




        public Item(String value) {
            this.value = value;
        }




        public void setValue(String value) {
            this.value = value;
        }




        public String getValue() {
            return value;
        }




        @Override
        public final boolean equals(final Object other) {
            if (!(other instanceof Item)) {
                return false;
            }
            if (this.value.equals(((Item) other).value)) {
                return true;
            } else {
                return false;
            }
        }




        @Override
        public int hashCode() {
            int hash = 5;
            hash = 23 * hash + (this.value != null ? this.value.hashCode() : 0);
            return hash;
        }




    }




}



