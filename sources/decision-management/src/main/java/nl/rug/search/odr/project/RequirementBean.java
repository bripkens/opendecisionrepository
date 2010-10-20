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
public class RequirementBean extends GenericDaoBean<Requirement, Long> implements RequirementLocal {

    @PersistenceContext
    private EntityManager entityManager;




    @Override
    public boolean isPersistable(Requirement entity) {
        return entity.isPersistable();
    }
}
