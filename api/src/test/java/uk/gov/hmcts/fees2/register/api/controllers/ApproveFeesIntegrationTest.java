package uk.gov.hmcts.fees2.register.api.controllers;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.base.BaseIntegrationTest;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApproveFeesIntegrationTest extends BaseIntegrationTest {

    private CreateFixedFeeDto dto() {
        return new CreateFixedFeeDto()
            .setService("civil money claims")
            .setEvent("issue")
            .setJurisdiction1("civil")
            .setJurisdiction2("family court")
            .setChannel("online");
    }

    @Test
    public void testUnapprovedFeesAreRetrieved() throws Exception {
        CreateFixedFeeDto dto = dto();
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
                Fee2Dto fee = fee2Dtos.stream().filter(f -> f.getCode().equals(uri[3])).findAny().get();
                assertThat(fee.getCode()).isEqualTo(uri[3]);
            }));

        forceDeleteFee(uri[3]);

    }
}
