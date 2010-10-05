/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.odr.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Stefan
 */
//@Entity
public class OprLink implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        StringValidator.isValid(link);
        this.link = link;
    }

    public Long getOprLinkId() {
        return id;
    }

    public void setOprLinkId(Long oprLinkId) {
        this.id = oprLinkId;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OprLink)) {
            return false;
        }
        OprLink other = (OprLink) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "nl.rug.search.odr.entities.OprLink[id=" + id + "]";
    }

}
