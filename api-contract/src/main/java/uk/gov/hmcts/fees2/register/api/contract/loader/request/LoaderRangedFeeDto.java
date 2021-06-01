package uk.gov.hmcts.fees2.register.api.contract.loader.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
public class LoaderRangedFeeDto extends LoaderFeeDto {

    @JsonProperty("min_range")
    private BigDecimal minRange;

    @JsonProperty("max_range")
    private BigDecimal maxRange;

    @JsonProperty("range_unit")
    private String rangeUnit;

    public LoaderRangedFeeDto(String code, String newCode, LoaderFeeVersionDto version, String jurisdiction1, String jurisdiction2, String service, String channel, String event, String applicantType, BigDecimal maxRange, BigDecimal minRange, String keyword, String reasonForUpdate) {
        super(code, newCode, version, jurisdiction1, jurisdiction2, service, channel, event, applicantType, false, keyword, reasonForUpdate);
    }
}
