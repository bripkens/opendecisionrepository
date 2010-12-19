package nl.rug.search.odr.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
import nl.rug.search.odr.RequestParameter;

import nl.rug.search.odr.entities.Concern;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ConcernLocal;
import nl.rug.search.odr.project.IterationLocal;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.util.ErrorUtil;
import nl.rug.search.odr.util.JsfUtil;
import sun.misc.Sort;

/**
 *
 * @author Stefan
 */
@ManagedBean
@ViewScoped
public class memberTableController {

    @EJB
    private ProjectLocal projectLocal;

    private List<ProjectMember> members;

    private ProjectMember member;

    private Project project;

    private ProjectMember memberToRemove;

    private NavigationBuilder navi;




    @PostConstruct
    public void getIterationsFromDb() {

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
        navi = new NavigationBuilder();

        if (project == null) {
            ErrorUtil.showIdNotRegisteredError();
            return;
        } else if (project != null && !memberIsInProject()) {
            ErrorUtil.showNoMemberError();
            return;
        }

        members = new ArrayList<ProjectMember>(project.getMembers());
    }




    private boolean memberIsInProject() {
        long userId = AuthenticationUtil.getUserId();
        for (ProjectMember pm : project.getMembers()) {
            if (pm.getPerson().getId().equals(userId)) {
                this.member = pm;
                return true;
            }
        }
        return false;
    }




    public Collection<ProjectMember> getList() {
        Iterator<ProjectMember> it = members.iterator();
        while (it.hasNext()) {
            if (it.next().isRemoved() == true) {
                it.remove();
            }
        }
        Collections.sort(members, new ProjectMember.NameComparator());
        return members;
    }




    public boolean isValid() {
        return project != null;
    }




    public void showRemoveMemberConfirmation(ActionEvent e) {
        ProjectMember me = (ProjectMember) e.getComponent().getAttributes().get("member");

        memberToRemove = me;

        JsfUtil.addJavascriptCall("odr.showMemberRemoveForm();");
    }




    public ProjectMember getMemberToRemove() {
        return memberToRemove;
    }




    public void setMemberToRemove(ProjectMember memberToRemove) {
        this.memberToRemove = memberToRemove;
    }




    public void removeMember() {
        for (ProjectMember mem : project.getMembers()) {
            if (mem.equals(memberToRemove)) {
                mem.setRemoved(true);
                break;
            }
        }

        projectLocal.merge(project);

        JsfUtil.addJavascriptCall("odr.popup.hide();");

    }




    public List<NavigationBuilder.NavigationLink> getNavigationBar() {
        navi.setNavigationSite(FacesContext.getCurrentInstance().getViewRoot().getViewId());
        navi.setProject(project);

        return navi.getNavigationBar();
    }




    public boolean isCurrentUser(ProjectMember member) {
        if (this.member.equals(member)) {
            return true;
        }
        return false;
    }




}



