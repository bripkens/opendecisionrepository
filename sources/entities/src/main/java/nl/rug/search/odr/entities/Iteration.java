/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.entities;

import java.util.Comparator;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Stefan
 */
@Entity
public class Iteration extends BaseEntity<Iteration> {

    private static final long serialVersionUID = 1L;
//
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
//
    @Column(length = 50, nullable = false)
    private String name;
//
    @Column(length = 1000)
    private String description;
//
    @Column (nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date documentedWhen;
//
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date startDate;
//
    @Column
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date endDate;
//
    @OneToOne
    ProjectMember projectMember;


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
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDocumentedWhen() {
        return documentedWhen;
    }

    public void setDocumentedWhen(Date documentedWhen) {
        this.documentedWhen = documentedWhen;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (endDate == null) {
            throw new BusinessException("End date is null.");
        } else if (startDate != null && startDate.getTime() >= endDate.getTime()) {
            throw new BusinessException("enddate has to be after startdate");
        }
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        if (startDate == null) {
            throw new BusinessException("Start date is null.");
        } else if (endDate != null && startDate.getTime() > endDate.getTime()) {
            throw new BusinessException("the startDate has to be before the endDate");
        }
        this.startDate = startDate;
    }

    public ProjectMember getProjectMember() {
        return projectMember;
    }

    public void setProjectMember(ProjectMember projectMember) {
        if(projectMember == null){
            throw new BusinessException("Please provide a projectMember");
        }
        this.projectMember = projectMember;
    }

    @Override
    public boolean isPersistable() {
        if (name == null || startDate == null || endDate == null || documentedWhen == null || projectMember == null) {
            return false;
        }
        return true;
    }

    public long getDurationInDays() {
        if (startDate == null) {
            throw new BusinessException("can calculate duration without a startDate");
        }
        long endMilliseconds = 0;
        if (endDate != null) {
            endMilliseconds = endDate.getTime();
        } else {
            endMilliseconds = new Date().getTime();
        }

        long duration = 0;
        duration = endMilliseconds - startDate.getTime();
        duration /= 24 * 60 * 60 * 1000;
        return duration;
    }

    @Override
    protected Object[] getCompareData() {
        return new Object[]{name, description, startDate, endDate, documentedWhen, projectMember};

    }

    public static class EndDateComparator implements Comparator<Iteration> {

        @Override
        public int compare(Iteration o1, Iteration o2) {
            if (o1.endDate == null && o2.endDate == null) {
                return 0;
            } else if (o1.endDate == null) {
                return -1;
            } else if (o2.endDate == null) {
                return 1;
            }
            return o1.endDate.compareTo(o2.endDate);
        }
    }
}
