package uk.gov.hmcts.fees2.register.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeNotFoundException;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeVersionNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeeVersionRepository extends JpaRepository<FeeVersion, Long> {

    FeeVersion findByFee_CodeAndVersion(String feeCode, Integer version);

    List<FeeVersion> findByStatus(FeeVersionStatus status);

}
