package uk.gov.hmcts.fees2.register.data.model;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "FeeCodeHistoryWith")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "feecode_history")
@EqualsAndHashCode(callSuper = false)
public class FeeCodeHistory extends AbstractEntity {

    @Column(name = "old_code")
    private String old_code;

    @Column(name = "new_code")
    private String new_code;

    @CreationTimestamp
    @Column(name = "date_created")
    private Date dateCreated;

    @UpdateTimestamp
    @Column(name = "date_updated")
    private Date dateUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_id", insertable = false, updatable = false)
    private Fee fee;

}
