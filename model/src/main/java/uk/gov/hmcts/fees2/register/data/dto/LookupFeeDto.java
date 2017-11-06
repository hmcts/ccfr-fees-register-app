package uk.gov.hmcts.fees2.register.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class LookupFeeDto {

    private String service;

    private String jurisdiction1;

    private String jurisdiction2;

    private String channel;

    private String event;

    private String direction;

    private BigDecimal amount;

}
