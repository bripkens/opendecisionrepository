package nl.rug.search.odr.viewpoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nl.rug.search.odr.entities.BaseEntity;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class Visualization extends BaseEntity<Visualization>{

    private static final long serialVersionUID = 1l;

    @RequiredFor(Viewpoint.RELATIONSHIP)
    private Long id;

    @RequiredFor(Viewpoint.RELATIONSHIP)
    private String name;

    @RequiredFor(Viewpoint.RELATIONSHIP)
    private Date documentedWhen;

    @RequiredFor(Viewpoint.RELATIONSHIP)
    private Viewpoint type;

    @RequiredFor(Viewpoint.RELATIONSHIP)
    private List<Node> nodes;

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
        return new Object[] {documentedWhen, name, type};
    }
}
