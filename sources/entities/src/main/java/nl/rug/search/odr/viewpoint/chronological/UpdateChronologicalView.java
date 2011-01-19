
package nl.rug.search.odr.viewpoint.chronological;

import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.viewpoint.Handle;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class UpdateChronologicalView {
    private final Project p;
    private final ChronologicalViewVisualization oldVisualization;

    private ChronologicalViewVisualization newVisualization;




    public UpdateChronologicalView(Project p, ChronologicalViewVisualization oldVisualization) {
        this.p = p;
        this.oldVisualization = oldVisualization;
    }

    public ChronologicalViewVisualization getView() {
        this.newVisualization = new InitChronologicalView(p).getView();

        copyNodeDetails();
        copyAssociationHandles();

        return this.newVisualization;
    }

    private void copyNodeDetails() {
        for(ChronologicalViewNode newNode : newVisualization.getNodes()) {
            ChronologicalViewNode oldNode = getOldNode(newNode);

            if (oldNode != null) {
                copyDetails(oldNode, newNode);
            }
        }
    }

    private ChronologicalViewNode getOldNode(ChronologicalViewNode newNode) {
        return oldVisualization.getNode(newNode.getIteration(), newNode.getVersion(), newNode.isEndPoint(), newNode.isDisconnected());
    }

    private void copyDetails(ChronologicalViewNode source, ChronologicalViewNode target) {
        target.setX(source.getX());
        target.setY(source.getY());
        target.setWidth(source.getWidth());
        target.setHeight(source.getHeight());
        target.setVisible(source.isVisible());
    }

    private void copyAssociationHandles() {
        for (ChronologicalViewAssociation newAssociation : newVisualization.getAssociations()) {
            ChronologicalViewAssociation oldAssociation = getOldAssociation(newAssociation);

            if (oldAssociation != null) {
                copyDetails(oldAssociation, newAssociation);
            }
        }
    }

    private ChronologicalViewAssociation getOldAssociation(ChronologicalViewAssociation newAssociation) {
        return oldVisualization.getAssociation(newAssociation.getSourceIteration(), newAssociation.getSourceVersion(),
                newAssociation.getTargetIteration(), newAssociation.getTargetVersion());
    }

    private void copyDetails(ChronologicalViewAssociation source, ChronologicalViewAssociation target) {
        target.setLabelX(source.getLabelX());
        target.setLabelY(source.getLabelY());

        for(Handle oldHandle : source.getHandles()) {
            Handle newHandle = new Handle();

            newHandle.setX(oldHandle.getX());
            newHandle.setY(oldHandle.getY());

            target.addHandle(newHandle);
        }
    }
}
