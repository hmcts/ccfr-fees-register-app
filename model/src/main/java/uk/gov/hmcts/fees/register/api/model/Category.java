package uk.gov.hmcts.fees.register.api.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "categoryWith")
public class Category {
    @Id
    @NonNull
    private Integer id;
    @NonNull
    private String code;
    @NonNull
    private String description;
    @ManyToOne
    private RangeGroup rangeGroup;
}
