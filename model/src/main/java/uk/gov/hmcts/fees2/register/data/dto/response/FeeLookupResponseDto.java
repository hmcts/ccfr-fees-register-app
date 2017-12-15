package uk.gov.hmcts.fees2.register.data.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    private String description;

    private Integer version;

    @JsonProperty("fee_amount")
    private BigDecimal feeAmount;

}
