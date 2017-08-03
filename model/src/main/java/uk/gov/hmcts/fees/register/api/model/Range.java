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

    @Builder(builderMethodName = "rangeWith")
    public Range(Integer from, Integer to, Fee fee) {
        checkFromLowerThanTo(from, to);
        this.from = from;
        this.to = to;
        this.fee = fee;
    }

    public void setFrom(Integer from) {
        checkFromLowerThanTo(from, to);
        this.from = from;
    }

    public void setTo(Integer to) {
        checkFromLowerThanTo(from, to);
        this.to = to;
    }

    private void checkFromLowerThanTo(Integer from, Integer to) {
        if (to != null && from >= to) {
            throw new RangeEmptyException();
        }
    }

    public boolean follows(Range previous) {
        return (previous.to != null) && (this.from == previous.to + 1);
    }
}
