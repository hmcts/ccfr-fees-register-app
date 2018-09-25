package uk.gov.hmcts;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.logging.Markers;

@Component
public class FeesApplicationListener implements ApplicationListener<ContextClosedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(FeesApplicationListener.class);

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        LOG.error(Markers.fatal, "Fees register application closing context with object: {}", event.getSource());
    }
}
