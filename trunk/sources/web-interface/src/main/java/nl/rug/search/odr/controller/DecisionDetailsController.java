package nl.rug.search.odr.controller;

import info.bliki.wiki.model.WikiModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import nl.rug.search.odr.Filename;
import nl.rug.search.odr.NavigationBuilder;
import nl.rug.search.odr.QueryStringBuilder;
import nl.rug.search.odr.RequestAnalyser;
import nl.rug.search.odr.RequestAnalyser.RequestAnalyserDto;
import nl.rug.search.odr.RequestParameter;
import nl.rug.search.odr.decision.DecisionLocal;
import nl.rug.search.odr.entities.ComponentValue;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Relationship;
import nl.rug.search.odr.entities.RelationshipType;
import nl.rug.search.odr.entities.Concern;
import nl.rug.search.odr.entities.State;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.project.ProjectLocal;
import nl.rug.search.odr.util.CustomWikiModel;
import nl.rug.search.odr.util.ErrorUtil;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@RequestScoped
@ManagedBean
public class DecisionDetailsController {

    // <editor-fold defaultstate="collapsed" desc="POJOs">
    private boolean validRequest;

    private Project project;

    private Decision decision;

    private Version version;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="EJBs">
    @EJB
    private ProjectLocal pl;

    @EJB
    private DecisionLocal dl;
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="construction">


    @PostConstruct
    public void setUp() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().
                getExternalContext().
                getRequest();


        RequestAnalyser analyser = new RequestAnalyser(request, pl);

        RequestAnalyserDto result = analyser.analyse();

        if (result.isValid()) {
            setUpDecisionSpecific(result);
        } else {
            result.executeErrorAction();
        }
    }




    private void setUpDecisionSpecific(RequestAnalyserDto analyserDto) {
        HttpServletRequest request = analyserDto.getRequest();

        String decisionIdParam = request.getParameter(RequestParameter.DECISION_ID);
        String versionIdParam = request.getParameter(RequestParameter.VERSION_ID);

        long decisionId = -1;
        long versionId = -1;

        try {
            decisionId = Long.parseLong(decisionIdParam);

            if (versionIdParam != null) {
                versionId = Long.parseLong(versionIdParam);
            }
        } catch (NumberFormatException ex) {
            ErrorUtil.showInvalidIdError();
            return;
        }

        decision = analyserDto.getProject().getDecision(decisionId);

        if (decision == null) {
            ErrorUtil.showInvalidIdError();
            return;
        }

        if (versionId == -1) {
            version = decision.getCurrentVersion();

        } else {
            version = decision.getVersion(versionId);

            if (version == null) {
                ErrorUtil.showInvalidIdError();
                return;
            }

        }

        this.project = analyserDto.getProject();
        


        validRequest = true;
    }
    // </editor-fold>







    public List<NavigationBuilder.NavigationLink> getNavigationBar() {
        NavigationBuilder navi = new NavigationBuilder();
        navi.setNavigationSite(FacesContext.getCurrentInstance().getViewRoot().getViewId());
        navi.setDecision(decision);
        navi.setProject(project);
        navi.setVersion(version);
        return navi.getNavigationBar();
    }


    // <editor-fold defaultstate="collapsed" desc="getter">


    public boolean isValid() {
        return validRequest;
    }




    public Decision getDecision() {
        return decision;
    }




    public Version getVersion() {
        return version;
    }




    public boolean hasValues() {
        return decision != null && !decision.getValues().isEmpty();
    }




    public List<DescriptionDto> getValues() {
        List<ComponentValue> values = new ArrayList<ComponentValue>(decision.getValues());

        Collections.sort(values, new ComponentValue.OrderComparator());

        List<DescriptionDto> valuesWithHtmlSyntax = new ArrayList<DescriptionDto>(values.size());

        CustomWikiModel wcm = new CustomWikiModel();


        for (ComponentValue value : values) {
            DescriptionDto description = new DescriptionDto();
            description.setContent(wcm.wikiToHtml(value.getValue()));
            description.setHeadline(value.getComponent().getLabel());

            valuesWithHtmlSyntax.add(description);
        }

        return valuesWithHtmlSyntax;
    }




    public List<ProjectMember> getInitiators() {
        List<ProjectMember> members = new ArrayList<ProjectMember>(version.getInitiators());

        Collections.sort(members, new ProjectMember.NameComparator());

        return members;
    }




    public List<Concern> getConcerns() {
        return new ArrayList<Concern>(version.getConcerns());
    }




    public List<RelationshipDto> getOutgoingRelationships() {
        Collection<Relationship> relationships = version.getOutgoingRelationships();

        List<RelationshipDto> result = new ArrayList<RelationshipDto>(relationships.size());

        for (Relationship eachRelationship : relationships) {
            RelationshipDto dto = new RelationshipDto();
            dto.setType(eachRelationship.getType());
            dto.setVersion(eachRelationship.getTarget());
            dto.setDecision(dto.getVersion().getDecision());
            result.add(dto);
        }

        return result;
    }




    public List<RelationshipDto> getIncomingRelationships() {
        Collection<Relationship> relationships = version.getIncomingRelationships();

        List<RelationshipDto> result = new ArrayList<RelationshipDto>(relationships.size());

        for (Relationship eachRelationship : relationships) {
            RelationshipDto dto = new RelationshipDto();
            dto.setType(eachRelationship.getType());
            dto.setVersion(eachRelationship.getSource());
            dto.setDecision(dto.getVersion().getDecision());
            result.add(dto);
        }

        return result;
    }




    public List<HistoryDto> getFuture() {
        Collection<Version> versions = decision.getVersions();
        List<HistoryDto> future = new ArrayList<HistoryDto>(versions.size() - 1);

        for (Version eachVersion : versions) {
            boolean after;

            after = eachVersion.getDecidedWhen().compareTo(version.getDecidedWhen()) > 0
                    || (eachVersion.getDecidedWhen().compareTo(version.getDecidedWhen()) == 0
                    && eachVersion.getDocumentedWhen().compareTo(version.getDocumentedWhen()) > 0);

            if (eachVersion.getId() != version.getId() && after) {
                HistoryDto dto = new HistoryDto();
                dto.setDecidedWhen(eachVersion.getDecidedWhen());
                dto.setDocumentedWhen(eachVersion.getDocumentedWhen());
                dto.setState(eachVersion.getState());
                dto.setVersionId(eachVersion.getId());
                future.add(dto);
            }
        }

        Collections.sort(future);

        return future;
    }




    public List<HistoryDto> getHistory() {
        Collection<Version> versions = decision.getVersions();
        List<HistoryDto> history = new ArrayList<HistoryDto>(versions.size() - 1);

        for (Version eachVersion : versions) {
            boolean before;

            before = eachVersion.getDecidedWhen().compareTo(version.getDecidedWhen()) < 0
                    || (eachVersion.getDecidedWhen().compareTo(version.getDecidedWhen()) == 0
                    && eachVersion.getDocumentedWhen().compareTo(version.getDocumentedWhen()) < 0);

            if (eachVersion.getId() != version.getId() && before) {
                HistoryDto dto = new HistoryDto();
                dto.setDecidedWhen(eachVersion.getDecidedWhen());
                dto.setDocumentedWhen(eachVersion.getDocumentedWhen());
                dto.setState(eachVersion.getState());
                dto.setVersionId(eachVersion.getId());
                history.add(dto);
            }
        }

        Collections.sort(history);

        return history;
    }




    public String getConcernLink(Concern concern) {
        return new QueryStringBuilder().setUrl(Filename.CONCERN_DETAILS).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.CONCERN_GROUP_ID, concern.getGroup()).
                toString();
    }




    public String getDecisionLink(long versionId) {
        return new QueryStringBuilder().setUrl(Filename.DECISION_DETAILS).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.DECISION_ID, decision.getId()).
                append(RequestParameter.VERSION_ID, versionId).
                toString();
    }

    public String getCreateLink() {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_DECISION).
                append(RequestParameter.ID, project.getId()).
                toString().concat(RequestParameter.AMPERSAND +RequestParameter.CREATE);
    }


    public String getUpdateLink() {
        return new QueryStringBuilder().setUrl(Filename.MANAGE_DECISION).
                append(RequestParameter.ID, project.getId()).
                append(RequestParameter.DECISION_ID, decision.getId()).
                append(RequestParameter.VERSION_ID, version.getId()).
                toString();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="dtos">



    public static class DescriptionDto {

        private String headline;

        private String content;




        public String getContent() {
            return content;
        }




        public void setContent(String content) {
            this.content = content;
        }




        public String getHeadline() {
            return headline;
        }




        public void setHeadline(String headline) {
            this.headline = headline;
        }




    }




    public class RelationshipDto {

        private RelationshipType type;

        private Decision decision;

        private Version version;




        public String getDecisionLink() {
            return new QueryStringBuilder().setUrl("decisionDetails.html").
                    append(RequestParameter.ID, project.getId()).
                    append(RequestParameter.DECISION_ID, decision.getId()).
                    append(RequestParameter.VERSION_ID, version.getId()).
                    toString();
        }




        public Decision getDecision() {
            return decision;
        }




        public void setDecision(Decision decision) {
            this.decision = decision;
        }




        public RelationshipType getType() {
            return type;
        }




        public void setType(RelationshipType type) {
            this.type = type;
        }




        public Version getVersion() {
            return version;
        }




        public void setVersion(Version version) {
            this.version = version;
        }




    }




    public static class HistoryDto implements Comparable<HistoryDto> {

        private State state;

        private Date decidedWhen;

        private Date documentedWhen;

        private long versionId;




        public Date getDecidedWhen() {
            return decidedWhen;
        }




        public Date getDocumentedWhen() {
            return documentedWhen;
        }




        public State getState() {
            return state;
        }




        public void setDecidedWhen(Date decidedWhen) {
            this.decidedWhen = decidedWhen;
        }




        public void setDocumentedWhen(Date documentedWhen) {
            this.documentedWhen = documentedWhen;
        }




        public void setState(State state) {
            this.state = state;
        }




        @Override
        public int compareTo(HistoryDto o) {
            int result = o.decidedWhen.compareTo(decidedWhen);

            if (result == 0) {
                result = o.documentedWhen.compareTo(documentedWhen);
            }

            return result;
        }




        public void setVersionId(long versionId) {
            this.versionId = versionId;
        }




        public long getVersionId() {
            return versionId;
        }




    }

    // </editor-fold>
}



