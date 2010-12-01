package nl.rug.search.odr.viewpoint.relationship;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.viewpoint.AbstractVisualization;
import nl.rug.search.odr.viewpoint.RequiredFor;
import nl.rug.search.odr.viewpoint.Viewpoint;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class RelationshipViewVisualization extends AbstractVisualization<RelationshipViewVisualization, RelationshipViewNode, RelationshipViewAssociation> {

    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    @RequiredFor(Viewpoint.RELATIONSHIP)
    private List<RelationshipViewNode> nodes;

    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    @RequiredFor(Viewpoint.RELATIONSHIP)
    private List<RelationshipViewAssociation> associations;




    public RelationshipViewVisualization() {
        nodes = new ArrayList<RelationshipViewNode>();
        associations = new ArrayList<RelationshipViewAssociation>();
    }




    public boolean containsVersion(Version v) {
        for (RelationshipViewNode node : getNodes()) {
            if (node.getVersion().equals(v)) {
                return true;
            }
        }
        return false;
    }




    @Override
    public void addNode(RelationshipViewNode node) {
        nodes.add(node);
    }




    @Override
    public List<RelationshipViewNode> getNodes() {
        return nodes;
    }




    @Override
    public void removeAllNodes() {
        nodes.clear();
    }




    @Override
    public void removeNode(RelationshipViewNode node) {
        nodes.remove(node);
    }




    @Override
    public void setNodes(List<RelationshipViewNode> nodes) {
        this.nodes = nodes;
    }




    @Override
    public void addAssociation(RelationshipViewAssociation association) {
        associations.add(association);
    }




    @Override
    public void removeAssociation(RelationshipViewAssociation association) {
        associations.remove(association);
    }




    @Override
    public List<RelationshipViewAssociation> getAssociations() {
        return associations;
    }




    @Override
    public void setAssociations(List<RelationshipViewAssociation> associations) {
        this.associations = associations;
    }




    @Override
    public void removeAllAssociations() {
        associations.clear();
    }




}



