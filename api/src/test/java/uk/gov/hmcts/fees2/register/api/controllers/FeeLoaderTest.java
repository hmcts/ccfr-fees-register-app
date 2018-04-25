package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.util.FeeLoader;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by tarun on 15/11/2017.
 *
 * Fee loader test
 */
public class FeeLoaderTest extends BaseIntegrationTest {

    @Mock
    private ApplicationArguments args;

    @Autowired
    private FeeLoader feeLoader;


    @Test
    public void testCMCFeeLoading_1() throws Exception{
        // Run the fee loader
        feeLoader.run(args);

        restActions
            .get("/fees-register/fees?channel=default&service=civil money claims")
            .andExpect(status().isOk())
            .andExpect(body().asListOf(Fee2Dto.class, fee2Dtos -> {
                fee2Dtos.stream().forEach(f -> {
                    assertThat(f.getServiceTypeDto().getName()).isEqualTo("civil money claims");
                });
            }));
    }
}
