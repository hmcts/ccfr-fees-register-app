package uk.gov.hmcts.fees2.register.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "UnitWith")
@Table(name = "range_unit")
public class RangeUnit implements Serializable{

    public RangeUnit(String name) {
        this.name = name;
    }

    @Id
    @Column(name = "name")
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
