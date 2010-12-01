package nl.rug.search.odr.viewpoint.chronological;

import nl.rug.search.odr.viewpoint.relationship.*;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import nl.rug.search.odr.viewpoint.AbstractVisualization;
import nl.rug.search.odr.viewpoint.RequiredFor;
import nl.rug.search.odr.viewpoint.Viewpoint;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class ChronologicalViewVisualization extends AbstractVisualization {

    private static final long serialVersionUID = 1l;

    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    @RequiredFor(Viewpoint.RELATIONSHIP)
    private List<ChronologicalViewAssociation> associations;




    public ChronologicalViewVisualization() {
        associations = new ArrayList<ChronologicalViewAssociation>();
    }




    public void addAssociation(ChronologicalViewAssociation association) {
        associations.add(association);
    }




    public void removeAssociation(ChronologicalViewAssociation association) {
        associations.remove(association);
    }




    public List<ChronologicalViewAssociation> getAssociations() {
        return associations;
    }




    public void setAssociations(List<ChronologicalViewAssociation> associations) {
        this.associations = associations;
    }




    public void removeAllAssociations() {
        associations.clear();
    }





    @Override
    @Transient
    public Viewpoint getType() {
        return Viewpoint.CHRONOLOGICAL;
    }




}



