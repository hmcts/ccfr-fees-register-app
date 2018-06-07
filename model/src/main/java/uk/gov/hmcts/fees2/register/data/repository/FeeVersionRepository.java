package uk.gov.hmcts.fees2.register.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeNotFoundException;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeVersionNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface FeeVersionRepository extends JpaRepository<FeeVersion, Long> {

    FeeVersion findByFee_CodeAndVersion(String feeCode, Integer version);

    Optional<FeeVersion> findByFee_Code(String feeCode);

    default FeeVersion findByFeeCodeOrThrow(String feeCode) {
        return findByFee_Code(feeCode).orElseThrow(() -> new FeeVersionNotFoundException("No fee version found for the fee code: " + feeCode));
    }

    List<FeeVersion> findByStatus(FeeVersionStatus status);

    List<FeeVersion> findByStatusAndAuthor(FeeVersionStatus status, String author);

    List<FeeVersion> findByFee_CodeAndStatus(String feeCode, FeeVersionStatus status);

    @Query("SELECT coalesce(max(v.version), 0) FROM Fee f, FeeVersion v where f.id = v.fee.id and f.code = ?1")
    Integer getMaxFeeVersion(String code);

}
