package uk.gov.hmcts.fees2.register.api.contract.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class CreateFixedFeeDto extends CreateFeeDto{

    public CreateFixedFeeDto(String code, FeeVersionDto version, String jurisdiction1, String jurisdiction2, String service, String channel, String direction, String event, String memoLine, String feeOrderName, String naturalAccountCode) {
        super(code, version, jurisdiction1, jurisdiction2, service, channel, direction, event, memoLine, feeOrderName, naturalAccountCode);
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

    public CreateFixedFeeDto setDirection(String direction) {
        this.direction = direction;
        return this;
    }

    public CreateFixedFeeDto setEvent(String event) {
        this.event = event;
        return this;
    }

    public CreateFixedFeeDto setMemoLine(String memoLine) {
        this.memoLine = memoLine;
        return this;
    }

    public CreateFixedFeeDto setFeeOrderName(String feeOrderName) {
        this.feeOrderName = feeOrderName;
        return this;
    }

    public CreateFixedFeeDto setNaturalAccountCode(String naturalAccountCode) {
        this.naturalAccountCode = naturalAccountCode;
        return this;
    }

}
