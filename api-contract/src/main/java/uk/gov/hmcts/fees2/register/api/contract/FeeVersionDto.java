package uk.gov.hmcts.fees2.register.api.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.VolumeAmountDto;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder(builderMethodName = "feeVersionDtoWith")
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class FeeVersionDto {

    @JsonProperty("version")
    private Integer version;

    @JsonProperty("valid_from")
    private Date validFrom;

    @JsonProperty("valid_to")
    private Date validTo;

    private String description;

    private FeeVersionStatusDto status;

    @JsonProperty("flat_amount")
    private FlatAmountDto flatAmount;

    @JsonProperty("percentage_amount")
    private PercentageAmountDto percentageAmount;

    @JsonProperty("volume_amount")
    private VolumeAmountDto volumeAmount;

    @JsonIgnore
    public BigDecimal getAmount() {
        if (getFlatAmount() != null) {
            return getFlatAmount().getAmount();
        } else if (getVolumeAmount() != null) {
            return getVolumeAmount().getAmount();
        } else if (getPercentageAmount() != null){
            return getPercentageAmount().getPercentage();
        }

        return null;
    }

    private String author;

    private String approvedBy;

    @JsonProperty("memo_line")
    private String memoLine;

    @JsonProperty("statutory_instrument")
    private String statutoryInstrument;

    @JsonProperty("si_ref_id")
    private String siRefId;

    @JsonProperty("natural_account_code")
    private String naturalAccountCode;

    @JsonProperty("fee_order_name")
    private String feeOrderName;

    @JsonProperty("direction")
    private String direction;

}
