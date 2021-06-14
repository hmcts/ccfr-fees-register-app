package uk.gov.hmcts.fees2.register.api.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName = "reasonDtoWith")
public class ReasonDto {

    private String reasonForReject;
}
