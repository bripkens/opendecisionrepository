package nl.rug.search.odr.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nl.rug.search.odr.RequestAnalyser;
import nl.rug.search.odr.RequestAnalyser.RequestAnalyserDto;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.StringValidator;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.RelationshipType;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.project.RelationshipTypeLocal;
import nl.rug.search.odr.util.ErrorUtil;
import nl.rug.search.odr.util.JsfUtil;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;

import com.sun.faces.util.MessageFactory;

/**
 *
 * @author Stefan
 */
@ManagedBean
@ViewScoped
public class RelationshipTypeController {

    @EJB
    private ProjectLocal projectLocal;
    @EJB
    private RelationshipTypeLocal relationshipTypeLocal;
    private Project project;
    private RelationshipType relationshipType = null;
    private boolean validRequest;
    private String originalRelationshipTypeName;
    public static final String USEDRELATIONSHIPTYPENAME_ID =
            "nl.rug.search.odr.controller.RElationshiptypeController.DUPLICATENAME";

    @PostConstruct
    public void postConstruct() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().
                getExternalContext().
                getRequest();


        RequestAnalyser analyser = new RequestAnalyser(request, projectLocal);
        RequestAnalyserDto result = analyser.analyse();


        if (result.isValid()) {
            project = result.getProject();
            if (request.getParameter(RequestParameter.RELATIONSHIPTYPE_ID) != null) {
                long relationshipTypeId = -1l;

                try {
                    relationshipTypeId = Long.parseLong(request.getParameter(RequestParameter.RELATIONSHIPTYPE_ID));
                } catch (NumberFormatException e) {
                    ErrorUtil.showInvalidIdError();
                    validRequest = false;
                    return;
                }
                relationshipType = relationshipTypeLocal.getById(relationshipTypeId);
                originalRelationshipTypeName = relationshipType.getName();
                if (relationshipType == null || !project.getRelationshipTypes().contains(relationshipType)) {
                    ErrorUtil.showInvalidIdError();
                    validRequest = false;
                    return;
                }
            } else {
                //create new relationshipType
                relationshipType = new RelationshipType();
                relationshipType.setCommon(false);
            }
        }
        validRequest = true;
    }

    /**
     * adds the specific attributes to the navigationbuilder and returns a list with all information that
     * are neccessary to create the breadcrumbtrail
     * @return the list of navigationlink for the breadcrumbtrail
     */
//    public List<NavigationBuilder.NavigationLink> getNavigationBar() {
//        if (!validRequest) {
//            return Collections.EMPTY_LIST;
//        }
//        navi.setNavigationSite(FacesContext.getCurrentInstance().getViewRoot().getViewId());
//        navi.setProject(project);
//        navi.setIteration(iteration);
//        if (isUpdate) {
//            navi.setOption(Action.EDIT);
//        } else {
//            navi.setOption(Action.CREATE);
//        }
//        return navi.getNavigationBar();
//    }
    public boolean isValid() {
        return validRequest;
    }

    public long getProjectId() {
        return project.getId();
    }

    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    /**
     * forwarding to the projectDetails page
     */
    public void abortForm() {
        JsfUtil.redirect(RequestParameter.PROJECT_PATH_SHORT + project.getName());
    }

    /**
     * get all ErrorMessages which are in the FacesContext
     * @return the list of all messages
     */
    public List<FacesMessage> getCurrentMessages() {
        List<FacesMessage> result = new ArrayList<FacesMessage>();
        Iterator<FacesMessage> iter = FacesContext.getCurrentInstance().getMessages();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    public void submit() {
        if (relationshipTypeLocal.isPersistable(relationshipType)) {
            project.addRelationshipType(relationshipType);
            projectLocal.merge(project);
        }
        JsfUtil.redirect(RequestParameter.PROJECT_PATH_SHORT.concat(project.getName()));
    }

    public void reset() {
        if (project.getId() != null) {
            JsfUtil.redirect(RequestParameter.PROJECT_PATH_SHORT.concat(project.getName()));
        } else {
            JsfUtil.redirect(RequestParameter.PROJECT_PATH_SHORT);
        }
    }

    public void checkRelationshipTypeName(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {

        String newName = value.toString().trim();

        if (!StringValidator.isValid(newName, false)) {
            return;
        }

        if (originalRelationshipTypeName != null
                && newName.equalsIgnoreCase(originalRelationshipTypeName)) {
            return;

        }

        if (!relationshipTypeLocal.isUsed(newName)) {
            return;
        }

        throw new ValidatorException(MessageFactory.getMessage(
                fc,
                USEDRELATIONSHIPTYPENAME_ID,
                new Object[]{
                    MessageFactory.getLabel(fc, uic)
                }));
    }
}
