package uk.gov.hmcts.fees.register.api.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RangeGroupDto {
    private final String code;
    private final String description;
    private final List<RangeDto> ranges;

    @JsonCreator
    @Builder(builderMethodName = "rangeGroupDtoWith")
    public RangeGroupDto(@JsonProperty("code") String code,
                         @JsonProperty("description") String description,
                         @JsonProperty("ranges") List<RangeDto> ranges) {
        this.code = code;
        this.description = description;
        this.ranges = ranges;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RangeDto {
        private Integer from;
        private Integer to;
        private FeeDto fee;

        @JsonCreator
        @Builder(builderMethodName = "rangeDtoWith")
        public RangeDto(@JsonProperty("from") Integer from,
                        @JsonProperty("to") Integer to,
                        @JsonProperty("fee") FeeDto fee) {
            this.from = from;
            this.to = to;
            this.fee = fee;
        }
    }
}
