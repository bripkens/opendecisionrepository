package nl.rug.search.odr.viewpoint.relationship;

import nl.rug.search.odr.viewpoint.relationship.RelationshipViewVisualization;
import nl.rug.search.odr.viewpoint.relationship.RelationshipViewAssociation;
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

    private RelationshipViewVisualization visualization;




    public InitRelationshipView(Project p) {
        this.project = p;
    }


    // <editor-fold defaultstate="collapsed" desc="creation of an initial view">


    public RelationshipViewVisualization getView() {
        visualization = new RelationshipViewVisualization();
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

            RelationshipViewNode node = new RelationshipViewNode();
            node.setVersion(eachDecision.getCurrentVersion());
            visualization.addNode(node);
        }
    }



    public void sortVisualization(RelationshipViewVisualization visualization) {
        Collections.sort(visualization.getNodes(), new RelationshipViewNode.DecisionNameComparator());
    }


    private void sortNodes() {
        sortVisualization(visualization);
    }




    private void addAssociations() {
        for (RelationshipViewNode node : visualization.getNodes()) {
            for (Relationship eachRelationship : node.getVersion().getOutgoingRelationships()) {

                if (!visualization.containsVersion(eachRelationship.getTarget())) {
                    continue;
                }


                RelationshipViewAssociation association = new RelationshipViewAssociation();
                association.setRelationship(eachRelationship);
                visualization.addAssociation(association);
            }
        }
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="update view">


    public void updateView(RelationshipViewVisualization visualization) {
        this.visualization = visualization;

        removeNodesWithRemovedDecisions();
        replaceOldVersions();
        addNewNodes();
    }




    private void removeNodesWithRemovedDecisions() {
        Iterator<RelationshipViewNode> it = visualization.getNodes().iterator();

        while (it.hasNext()) {
            RelationshipViewNode n = it.next();

            if (n.getVersion().getDecision().isRemoved()) {
                it.remove();
                removeRelationshipsWithVersion(n.getVersion());
            }
        }
    }




    private void removeRelationshipsWithVersion(Version v) {
        Iterator<RelationshipViewAssociation> it = visualization.getAssociations().iterator();

        while (it.hasNext()) {
            RelationshipViewAssociation a = it.next();

            if (a.getRelationship().getSource().equals(v) || a.getRelationship().getTarget().equals(v)) {
                it.remove();
            }
        }
    }




    private void replaceOldVersions() {
        for (RelationshipViewNode node : visualization.getNodes()) {
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


            RelationshipViewAssociation association = new RelationshipViewAssociation();
            association.setRelationship(r);
            visualization.addAssociation(association);
        }

        for (Relationship r : v.getIncomingRelationships()) {

            if (!visualization.containsVersion(r.getSource()) || containsRelationship(r)) {
                continue;
            }


            RelationshipViewAssociation association = new RelationshipViewAssociation();
            association.setRelationship(r);
            visualization.addAssociation(association);
        }
    }


    private boolean containsRelationship(Relationship r) {
        for(RelationshipViewAssociation a : visualization.getAssociations()) {
            if (a.getRelationship().equals(r)) {
                return true;
            }
        }

        return false;
    }


    private void addNewNodes() {
        for (Decision d : project.getDecisions()) {
            if (!d.isRemoved() && !visualizationContainsDecision(d)) {
                RelationshipViewNode node = new RelationshipViewNode();
                node.setVersion(d.getCurrentVersion());
                addNewRelationshipsWithVersion(node.getVersion());
                visualization.addNode(node);
            }
        }
    }




    private boolean visualizationContainsDecision(Decision d) {
        for (RelationshipViewNode n : visualization.getNodes()) {
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



