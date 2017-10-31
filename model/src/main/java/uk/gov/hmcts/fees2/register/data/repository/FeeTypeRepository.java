package uk.gov.hmcts.fees2.register.data.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.data.model.FeeType;

/**
 *
 * Specifies methods used to obtain and modify FeeType related information
 * which is stored in the database.
 *
 *  @author Tarun Palisetty
 */

@Repository
@Transactional(readOnly = true)
public interface FeeTypeRepository extends AbstractRepository<FeeType, String> {

    @Override
    default String getEntityName() {
        return FeeType.class.getSimpleName();
    }
}
