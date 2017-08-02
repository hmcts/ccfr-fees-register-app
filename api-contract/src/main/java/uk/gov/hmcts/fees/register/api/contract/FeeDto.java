package uk.gov.hmcts.fees.register.api.contract;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = FixedFeeDto.class, name = "fixed"),
    @JsonSubTypes.Type(value = PercentageFeeDto.class, name = "percentage")})
public class FeeDto {
    private final String code;
    @NotEmpty
    private final String description;
}
