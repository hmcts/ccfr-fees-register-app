package uk.gov.hmcts.fees2.register.api.contract.loader.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFeeDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
public abstract class LoaderFeeDto extends CreateFeeDto {

    @JsonProperty("code")
    private String code;

    @JsonProperty("new_code")
    private String newCode;

    public LoaderFeeDto(String code, String newCode, FeeVersionDto version, String jurisdiction1, String jurisdiction2, String service, String channel, String event, String applicantType, Boolean unspecifiedClaimAmount) {
        super(code, newCode, version, jurisdiction1, jurisdiction2, service, channel, event, applicantType, unspecifiedClaimAmount);
        this.code = code;
        this.newCode = newCode;
    }
}
