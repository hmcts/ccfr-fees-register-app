package uk.gov.hmcts.fees2.register.api.contract;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * A Dto class which contains the information about FeeType entity.
 *
 */


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Builder(builderMethodName = "feeTypeDtoWith")
public class FeeTypeDto {

    private String name;
}
