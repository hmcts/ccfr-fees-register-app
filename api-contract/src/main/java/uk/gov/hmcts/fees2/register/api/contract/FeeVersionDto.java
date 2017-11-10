package uk.gov.hmcts.fees2.register.api.contract;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class FeeVersionDto {

    private Integer version;

    private Date validFrom;

    private Date validTo;

    private String description;

    private FeeVersionStatus status;

    // Is there a better way to specify this ?

    private FlatAmountDto flatAmount;

    private PercentageAmountDto percentageAmount;


}
