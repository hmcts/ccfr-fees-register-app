package uk.gov.hmcts.fees2.register.api.contract.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class RangedFeeDto extends FeeDto{

    @JsonProperty("min_range")
    private BigDecimal minRange;

    @JsonProperty("max_range")
    private BigDecimal maxRange;

    @JsonProperty("range_unit")
    private String rangeUnit;

    @Builder(builderMethodName = "rangedFeeDtoWith")
    public RangedFeeDto(String code, String newCode, FeeVersionDto version, String jurisdiction1, String jurisdiction2, String service, String channel, String event, String applicantType, BigDecimal maxRange, BigDecimal minRange, String keyword) {
        super(code, newCode, version, jurisdiction1, jurisdiction2, service, channel, event, applicantType, false, keyword);
        this.maxRange = maxRange;
        this.minRange = minRange;
    }

    public BigDecimal getMinRange() {
        return minRange;
    }

    public RangedFeeDto setMinRange(BigDecimal minRange) {
        this.minRange = minRange;
        return this;
    }

    public BigDecimal getMaxRange() {
        return maxRange;
    }

    public RangedFeeDto setMaxRange(BigDecimal maxRange) {
        this.maxRange = maxRange;
        return this;
    }

    /* --- */

    public RangedFeeDto setCode(String code) {
        this.code = code;
        return this;
    }

    public RangedFeeDto setVersion(FeeVersionDto version) {
        this.version = version;
        return this;
    }

    public RangedFeeDto setJurisdiction1(String jurisdiction1) {
        this.jurisdiction1 = jurisdiction1;
        return this;
    }

    public RangedFeeDto setJurisdiction2(String jurisdiction2) {
        this.jurisdiction2 = jurisdiction2;
        return this;
    }

    public RangedFeeDto setService(String service) {
        this.service = service;
        return this;
    }

    public RangedFeeDto setChannel(String channel) {
        this.channel = channel;
        return this;
    }


    public RangedFeeDto setEvent(String event) {
        this.event = event;
        return this;
    }

    public RangedFeeDto setApplicant(String applicantType) {
        this.applicantType = applicantType;
        return this;
    }

    public String getRangeUnit() {
        return rangeUnit;
    }

    public RangedFeeDto setRangeUnit(String rangeUnit) {
        this.rangeUnit = rangeUnit;
        return this;
    }

    public RangedFeeDto setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

}
