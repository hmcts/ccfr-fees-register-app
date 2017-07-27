package uk.gov.hmcts.fees.register.api.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import uk.gov.hmcts.fees.register.api.repositories.FeesRegisterRepository;
import uk.gov.hmcts.fees.register.legacymodel.FeesRegister;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {FeesRegisterRepository.class, ObjectMapper.class})
public class FeesRegisterRepositoryFromEnvTest {
    @Autowired
    private FeesRegisterRepository feesRegisterRepository;
    private FeesRegister loadedRegister;

    static {
        try {
            String path = ResourceUtils.getFile("classpath:FeesRegister.json").getAbsolutePath();
            System.setProperty("FEE_REGISTER_DB", "file:" + path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setUp() throws Exception {
        feesRegisterRepository.init();
        loadedRegister = feesRegisterRepository.getFeesRegister();
    }

    @Test
    public void shouldLoadFeesRegisterJson() {
        assertThat(loadedRegister.getCategories()).hasSize(5);
        assertThat(loadedRegister.getClaimCategory("hearingfees").get().getFlatFees()).hasSize(2);
    }

}
