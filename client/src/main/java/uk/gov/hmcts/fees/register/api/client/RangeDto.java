package uk.gov.hmcts.fees.register.api.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder(builderMethodName = "rangeDtoWith")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RangeDto {

    @JsonProperty("start")
    private final int startAmount;
    @JsonProperty("upto")
    private final int uptoAmount;
    private final FeesDto fee;
}
