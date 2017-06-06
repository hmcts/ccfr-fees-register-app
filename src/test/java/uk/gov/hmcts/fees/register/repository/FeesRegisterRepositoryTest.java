package uk.gov.hmcts.fees.register.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.fees.register.model.FeesRegister;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.fees.register.model.FixedFee.fixedFeeWith;

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
        assertThat(loadedRegister.getServiceName()).isEqualTo("cmc");
        assertThat(loadedRegister.getCategories()).hasSize(2);
        assertThat(loadedRegister.getFlatFees()).hasSize(2);
    }

    @Test
    public void fixedAmountShouldBeLoaded() {
        assertThat(loadedRegister.getFlatFees().get(0)).isEqualTo(
                fixedFeeWith()
                        .id("X0046")
                        .amount(109000)
                        .description("Civil Court fees - Hearing fees - Multi track claim")
                        .build()
        );
    }
}