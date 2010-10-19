/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Ben
 */
@Entity
public class Decision extends BaseEntity<Decision> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Version> versions;

    @ManyToOne
    private DecisionTemplate template;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<ComponentValue> values;

    @OneToOne
    private OprLink link;




    public Decision() {
        versions = new ArrayList<Version>();
        values = new ArrayList<ComponentValue>();
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




    public Collection<Version> getVersions() {
        return Collections.unmodifiableCollection(versions);
    }




    public void setVersions(Collection<Version> versions) {
        if (versions == null) {
            throw new BusinessException("Collection of versions is null");
        }

        this.versions = versions;
    }




    public void addVersion(Version version) {
        if (version == null) {
            throw new BusinessException("Version is null.");
        }

        versions.add(version);
    }




    public void removeAllVersions() {
        versions.clear();
    }




    public void removeVersion(Version version) {
        if (version == null) {
            throw new BusinessException("Version is null.");
        }

        versions.remove(version);
    }




    public DecisionTemplate getTemplate() {
        return template;
    }




    public void setTemplate(DecisionTemplate template) {
        if (template == null) {
            throw new BusinessException("The template may not be null");
        }

        this.template = template;
    }




    public Collection<ComponentValue> getValues() {
        return Collections.unmodifiableCollection(values);
    }




    public void setValues(Collection<ComponentValue> values) {
        if (values == null) {
            throw new BusinessException("Values are null");
        }

        this.values = values;
    }




    public void addValue(ComponentValue value) {
        if (value == null) {
            throw new BusinessException("Value is null");
        }

        values.add(value);
    }




    public void removeValue(ComponentValue value) {
        if (value == null) {
            throw new BusinessException("Value is null");
        }

        values.remove(value);
    }




    public void removeAllValues() {
        this.values.clear();
    }




    public OprLink getLink() {
        return link;
    }




    public void setLink(OprLink link) {
        this.link = link;
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{name, link};
    }




    @Override
    public boolean isPersistable() {
        return name != null && template != null && !versions.isEmpty();
    }
}
