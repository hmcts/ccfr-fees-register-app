package uk.gov.hmcts.fees2.register.api.contract.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ApproveFeeDto {

    @JsonProperty("fee_code")
    @NotNull
    private String feeCode;

    @JsonProperty("fee_version")
    @NotNull
    private Integer feeVersion;

}
