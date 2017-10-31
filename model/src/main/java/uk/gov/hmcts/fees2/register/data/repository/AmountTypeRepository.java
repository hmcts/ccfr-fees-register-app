package uk.gov.hmcts.fees2.register.data.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.data.model.AmountType;

/**
 * Specifies methods used to obtain and modify AmountType related information
 * which is stored in the database.
 *
 * @author Tarun Palisetty
 *
 */

@Repository
@Transactional(readOnly = true)
public interface AmountTypeRepository extends AbstractRepository<AmountType, String> {

    @Override
    default String getEntityName() {
        return AmountType.class.getSimpleName();
    }

}
