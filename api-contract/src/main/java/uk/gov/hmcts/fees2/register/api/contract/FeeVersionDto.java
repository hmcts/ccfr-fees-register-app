package uk.gov.hmcts.fees2.register.api.contract;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.VolumeAmountDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class FeeVersionDto {

    private Integer version;

    @JsonProperty("valid_from")
    private Date validFrom;

    @JsonProperty("valid_to")
    private Date validTo;

    private String description;

    private FeeVersionStatus status;

    @JsonProperty("flat_amount")
    private FlatAmountDto flatAmount;

    @JsonProperty("percentage_amount")
    private PercentageAmountDto percentageAmount;

    @JsonProperty("volume_amount")
    private VolumeAmountDto volumeAmount;

    private String author;

    private String approvedBy;
}
