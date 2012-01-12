package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@NamedQueries(value = {
    @NamedQuery(name = "DecisionTemplate.getAll",
                query = "SELECT dt FROM DecisionTemplate dt"),
    @NamedQuery(name = DecisionTemplate.NAMED_QUERY_IS_NAME_USED,
                query = "SELECT COUNT(t) FROM DecisionTemplate t WHERE LOWER(t.name) = :name"),
    @NamedQuery(name = DecisionTemplate.NAMED_QUERY_GET_BY_NAME,
                query = "SELECT t FROM DecisionTemplate t WHERE LOWER(t.name) = :name")
})
@Entity
public class DecisionTemplate extends BaseEntity<DecisionTemplate> {

    private static final long serialVersionUID = 1L;

    public static final String NAMED_QUERY_IS_NAME_USED = "DecisionTemplate.isNameUsed";

    public static final String NAMED_QUERY_GET_BY_NAME = "DecisionTemplate.getByName";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 50,
            nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Collection<TemplateComponent> components;




    public DecisionTemplate() {
        components = new ArrayList<TemplateComponent>();
    }




    @Override
    public Long getId() {
        return id;
    }




    @Override
    public void setId(Long id) {
        if (id == null) {
            throw new BusinessException("Id is null.");
        }

        this.id = id;
    }




    public String getName() {
        return name;
    }




    public void setName(String name) {
        StringValidator.isValid(name);

        String trimmedName = name.trim();

        if (trimmedName.length() <= 2 || trimmedName.length() > 50) {
            throw new BusinessException("Name is too long or too short.");
        }

        this.name = trimmedName;
    }




    public void addComponent(TemplateComponent component) {
        if (component == null) {
            throw new BusinessException("Component is null");
        }

        components.add(component);
    }




    public void removeComponent(TemplateComponent component) {
        if (component == null) {
            throw new BusinessException("Component is null");
        }

        components.remove(component);
    }




    public Collection<TemplateComponent> getComponents() {
        return Collections.unmodifiableCollection(components);
    }




    public void setComponents(Collection<TemplateComponent> components) {
        if (components == null) {
            throw new BusinessException("Components is null");
        }

        this.components = components;
    }




    public void removeAllComponents() {
        components.clear();
    }




    @Override
    public boolean isPersistable() {
        return name != null;
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{name};
    }

    public static class NameComparator implements Comparator<DecisionTemplate> {

        @Override
        public int compare(DecisionTemplate o1, DecisionTemplate o2) {
            return o1.name.compareToIgnoreCase(o2.name);
        }
    }
}
