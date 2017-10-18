package uk.gov.hmcts.fees2.register.data.repository;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.data.model.ServiceType;


/**
 *
 * Specifies methods used to obtain and modify ServiceType related information
 * which is stored in the database.
 *
 * @author Tarun Palisetty
 *
 */

@Repository
@Transactional(readOnly = true)
public interface ServiceTypeRepository extends AbstractRepository<ServiceType, Long> {

    @Override
    default String getEntityName() {
        return Service.class.getSimpleName();
    }
}
