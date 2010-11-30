package nl.rug.search.odr.viewpoint;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.Relationship;
import nl.rug.search.odr.entities.Version;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class InitRelationshipView {


    private final Project project;

    private Visualization visualization;




    public InitRelationshipView(Project p) {
        this.project = p;
    }


    // <editor-fold defaultstate="collapsed" desc="creation of an initial view">


    public Visualization getView() {
        visualization = new Visualization();
        visualization.setType(Viewpoint.RELATIONSHIP);
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

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="update view">


    public void updateView(Visualization visualization) {
        this.visualization = visualization;

        removeNodesWithRemovedDecisions();
        replaceOldVersions();
        addNewNodes();
        sortNodes();
    }




    private void removeNodesWithRemovedDecisions() {
        Iterator<Node> it = visualization.getNodes().iterator();

        while (it.hasNext()) {
            Node n = it.next();

            if (n.getVersion().getDecision().isRemoved()) {
                it.remove();
                removeRelationshipsWithVersion(n.getVersion());
            }
        }
    }




    private void removeRelationshipsWithVersion(Version v) {
        Iterator<Association> it = visualization.getAssociations().iterator();

        while (it.hasNext()) {
            Association a = it.next();

            if (a.getRelationship().getSource().equals(v) || a.getRelationship().getTarget().equals(v)) {
                it.remove();
            }
        }
    }




    private void replaceOldVersions() {
        for (Node node : visualization.getNodes()) {
            Version currentVersion = node.getVersion().getDecision().getCurrentVersion();

            if (!node.getVersion().equals(currentVersion)) {
                removeRelationshipsWithVersion(node.getVersion());
                node.setVersion(currentVersion);
            }
            
            addNewRelationshipsWithVersion(node.getVersion());
        }
    }




    private void addNewRelationshipsWithVersion(Version v) {
        for (Relationship r : v.getOutgoingRelationships()) {

            if (!visualization.containsVersion(r.getTarget()) || containsRelationship(r)) {
                continue;
            }


            Association association = new Association();
            association.setRelationship(r);
            visualization.addAssociation(association);
        }

        for (Relationship r : v.getIncomingRelationships()) {

            if (!visualization.containsVersion(r.getSource()) || containsRelationship(r)) {
                continue;
            }


            Association association = new Association();
            association.setRelationship(r);
            visualization.addAssociation(association);
        }
    }


    private boolean containsRelationship(Relationship r) {
        for(Association a : visualization.getAssociations()) {
            if (a.getRelationship().equals(r)) {
                return true;
            }
        }

        return false;
    }


    private void addNewNodes() {
        for (Decision d : project.getDecisions()) {
            if (!d.isRemoved() && !visualizationContainsDecision(d)) {
                Node node = new Node();
                node.setVersion(d.getCurrentVersion());
                addNewRelationshipsWithVersion(node.getVersion());
                visualization.addNode(node);
            }
        }
    }




    private boolean visualizationContainsDecision(Decision d) {
        for (Node n : visualization.getNodes()) {
            if (n.getVersion().getDecision().equals(d)) {
                return true;
            }
        }

        return false;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getter">


    public Project getProject() {
        return project;
    }
    // </editor-fold>
}



