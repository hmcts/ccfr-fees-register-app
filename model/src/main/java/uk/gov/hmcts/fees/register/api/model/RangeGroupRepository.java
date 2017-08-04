package uk.gov.hmcts.fees.register.api.model;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.fees.register.api.model.exceptions.RangeGroupNotFoundException;

@Repository
public interface RangeGroupRepository extends JpaRepository<RangeGroup, Integer> {
    Optional<RangeGroup> findByCode(String code);

    default RangeGroup findByCodeOrThrow(String code) {
        return findByCode(code).orElseThrow(() -> new RangeGroupNotFoundException(code));
    }
}
