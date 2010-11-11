
package nl.rug.search.odr.decision;

import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.Decision;

/**
 *
 * @author Stefan
 */
public interface DecisionLocal  extends GenericDaoLocal<Decision, Long>  {
    
    public boolean isNameUsed(String decisionName, long projectId);




    public nl.rug.search.odr.entities.Decision getByVersion(long versionId);

}
