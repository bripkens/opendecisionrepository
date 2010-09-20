/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author Stefan
 */
//@Entity
public class Iteration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long iterationId;
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

    @ManyToMany
    private Version version;

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        if(version == null){
            throw new BusinessException("Version is null.");
        }
        this.version = version;
    }

    public Long getIterationId() {
        return iterationId;
    }

    public void setId(Long id) {
        if (id == null) {
            throw new BusinessException("Id is null.");
        }
        this.iterationId = id;
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
        if(startDate == null){
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(iterationId).append(name).append(description).append(startDate).
                append(endDate).toHashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (object.getClass() != getClass()) {
            return false;
        }

        Iteration ac = (Iteration) object;
        return new EqualsBuilder().
                append(iterationId, ac.iterationId).
                append(name, ac.name).
                append(description, ac.description).
                append(startDate, ac.startDate).
                append(endDate, ac.endDate).
                isEquals();
    }

    @Override
    public String toString() {
        return "Iteration{" + "id=" + iterationId + "type=" + name + '}';
    }
}
