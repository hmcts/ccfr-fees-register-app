package uk.gov.hmcts.fees.register.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
    @ManyToOne
    private RangeGroup rangeGroup;
    @NonNull
    @Column(name = "value_from")
    private Integer from;
    @Column(name = "value_to")
    private Integer to;
    @NonNull
    @ManyToOne
    private FeeOld fee;

    @Builder(builderMethodName = "rangeWith")
    public Range(Integer from, Integer to, FeeOld fee) {
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

    public boolean containsValue(int value) {
        return from <= value && (to == null || value <= to);
    }

}
