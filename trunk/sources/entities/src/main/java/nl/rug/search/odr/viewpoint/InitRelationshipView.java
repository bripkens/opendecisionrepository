package nl.rug.search.odr.viewpoint;

import java.util.Collections;
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
        sortNodes();
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



    private void sortNodes() {
        Collections.sort(visualization.getNodes(), new Node.DecisionNameComparator());
    }



    private void addAssociations() {
        for (Node node : visualization.getNodes()) {
            for (Relationship eachRelationship : node.getVersion().getOutgoingRelationships()) {

                if (!visualization.containsVersion(eachRelationship.getTarget())) {
                    continue;
                }


                Association association = new Association();
                association.setRelationship(eachRelationship);
                visualization.addAssociation(association);
            }
        }
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
