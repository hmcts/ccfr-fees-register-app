package uk.gov.hmcts.fees.register.api.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "rangeWith")
public class Range {
    @Id
    private Integer id;
    @NonNull
    private Integer rangeGroupId;
    @NonNull
    private Integer from;
    private Integer to;
    @NonNull
    @ManyToOne
    private Fee fee;
}
