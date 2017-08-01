package uk.gov.hmcts.fees.register.api.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Builder(builderMethodName = "feesDtoWith")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeesDto {


    private final String id;
    private final String type;
    private final int amount;
    private final String description;
    private final BigDecimal percentage;

}
