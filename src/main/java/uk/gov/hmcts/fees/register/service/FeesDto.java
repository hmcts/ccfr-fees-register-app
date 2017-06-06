package uk.gov.hmcts.fees.register.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Created by sacs on 05/06/2017.
 */

@JsonInclude(NON_NULL)
@Builder(builderMethodName = "feeDtoWith")
@AllArgsConstructor
@NoArgsConstructor
@Data

public class FeesDto {

    private String id;
    private Integer amount;
    private String description;
    private BigDecimal percentage;
    private Integer feeAmount;

}
