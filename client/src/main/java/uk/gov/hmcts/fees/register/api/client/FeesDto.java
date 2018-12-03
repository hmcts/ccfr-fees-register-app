package uk.gov.hmcts.fees.register.api.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "feesDtoWith")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeesDto {

    private String id;
    private String type;
    private int amount;
    private String description;
    private BigDecimal percentage;

}
