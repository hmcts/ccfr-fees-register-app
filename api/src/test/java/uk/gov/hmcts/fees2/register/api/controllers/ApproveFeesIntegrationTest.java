package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApproveFeesIntegrationTest extends BaseIntegrationTest {

    private FixedFeeDto dto() {
        return new FixedFeeDto()
            .setService("civil money claims")
            .setEvent("issue")
            .setJurisdiction1("civil")
            .setJurisdiction2("family court")
            .setChannel("online");
    }

    @Test
    public void testUnapprovedFeesAreRetrieved() throws Exception {
        FixedFeeDto dto = dto();
        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setDescription("Hi");
        dto.setVersion(versionDto);

        String loc = restActions
            .withUser("admin")
            .post("/fees-register/fixed-fees", dto)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getHeader("Location");
        String[] uri = loc.split("/");

        restActions
            .get( "/fees-register/fees?feeVersionStatus=draft")
            .andExpect(status().isOk())
            .andExpect(body().asListOf(Fee2Dto.class, fee2Dtos -> {
                Fee2Dto fee = fee2Dtos.stream().filter(f -> uri[3].equals(f.getCode())).findAny().get();
                assertThat(fee.getCode()).isEqualTo(uri[3]);
            }));

        forceDeleteFee(uri[3]);
    }
}
