package nl.rug.search.odr.viewpoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import nl.rug.search.odr.entities.BaseEntity;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Entity
public class Visualization extends BaseEntity<Visualization> {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @RequiredFor(Viewpoint.RELATIONSHIP)
    private Long id;

    @RequiredFor(Viewpoint.RELATIONSHIP)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @RequiredFor(Viewpoint.RELATIONSHIP)
    private Date documentedWhen;

    @Enumerated(EnumType.STRING)
    @RequiredFor(Viewpoint.RELATIONSHIP)
    private Viewpoint type;

    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    @RequiredFor(Viewpoint.RELATIONSHIP)
    private List<Node> nodes;

    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    @RequiredFor(Viewpoint.RELATIONSHIP)
    private List<Association> associations;




    public Visualization() {
        nodes = new ArrayList<Node>();
        associations = new ArrayList<Association>();
    }




    public void addAssociation(Association association) {
        associations.add(association);
    }




    public void removeAssociation(Association association) {
        associations.remove(association);
    }




    public List<Association> getAssociations() {
        return associations;
    }




    public void setAssociations(List<Association> associations) {
        this.associations = associations;
    }




    public void removeAllAssociations() {
        associations.clear();
    }




    public Date getDocumentedWhen() {
        return documentedWhen;
    }




    public void setDocumentedWhen(Date documentedWhen) {
        this.documentedWhen = documentedWhen;
    }




    public String getName() {
        return name;
    }




    public void setName(String name) {
        this.name = name;
    }




    public void addNode(Node node) {
        nodes.add(node);
    }




    public void removeNode(Node node) {
        nodes.remove(node);
    }




    public List<Node> getNodes() {
        return nodes;
    }




    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }




    public boolean containsVersion(long versionId) {
        for (Node node : nodes) {
            if (node.getVersion().getId().equals(versionId)) {
                return true;
            }
        }

        return false;
    }




    public void removeAllNodes() {
        nodes.clear();
    }




    public Viewpoint getType() {
        return type;
    }




    public void setType(Viewpoint type) {
        this.type = type;
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
        return documentedWhen != null && type != null && !nodes.isEmpty();
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{documentedWhen, name, type};
    }
}
