package uk.gov.hmcts.fees2.register.api.contract;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;

/**
 * A Dto class which contains the information about AmountType entity.
 *
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Builder(builderMethodName = "amountTypeDtoWith")
public class AmountTypeDto {

    private String name;

}
