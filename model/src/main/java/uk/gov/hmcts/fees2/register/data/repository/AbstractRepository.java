package uk.gov.hmcts.fees2.register.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.fees2.register.data.exceptions.ReferenceDataNotFoundException;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

/**
 *
 * Specifies methods used to obtain and modify Amount related information
 * which is stored in the database.
 *
 * @author Tarun Palisetty
 *
 */

@NoRepositoryBean
public interface AbstractRepository<T, ID extends Serializable> extends JpaRepository<T, ID>{
    String getEntityName();

    Optional<T> findByName(String name);

    default T findByNameOrThrow(String name) {
        return findByName(name).orElseThrow(() -> new ReferenceDataNotFoundException(getEntityName(), name));
    }

}
