
package nl.rug.search.odr.viewpoint;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nl.rug.search.odr.entities.BaseEntity;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
public class Handle extends BaseEntity<Handle>{
    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @RequiredFor({Viewpoint.CHRONOLOGICAL, Viewpoint.RELATIONSHIP, Viewpoint.STAKEHOLDER_INVOLVEMENT})
    private int x;

    @RequiredFor({Viewpoint.CHRONOLOGICAL, Viewpoint.RELATIONSHIP, Viewpoint.STAKEHOLDER_INVOLVEMENT})
    private int y;




    @Override
    public Long getId() {
        return id;
    }




    @Override
    public void setId(Long id) {
        this.id = id;
    }




    public int getX() {
        return x;
    }




    public Handle setX(int x) {
        this.x = x;
        return this;
    }




    public int getY() {
        return y;
    }




    public Handle setY(int y) {
        this.y = y;
        return this;
    }




    @Override
    public boolean isPersistable() {
        return true;
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[] {x, y};
    }

    
}
