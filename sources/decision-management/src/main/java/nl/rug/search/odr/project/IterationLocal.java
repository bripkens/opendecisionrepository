
package nl.rug.search.odr.project;

import java.util.Collection;
import nl.rug.search.odr.entities.Iteration;

/**
 *
 * @author Stefan
 */
public interface IterationLocal {
    
    void addIteration(Iteration i);

    public Collection<Iteration> getAllITerationsByProjectId(long projectId);
    
}
