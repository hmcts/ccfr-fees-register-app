package uk.gov.hmcts.fees2.register.api.contract.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class FeeLookupResponseDto {

    private String code;

    private Integer version;

    private BigDecimal feeAmount;

}
