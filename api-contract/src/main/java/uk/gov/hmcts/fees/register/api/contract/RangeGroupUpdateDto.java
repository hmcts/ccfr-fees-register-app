package uk.gov.hmcts.fees.register.api.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RangeGroupUpdateDto {
    @NotEmpty
    @Length(max = 2000)
    private final String description;

    @NotNull
    @Valid
    private final List<RangeUpdateDto> ranges;

    @JsonCreator
    @Builder(builderMethodName = "rangeGroupUpdateDtoWith")
    public RangeGroupUpdateDto(@JsonProperty("description") String description,
                               @JsonProperty("ranges") List<RangeUpdateDto> ranges) {
        this.description = description;
        this.ranges = ranges;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RangeUpdateDto {
        @NotNull
        @Min(0)
        private Integer from;
        private Integer to;
        @NotEmpty
        private String feeCode;

        @JsonCreator
        @Builder(builderMethodName = "rangeUpdateDtoWith")
        public RangeUpdateDto(@JsonProperty("from") Integer from,
                              @JsonProperty("to") Integer to,
                              @JsonProperty("feeCode") String feeCode) {
            this.from = from;
            this.to = to;
            this.feeCode = feeCode;
        }
    }
}
