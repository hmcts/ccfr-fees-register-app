package uk.gov.hmcts.fees.register.api.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;


@Data
@Builder(builderMethodName = "rangeDtoWith")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RangeDto {

    private final int startAmount;
    private final int uptoAmount;
    private final FeesDto fee;
}
