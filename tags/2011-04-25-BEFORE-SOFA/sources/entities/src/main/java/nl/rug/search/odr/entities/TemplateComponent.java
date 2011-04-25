package nl.rug.search.odr.entities;

import javax.persistence.Column;
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
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@NamedQueries(value = {
    @NamedQuery(name = "TemplateComponent.getAll",
                query = "SELECT t FROM TemplateComponent t")
})
@Entity
public class TemplateComponent extends BaseEntity<TemplateComponent> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 50,
            nullable = false)
    private String label;

    @Column(name = "componentOrder")
    private int order;

    @Column(length=5000)
    private String localizationReference;




    public String getLabel() {
        return label;
    }




    public void setLabel(String label) {
        StringValidator.isValid(label);

        label = label.trim();

        if (label.length() <= 2 || label.length() > 50) {
            throw new BusinessException("Label is too long or too short.");
        }

        this.label = label;
    }




    public String getLocalizationReference() {
        return localizationReference;
    }




    public void setLocalizationReference(String localizationReference) {
        this.localizationReference = localizationReference;
    }




    public int getOrder() {
        return order;
    }




    public void setOrder(int order) {
        this.order = order;
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




    @Override
    public boolean isPersistable() {
        return label != null;
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{label, order, localizationReference};
    }
}
