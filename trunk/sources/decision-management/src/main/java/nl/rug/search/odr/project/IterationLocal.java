package nl.rug.search.odr.project;

import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Project;

/**
 *
 * @author Stefan
 */
public interface IterationLocal extends GenericDaoLocal<Iteration, Long> {

    public void updateIteration(Iteration sourceIteration);

    public void addIteration(Project pr, Iteration i);

}
