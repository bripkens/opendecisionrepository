package nl.rug.search.odr.viewpoint;

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
public abstract class AbstractVisualization extends BaseEntity<AbstractVisualization> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @RequiredFor(Viewpoint.RELATIONSHIP)
    private Long id;

    @RequiredFor(Viewpoint.RELATIONSHIP)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @RequiredFor(Viewpoint.RELATIONSHIP)
    private Date documentedWhen;

    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    @RequiredFor(Viewpoint.RELATIONSHIP)
    private List<Node> nodes;




    public AbstractVisualization() {
        nodes = new ArrayList<Node>();
    }




    public void addNode(Node node) {
        nodes.add(node);
    }




    public boolean containsVersion(Version v) {
        for (Node node : nodes) {
            if (node.getVersion().equals(v)) {
                return true;
            }
        }
        return false;
    }




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




    public List<Node> getNodes() {
        return nodes;
    }




    public abstract Viewpoint getType();




    public void removeAllNodes() {
        nodes.clear();
    }




    public void removeNode(Node node) {
        nodes.remove(node);
    }




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




    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }




    @Override
    public boolean isPersistable() {
        return getDocumentedWhen() != null && !nodes.isEmpty();
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{documentedWhen, name, getType()};
    }




}



