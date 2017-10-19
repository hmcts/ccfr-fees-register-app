package uk.gov.hmcts.fees2.register.data.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.data.model.Jurisdiction1;

/**
 *
 * Specifies methods used to obtain and modify Jurisdiction1 related information
 * which is stored in the database.
 *
 * @author Tarun Palisetty
 */

@Repository
@Transactional(readOnly = true)
public interface Jurisdiction1Repository extends AbstractRepository<Jurisdiction1, Long> {

    @Override
    default String getEntityName() {
        return Jurisdiction1.class.getSimpleName();
    }

}
