package uk.gov.hmcts.fees.register.api.model;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.fees.register.api.model.exceptions.FeeNotFoundException;

@Repository
public interface FeeRepository extends JpaRepository<FeeOld, Integer> {
    Optional<FeeOld> findByCode(String code);

    default FeeOld findByCodeOrThrow(String code) {
        return findByCode(code).orElseThrow(() -> new FeeNotFoundException(code));
    }

}
