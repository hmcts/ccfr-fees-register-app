package uk.gov.hmcts.fees.register.api.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;



@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(builderMethodName = "categoryDtoWith")
public class CategoryDto {


    private final String id;
    private final List<RangeDto> ranges;
    private final List<FeesDto> flatFees;

}
