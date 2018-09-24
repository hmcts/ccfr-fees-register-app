package uk.gov.hmcts;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import uk.gov.hmcts.logging.Markers;

import javax.servlet.ServletContextListener;
import javax.validation.constraints.NotNull;

@SpringBootApplication
public class FeesRegisterServiceApplication {
    private static final Logger LOG = LoggerFactory.getLogger(FeesRegisterServiceApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(FeesRegisterServiceApplication.class, args);
        } catch (RuntimeException ex) {
            LOG.error(Markers.fatal, "Application crashed with error message: ", ex);
            throw ex;
        }
    }

    @NotNull
    @Bean
    ServletListenerRegistrationBean<ServletContextListener> myServletListener() {
        ServletListenerRegistrationBean<ServletContextListener> srb = new ServletListenerRegistrationBean<>();
        srb.setListener(new FeesServletContextListener());
        return srb;
    }
}
