package nl.rug.search.odr;

import java.util.List;
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

        return entityManager.createNamedQuery(DecisionTemplate.NAMED_QUERY_IS_NAME_USED, Long.class).
                setParameter("name", name).
                getSingleResult() > 0;
    }




    @Override
    public DecisionTemplate getByName(String name) {
        name = name.trim().
                toLowerCase();

        return entityManager.createNamedQuery(DecisionTemplate.NAMED_QUERY_GET_BY_NAME, DecisionTemplate.class).
                setParameter("name", name).
                getSingleResult();
    }



    @Override
    public DecisionTemplate getSmallestTemplate() {
        DecisionTemplate template = null;

        for(DecisionTemplate currentTemplate : getAll()) {
            if (template == null) {
                template = currentTemplate;
            } else if (currentTemplate.getComponents().size() < template.getComponents().size()) {
                template = currentTemplate;
            }
        }

        return template;
    }
}
