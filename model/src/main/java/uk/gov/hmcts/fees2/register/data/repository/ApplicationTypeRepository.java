package uk.gov.hmcts.fees2.register.data.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.data.model.ApplicationType;
import uk.gov.hmcts.fees2.register.data.model.ChannelType;

@Repository
@Transactional(readOnly = true)
public interface ApplicationTypeRepository extends AbstractRepository<ApplicationType, String> {

    @Override
    default String getEntityName() {
        return ApplicationType.class.getSimpleName();
    }

}
