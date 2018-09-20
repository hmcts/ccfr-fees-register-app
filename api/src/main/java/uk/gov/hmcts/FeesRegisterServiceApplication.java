package uk.gov.hmcts;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FeesRegisterServiceApplication {
    private static final Logger LOG = LoggerFactory.getLogger(FeesRegisterServiceApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(FeesRegisterServiceApplication.class, args);
        } catch (RuntimeException ex) {
            Marker fatal = MarkerFactory.getMarker("FATAL");
            LOG.error(fatal, "Application crashed with error message: ", ex);
            throw ex;
        }
    }
}
