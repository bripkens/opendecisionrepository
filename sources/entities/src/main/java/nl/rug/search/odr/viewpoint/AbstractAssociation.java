package nl.rug.search.odr.viewpoint;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import nl.rug.search.odr.entities.BaseEntity;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@MappedSuperclass
public abstract class AbstractAssociation extends BaseEntity<AbstractAssociation> {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @RequiredFor({Viewpoint.RELATIONSHIP, Viewpoint.CHRONOLOGICAL, Viewpoint.STAKEHOLDER_INVOLVEMENT})
    private Long id;

    @RequiredFor({Viewpoint.RELATIONSHIP, Viewpoint.CHRONOLOGICAL, Viewpoint.STAKEHOLDER_INVOLVEMENT})
    private int labelX;

    @RequiredFor({Viewpoint.RELATIONSHIP, Viewpoint.CHRONOLOGICAL, Viewpoint.STAKEHOLDER_INVOLVEMENT})
    private int labelY;

    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    @OrderColumn
    @RequiredFor({Viewpoint.RELATIONSHIP, Viewpoint.CHRONOLOGICAL, Viewpoint.STAKEHOLDER_INVOLVEMENT})
    private List<Handle> handles;




    public AbstractAssociation() {
        handles = new ArrayList<Handle>();
    }




    public void addHandle(Handle handle) {
        handles.add(handle);
    }




    public List<Handle> getHandles() {
        return handles;
    }




    @Override
    public Long getId() {
        return id;
    }




    public int getLabelX() {
        return labelX;
    }




    public int getLabelY() {
        return labelY;
    }




    public void removeAllHandles() {
        handles.clear();
    }




    public void removeHandle(Handle handle) {
        handles.remove(handle);
    }




    public void setHandles(List<Handle> handles) {
        this.handles = handles;
    }




    @Override
    public void setId(Long id) {
        this.id = id;
    }




    public void setLabelX(int labelX) {
        this.labelX = labelX;
    }




    public void setLabelY(int labelY) {
        this.labelY = labelY;
    }




}



