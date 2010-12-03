package nl.rug.search.odr.viewpoint.chronological;

import java.util.List;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Version;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class VisualizationBuilder {

    private final ChronologicalViewVisualization visualization;

    private final List<ViewBlock> blocks;

    private ViewBlock nextBlock, currentBlock;




    public VisualizationBuilder(ChronologicalViewVisualization visualization, List<ViewBlock> blocks) {
        this.visualization = visualization;
        this.blocks = blocks;
    }




    public VisualizationBuilder build() {

        // Nothing to add to the visualization as there are no blocks existing
        if (blocks.isEmpty()) {
            return this;
        }

        currentBlock = blocks.get(0);

        for (int i = 0; i < blocks.size(); i++) {
            if (i + 1 < blocks.size()) {
                nextBlock = blocks.get(i + 1);
            } else {
                nextBlock = null;
            }

            handleCurrentBlock();

            currentBlock = nextBlock;
        }

        return this;
    }




    private void handleCurrentBlock() {
        handleIterationRelated();
        handleVersionGroupRelated();
    }




    private void handleIterationRelated() {
        ChronologicalViewNode iterationNode = addIterationNode(currentBlock.getIteration());


        if (currentBlock.getOrderedVersions().isEmpty() && nextBlock == null) {
            // if there is nothing after this node, set the marker value 'endPoint' to true which indicates that
            // this node as no outgoing associations
            iterationNode.setEndPoint(true);
        } else if (currentBlock.getOrderedVersions().isEmpty()) {
            // connect this iteration node to the next iteration node if no versions have been created during this
            // iteration
            addAssociation(null, currentBlock.getIteration(), null, nextBlock.getIteration());
        }
    }




    private ChronologicalViewNode addIterationNode(Iteration it) {
        assert it != null;

        ChronologicalViewNode node = new ChronologicalViewNode();
        node.setIteration(it);
        visualization.addNode(node);
        return node;
    }




    private ChronologicalViewAssociation addAssociation(Version sourceVersion, Iteration sourceIteration,
            Version targetVersion, Iteration targetIteration) {

        assert (sourceVersion != null || sourceIteration != null) && !(sourceVersion != null && sourceIteration != null)
                && (targetVersion != null || targetIteration != null) && !(targetVersion != null && targetIteration != null);

        ChronologicalViewAssociation association = new ChronologicalViewAssociation();

        association.setSourceIteration(sourceIteration);
        association.setTargetIteration(targetIteration);
        association.setSourceVersion(sourceVersion);
        association.setTargetVersion(targetVersion);

        visualization.addAssociation(association);

        return association;
    }




    private void handleVersionGroupRelated() {
        for (List<Version> versionGroup : currentBlock.getOrderedVersions()) {

            Version previousVersion = null;

            for (int j = 0; j < versionGroup.size(); j++) {
                Version currentVersion = versionGroup.get(j);

                ChronologicalViewNode versionNode = addVersionNode(currentVersion);

                if (previousVersion != null) {
                    addAssociation(previousVersion, null, currentVersion, null);
                } else {
                    addAssociation(null, currentBlock.getIteration(), currentVersion, null);
                }

                if (j + 1 == versionGroup.size() && nextBlock == null) {
                    versionNode.setEndPoint(true);
                } else if (j + 1 == versionGroup.size() && nextBlock != null) {
                    addAssociation(currentVersion, null, null, nextBlock.getIteration());
                }

                previousVersion = currentVersion;
            }
        }
    }




    private ChronologicalViewNode addVersionNode(Version v) {
        assert v != null;

        ChronologicalViewNode node = new ChronologicalViewNode();
        node.setVersion(v);
        visualization.addNode(node);

        return node;
    }


    // <editor-fold defaultstate="collapsed" desc="getter">


    public ChronologicalViewVisualization getVisualization() {
        return visualization;
    }
    // </editor-fold>




}



