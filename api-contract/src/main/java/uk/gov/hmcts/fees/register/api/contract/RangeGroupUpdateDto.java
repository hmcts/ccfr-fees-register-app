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
public class RangeGroupUpdateDto {
    private final String code;

    @NotEmpty
    @Length(max = 2000)
    private final String description;

    @NotNull
    private final List<RangeUpdateDto> ranges;

    @JsonCreator
    @Builder(builderMethodName = "rangeGroupUpdateDtoWith")
    public RangeGroupUpdateDto(@JsonProperty("code") String code,
                               @JsonProperty("description") String description,
                               @JsonProperty("ranges") List<RangeUpdateDto> ranges) {
        this.code = code;
        this.description = description;
        this.ranges = ranges;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RangeUpdateDto {
        @NotNull
        private Integer from;
        private Integer to;
        @NotNull
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
