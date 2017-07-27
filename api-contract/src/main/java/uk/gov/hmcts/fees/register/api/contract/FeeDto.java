package uk.gov.hmcts.fees.register.api.contract;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NonNull;

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
    @NonNull
    private final Integer id;
    @NonNull
    private final String code;
    @NonNull
    private final String description;
}
