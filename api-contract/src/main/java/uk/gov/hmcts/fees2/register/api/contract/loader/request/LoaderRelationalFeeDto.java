package uk.gov.hmcts.fees2.register.api.contract.loader.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoaderRelationalFeeDto extends LoaderFeeDto {

    public LoaderRelationalFeeDto(String code, String newCode, LoaderFeeVersionDto version, String jurisdiction1, String jurisdiction2, String service, String channel, String event, String applicantType, boolean unspecifiedClaimAmount, String keyword, String reasonForUpdate) {
        super(code, newCode, version, jurisdiction1, jurisdiction2, service, channel, event, applicantType, unspecifiedClaimAmount, keyword, reasonForUpdate);
    }

    public void setUnspecifiedClaimAmount(boolean unspecifiedClaimAmount) {
        this.unspecifiedClaimAmount = unspecifiedClaimAmount;
    }
}
