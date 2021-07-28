package uk.gov.hmcts.fees2.register.api.contract.loader.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.api.contract.request.FeeDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
public abstract class LoaderFeeDto extends FeeDto {

    @JsonProperty("code")
    private String code;

    @JsonProperty("new_code")
    private String newCode;

    private LoaderFeeVersionDto version;

    public LoaderFeeDto(final String code, final String newCode, final LoaderFeeVersionDto version, final String jurisdiction1,
                        final String jurisdiction2, final String service, final String channel, final String event, final String applicantType,
                        final Boolean unspecifiedClaimAmount, final String keyword, final Integer feeNumber) {
        super(code, newCode, version, jurisdiction1, jurisdiction2, service, channel, event, applicantType,
                unspecifiedClaimAmount, keyword, feeNumber);
        this.code = code;
        this.newCode = newCode;
        this.version = version;
    }
}
