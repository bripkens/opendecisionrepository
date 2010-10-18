
package nl.rug.search.odr;

import java.util.logging.Logger;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class DebugPhaseListener implements PhaseListener {

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        Logger.getLogger(this.getClass().getName()).fine("STARTING PHASE: ".concat(event.getPhaseId().toString()));
    }

    @Override
    public void afterPhase(PhaseEvent event) {
        Logger.getLogger(this.getClass().getName()).fine("ENDING PHASE: ".concat(event.getPhaseId().toString()));
    }

}