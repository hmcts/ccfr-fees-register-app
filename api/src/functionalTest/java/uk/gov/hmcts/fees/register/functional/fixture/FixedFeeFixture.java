package uk.gov.hmcts.fees.register.functional.fixture;

import org.apache.commons.lang3.RandomStringUtils;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionStatusDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto;

import java.math.BigDecimal;
import java.util.Date;

public class FixedFeeFixture {

    public static FixedFeeDto aFixedFee() {
        String keyword = RandomStringUtils.randomAlphanumeric(10);
        return FixedFeeDto.fixedFeeDtoWith()
            .applicantType("all")
            .channel("online")
            .event("issue")
            .jurisdiction1("tribunal")
            .jurisdiction2("gambling tribunal")
            .service("gambling")
            .keyword(keyword)
            .unspecifiedClaimAmount(true)
            .version(FeeVersionDto.feeVersionDtoWith()
                .version(1)
                .validFrom(new Date())
                .description("Test fee - Filing an application for a divorce")
                .status(FeeVersionStatusDto.draft)
                .flatAmount(new FlatAmountDto(BigDecimal.valueOf(300)))
                .memoLine("Test memo line")
                .statutoryInstrument("2016 No. 402")
                .feeOrderName("The Civil Proceedings")
                .direction("enhanced")
                .build())
            .build();
    }
}
