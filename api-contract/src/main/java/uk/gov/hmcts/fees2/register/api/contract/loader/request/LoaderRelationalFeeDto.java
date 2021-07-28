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

    public LoaderRelationalFeeDto(final String code, final String newCode, final LoaderFeeVersionDto version, final String jurisdiction1,
                                  final String jurisdiction2, final String service, final String channel, final String event,
                                  final String applicantType, final boolean unspecifiedClaimAmount, final String keyword, final Integer feeNumber) {
        super(code, newCode, version, jurisdiction1, jurisdiction2, service, channel, event, applicantType,
                unspecifiedClaimAmount, keyword, feeNumber);
    }

    public void setUnspecifiedClaimAmount(final boolean unspecifiedClaimAmount) {
        this.unspecifiedClaimAmount = unspecifiedClaimAmount;
    }
}
