package uk.gov.hmcts.fees.register.api.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.fees.register.model.FeesRegister;
import uk.gov.hmcts.fees.register.model.FeesRegisterNotLoadedException;

@Repository
public class FeesRegisterRepository {

    private static final Logger LOG = LoggerFactory.getLogger(FeesRegisterRepository.class);

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    private FeesRegister feesRegister = null;

    @Value("${FEE_REGISTER_DB:}")
    private String dbFilePath;

    @Autowired
    public FeesRegisterRepository(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        BufferedReader reader = null;
        try {
            if (null != dbFilePath && !dbFilePath.trim().equals("")) {
                loadDBFromEnv();
            } else {
                loadEmbeddedJson();
            }
        } catch (IOException | NullPointerException e) {
            LOG.error("Loading of FeesRegister.json file failed");
            throw new FeesRegisterNotLoadedException("Loading of FeesRegister.json file failed", e);
        }
    }

    private void loadEmbeddedJson() throws IOException {
        InputStream fileAsStream = resourceLoader.getResource("classpath:FeesRegister.json").getInputStream();

        BufferedReader reader1 = null;
        reader1 = new BufferedReader(new InputStreamReader(fileAsStream, "UTF-8"));
        feesRegister = objectMapper.readValue(reader1, FeesRegister.class);

    }

    private void loadDBFromEnv() throws IOException {

        feesRegister = objectMapper.readValue(new File(dbFilePath), FeesRegister.class);
    }

    public FeesRegister getFeesRegister() {
        return feesRegister;
    }
}
