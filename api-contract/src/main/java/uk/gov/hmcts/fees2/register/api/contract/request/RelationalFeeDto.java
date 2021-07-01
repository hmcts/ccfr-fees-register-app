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
    public RelationalFeeDto(final String code, final String newCode, final FeeVersionDto version, final String jurisdiction1,
                            final String jurisdiction2, final String service, final String channel, final String event, final String applicantType,
                            final boolean unspecifiedClaimAmount, final String keyword) {
        super(code, newCode, version, jurisdiction1, jurisdiction2, service, channel, event, applicantType,
                unspecifiedClaimAmount, keyword);
    }

    @Override
    public RelationalFeeDto setCode(final String code) {
        this.code = code;
        return this;
    }

    @Override
    public RelationalFeeDto setVersion(final FeeVersionDto version) {
        this.version = version;
        return this;
    }

    @Override
    public RelationalFeeDto setJurisdiction1(final String jurisdiction1) {
        this.jurisdiction1 = jurisdiction1;
        return this;
    }

    @Override
    public RelationalFeeDto setJurisdiction2(final String jurisdiction2) {
        this.jurisdiction2 = jurisdiction2;
        return this;
    }

    @Override
    public RelationalFeeDto setService(final String service) {
        this.service = service;
        return this;
    }

    @Override
    public RelationalFeeDto setChannel(final String channel) {
        this.channel = channel;
        return this;
    }


    @Override
    public RelationalFeeDto setEvent(final String event) {
        this.event = event;
        return this;
    }

    @Override
    public RelationalFeeDto setApplicantType(final String applicantType) {
        this.applicantType = applicantType;
        return this;
    }


    @Override
    public Boolean getUnspecifiedClaimAmount() {
        return unspecifiedClaimAmount;
    }

    @Override
    public RelationalFeeDto setUnspecifiedClaimAmount(final Boolean unspecifiedClaimAmount) {
        this.unspecifiedClaimAmount = unspecifiedClaimAmount;
        return this;
    }

    @Override
    public RelationalFeeDto setKeyword(final String keyword) {
        this.keyword = keyword;
        return this;
    }
}
