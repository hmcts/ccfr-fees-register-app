package uk.gov.hmcts.fees2.register.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.Fee;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface Fee2Repository extends JpaRepository<Fee, Long>, JpaSpecificationExecutor<Fee> {

    Optional<Fee> findByCode(String code);

    default Fee findByCodeOrThrow(String code) {
        return findByCode(code).orElseThrow(() -> new FeeNotFoundException(code));
    }

    void deleteFeeByCode(String code);

    @Query("SELECT coalesce(max(f.feeNumber), 0) FROM uk.gov.hmcts.fees2.register.data.model.Fee f")
    Integer getMaxFeeNumber();

}
