package nl.rug.search.odr.viewpoint;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import nl.rug.search.odr.entities.BaseEntity;
import nl.rug.search.odr.entities.Relationship;

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



