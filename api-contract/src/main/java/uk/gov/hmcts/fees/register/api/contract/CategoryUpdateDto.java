package uk.gov.hmcts.fees.register.api.contract;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Builder(builderMethodName = "categoryUpdateDtoWith")
public class CategoryUpdateDto {
    @NotEmpty
    @Length(max = 2000)
    private String description;
    private String rangeGroupCode;
    private List<String> feeCodes;
}
