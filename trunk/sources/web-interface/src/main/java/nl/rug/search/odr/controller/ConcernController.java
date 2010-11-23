package nl.rug.search.odr.controller;

import com.sun.faces.util.MessageFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.Filename;
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
    private long concernId;
    private Collection<String> autoComplete;
    private boolean isUpdate = false;
    private ProjectMember member;
    private long groupId;
    private long group;
    private Concern newestGroupConcern;
    //attributes
    private String externalId = "";
    private String name = "";
    private String description = "";
    private ArrayList<Item> tags;
    private String concernIdParameter;
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

        RequestAnalyser analyser = new RequestAnalyser(request, projectLocal);
        RequestAnalyserDto result = analyser.analyse();


        if (result.isValid()) {
            autoComplete = new ArrayList<String>();
            tags = new ArrayList<Item>();
            member = result.getMember();
            setUpConcernSpecific(result);
        } else if (project == null) {
            result.executeErrorAction();

        } else if (!projectLocal.isMember(AuthenticationUtil.getUserId(), project.getId())) {
            concern = null;
        }


    }



    /*
     * searches the newest Concern with the group parameter
     */

    private Concern getGroupParameter(RequestAnalyserDto requestAnalyser) {
        String groupIdParameter = requestAnalyser.getRequest().getParameter(RequestParameter.CONCERN_GROUP_ID);

        if (groupIdParameter != null) {
            try {
                groupId = Long.parseLong(groupIdParameter);
                newestGroupConcern = null;
                for (Concern con : project.getConcerns()) {
                    if (newestGroupConcern == null || (con.getGroup().equals(groupId)
                            && con.getCreatedWhen().after(newestGroupConcern.getCreatedWhen()))) {
                        newestGroupConcern = con;
                    }
                }
            } catch (NumberFormatException ex) {
                ErrorUtil.showIterationIdNotRegisteredError();
                return null;
            }
            return newestGroupConcern;
        }
        return null;
    }


    /*
     * returns the specific concern 
     */


    private Concern getConernIdParameter(RequestAnalyserDto requestAnalyser) {
        concernIdParameter = requestAnalyser.getRequest().
                getParameter(RequestParameter.CONCERN_ID);
        Concern temp = new Concern();

        if (concernIdParameter == null) {
            isUpdate = false;
        } else {
            try {
                concernId = Long.parseLong(concernIdParameter);
                isUpdate = true;

                for (Concern con : project.getConcerns()) {
                    if (con.getId().equals(concernId)) {
                        temp = con;
                        break;
                    }
                }
            } catch (NumberFormatException ex) {
                ErrorUtil.showIterationIdNotRegisteredError();
                return null;
            }
            return temp;
        }
        return null;
    }




    private void setUpConcernSpecific(RequestAnalyserDto requestAnalyser) {
        project = requestAnalyser.getProject();

        Concern temp1 = getGroupParameter(requestAnalyser);
        if (temp1 != null) {
            concern = temp1;
        }

        Concern temp = getConernIdParameter(requestAnalyser);
        if (temp != null) {
            concern = temp;
        }

        if (temp1 == null && temp == null) {
            concern = new Concern();
        }


        if (temp != null) {
            this.name = concern.getName();
            this.description = concern.getDescription();
            this.externalId = concern.getExternalId();
            this.group = concern.getGroup();

            for (String s : concern.getTags()) {
                this.tags.add(new Item(s));
            }
        }

        int numberToAdd = 3 - (tags.size() % 3);
        for (int i = 0;
                i < numberToAdd;
                i++) {
            tags.add(new Item(""));
        }
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
                System.out.println(tags.size() + "<- size | tag added -> " + tag.getValue());
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
        System.out.println(concern + " ist concern null?");
        System.out.println(project.getId() + " ist project null?");
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


        for (int i = 0; i
                        < tags.size(); i++) {
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
//        System.out.println("1");
//
//        String externalId = value.toString();
//        System.out.println("2");
//        boolean inUse = false;
//        System.out.println(project + " : project");
//        for (Concern concern : project.getConcerns()) {
//            if (concern.getExternalId() != null && concern.getExternalId().equals(externalId)) {
//                if (!concern.getId().equals(concernId)) {
//                    inUse = true;
//                    break;
//                }
//            }
//        }
//        System.out.println("3");
//        if (inUse) {
//            throw new ValidatorException(MessageFactory.getMessage(
//                    fc,
//                    EXTERNALID_ALREADY_IN_USE,
//                    new Object[]{
//                        MessageFactory.getLabel(fc, uic)
//                    }));
//        }
//        System.out.println("4");
//    }
//
//
//
//
    public void validateName(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
        String name = value.toString();



        boolean inUse = false;


        for (Concern concern : project.getConcerns()) {
            if (concern.getName().equals(name) && !name.isEmpty()) {
                if (!concern.getId().equals(concernId)) {
                    inUse = true;


                    break;


                }
            }
        }
        if (inUse) {
            throw new ValidatorException(MessageFactory.getMessage(
                    fc,
                    NAME_ALREADY_IN_USE,
                    new Object[]{
                        MessageFactory.getLabel(fc, uic)
                    }));


        }
    }

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
