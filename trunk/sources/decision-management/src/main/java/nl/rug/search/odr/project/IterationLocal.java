package nl.rug.search.odr.project;

import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.Iteration;

/**
 *
 * @author Stefan
 */
public interface IterationLocal extends GenericDaoLocal<Iteration, Long> {

    public boolean isIntersection(Iteration entity, long projectId);




}



