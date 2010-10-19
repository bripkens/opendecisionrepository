package nl.rug.search.odr;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.entities.DecisionTemplate;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class DecisionTemplateBean extends GenericDaoBean<DecisionTemplate, Long> implements DecisionTemplateLocal {

    @PersistenceContext
    private EntityManager entityManager;




    @Override
    public boolean isPersistable(DecisionTemplate entity) {
        return entity != null && entity.isPersistable() && !isNameUsed(entity.getName());
    }




    @Override
    public boolean isNameUsed(String name) {
        name = name.trim().
                toLowerCase();

        return entityManager.createNamedQuery("DecisionTemplate.isNameUsed", Long.class).
                setParameter("name", name).
                getSingleResult() > 0;
    }
}
