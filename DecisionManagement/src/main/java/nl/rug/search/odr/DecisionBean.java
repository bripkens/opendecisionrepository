/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.rug.search.odr;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.entities.Decision;

/**
 *
 * @author Stefan
 */
@Stateless
public class DecisionBean implements DecisionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    public void createDecision(Decision d) {
        entityManager.persist(d);
    }

}
