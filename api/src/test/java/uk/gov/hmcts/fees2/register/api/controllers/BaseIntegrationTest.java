package uk.gov.hmcts.fees2.register.api.controllers;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.data.model.ChannelType;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BaseIntegrationTest extends BaseTest{

    protected ResultActions getFeeAndExpectStatusIsOk(String code) throws Exception {
        return restActions
            .get(URIUtils.getUrlForGetMethod(FeeController.class, "getFee"), code)
            .andExpect(status().isOk());
    }

    protected void saveRangedFeeAndCheckStatusIsCreated(CreateRangedFeeDto dto) throws Exception {
        restActions
            .withUser("admin")
            .post(
                URIUtils.getUrlForPostMethod(FeeController.class, "createRangedFee"),
                dto
            )
            .andExpect(status().isCreated());
    }

    protected ResultMatcher versionIsOneAndStatusIsDraft() {
        return body().as(Fee2Dto.class, (feeDto) -> {

            FeeVersionDto v = feeDto.getFeeVersionDtos().get(0);
            assertThat(v.getVersion().equals(1));
            assertThat(v.getStatus() == FeeVersionStatus.draft);
        });
    }

    protected ResultMatcher channelIsDefault() {
        return body().as(Fee2Dto.class, (feeDto) -> {
            assertThat(feeDto.getChannelTypeDto().getName().equals(ChannelType.DEFAULT));
        });
    }


}
