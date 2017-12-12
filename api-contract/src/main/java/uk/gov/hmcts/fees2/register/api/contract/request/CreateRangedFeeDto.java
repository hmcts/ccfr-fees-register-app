package uk.gov.hmcts.fees2.register.api.contract.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class CreateRangedFeeDto extends CreateFeeDto{

    private BigDecimal minRange;

    private BigDecimal maxRange;

    private String rangeUnit;

    public CreateRangedFeeDto(String code, FeeVersionDto version, String jurisdiction1, String jurisdiction2, String service, String channel, String direction, String event, String memoLine, String feeOrderName, String naturalAccountCode, BigDecimal maxRange,  BigDecimal minRange) {
        super(code, version, jurisdiction1, jurisdiction2, service, channel, direction, event, memoLine, feeOrderName, naturalAccountCode, null, null, false);
        this.maxRange = maxRange;
        this.minRange = minRange;
    }

    public BigDecimal getMinRange() {
        return minRange;
    }

    public CreateRangedFeeDto setMinRange(BigDecimal minRange) {
        this.minRange = minRange;
        return this;
    }

    public BigDecimal getMaxRange() {
        return maxRange;
    }

    public CreateRangedFeeDto setMaxRange(BigDecimal maxRange) {
        this.maxRange = maxRange;
        return this;
    }

    /* --- */

    public CreateRangedFeeDto setCode(String code) {
        this.code = code;
        return this;
    }

    public CreateRangedFeeDto setVersion(FeeVersionDto version) {
        this.version = version;
        return this;
    }

    public CreateRangedFeeDto setJurisdiction1(String jurisdiction1) {
        this.jurisdiction1 = jurisdiction1;
        return this;
    }

    public CreateRangedFeeDto setJurisdiction2(String jurisdiction2) {
        this.jurisdiction2 = jurisdiction2;
        return this;
    }

    public CreateRangedFeeDto setService(String service) {
        this.service = service;
        return this;
    }

    public CreateRangedFeeDto setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public CreateRangedFeeDto setDirection(String direction) {
        this.direction = direction;
        return this;
    }

    public CreateRangedFeeDto setEvent(String event) {
        this.event = event;
        return this;
    }

    public CreateRangedFeeDto setMemoLine(String memoLine) {
        this.memoLine = memoLine;
        return this;
    }

    public CreateRangedFeeDto setFeeOrderName(String feeOrderName) {
        this.feeOrderName = feeOrderName;
        return this;
    }

    public CreateRangedFeeDto setNaturalAccountCode(String naturalAccountCode) {
        this.naturalAccountCode = naturalAccountCode;
        return this;
    }

    public CreateRangedFeeDto setStatutoryInstrument(String statutoryInstrument){
        this.statutoryInstrument = statutoryInstrument;
        return this;
    }

    public CreateRangedFeeDto setSIRefId(String siRefID){
        this.siRefId = siRefID;
        return this;
    }

    public String getRangeUnit() {
        return rangeUnit;
    }

    public CreateRangedFeeDto setRangeUnit(String rangeUnit) {
        this.rangeUnit = rangeUnit;
        return this;
    }



}
