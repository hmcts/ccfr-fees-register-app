package uk.gov.hmcts.fees2.register.api.controllers;

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

        String loc = saveFeeAndCheckStatusIsCreated(dto);
        String[] uri = loc.split("/");

        restActions
            .get( "/fees-register/fees?feeVersionStatus=draft")
            .andExpect(status().isOk())
            .andExpect(body().asListOf(Fee2Dto.class, fee2Dtos -> {
                assertTrue(fee2Dtos.stream().filter(f -> f.getCode().equals(uri[3])).findAny().isPresent());
            }));

        forceDeleteFee(uri[3]);

    }
}
