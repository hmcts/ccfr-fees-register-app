package uk.gov.hmcts.fees2.register.api.controllers;

import org.junit.Test;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;
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
            .setDirection("license")
            .setJurisdiction2("family court")
            .setChannel("online")
            .setCode("XXX" + String.valueOf(System.currentTimeMillis()))
            .setMemoLine("description");
    }

    @Test
    public void testListPendingApprovalFees() throws Exception {

        CreateFixedFeeDto dto = dto();
        FeeVersionDto versionDto = new FeeVersionDto();
        versionDto.setFlatAmount(new FlatAmountDto(BigDecimal.TEN));
        versionDto.setDescription("Hi");
        dto.setVersion(versionDto);

        saveFeeAndCheckStatusIsCreated(dto);

        restActions
            .get(URIUtils.getUrlForGetMethod(FeeController.class, "getUnapprovedFees"))
            .andExpect(status().isOk())
            .andExpect(
                body().as(List.class, (list) ->
                    {
                        assertTrue(list.size() > 0);
                        assertTrue(list.stream().filter(o -> ((Map) o).get("code").equals(dto.getCode())).findAny().isPresent());
                    }
                )
            );
    }
}
