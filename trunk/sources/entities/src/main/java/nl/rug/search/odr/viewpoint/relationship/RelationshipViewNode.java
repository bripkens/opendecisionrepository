package nl.rug.search.odr.viewpoint.relationship;

import java.util.Comparator;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
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
public class RelationshipViewNode extends AbstractNode {

    private static final long serialVersionUID = 1l;

    @ManyToOne
    @RequiredFor(Viewpoint.RELATIONSHIP)
    private Version version;

    




    public Version getVersion() {
        return version;
    }




    public void setVersion(Version version) {
        this.version = version;
    }




    @Override
    public boolean isPersistable() {
        return version != null;
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{getX(), getY(), isVisible()};
    }



    public static class DecisionNameComparator implements Comparator<RelationshipViewNode> {




        @Override
        public int compare(RelationshipViewNode o1, RelationshipViewNode o2) {
            return o1.version.getDecision().getName().compareToIgnoreCase(o2.version.getDecision().getName());
        }

    }
}
