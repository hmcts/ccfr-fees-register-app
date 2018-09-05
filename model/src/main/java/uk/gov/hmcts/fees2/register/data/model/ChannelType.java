package uk.gov.hmcts.fees2.register.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * An entity class which contains the information of a ChannelType
 */

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "channelWith")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "channel_type")
public class ChannelType implements Serializable {

    public static final String DEFAULT = "default";

    @Id
    @Column(name = "name", nullable = false)
    @EqualsAndHashCode.Include
    private String name;

    @Column(name = "creation_time", nullable = false)
    @JsonIgnore
    private Date creationTime;

    @Column(name = "last_updated", nullable = false)
    @JsonIgnore
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
