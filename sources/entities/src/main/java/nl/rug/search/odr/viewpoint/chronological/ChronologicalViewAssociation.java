
package nl.rug.search.odr.viewpoint.chronological;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Version;
import nl.rug.search.odr.viewpoint.AbstractAssociation;
import nl.rug.search.odr.viewpoint.RequiredFor;
import nl.rug.search.odr.viewpoint.Viewpoint;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class ChronologicalViewAssociation extends AbstractAssociation {


    @RequiredFor(Viewpoint.CHRONOLOGICAL)
    @ManyToOne(optional=true)
    private Version sourceVersion;

    @RequiredFor(Viewpoint.CHRONOLOGICAL)
    @ManyToOne(optional=true)
    private Iteration sourceIteration;

    @RequiredFor(Viewpoint.CHRONOLOGICAL)
    @ManyToOne(optional=true)
    private Version targetVersion;

    @RequiredFor(Viewpoint.CHRONOLOGICAL)
    @ManyToOne(optional=true)
    private Iteration targetIteration;




    public Iteration getSourceIteration() {
        return sourceIteration;
    }




    public void setSourceIteration(Iteration sourceIteration) {
        this.sourceIteration = sourceIteration;
    }




    public Version getSourceVersion() {
        return sourceVersion;
    }




    public void setSourceVersion(Version sourceVersion) {
        this.sourceVersion = sourceVersion;
    }




    public Iteration getTargetIteration() {
        return targetIteration;
    }




    public void setTargetIteration(Iteration targetIteration) {
        this.targetIteration = targetIteration;
    }




    public Version getTargetVersion() {
        return targetVersion;
    }




    public void setTargetVersion(Version targetVersion) {
        this.targetVersion = targetVersion;
    }


    @Override
    public boolean isPersistable() {
        return (sourceVersion != null || sourceIteration != null) && !(sourceVersion != null && sourceIteration != null)
                && (targetVersion != null || targetIteration != null) && !(targetVersion != null && targetIteration != null);
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[] {};
    }

}
