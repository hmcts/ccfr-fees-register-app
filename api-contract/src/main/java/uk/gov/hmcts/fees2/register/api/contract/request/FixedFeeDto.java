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
public class FixedFeeDto extends FeeDto{

    @Builder(builderMethodName = "fixedFeeDtoWith")
    public FixedFeeDto(String code, String newCode, FeeVersionDto version, String jurisdiction1, String jurisdiction2, String service, String channel, String event, String applicantType, boolean unspecifiedClaimAmount, String keyword, String reasonForUpdate) {
        super(code, newCode, version, jurisdiction1, jurisdiction2, service, channel, event, applicantType, unspecifiedClaimAmount, keyword, reasonForUpdate);
    }

    public FixedFeeDto setCode(String code) {
        this.code = code;
        return this;
    }

    public FixedFeeDto setVersion(FeeVersionDto version) {
        this.version = version;
        return this;
    }

    public FixedFeeDto setJurisdiction1(String jurisdiction1) {
        this.jurisdiction1 = jurisdiction1;
        return this;
    }

    public FixedFeeDto setJurisdiction2(String jurisdiction2) {
        this.jurisdiction2 = jurisdiction2;
        return this;
    }

    public FixedFeeDto setService(String service) {
        this.service = service;
        return this;
    }

    public FixedFeeDto setChannel(String channel) {
        this.channel = channel;
        return this;
    }


    public FixedFeeDto setEvent(String event) {
        this.event = event;
        return this;
    }

    public FixedFeeDto setApplicantType(String applicantType) {
        this.applicantType = applicantType;
        return this;
    }


    public Boolean getUnspecifiedClaimAmount() {
        return unspecifiedClaimAmount;
    }

    public FixedFeeDto setUnspecifiedClaimAmount(Boolean unspecifiedClaimAmount) {
        this.unspecifiedClaimAmount = unspecifiedClaimAmount;
        return this;
    }

    public FixedFeeDto setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

}
