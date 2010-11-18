package nl.rug.search.odr.viewpoint;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.Project;
import nl.rug.search.odr.entities.Relationship;
import nl.rug.search.odr.entities.Version;
import org.eclipse.persistence.descriptors.changetracking.ChangeTracker;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Stateless
public class CircularReferenceResolution implements CircularReferenceResolutionLocal {

    @PersistenceContext
    private EntityManager manager;




    @Override
    public CircularReferenceResolution dissolveCircularRelationships(Project p) {

        for (Decision d : p.getDecisions()) {
            for (Version v : d.getVersions()) {
                for (Relationship r : v.getRelationships()) {
                    manager.detach(r);

                    Version originalVersion = r.getTarget();

                    Version clonedVersion = new Version();
                    clonedVersion.setId(originalVersion.getId());
                    clonedVersion.setDocumentedWhen(originalVersion.getDocumentedWhen());
                    clonedVersion.setDecidedWhen(originalVersion.getDecidedWhen());
                    clonedVersion.setRemoved(originalVersion.isRemoved());
                    clonedVersion.setState(originalVersion.getState());
                    clonedVersion.setConcerns(originalVersion.getConcerns());
                    clonedVersion.setInitiators(originalVersion.getInitiators());

                    r.setTarget(clonedVersion);
                }
            }
        }

        return this;
    }
}
