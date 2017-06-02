package uk.gov.hmcts.register.fees.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NonNull;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class Fee {
    @NonNull
    private final String id;
    @NonNull
    private final String description;
}
