
package nl.rug.search.odr.viewpoint.chronological;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.viewpoint.AbstractAssociation;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class ChronologicalViewAssociation extends AbstractAssociation {


    @ManyToOne(optional=true)
    private Version sourceVersion;

    @ManyToOne(optional=true)
    private Iteration sourceIteration;

    @ManyToOne(optional=true)
    private Version targetVersion;

    @ManyToOne(optional=true)
    private Iteration targetIteration;



    @Override
    public boolean isPersistable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }




    @Override
    protected Object[] getCompareData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
