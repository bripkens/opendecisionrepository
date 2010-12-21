package nl.rug.search.odr.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.NavigationBuilder;
import nl.rug.search.odr.RequestAnalyser;
import nl.rug.search.odr.RequestAnalyser.RequestAnalyserDto;
import nl.rug.search.odr.RequestParameter;

import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
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
public class memberTableController {

    @EJB
    private ProjectLocal projectLocal;

    private List<ProjectMember> members;

    private ProjectMember member;

    private Project project;

    private ProjectMember memberToRemove;

    private NavigationBuilder navi;

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
            member = result.getMember();
            members = new ArrayList<ProjectMember>(project.getMembers());
            validRequest = true;
        } else {
            result.executeErrorAction();
            validRequest = false;
        }
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
        return validRequest;
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



