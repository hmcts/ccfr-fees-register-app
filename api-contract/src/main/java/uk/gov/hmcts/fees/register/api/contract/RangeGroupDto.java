package uk.gov.hmcts.fees.register.api.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RangeGroupDto {
    private final Integer id;
    @NonNull
    private final String description;
    @NonNull
    private final List<RangeDto> ranges;

    @JsonCreator
    @Builder(builderMethodName = "rangeGroupDtoWith")
    public RangeGroupDto(@JsonProperty("id") Integer id,
                         @JsonProperty("description") String description,
                         @JsonProperty("ranges") List<RangeDto> ranges) {
        this.id = id;
        this.description = description;
        this.ranges = ranges;
    }
}
