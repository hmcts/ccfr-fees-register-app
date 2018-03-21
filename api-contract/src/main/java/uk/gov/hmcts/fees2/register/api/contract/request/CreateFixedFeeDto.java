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
public class CreateFixedFeeDto extends CreateFeeDto{

    public CreateFixedFeeDto(String code, FeeVersionDto version, String jurisdiction1, String jurisdiction2, String service, String channel, String event, String applicantType, boolean unspecifiedClaimAmount) {
        super(code, version, jurisdiction1, jurisdiction2, service, channel, event, applicantType, unspecifiedClaimAmount);
    }

    public CreateFixedFeeDto setCode(String code) {
        this.code = code;
        return this;
    }

    public CreateFixedFeeDto setVersion(FeeVersionDto version) {
        this.version = version;
        return this;
    }

    public CreateFixedFeeDto setJurisdiction1(String jurisdiction1) {
        this.jurisdiction1 = jurisdiction1;
        return this;
    }

    public CreateFixedFeeDto setJurisdiction2(String jurisdiction2) {
        this.jurisdiction2 = jurisdiction2;
        return this;
    }

    public CreateFixedFeeDto setService(String service) {
        this.service = service;
        return this;
    }

    public CreateFixedFeeDto setChannel(String channel) {
        this.channel = channel;
        return this;
    }


    public CreateFixedFeeDto setEvent(String event) {
        this.event = event;
        return this;
    }

    public CreateFixedFeeDto setApplicantType(String applicantType) {
        this.applicantType = applicantType;
        return this;
    }


    public Boolean getUnspecifiedClaimAmount() {
        return unspecifiedClaimAmount;
    }

    public CreateFixedFeeDto setUnspecifiedClaimAmount(Boolean unspecifiedClaimAmount) {
        this.unspecifiedClaimAmount = unspecifiedClaimAmount;
        return this;
    }



}
