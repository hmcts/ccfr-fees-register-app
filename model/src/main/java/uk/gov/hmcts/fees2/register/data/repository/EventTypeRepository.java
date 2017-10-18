package uk.gov.hmcts.fees2.register.data.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.data.model.EventType;


/**
 *
 * Specifies methods used to obtain and modify EventType related information
 * which is stored in the database.
 *
 * @author Tarun Palisetty
 *
 */

@Repository
@Transactional(readOnly = true)
public interface EventTypeRepository extends AbstractRepository<EventType, Long> {

    @Override
    default String getEntityName() {
        return EventType.class.getSimpleName();
    }
}
