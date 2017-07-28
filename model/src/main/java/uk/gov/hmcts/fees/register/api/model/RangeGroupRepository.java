package uk.gov.hmcts.fees.register.api.model;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RangeGroupRepository extends JpaRepository<RangeGroup, Integer> {
    Optional<RangeGroup> findById(Integer integer);
}
