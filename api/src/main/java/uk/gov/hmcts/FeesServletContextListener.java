package uk.gov.hmcts;


import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.logging.Markers;

public class FeesServletContextListener implements ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(FeesServletContextListener.class);

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.error(Markers.fatal, "Fees register application shutting down {}", sce);
    }
}
