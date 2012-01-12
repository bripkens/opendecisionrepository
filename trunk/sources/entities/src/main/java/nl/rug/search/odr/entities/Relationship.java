package nl.rug.search.odr.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.viewpoint.RequiredFor;
import nl.rug.search.odr.viewpoint.Viewpoint;

/**
 *
 * @author Ben
 */
@NamedQueries(value = {
    @NamedQuery(name = "Relationship.getAll",
                query = "SELECT r FROM Relationship r")
})
@Entity
public class Relationship extends BaseEntity<Relationship> {

    private static final long serialVersionUID = 1L;
  
    @RequiredFor(Viewpoint.RELATIONSHIP)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @RequiredFor(Viewpoint.RELATIONSHIP)
    @ManyToOne
    private RelationshipType type;

    @RequiredFor(Viewpoint.RELATIONSHIP)
    @ManyToOne
    private Version source;

    @RequiredFor(Viewpoint.RELATIONSHIP)
    @ManyToOne
    private Version target;




    public Version getTarget() {
        return target;
    }




    public void setTarget(Version target) {
        if (this.target != null) {
            this.target.internalRemoveIncomingRelationship(this);
        }

        this.target = target;

        if (this.target != null) {
            target.internalAddIncomingRelationship(this);
        }
    }




    public Version getSource() {
        return source;
    }




    public void setSource(Version source) {
        if (this.source != null) {
            this.source.internalRemoveOutgoingRelationship(this);
        }

        this.source = source;

        if (this.source != null) {
            source.internalAddOutgoingRelationship(this);
        }
    }




    public RelationshipType getType() {
        return type;
    }




    public void setType(RelationshipType type) {
        if (type == null) {
            throw new BusinessException("Type is null");
        }

        this.type = type;
    }




    @Override
    public Long getId() {
        return id;
    }




    @Override
    public void setId(Long id) {
        if (id == null) {
            throw new BusinessException("Id is null.");
        }
        this.id = id;
    }




    @Override
    public boolean isPersistable() {
        return type != null && target != null && source != null;
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{type, source, type};
    }
}
