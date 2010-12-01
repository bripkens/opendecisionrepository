package nl.rug.search.odr.viewpoint.chronological;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import nl.rug.search.odr.viewpoint.AbstractVisualization;
import nl.rug.search.odr.viewpoint.RequiredFor;
import nl.rug.search.odr.viewpoint.Viewpoint;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class ChronologicalViewVisualization extends AbstractVisualization<ChronologicalViewVisualization, ChronologicalViewNode, ChronologicalViewAssociation> {

    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    @RequiredFor(Viewpoint.CHRONOLOGICAL)
    private List<ChronologicalViewNode> nodes;

    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    @RequiredFor(Viewpoint.CHRONOLOGICAL)
    private List<ChronologicalViewAssociation> associations;




    public ChronologicalViewVisualization() {
        nodes = new ArrayList<ChronologicalViewNode>();
        associations = new ArrayList<ChronologicalViewAssociation>();
    }




    @Override
    public void addNode(ChronologicalViewNode node) {
        nodes.add(node);
    }




    @Override
    public List<ChronologicalViewNode> getNodes() {
        return nodes;
    }




    @Override
    public void removeAllNodes() {
        nodes.clear();
    }




    @Override
    public void removeNode(ChronologicalViewNode node) {
        nodes.remove(node);
    }




    @Override
    public void setNodes(List<ChronologicalViewNode> nodes) {
        this.nodes = nodes;
    }




    @Override
    public void addAssociation(ChronologicalViewAssociation association) {
        associations.add(association);
    }




    @Override
    public void removeAssociation(ChronologicalViewAssociation association) {
        associations.remove(association);
    }




    @Override
    public List<ChronologicalViewAssociation> getAssociations() {
        return associations;
    }




    @Override
    public void setAssociations(List<ChronologicalViewAssociation> associations) {
        this.associations = associations;
    }




    @Override
    public void removeAllAssociations() {
        associations.clear();
    }




}



