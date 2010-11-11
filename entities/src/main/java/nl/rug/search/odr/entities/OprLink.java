/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import nl.rug.search.odr.BusinessException;
import nl.rug.search.odr.StringValidator;

/**
 *
 * @author Stefan
 */
@NamedQueries(value = {
    @NamedQuery(name = "OprLink.getAll",
                query= "SELECT l FROM OprLink l")
})
@Entity
public class OprLink extends BaseEntity<OprLink> {

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




    @Override
    public boolean isPersistable() {
        return link != null;
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[] {link};
    }
}
