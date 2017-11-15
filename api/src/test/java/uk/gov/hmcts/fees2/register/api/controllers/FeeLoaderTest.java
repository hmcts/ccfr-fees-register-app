package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.controllers.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.api.controllers.FeeController;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.util.FeeLoader;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by tarun on 15/11/2017.
 */
public class FeeLoaderTest extends BaseIntegrationTest {

    @Mock
    private ApplicationArguments args;

    @Autowired
    private FeeLoader feeLoader;

    @Test
    public void testCMCFeeLoading() throws Exception{
        // Delete the Fee:X0001 if it already exists
        deleteFee("X0001");

        // Run the fee loader
        feeLoader.run(args);

        restActions
            .get(URIUtils.getUrlForGetMethod(FeeController.class, "search"), "channel=default&service=civil money claims")
            .andExpect(status().isOk())
            .andExpect(body().asListOf(Fee2Dto.class, fee2Dtos -> {
                assertThat(fee2Dtos).anySatisfy(fee2Dto -> {
                    assertThat(fee2Dto.getCode()).isEqualTo("X0001");
                    assertThat(fee2Dto.getEventTypeDto().getName()).isEqualTo("issue");
                    assertThat(fee2Dto.getChannelTypeDto().getName()).isEqualTo("default");
                    assertThat(fee2Dto.getServiceTypeDto().getName()).isEqualTo("civil money claims");
                    assertThat(fee2Dto.getJurisdiction1Dto().getName()).isEqualTo("civil");
                    assertThat(fee2Dto.getJurisdiction2Dto().getName()).isEqualTo("county court");
                    assertThat(fee2Dto.getDirectionTypeDto().getName()).isEqualTo("enhanced");
                    assertThat(fee2Dto.getFeeVersionDtos().get(0).getFlatAmount().getAmount()).isEqualTo(new BigDecimal("35.00"));
                    assertThat(fee2Dto.getFeeVersionDtos().get(0).getStatus()).isEqualTo(FeeVersionStatus.approved);
                });
            }));

    }


}
