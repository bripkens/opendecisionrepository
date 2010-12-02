package nl.rug.search.odr;

import nl.rug.search.odr.util.AuthenticationUtil;
import nl.rug.search.odr.util.ErrorUtil;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.viewpoint.Viewpoint;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class RequestAnalyser {

    private final HttpServletRequest request;

    private final ProjectLocal pl;

    private final RequestAnalyserDto result;




    public RequestAnalyser(HttpServletRequest request, ProjectLocal pl) {
        this.request = request;
        this.pl = pl;
        this.result = new RequestAnalyserDto();
        this.result.setRequest(request);
    }




    public RequestAnalyserDto analyse() {
        checkAuthentication();

        loadProject();

        checkProjectMember();

        return result;
    }




    private void checkAuthentication() {
        if (!AuthenticationUtil.isAuthtenticated()) {

            result.setErrorAction(new Runnable() {

                @Override
                public void run() {
                    ErrorUtil.showNotAuthenticatedError();
                }




            });
        }
    }




    private void loadProject() {
        if (!result.isValid()) {
            return;
        }

        String projectIdParameter;
        projectIdParameter = request.getParameter(RequestParameter.ID);

        if (projectIdParameter == null) {

            result.setErrorAction(new Runnable() {

                @Override
                public void run() {
                    ErrorUtil.showInvalidIdError();
                }




            });
            return;
        }

        long projectId;

        try {
            projectId = Long.parseLong(projectIdParameter);
        } catch (NumberFormatException e) {

            result.setErrorAction(new Runnable() {

                @Override
                public void run() {
                    ErrorUtil.showInvalidIdError();
                }




            });
            return;
        }

        result.setProject(pl.getById(projectId));

        if (result.getProject() == null) {

            result.setErrorAction(new Runnable() {

                @Override
                public void run() {
                    ErrorUtil.showIdNotRegisteredError();
                }




            });
        }
    }




    private void checkProjectMember() {
        if (!result.isValid()) {
            return;
        }

        long userId = AuthenticationUtil.getUserId();

        for (ProjectMember pm : result.getProject().
                getMembers()) {
            if (pm.getPerson().
                    getId().
                    equals(userId)) {
                result.setMember(pm);
                return;
            }
        }


        result.setErrorAction(new Runnable() {

            @Override
            public void run() {
                ErrorUtil.showNoMemberError();
            }




        });
    }




    public static Viewpoint getViewpoint(HttpServletRequest request) {
        String stakeholderParam = request.getParameter(RequestParameter.STAKEHOLDER_VIEWPOINT);
        String relationshipParam = request.getParameter(RequestParameter.RELATIONSHIP_VIEWPOINT);
        String chronologicalParam = request.getParameter(RequestParameter.CHRONOLOGICAL_VIEWPOINT);

        int paramCounter = 0;
        paramCounter += stakeholderParam != null ? 1 : 0;
        paramCounter += relationshipParam != null ? 1 : 0;
        paramCounter += chronologicalParam != null ? 1 : 0;

        if (paramCounter != 1) {
            return null;
        }

        Viewpoint point = (stakeholderParam != null) ? Viewpoint.STAKEHOLDER_INVOLVEMENT : Viewpoint.CHRONOLOGICAL;
        point = (relationshipParam != null) ? Viewpoint.RELATIONSHIP : point;

        return point;
    }




    public static class RequestAnalyserDto {

        private Project project;

        private ProjectMember member;

        private HttpServletRequest request;

        private Runnable errorAction;




        public ProjectMember getMember() {
            return member;
        }




        private void setMember(ProjectMember member) {
            this.member = member;
        }




        public Project getProject() {
            return project;
        }




        private void setProject(Project project) {
            this.project = project;
        }




        public HttpServletRequest getRequest() {
            return request;
        }




        private void setRequest(HttpServletRequest request) {
            this.request = request;
        }




        public boolean isValid() {
            return errorAction == null;
        }




        private void setErrorAction(Runnable errorAction) {
            this.errorAction = errorAction;
        }




        public void executeErrorAction() {
            if (errorAction != null) {
                errorAction.run();
            }
        }




    }




}



