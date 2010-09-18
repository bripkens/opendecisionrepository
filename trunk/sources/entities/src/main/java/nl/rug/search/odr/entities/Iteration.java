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
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Stefan
 */
@Entity
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
            throw new NullPointerException("please provide a version");
        }
        this.version = version;
    }

    public Long getIterationId() {
        return iterationId;
    }

    public void setId(Long id) {
        if (id == null) {
            throw new NullPointerException("please provide an id");
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
            throw new NullPointerException("please provide a endDate");
        } else if (startDate.getTime() > endDate.getTime()) {
            throw new RuntimeException("enddate has to be after startdate");
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
            throw new NullPointerException("Please provide a startDate");
        }
        if (endDate != null) {
            if (startDate.getTime() < endDate.getTime()) {
                this.startDate = startDate;
            } else {
                throw new RuntimeException("the startDate has to be before the endDate");
            }
        }
        this.startDate = startDate;

    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iterationId != null ? iterationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Iteration)) {
            return false;
        }
        Iteration other = (Iteration) object;
        if ((this.iterationId == null && other.iterationId != null) || (this.iterationId != null && !this.iterationId.equals(other.iterationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "nl.rug.search.odr.entities.Iteration[id=" + iterationId + "]";
    }
}
