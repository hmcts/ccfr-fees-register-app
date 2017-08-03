package uk.gov.hmcts.fees.register.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer rangeGroupId;
    @NonNull
    @Column(name = "value_from")
    private Integer from;
    @Column(name = "value_to")
    private Integer to;
    @NonNull
    @ManyToOne
    private Fee fee;
}
