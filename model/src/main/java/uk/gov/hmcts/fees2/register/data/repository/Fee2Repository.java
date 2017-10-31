package uk.gov.hmcts.fees2.register.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.Fee2;

import java.util.Optional;

/**
 * Created by tarun on 30/10/2017.
 */

@Repository
public interface Fee2Repository extends JpaRepository<Fee2, Long> {

    Optional<Fee2> findByCode(String code);

    default Fee2 findByCodeOrThrow(String code) {
        return findByCode(code).orElseThrow(() -> new FeeNotFoundException(code));
    }
}
