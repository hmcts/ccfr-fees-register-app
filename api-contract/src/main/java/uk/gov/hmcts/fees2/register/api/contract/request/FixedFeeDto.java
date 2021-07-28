package uk.gov.hmcts.fees2.register.api.contract.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FixedFeeDto extends FeeDto {

    @Builder(builderMethodName = "fixedFeeDtoWith")
    public FixedFeeDto(final String code, final String newCode, final FeeVersionDto version, final String jurisdiction1,
                       final String jurisdiction2,
                       final String service, final String channel, final String event, final String applicantType,
                       final boolean unspecifiedClaimAmount, final String keyword, final Integer feeNumber) {
        super(code, newCode, version, jurisdiction1, jurisdiction2, service, channel, event, applicantType,
                unspecifiedClaimAmount, keyword, feeNumber);
    }

    public FixedFeeDto setCode(final String code) {
        this.code = code;
        return this;
    }

    public FixedFeeDto setVersion(final FeeVersionDto version) {
        this.version = version;
        return this;
    }

    public FixedFeeDto setJurisdiction1(final String jurisdiction1) {
        this.jurisdiction1 = jurisdiction1;
        return this;
    }

    public FixedFeeDto setJurisdiction2(final String jurisdiction2) {
        this.jurisdiction2 = jurisdiction2;
        return this;
    }

    public FixedFeeDto setService(final String service) {
        this.service = service;
        return this;
    }

    public FixedFeeDto setChannel(final String channel) {
        this.channel = channel;
        return this;
    }


    public FixedFeeDto setEvent(final String event) {
        this.event = event;
        return this;
    }

    public FixedFeeDto setApplicantType(final String applicantType) {
        this.applicantType = applicantType;
        return this;
    }


    @Override
    public Boolean getUnspecifiedClaimAmount() {
        return unspecifiedClaimAmount;
    }

    public FixedFeeDto setUnspecifiedClaimAmount(final Boolean unspecifiedClaimAmount) {
        this.unspecifiedClaimAmount = unspecifiedClaimAmount;
        return this;
    }

    public FixedFeeDto setKeyword(final String keyword) {
        this.keyword = keyword;
        return this;
    }

}
