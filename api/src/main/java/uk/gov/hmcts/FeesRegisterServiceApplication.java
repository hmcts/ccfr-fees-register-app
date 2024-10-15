package uk.gov.hmcts;


import jakarta.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import uk.gov.hmcts.logging.Markers;

@SpringBootApplication
public class FeesRegisterServiceApplication {
    static {
        System.setProperty("java.awt.headless", "true");
    }
    private static final Logger LOG = LoggerFactory.getLogger(FeesRegisterServiceApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(FeesRegisterServiceApplication.class, args);
        } catch (RuntimeException ex) {
            LOG.error(Markers.fatal, "Application crashed with error message: ", ex);
        }
    }

    @Bean
    ServletListenerRegistrationBean<ServletContextListener> myServletListener() {
        ServletListenerRegistrationBean<ServletContextListener> srb = new ServletListenerRegistrationBean<>();
        srb.setListener(new FeesServletContextListener());
        return srb;
    }
}
