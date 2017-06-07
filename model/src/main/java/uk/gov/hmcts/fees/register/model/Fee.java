package uk.gov.hmcts.fees.register.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NonNull;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = FixedFee.class, name = "fixed"),
    @JsonSubTypes.Type(value = PercentageFee.class, name = "percentage")})
public class Fee {
    @NonNull
    private final String id;
    @NonNull
    private final String description;
}
