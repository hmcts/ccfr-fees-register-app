package uk.gov.hmcts.fees.register.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorColumn(name = "type")
@Table(name = "fee_old")
public abstract class FeeOld implements Calculateable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    private String code;
    @NonNull
    private String description;
}



