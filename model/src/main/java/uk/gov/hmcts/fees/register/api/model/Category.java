package uk.gov.hmcts.fees.register.api.model;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "categoryWith")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    private String code;
    @NonNull
    private String description;
    @ManyToOne
    private RangeGroup rangeGroup;
    @ManyToMany
    @JoinTable(
        name = "category_fee",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "fee_id")
    )
    private List<FeeOld> fees;
}
