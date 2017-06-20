package uk.gov.hmcts.fees.register.api.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.fees.register.api.repositories.FeesRegisterRepository;
import uk.gov.hmcts.fees.register.model.FeesRegister;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {FeesRegisterRepository.class, ObjectMapper.class})
public class FeesRegisterRepositoryFromEnvTest {
    @Autowired
    private DefaultResourceLoader defaultResourceLoader;

    @Autowired
    private FeesRegisterRepository feesRegisterRepository;
    private FeesRegister loadedRegister;

    @Before
    public void setUp() throws Exception {
        String path = defaultResourceLoader.getResource("classpath:FeesRegister.json").getURL().getFile();

        System.setProperty("FEE_REGISTER_DB", path);

        feesRegisterRepository.init();
        loadedRegister = feesRegisterRepository.getFeesRegister();
    }

    @Test
    public void shouldLoadFeesRegisterJson() {
        assertThat(loadedRegister.getServiceName()).isEqualTo("cmc");
        assertThat(loadedRegister.getCategories()).hasSize(2);
        assertThat(loadedRegister.getClaimCategory("hearingfees").get().getFlatFees()).hasSize(2);
    }

}
