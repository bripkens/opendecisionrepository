
package nl.rug.search.odr.viewpoint;

import nl.rug.search.odr.entities.BaseEntity;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class Handle extends BaseEntity<Handle>{
    private static final long serialVersionUID = 1l;

    @RequiredFor(Viewpoint.RELATIONSHIP)
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




    public void setX(int x) {
        this.x = x;
    }




    public int getY() {
        return y;
    }




    public void setY(int y) {
        this.y = y;
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
