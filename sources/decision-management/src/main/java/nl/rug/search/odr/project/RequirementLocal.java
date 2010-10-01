package nl.rug.search.odr.project;

import nl.rug.search.odr.GenericDaoLocal;
import nl.rug.search.odr.entities.Iteration;
import nl.rug.search.odr.entities.Requirement;

/**
 *
 * @author Stefan
 */
public interface RequirementLocal extends GenericDaoLocal<Requirement, Long> {

    public void updateRequirement(Requirement sourceRequirement);

    public void persistRequirement(Requirement r);
}
