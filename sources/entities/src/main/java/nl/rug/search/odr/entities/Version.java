/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.entities;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import nl.rug.search.odr.BusinessException;

/**
 *
 * @author Stefan
 */
@Entity
public class Version extends BaseEntity<Version> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date documentedWhen;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date decidedWhen;

    private boolean removed;




    @Override
    public Long getId() {
        return id;
    }




    @Override
    public void setId(Long id) {
        if (id == null) {
            throw new BusinessException("Id is null");
        }

        this.id = id;
    }




    public Date getDecidedWhen() {
        return decidedWhen;
    }




    public void setDecidedWhen(Date decidedWhen) {
        if (decidedWhen == null) {
            throw new BusinessException("decidedWhen is null");
        }

        this.decidedWhen = decidedWhen;
    }




    public Date getDocumentedWhen() {
        return documentedWhen;
    }




    public void setDocumentedWhen(Date documentedWhen) {
        if (documentedWhen == null) {
            throw new BusinessException("documentedWhen is null");
        }

        this.documentedWhen = documentedWhen;
    }




    public boolean isRemoved() {
        return removed;
    }




    public void setRemoved(boolean removed) {
        this.removed = removed;
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{documentedWhen, decidedWhen, removed};
    }




    @Override
    public boolean isPersistable() {
        return documentedWhen != null && decidedWhen != null;
    }
}
