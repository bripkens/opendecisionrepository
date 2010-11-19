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
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Stefan
 * @modified Ben
 */
@NamedQueries(value = {
    @NamedQuery(name = "Concern.getAll",
                query = "SELECT c FROM Concern c"),
    @NamedQuery(name = Concern.NAMED_QUERY_FIND_SIMILAR_TAGS,
                query = "SELECT DISTINCT(t) FROM Concern AS c, IN (c.tags) t WHERE t LIKE :keyword")
})
@Entity
public class Concern extends BaseEntity<Concern> {

    public static final String NAMED_QUERY_FIND_SIMILAR_TAGS = "Concern.findSimilarTags";
    public static final String NAMES_QUERY_FIND_ALL_CONCERNS_FOR_PROJECT = "Concern.findAllConcernsForProject";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String externalId;
    @Column(nullable = false,
            length = 255)
    private String name;
    @Column(length = 1000)
    private String description;
    @Column(name = "parentId")
    private Long group;
//
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdWhen;
    //
    @ManyToMany
    private Collection<ProjectMember> initiators;
    @ElementCollection
    @CollectionTable(name = "tags")
    private Collection<String> tags;




    public Concern() {
        initiators = new ArrayList<ProjectMember>();
        tags = new ArrayList<String>();
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




    public String getDescription() {
        return description;
    }




    public void setDescription(String description) {
        this.description = description;
    }




    public Long getGroup() {
        return group;
    }




    public void setGroup(Long group) {
        if (group == null) {
            throw new BusinessException("Please provide a previous version");
        }
        this.group = group;
    }




    public String getName() {
        return name;
    }




    public void setName(String name) {
        StringValidator.isValid(name);
        this.name = name;
    }




    public Collection<ProjectMember> getInitiators() {
        return Collections.unmodifiableCollection(initiators);
    }




    public void setInitiators(Collection<ProjectMember> initiators) {
        if (initiators == null) {
            throw new BusinessException("Initiators is null");
        }

        this.initiators = initiators;
    }




    public void addInitiator(ProjectMember initiator) {
        if (initiator == null) {
            throw new BusinessException("Initiator is null");
        }

        this.initiators.add(initiator);
    }




    public void removeInitiator(ProjectMember initiator) {
        if (initiator == null) {
            throw new BusinessException("Initiator is null");
        }

        this.initiators.remove(initiator);
    }




    public void removeAllInitiators() {
        initiators.clear();
    }




    public String getExternalId() {
        return externalId;
    }




    public void setExternalId(String externalId) {
        StringValidator.isValid(externalId);
        this.externalId = externalId;
    }




    public Collection<String> getTags() {
        return tags;
    }




    public void addTag(String tag) {
        if (tag == null) {
            throw new BusinessException("Please provide a tag");
        }
        tags.add(tag);
    }




    public void removeTag(String tag) {
        if (tag == null) {
            throw new BusinessException("Please provide a tag");
        }
        tags.remove(tag);
    }




    public void removeAllTags() {
        tags.clear();
    }




    public void setTags(Collection<String> tags) {
        if (tags == null) {
            throw new BusinessException("Initiator is null");
        }
        this.tags = tags;
    }




    public Date getCreatedWhen() {
        return createdWhen;
    }




    public void setCreatedWhen(Date createdWhen) {
        if (createdWhen == null) {
            throw new BusinessException("Initiator is null");
        }
        this.createdWhen = createdWhen;
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{name, description};
    }




    @Override
    public boolean isPersistable() {
        return name != null && !initiators.isEmpty() && (createdWhen != null);
    }

    public static class NameComparator implements Comparator<Concern> {

        @Override
        public int compare(Concern o1, Concern o2) {
            return o1.name.compareToIgnoreCase(o2.name);
        }
    }

    public static class ExternalIdComparator implements Comparator<Concern> {

        @Override
        public int compare(Concern o1, Concern o2) {
            return o1.externalId.compareTo(o2.externalId);
        }
    }
}
