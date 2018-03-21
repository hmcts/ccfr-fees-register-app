package uk.gov.hmcts.fees2.register.data.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.data.model.ApplicantType;

@Repository
@Transactional(readOnly = true)
public interface ApplicantTypeRepository extends AbstractRepository<ApplicantType, String> {

    @Override
    default String getEntityName() {
        return ApplicantType.class.getSimpleName();
    }

}
