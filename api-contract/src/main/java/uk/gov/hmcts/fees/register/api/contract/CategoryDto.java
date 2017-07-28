package uk.gov.hmcts.fees.register.api.contract;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Builder(builderMethodName="categoryDtoWith")
public class CategoryDto {

    @NonNull
    private  Integer id;
    @NonNull
    private  String title;

    private  Integer rangeGroupId;



}
