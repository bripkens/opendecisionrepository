package nl.rug.search.odr.viewpoint;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import nl.rug.search.odr.entities.BaseEntity;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@MappedSuperclass
public abstract class AbstractNode extends BaseEntity<AbstractNode> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @RequiredFor({Viewpoint.RELATIONSHIP, Viewpoint.CHRONOLOGICAL, Viewpoint.STAKEHOLDER_INVOLVEMENT})
    private Long id;

    @RequiredFor({Viewpoint.RELATIONSHIP, Viewpoint.CHRONOLOGICAL, Viewpoint.STAKEHOLDER_INVOLVEMENT})
    private int x;

    @RequiredFor({Viewpoint.RELATIONSHIP, Viewpoint.CHRONOLOGICAL, Viewpoint.STAKEHOLDER_INVOLVEMENT})
    private int y;

    @RequiredFor({Viewpoint.RELATIONSHIP, Viewpoint.CHRONOLOGICAL, Viewpoint.STAKEHOLDER_INVOLVEMENT})
    private boolean visible;







    @Override
    public Long getId() {
        return id;
    }




    public int getX() {
        return x;
    }




    public int getY() {
        return y;
    }




    public boolean isVisible() {
        return visible;
    }




    @Override
    public void setId(Long id) {
        this.id = id;
    }




    public void setVisible(boolean visible) {
        this.visible = visible;
    }




    public void setX(int x) {
        this.x = x;
    }




    public void setY(int y) {
        this.y = y;
    }




}



