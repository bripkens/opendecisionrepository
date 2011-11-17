package nl.rug.search.odr.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;
import nl.rug.search.odr.viewpoint.RequiredFor;
import nl.rug.search.odr.viewpoint.Viewpoint;

/**
 *
 * @author Ben
 */
@NamedQueries(value = {
    @NamedQuery(name = "RelationshipType.getAll",
                query = "SELECT r FROM RelationshipType r"),
    @NamedQuery(name = RelationshipType.NAMED_QUERY_GET_PUBLIC_TYPES,
                query = "SELECT r FROM RelationshipType r WHERE r.common = TRUE")
})
@Entity
public class RelationshipType extends BaseEntity<RelationshipType> {

    private static final long serialVersionUID = 1l;

    public static final String NAMED_QUERY_GET_PUBLIC_TYPES = "RelationshipType.getPublicTypes";

    @RequiredFor(Viewpoint.RELATIONSHIP)
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @RequiredFor(Viewpoint.RELATIONSHIP)
    private String name;

    private boolean common;

    private String description;




    @Override
    public Long getId() {
        return id;
    }




    @Override
    public void setId(Long id) {
        if (id == null) {
            throw new BusinessException("Id is null");
        }

        this.id = id;
    }




    public boolean isCommon() {
        return common;
    }




    public void setCommon(boolean common) {
        this.common = common;
    }




    public String getName() {
        return name;
    }




    public void setName(String name) {
        StringValidator.isValid(name);
        this.name = name;
    }




    public String getDescription() {
        return description;
    }




    public void setDescription(String description) {
        this.description = description;
    }




    @Override
    public boolean isPersistable() {
        return name != null;
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{name, common, description};
    }
}
