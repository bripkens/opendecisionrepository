package nl.rug.search.odr;

import nl.rug.search.odr.entities.DecisionTemplate;

import javax.ejb.Local;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Local
public interface DecisionTemplateLocal extends GenericDaoLocal<DecisionTemplate, Long> {

    boolean isNameUsed(java.lang.String name);

    @java.lang.Override
    boolean isPersistable(DecisionTemplate entity);

    DecisionTemplate getByName(String name);

    DecisionTemplate getSmallestTemplate();
}
