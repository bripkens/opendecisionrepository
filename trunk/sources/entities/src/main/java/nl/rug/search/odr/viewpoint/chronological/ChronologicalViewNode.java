package nl.rug.search.odr.viewpoint.chronological;

import java.util.Comparator;
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

    @ManyToOne(optional=true)
    @RequiredFor(Viewpoint.CHRONOLOGICAL)
    private Version version;

    @ManyToOne(optional=true)
    @RequiredFor(Viewpoint.CHRONOLOGICAL)
    private Iteration iteration;




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




    @Override
    public boolean isPersistable() {
        return (version != null || iteration != null) && !(version != null && iteration != null);
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{getX(), getY(), isVisible()};
    }



    public static class DecisionNameComparator implements Comparator<ChronologicalViewNode> {




        @Override
        public int compare(ChronologicalViewNode o1, ChronologicalViewNode o2) {
            return o1.version.getDecision().getName().compareToIgnoreCase(o2.version.getDecision().getName());
        }

    }
}
