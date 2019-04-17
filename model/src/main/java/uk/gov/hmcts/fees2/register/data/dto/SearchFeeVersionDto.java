package uk.gov.hmcts.fees2.register.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Stream;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchFeeVersionDto {
    private String author;

    private String approvedBy;

    private Boolean isActive;

    private Boolean isExpired;

    @JsonProperty("version_status")
    private FeeVersionStatus versionStatus;

    private String description;

    private String siRefId;

    private BigDecimal feeVersionAmount;

    public boolean isNoFieldSet() {
        return Stream.of(author, approvedBy, isActive, isExpired, versionStatus, description, siRefId, feeVersionAmount)
            .allMatch(Objects::isNull);
    }
}
