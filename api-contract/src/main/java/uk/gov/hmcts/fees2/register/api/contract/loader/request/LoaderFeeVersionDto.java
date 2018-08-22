package uk.gov.hmcts.fees2.register.api.contract.loader.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoaderFeeVersionDto extends FeeVersionDto {

    @JsonProperty("version")
    private Integer version;

    @JsonProperty("new_version")
    private Integer newVersion;
}
