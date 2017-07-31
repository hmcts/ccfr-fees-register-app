package uk.gov.hmcts.fees.register.api.contract;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Builder(builderMethodName = "categoryDtoWith")
public class CategoryDto {
    private Integer id;
    @NonNull
    private String code;
    @NonNull
    private String description;
    @JsonProperty("rangeGroup")
    private RangeGroupDto rangeGroupDto;
}
