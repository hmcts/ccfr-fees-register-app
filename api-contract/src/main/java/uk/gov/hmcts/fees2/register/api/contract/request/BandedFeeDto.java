package uk.gov.hmcts.fees2.register.api.contract.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BandedFeeDto extends FixedFeeDto {

    public BandedFeeDto(String code, String newCode, FeeVersionDto version, String jurisdiction1, String jurisdiction2, String service, String channel, String event, String applicantType, boolean unspecifiedClaimAmount) {
        super(code, newCode, version, jurisdiction1, jurisdiction2, service, channel, event, applicantType, unspecifiedClaimAmount);
    }
}
