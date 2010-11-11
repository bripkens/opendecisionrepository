package nl.rug.search.odr.entities;

import java.util.Comparator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import nl.rug.search.odr.BusinessException;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@NamedQueries(value = {
    @NamedQuery(name = "ComponentValue.getAll",
                query= "SELECT cv FROM ComponentValue cv")
})
@Entity
public class ComponentValue extends BaseEntity<ComponentValue> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 5000, name = "valueField")
    private String value;

    @ManyToOne
    private TemplateComponent component;




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




    public String getValue() {
        return value;
    }




    public void setValue(String value) {
        this.value = value;
    }




    public TemplateComponent getComponent() {
        return component;
    }




    public void setComponent(TemplateComponent component) {
        if (component == null) {
            throw new BusinessException("Component is null");
        }

        this.component = component;
    }




    @Override
    public boolean isPersistable() {
        return component != null;
    }




    @Override
    protected Object[] getCompareData() {
        return new Object[]{value};
    }



    public static class OrderComparator implements Comparator<ComponentValue> {

        @Override
        public int compare(ComponentValue o1, ComponentValue o2) {
            return o1.getComponent().getOrder() - o2.getComponent().getOrder();
        }
        

    }
}
