package uk.gov.hmcts.fees.register.api.contract;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Builder(builderMethodName = "categoryDtoWith")
public class CategoryDto {
    private String code;
    private String description;
    private RangeGroupDto rangeGroup;
    private List<FeeDto> fees;
}
