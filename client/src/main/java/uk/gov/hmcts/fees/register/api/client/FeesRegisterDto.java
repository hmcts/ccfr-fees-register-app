package uk.gov.hmcts.fees.register.api.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder(builderMethodName = "feesRegisterDtoWith")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeesRegisterDto {


    private String serviceName;
    private List<CategoryDto> categories;
    private List<FeesDto> flatFees;

}
