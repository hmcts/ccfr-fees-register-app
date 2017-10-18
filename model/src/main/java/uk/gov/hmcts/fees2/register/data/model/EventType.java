package uk.gov.hmcts.fees2.register.data.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 *  An entity class which contains the information of a EventType
 */

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "eventWith")
@Table(name = "event_type")
public class EventType extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "creation_time", nullable = false)
    private Date creationTime;

    @Column(name = "last_updated", nullable = false)
    private Date lastUpdated;

    @PreUpdate
    public void preUpdate() {
        Date now = new Date();
        lastUpdated = now;
    }

    @PrePersist
    public void prePersist() {
        Date now = new Date();
        creationTime = now;
        lastUpdated = now;
    }
}
