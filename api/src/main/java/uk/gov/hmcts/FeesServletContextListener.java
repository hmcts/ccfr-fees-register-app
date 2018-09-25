package uk.gov.hmcts;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.logging.Markers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class FeesServletContextListener implements ServletContextListener{
    private static final Logger LOG = LoggerFactory.getLogger(FeesServletContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // unused
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.error(Markers.fatal, "Fees register application shutting down {}");
    }
}
