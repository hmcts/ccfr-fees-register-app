package uk.gov.hmcts.fees.register.api.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.fees.register.api.repositories.FeesRegisterRepository;
import uk.gov.hmcts.fees.register.model.FeesRegister;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {FeesRegisterRepository.class, ObjectMapper.class})
public class FeesRegisterRepositoryTest {
    @Autowired
    private FeesRegisterRepository feesRegisterRepository;
    private FeesRegister loadedRegister;

    @Before
    public void setUp() throws Exception {
        loadedRegister = feesRegisterRepository.getFeesRegister();
    }

    @Test
    public void shouldLoadBuiltInFeesRegisterJson() {
        assertThat(loadedRegister.getCategories()).hasSize(2);
        assertThat(loadedRegister.getClaimCategory("hearingfees").get().getFlatFees()).hasSize(2);
    }

}
