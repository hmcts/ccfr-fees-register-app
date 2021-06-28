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
public class RelationalFeeDto extends FixedFeeDto {

    @Builder(builderMethodName = "relationalFeeDtoWith")
    public RelationalFeeDto(String code, String newCode, FeeVersionDto version, String jurisdiction1, String jurisdiction2, String service, String channel, String event, String applicantType, boolean unspecifiedClaimAmount, String keyword) {
        super(code, newCode, version, jurisdiction1, jurisdiction2, service, channel, event, applicantType, unspecifiedClaimAmount, keyword);
    }

    public RelationalFeeDto setCode(String code) {
        this.code = code;
        return this;
    }

    public RelationalFeeDto setVersion(FeeVersionDto version) {
        this.version = version;
        return this;
    }

    public RelationalFeeDto setJurisdiction1(String jurisdiction1) {
        this.jurisdiction1 = jurisdiction1;
        return this;
    }

    public RelationalFeeDto setJurisdiction2(String jurisdiction2) {
        this.jurisdiction2 = jurisdiction2;
        return this;
    }

    public RelationalFeeDto setService(String service) {
        this.service = service;
        return this;
    }

    public RelationalFeeDto setChannel(String channel) {
        this.channel = channel;
        return this;
    }


    public RelationalFeeDto setEvent(String event) {
        this.event = event;
        return this;
    }

    public RelationalFeeDto setApplicantType(String applicantType) {
        this.applicantType = applicantType;
        return this;
    }


    public Boolean getUnspecifiedClaimAmount() {
        return unspecifiedClaimAmount;
    }

    public RelationalFeeDto setUnspecifiedClaimAmount(Boolean unspecifiedClaimAmount) {
        this.unspecifiedClaimAmount = unspecifiedClaimAmount;
        return this;
    }

    public RelationalFeeDto setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }
}
