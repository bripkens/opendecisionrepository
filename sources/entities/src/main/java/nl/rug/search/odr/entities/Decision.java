/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Ben
 */
@NamedQueries(value = {
    @NamedQuery(name = "Decision.isNameUsed",
                query = "SELECT COUNT(d) FROM Decision d WHERE d.id = :id AND LOWER(d.name) = :name")
})
@Entity
public class Decision extends BaseEntity<Decision> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 50,
            nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Collection<Version> versions;

    @ManyToOne
    private DecisionTemplate template;

    @OneToMany(cascade = CascadeType.ALL,
               orphanRemoval = true)
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




    public Version getCurrentVersion() {
        if (versions.isEmpty()) {
            return null;
        }

        Version currentVersion = versions.iterator().
                next();

        for (Version v : versions) {
            if (v.getDecidedWhen().
                    after(currentVersion.getDecidedWhen())) {
                currentVersion = v;
            }
        }

        return currentVersion;
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{name, link};
    }




    @Override
    public boolean isPersistable() {
        return name != null && !versions.isEmpty();
    }

    public static class NameComparator implements Comparator<Decision> {

        @Override
        public int compare(Decision o1, Decision o2) {
            return o1.name.compareToIgnoreCase(o2.name);
        }
    }
}
