package nl.rug.search.odr.viewpoint;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Association extends BaseEntity<Association> {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @RequiredFor(Viewpoint.RELATIONSHIP)
    private Long id;

    @ManyToOne
    @RequiredFor(Viewpoint.RELATIONSHIP)
    private Relationship relationship;

    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    @OrderColumn
    @RequiredFor(Viewpoint.RELATIONSHIP)
    private List<Handle> handles;




    public Association() {
        handles = new ArrayList<Handle>();
    }





    public void addHandle(Handle handle) {
        handles.add(handle);
    }




    public void removeHandle(Handle handle) {
        handles.remove(handle);
    }




    public List<Handle> getHandles() {
        return handles;
    }




    public void setHandles(List<Handle> handles) {
        this.handles = handles;
    }




    public void removeAllHandles() {
        handles.clear();
    }




    public Relationship getRelationship() {
        return relationship;
    }




    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }




    @Override
    public Long getId() {
        return id;
    }




    @Override
    public void setId(Long id) {
        this.id = id;
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
