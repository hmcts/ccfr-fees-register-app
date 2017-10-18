package uk.gov.hmcts.fees2.register.data.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.UUID;

/**
 * An entity class which contains used of entity Id generation
 *
 */


@Getter
@MappedSuperclass
public class AbstractEntity implements Serializable {

    private static final Long servialVersionUID = 1L;

    //private String uuid = UUID.randomUUID().toString();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
