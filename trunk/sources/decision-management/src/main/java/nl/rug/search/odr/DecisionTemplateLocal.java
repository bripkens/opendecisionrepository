
package nl.rug.search.odr;

import javax.ejb.Local;
import nl.rug.search.odr.entities.DecisionTemplate;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Local
public interface DecisionTemplateLocal extends GenericDaoLocal<DecisionTemplate, Long> {




    public boolean isNameUsed(java.lang.String name);




    @java.lang.Override
    public boolean isPersistable(nl.rug.search.odr.entities.DecisionTemplate entity);
    
}
