/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Stefan
 */
@Entity
public class Iteration extends BaseEntity<Iteration>{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;
    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<Version> versions;

    public Iteration() {
        versions = new ArrayList<Version>();
    }

    public Collection<Version> getVersions() {
        return versions;
    }

    public void setVersions(Collection<Version> versions) {
        this.versions = versions;
    }

    public void removeVersion(Version version) {
        if (version == null) {
            throw new BusinessException("remove version is null.");
        }
        versions.remove(version);
    }

    public void addVersion(Version version) {
        if (version == null) {
            throw new BusinessException("Version is null.");
        }
        versions.add(version);
    }

    public Long getId() {
        return id;
    }

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
        StringValidator.isValid(description);
        this.description = description;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (endDate == null) {
            throw new BusinessException("End date is null.");
        } else if (startDate != null && startDate.getTime() > endDate.getTime()) {
            throw new BusinessException("enddate has to be after startdate");
        }
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        StringValidator.isValid(name);
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        if (startDate == null) {
            throw new BusinessException("Start date is null.");
        }
        if (endDate != null) {
            if (startDate.getTime() < endDate.getTime()) {
                this.startDate = startDate;
            } else {
                throw new BusinessException("the startDate has to be before the endDate");
            }
        }
        this.startDate = startDate;

    }

    public boolean isPersistable() {
        if (name == null) {
            return false;
        }
        return true;
    }

    @Override
    protected Object[] getCompareData() {
        return new Object[]{name, description, startDate, endDate};
    }
}
