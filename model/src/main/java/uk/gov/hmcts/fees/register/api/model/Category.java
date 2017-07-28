package uk.gov.hmcts.fees.register.api.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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
    @NonNull
    @Column(name = "range_group_id")
    private Integer rangeGroupId;


}
