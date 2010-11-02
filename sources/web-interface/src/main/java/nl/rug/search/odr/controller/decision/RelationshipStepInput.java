
package nl.rug.search.odr.controller.decision;

import nl.rug.search.odr.entities.Decision;
import nl.rug.search.odr.entities.RelationshipType;
import nl.rug.search.odr.entities.Version;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class RelationshipStepInput {
    private Decision decision;

    private RelationshipType type;

    private Version version;




    public RelationshipStepInput() {
    }




    public Decision getDecision() {
        return decision;
    }




    public void setDecision(Decision decision) {
        this.decision = decision;
    }




    public RelationshipType getType() {
        return type;
    }




    public void setType(RelationshipType type) {
        this.type = type;
    }




    public Version getVersion() {
        return version;
    }




    public void setVersion(Version version) {
        this.version = version;
    }
}
