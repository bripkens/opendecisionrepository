
package nl.rug.search.odr.project;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.GenericDaoBean;
import nl.rug.search.odr.entities.Requirement;

/**
 *
 * @author Stefan
 */
@Stateless
public class RequirementBean extends GenericDaoBean<Requirement, Long> implements RequirementLocal{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void updateRequirement(Requirement sourceRequirement) {
        entityManager.merge(sourceRequirement);
    }

    @Override
    public void persistRequirement(Requirement r) {
        entityManager.persist(r);
    }

    @Override
    public boolean isPersistable(Requirement entity) {
        return true;
    }
}
