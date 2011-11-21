package nl.rug.search.odr.viewpoint.chronological;

import java.util.Comparator;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.viewpoint.AbstractNode;
import nl.rug.search.odr.viewpoint.RequiredFor;
import nl.rug.search.odr.viewpoint.Viewpoint;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class ChronologicalViewNode extends AbstractNode {

    private static final long serialVersionUID = 1l;

    @ManyToOne(optional = true)
    @RequiredFor(Viewpoint.CHRONOLOGICAL)
    private Version version;

    @ManyToOne(optional = true)
    @RequiredFor(Viewpoint.CHRONOLOGICAL)
    private Iteration iteration;

    @RequiredFor(Viewpoint.CHRONOLOGICAL)
    private boolean endPoint;

    @RequiredFor(Viewpoint.CHRONOLOGICAL)
    private boolean disconnected;




    public Iteration getIteration() {
        return iteration;
    }




    public void setIteration(Iteration iteration) {
        this.iteration = iteration;
    }




    public Version getVersion() {
        return version;
    }




    public void setVersion(Version version) {
        this.version = version;
    }




    public boolean isEndPoint() {
        return endPoint;
    }




    public void setEndPoint(boolean endPoint) {
        this.endPoint = endPoint;
    }




    public boolean isDisconnected() {
        return disconnected;
    }




    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }




    @Override
    public boolean isPersistable() {
        return (version != null || iteration != null) && !(version != null && iteration != null);
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{getX(), getY(), isVisible(), endPoint, disconnected};
    }




    public static class DateComparator implements Comparator<ChronologicalViewNode> {

        @Override
        public int compare(ChronologicalViewNode o1, ChronologicalViewNode o2) {
            Date d1 = null, d2 = null;

            if (o1.version != null) {
                d1 = o1.version.getDecidedWhen();
            } else {
                d1 = o1.iteration.getStartDate();
            }

            if (o2.version != null) {
                d2 = o2.version.getDecidedWhen();
            } else {
                d2 = o2.iteration.getStartDate();
            }

            return d1.compareTo(d2);
        }




    }




}



