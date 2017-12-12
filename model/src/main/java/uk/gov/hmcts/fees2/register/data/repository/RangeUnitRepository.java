package uk.gov.hmcts.fees2.register.data.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.data.model.RangeUnit;

@Repository
@Transactional(readOnly = true)
public interface RangeUnitRepository extends AbstractRepository<RangeUnit, String>{

    @Override
    default String getEntityName() {
        return RangeUnit.class.getSimpleName();
    }
}
