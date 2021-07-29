package uk.gov.hmcts.fees2.register.api.contract.loader.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public LoaderRangedFeeDto(final String code, final String newCode, final LoaderFeeVersionDto version, final String jurisdiction1,
                              final String jurisdiction2, final String service, final String channel, final String event, final String applicantType,
                              final BigDecimal maxRange, final BigDecimal minRange, final String keyword) {
        super(code, newCode, version, jurisdiction1, jurisdiction2, service, channel, event, applicantType, false,
                keyword);
    }
}
