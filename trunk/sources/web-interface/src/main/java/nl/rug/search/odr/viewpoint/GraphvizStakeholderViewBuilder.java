package nl.rug.search.odr.viewpoint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.ProjectMember;
import nl.rug.search.odr.entities.Version;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class GraphvizStakeholderViewBuilder {

    private StringBuilder builder;

    private final Project project;

    private final Iteration targetIteration;

    private Set<ProjectMember> memberWhoParticipatedInIteration;

    private List<Version> versionsOfIteration;




    public GraphvizStakeholderViewBuilder(Project p, Iteration targetIteration) {
        this.project = p;
        this.targetIteration = targetIteration;
    }




    public String getDot() {
        builder = new StringBuilder();
        memberWhoParticipatedInIteration = new HashSet<ProjectMember>();
        versionsOfIteration = new ArrayList<Version>();

        addStartMarkup();

        

        addMarkupAfterVersions();

        addVersions();

        addActors();

        addRelationships();

        addEndMarkup();

        return builder.toString();
    }




    private void addStartMarkup() {
        builder.append("digraph G {\n"
                + "  subgraph cluster0 {\n"
                + "    label = <<font color=\"red4\" POINT-SIZE=\"12.0\">").
                append(targetIteration.getName()).
                append("</font>>;\n"
                + "    color = black;\n"
                + "    fillcolor = none;\n"
                + "    style=filled;\n");
    }




    private void addVersions() {
        for (Decision d : project.getDecisions()) {
            for (Version v : d.getVersions()) {
                if (createdDuringIteration(v)) {
                    addVersion(v);
                }
            }
        }
    }




    private boolean createdDuringIteration(Version v) {
        return v.getDecidedWhen().after(targetIteration.getStartDate())
                && v.getDecidedWhen().before(targetIteration.getEndDate());
    }




    private void addVersion(Version v) {
        versionsOfIteration.add(v);

        builder.append("    ").
                append(v.getId()).
                append("[style=\"rounded,filled\", fillcolor=white, shape=box, label=<\n"
                + "      <TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"3\" CELLPADDING=\"0\">\n"
                + "        <tr>\n"
                + "          <TD><font color=\"red4\" POINT-SIZE=\"10.0\">").
                append(v.getState().getStatusName()).
                append("</font></TD>\n"
                + "        </tr>\n"
                + "        <tr>\n"
                + "          <TD><font POINT-SIZE=\"12\">").
                append(v.getDecision().getName()).
                append("</font></TD>\n"
                + "        </tr>\n"
                + "      </TABLE>>];\n");
    }




    private void addMarkupAfterVersions() {
        builder.append("    }\n");
    }




    private void addActors() {
        for (Version v : versionsOfIteration) {
            for (ProjectMember m : v.getInitiators()) {
                memberWhoParticipatedInIteration.add(m);
            }
        }

        for (ProjectMember m : memberWhoParticipatedInIteration) {
            addActor(m);
        }
    }




    private void addActor(ProjectMember m) {
        String label = (m.getPerson().getName() != null) ? m.getPerson().getName() : m.getPerson().getEmail();

        builder.append("    \"").
                append(m.getId()).
                append("\" [label=<<font POINT-SIZE=\"12.0\">").
                append(label).
                append("</font>>];\n");
    }




    private void addRelationships() {
        for (Version v : versionsOfIteration) {
            for (ProjectMember m : v.getInitiators()) {
                builder.append("    \"").
                        append(m.getId()).
                        append("\" -> \"").
                        append(v.getId()).
                        append("\" [label=<<font color=\"red4\" POINT-SIZE=\"10.0\">").
                        append(v.getState().getActionName()).
                        append("</font>>];\n");
            }
        }
    }




    private void addEndMarkup() {
        builder.append("}");
    }




    @Override
    public String toString() {
        return getDot();
    }




}



