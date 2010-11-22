package nl.rug.search.odr.viewpoint;

import java.util.Date;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.Relationship;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class InitRelationshipView {

    private final Viewpoint type;

    private final Project project;

    private Visualization visualization;




    public InitRelationshipView(Viewpoint type, Project p) {
        this.type = type;
        this.project = p;
    }




    public Visualization getView() {
        visualization = new Visualization();
        visualization.setType(type);
        visualization.setDocumentedWhen(new Date());

        addNodes();
        addAssociations();

        return visualization;
    }




    private void addNodes() {
        for (Decision eachDecision : project.getDecisions()) {
            if (eachDecision.isRemoved()) {
                continue;
            }

            Node node = new Node();
            node.setVersion(eachDecision.getCurrentVersion());
            visualization.addNode(node);
        }
    }




    private void addAssociations() {
        for (Decision eachDecision : project.getDecisions()) {
            for (Relationship eachRelationship : eachDecision.getCurrentVersion().getOutgoingRelationships()) {

                if (!visualizationContainBothEndpoints(eachRelationship)) {
                    continue;
                }

                Association association = new Association();
                association.setRelationship(eachRelationship);
                visualization.addAssociation(association);
            }
        }
    }




    private boolean visualizationContainBothEndpoints(Relationship relationship) {
        return visualization.containsVersion(relationship.getSource().getId())
               && visualization.containsVersion(relationship.getTarget().getId());

    }

    // <editor-fold defaultstate="collapsed" desc="getter">



    public Project getProject() {
        return project;
    }




    public Viewpoint getType() {
        return type;
    }
    // </editor-fold>
}
