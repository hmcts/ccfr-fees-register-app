package uk.gov.hmcts.fees.register.api.model;


import lombok.AllArgsConstructor;
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
public class Category {

    @Id
    @NonNull
    private Integer id;
    @NonNull
    private String title;
    @ManyToOne
    @JoinColumn(name = "range_group_id")
    private RangeGroup rangeGroup;


}
