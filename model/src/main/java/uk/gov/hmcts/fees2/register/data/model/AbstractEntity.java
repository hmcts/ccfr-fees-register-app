package uk.gov.hmcts.fees2.register.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * An entity class which contains used of entity Id generation
 *
 */
@Getter
@MappedSuperclass
public class AbstractEntity extends Object implements Serializable {

    private static final Long servialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    protected Long id;
}
