package nl.rug.search.odr.viewpoint;

import nl.rug.search.odr.viewpoint.relationship.RelationshipViewNode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import nl.rug.search.odr.entities.BaseEntity;
import nl.rug.search.odr.entities.Version;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@MappedSuperclass
public abstract class AbstractVisualization<C extends BaseEntity<C>, N extends AbstractNode, A extends AbstractAssociation> extends BaseEntity<C> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @RequiredFor({Viewpoint.RELATIONSHIP, Viewpoint.CHRONOLOGICAL, Viewpoint.STAKEHOLDER_INVOLVEMENT})
    private Long id;

    @RequiredFor({Viewpoint.RELATIONSHIP, Viewpoint.CHRONOLOGICAL, Viewpoint.STAKEHOLDER_INVOLVEMENT})
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @RequiredFor({Viewpoint.RELATIONSHIP, Viewpoint.CHRONOLOGICAL, Viewpoint.STAKEHOLDER_INVOLVEMENT})
    private Date documentedWhen;

//    Commented out as this would reduce code duplication but it doesn't work with JPA
//    @OneToMany(cascade = CascadeType.ALL,
//               orphanRemoval = true)
//    @RequiredFor({Viewpoint.RELATIONSHIP, Viewpoint.CHRONOLOGICAL, Viewpoint.STAKEHOLDER_INVOLVEMENT})
//    private List<N> nodes;
//
//    @OneToMany(cascade = CascadeType.ALL,
//               orphanRemoval = true)
//    @RequiredFor({Viewpoint.RELATIONSHIP, Viewpoint.CHRONOLOGICAL, Viewpoint.STAKEHOLDER_INVOLVEMENT})
//    private List<A> associations;



//    public AbstractVisualization() {
//        nodes = new ArrayList<N>();
//        associations = new ArrayList<A>();
//    }
//
//
//
//
//    public void addNode(N node) {
//        nodes.add(node);
//    }
    public Date getDocumentedWhen() {
        return documentedWhen;
    }




    @Override
    public Long getId() {
        return id;
    }




    public String getName() {
        return name;
    }




//    public List<N> getNodes() {
//        return nodes;
//    }
//
//
//
//
//    public void removeAllNodes() {
//        nodes.clear();
//    }
//
//
//
//
//    public void removeNode(N node) {
//        nodes.remove(node);
//    }
    public void setDocumentedWhen(Date documentedWhen) {
        this.documentedWhen = documentedWhen;
    }




    @Override
    public void setId(Long id) {
        this.id = id;
    }




    public void setName(String name) {
        this.name = name;
    }




//    public void setNodes(List<N> nodes) {
//        this.nodes = nodes;
//    }
//
//
//
//
//    public void addAssociation(A association) {
//        associations.add(association);
//    }
//
//
//
//
//    public void removeAssociation(A association) {
//        associations.remove(association);
//    }
//
//
//
//
//    public List<A> getAssociations() {
//        return associations;
//    }
//
//
//
//
//    public void setAssociations(List<A> associations) {
//        this.associations = associations;
//    }
//
//
//
//
//    public void removeAllAssociations() {
//        associations.clear();
//    }
    public abstract void setNodes(List<N> nodes);




    public abstract List<N> getNodes();




    public abstract void removeAllNodes();




    public abstract void removeNode(N node);




    public abstract void addNode(N node);




    public abstract void setAssociations(List<A> associations);




    public abstract List<A> getAssociations();




    public abstract void removeAllAssociations();




    public abstract void removeAssociation(A association);




    public abstract void addAssociation(A association);




    @Override
    public boolean isPersistable() {
        return getDocumentedWhen() != null && !getNodes().isEmpty();
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{documentedWhen, name};
    }




}



