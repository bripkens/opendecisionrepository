/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import nl.rug.search.odr.viewpoint.RequiredFor;
import nl.rug.search.odr.viewpoint.Viewpoint;

/**
 *
 * @author Ben
 */
@NamedQueries(value = {
    @NamedQuery(name = "Decision.getAll",
                query = "SELECT d FROM Decision d"),
    @NamedQuery(name = Decision.NAMED_QUERY_IS_NAME_USED,
                query = "SELECT COUNT(d) FROM Decision d WHERE d.id = :id AND LOWER(d.name) = :name"),
    @NamedQuery(name = Decision.NAMED_QUERY_GET_BY_VERSION,
                query = "SELECT d FROM Decision AS d, IN(d.versions) v WHERE v.id = :id")
})
@Entity
public class Decision extends BaseEntity<Decision> {

    private static final long serialVersionUID = 1L;

    public static final String NAMED_QUERY_IS_NAME_USED = "Decision.isNameUsed";

    public static final String NAMED_QUERY_GET_BY_VERSION = "Decision.getByVersion";

    @RequiredFor({Viewpoint.CHRONOLOGICAL, Viewpoint.RELATIONSHIP})
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @RequiredFor({Viewpoint.CHRONOLOGICAL, Viewpoint.RELATIONSHIP})
    @Column(length = 50,
            nullable = false)
    private String name;

    @RequiredFor({Viewpoint.CHRONOLOGICAL, Viewpoint.RELATIONSHIP})
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




    public Version getVersion(long versionId) {
        for (Version version : versions) {
            if (version.getId() != null && version.getId().equals(versionId)) {
                return version;
            }
        }

        return null;
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

        Version currentVersion = null;

        for (Version v : versions) {
            if (currentVersion == null && !v.isRemoved()) {
                currentVersion = v;
            } else if (!v.isRemoved() && v.getDecidedWhen().after(currentVersion.getDecidedWhen())) {
                currentVersion = v;
            }
        }

        return currentVersion;
    }




    public Version getFirstVersion() {
        if (versions.isEmpty()) {
            return null;
        }

        Version firstVersion = null;

        for (Version v : versions) {
            if (firstVersion == null && !v.isRemoved()) {
                firstVersion = v;
            } else if (!v.isRemoved() && v.getDecidedWhen().before(firstVersion.getDecidedWhen())) {
                firstVersion = v;
            }
        }

        return firstVersion;
    }




    public boolean isRemoved() {
        for (Version version : versions) {
            if (!version.isRemoved()) {
                return false;
            }
        }

        return true;
    }




    public void remove() {
        for (Version version : versions) {
            version.setRemoved(true);
        }
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{name, link};
    }




    public Date getLatestDocumentation() {
        Date latestDocumentation = null;

        for (Version version : versions) {
            if (latestDocumentation == null || latestDocumentation.before(version.getDocumentedWhen())) {
                latestDocumentation = version.getDocumentedWhen();
            }
        }

        return latestDocumentation;
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

    public static class DocumentedWhenComparator implements Comparator<Decision> {

        @Override
        public int compare(Decision o1, Decision o2) {
            return o1.getLatestDocumentation().compareTo(o2.getLatestDocumentation());
        }
    }
}
