package nl.rug.search.odr.viewpoint.relationship;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import nl.rug.search.odr.entities.Relationship;
import nl.rug.search.odr.viewpoint.AbstractAssociation;
import nl.rug.search.odr.viewpoint.RequiredFor;
import nl.rug.search.odr.viewpoint.Viewpoint;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class RelationshipViewAssociation extends AbstractAssociation {

    private static final long serialVersionUID = 1l;

    @ManyToOne
    @RequiredFor(Viewpoint.RELATIONSHIP)
    private Relationship relationship;




    public Relationship getRelationship() {
        return relationship;
    }




    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }




    @Override
    public boolean isPersistable() {
        return relationship != null;
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{};
    }




}



