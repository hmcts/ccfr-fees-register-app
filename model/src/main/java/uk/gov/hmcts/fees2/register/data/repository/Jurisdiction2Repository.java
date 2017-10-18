package uk.gov.hmcts.fees2.register.data.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.data.model.Jurisdiction2;

/**
 *
 * Specifies methods used to obtain and modify Jurisdiction2 related information
 * which is stored in the database.
 *
 * @author Tarun Palisetty
 */

@Repository
@Transactional(readOnly = true)
public interface Jurisdiction2Repository extends AbstractRepository<Jurisdiction2, Long> {

    @Override
    default String getEntityName() {
        return Jurisdiction2.class.getSimpleName();
    }
}
