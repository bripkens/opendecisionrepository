package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    @NamedQuery(name = "DecisionTemplate.isNameUsed",
                query = "SELECT COUNT(t) FROM DecisionTemplate t WHERE LOWER(t.name) = :name")
})
@Entity
public class DecisionTemplate extends BaseEntity<DecisionTemplate> {

    private static final long serialVersionUID = 1L;

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

        name = name.trim();

        if (name.length() <= 2 || name.length() > 50) {
            throw new BusinessException("Name is too long or too short.");
        }

        this.name = name;
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
}
