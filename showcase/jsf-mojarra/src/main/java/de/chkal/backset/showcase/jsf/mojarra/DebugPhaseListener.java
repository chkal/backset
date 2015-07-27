package de.chkal.backset.showcase.jsf.mojarra;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This PhaseListener is used to display the JSF phases.
 */
public class DebugPhaseListener implements PhaseListener {

    private static final Logger log = LoggerFactory.getLogger(DebugPhaseListener.class);

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        log.debug("*** " + event.getPhaseId().getName() + " ***");
    }

    @Override
    public void afterPhase(PhaseEvent event) {
        log.debug("    " + event.getPhaseId().getName());
    }

}
