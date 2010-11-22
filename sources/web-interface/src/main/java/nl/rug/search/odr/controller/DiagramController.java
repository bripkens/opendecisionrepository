package nl.rug.search.odr.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.Filename;
import nl.rug.search.odr.QueryStringBuilder;
import nl.rug.search.odr.RequestAnalyser;
import nl.rug.search.odr.RequestAnalyser.RequestAnalyserDto;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ProjectLocal;

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

    private boolean requestError;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="construction">
    @PostConstruct
    private void setUp() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().
                getExternalContext().
                getRequest();

        RequestAnalyser analyser = new RequestAnalyser(request, pl);

        RequestAnalyserDto result = analyser.analyse();

        if (!result.isValid()) {
            result.executeErrorAction();
            requestError = true;
            return;
        }

        project = result.getProject();
        member = result.getMember();
        requestError = false;
    }
    // <editor-fold defaultstate="collapsed" desc="messages">


    // <editor-fold defaultstate="collapsed" desc="getter and setter">
    public String getDataRequestUrl() {
        return Filename.VIEWPOINT_DATA_PROVIDER;
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
