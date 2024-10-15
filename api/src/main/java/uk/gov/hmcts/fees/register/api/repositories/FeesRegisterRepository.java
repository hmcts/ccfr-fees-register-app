package uk.gov.hmcts.fees.register.api.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.fees.register.legacymodel.FeesRegister;
import uk.gov.hmcts.fees.register.legacymodel.FeesRegisterNotLoadedException;

import java.io.IOException;
import java.io.InputStream;

@Repository
public class FeesRegisterRepository {

    private static final Logger LOG = LoggerFactory.getLogger(FeesRegisterRepository.class);

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    private FeesRegister feesRegister = null;

    @Value("${FEE_REGISTER_DB:classpath:FeesRegister.json}")
    private String dbFilePath;

    @Autowired
    public FeesRegisterRepository(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        try {
            LOG.info("Loading fees register from {}", dbFilePath);
            feesRegister = loadFromResource(dbFilePath);
            LOG.info("Fees register loaded from {}", dbFilePath);
        } catch (IOException | NullPointerException e) {
            LOG.error("Loading of FeesRegister.json file failed");
            throw new FeesRegisterNotLoadedException("Loading of FeesRegister.json file failed", e);
        }
    }

    private FeesRegister loadFromResource(String location) throws IOException {
        InputStream fileAsStream = resourceLoader.getResource(location).getInputStream();
        return objectMapper.readValue(fileAsStream, FeesRegister.class);
    }

    public FeesRegister getFeesRegister() {
        return feesRegister;
    }
}
