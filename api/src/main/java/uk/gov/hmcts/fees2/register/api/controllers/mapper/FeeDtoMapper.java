package uk.gov.hmcts.fees2.register.api.controllers.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeRequest;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.repository.Jurisdiction1Repository;
import uk.gov.hmcts.fees2.register.data.repository.Jurisdiction2Repository;

@Component
public class FeeDtoMapper {

    private Jurisdiction1Repository jurisdiction1Repository;

    private Jurisdiction2Repository jurisdiction2Repository;

    @Autowired
    public FeeDtoMapper(
        Jurisdiction1Repository jurisdiction1Repository,
        Jurisdiction2Repository jurisdiction2Repository

                        ) {

        this.jurisdiction1Repository = jurisdiction1Repository;
        this.jurisdiction2Repository = jurisdiction2Repository;
    }

    public Fee toFee(CreateRangedFeeRequest request) {
        return null;
    }
}
