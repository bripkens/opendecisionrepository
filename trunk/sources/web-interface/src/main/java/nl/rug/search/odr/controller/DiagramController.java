package nl.rug.search.odr.controller;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import nl.rug.search.odr.Filename;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.viewpoint.Viewpoint;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@RequestScoped
@ManagedBean
public class DiagramController {

    // <editor-fold defaultstate="collapsed" desc="EJBs">
    @EJB
    private ProjectLocal pl;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="attributes">
    private Project project;

    private ProjectMember member;

    private Viewpoint point;

    private boolean requestError;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="construction">



//    @PostConstruct
//    private void setUp() {
//        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().
//                getExternalContext().
//                getRequest();
//
//        RequestAnalyser analyser = new RequestAnalyser(request, pl);
//
//        RequestAnalyserDto result = analyser.analyse();
//
//        if (!result.isValid()) {
//            result.executeErrorAction();
//            requestError = true;
//            return;
//        }
//
//        point = RequestAnalyser.getViewpoint(request);
//
//        if (point == null) {
//            ErrorUtil.showInvalidParametersError();
//            requestError = true;
//            return;
//        }
//
//        project = result.getProject();
//        member = result.getMember();
//        requestError = false;
//    }
    // <editor-fold defaultstate="collapsed" desc="messages">


    // <editor-fold defaultstate="collapsed" desc="getter and setter">


    public String getDataRequestUrl() {
        return Filename.VIEWPOINT_DATA_PROVIDER;
    }




    public String getDataTargetUrl() {
        return Filename.VIEWPOINT_DATA_RECEIVER;
    }


    public String getProjectIdParameter() {
        return RequestParameter.ID;
    }

    public String getDataParameter() {
        return RequestParameter.DATA;
    }

    public String getBackUrl() {
        return RequestParameter.PROJECT_PATH_SHORT.substring(1).concat(project.getName());
    }


    public String getChronologicalViewParameterName() {
        return RequestParameter.CHRONOLOGICAL_VIEWPOINT;
    }

    public String getRelationshipViewParameterName() {
        return RequestParameter.RELATIONSHIP_VIEWPOINT;
    }

    public String getStakeholderInvolvementViewParameterName() {
        return RequestParameter.STAKEHOLDER_VIEWPOINT;
    }

    public String getViewpointParameter() {
        switch (point) {
            case CHRONOLOGICAL:
                return RequestParameter.CHRONOLOGICAL_VIEWPOINT;
            case RELATIONSHIP:
                return RequestParameter.RELATIONSHIP_VIEWPOINT;
            case STAKEHOLDER_INVOLVEMENT:
                return RequestParameter.STAKEHOLDER_VIEWPOINT;
        }

        throw new RuntimeException("Unknown viewpoint.");
    }




    public ProjectMember getMember() {
        return member;
    }




    public void setMember(ProjectMember member) {
        this.member = member;
    }




    public Project getProject() {
        return project;
    }




    public void setProject(Project project) {
        this.project = project;
    }




    public boolean isRequestError() {
        return requestError;
    }




    public void setRequestError(boolean requestError) {
        this.requestError = requestError;
    }
    // </editor-fold>




}



