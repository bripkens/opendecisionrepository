package nl.rug.search.odr.viewpoint.chronological;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import nl.rug.search.odr.entities.Iteration;
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




    public boolean containsVersion(Version v) {
        for (ChronologicalViewNode node : getNodes()) {
            if (node.getVersion() != null && node.getVersion().equals(v)) {
                return true;
            }
        }
        return false;
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




    void sortNodes(Comparator<ChronologicalViewNode> comp) {
        Collections.sort(nodes, comp);
    }




    public ChronologicalViewNode getNode(Iteration i, Version version, boolean endpoint, boolean disconnected) {
        for (ChronologicalViewNode node : getNodes()) {
            boolean iterationEqual = false, versionEqual = false;

            iterationEqual = isIterationEquals(node, i);

            if (!iterationEqual) {
                continue;
            }

            versionEqual = isNodeEquals(node, version);

            if (versionEqual && disconnected == node.isDisconnected() &&
                    endpoint == node.isEndPoint()) {
                return node;
            }
        }

        return null;
    }
    
    private boolean isIterationEquals(ChronologicalViewNode node, Iteration i) {
        return (node.getIteration() == null && i == null) ||
                (node.getIteration() != null && node.getIteration().equals(i));
    }

    private boolean isNodeEquals(ChronologicalViewNode node, Version version) {
        return (node.getVersion() == null && version == null) ||
                (node.getVersion() != null && node.getVersion().equals(version));
    }


    public ChronologicalViewAssociation getAssociation(Iteration sourceIteration,
            Version sourceVersion, Iteration targetIteration, Version targetVersion) {

        for (ChronologicalViewAssociation association : getAssociations()) {
            boolean sourceIterationEqual = false, sourceVersionEqual = false, targetIterationEqual = false,
                    targetVersionEqual = false;

            if (association.getSourceIteration() == null && sourceIteration == null) {
                sourceIterationEqual = true;
            } else if (association.getSourceIteration() != null && association.getSourceIteration().equals(sourceIteration)) {
                sourceIterationEqual = true;
            }

            if (!sourceIterationEqual) {
                continue;
            }

            if (association.getTargetIteration() == null && targetIteration == null) {
                targetIterationEqual = true;
            } else if (association.getTargetIteration() != null && association.getTargetIteration().equals(targetIteration)) {
                targetIterationEqual = true;
            }

            if (!targetIterationEqual) {
                continue;
            }

            if (association.getSourceVersion() == null && sourceVersion == null) {
                sourceVersionEqual = true;
            } else if (association.getSourceVersion() != null && association.getSourceVersion().equals(sourceVersion)) {
                sourceVersionEqual = true;
            }

            if (!sourceVersionEqual) {
                continue;
            }

            if (association.getTargetVersion() == null && targetVersion == null) {
                targetVersionEqual = true;
            } else if (association.getTargetVersion() != null && association.getTargetVersion().equals(targetVersion)) {
                targetVersionEqual = true;
            }

            if (targetVersionEqual) {
                return association;
            }
        }

        return null;
    }




}



