package uk.gov.hmcts.fees.register.api.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(builderMethodName = "rangeDtoWith")
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RangeDto {

    @JsonProperty("start")
    private Integer startAmount;
    @JsonProperty("upto")
    private Integer uptoAmount;
    private FeesDto fee;
}
