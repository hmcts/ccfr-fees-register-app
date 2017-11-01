package uk.gov.hmcts.fees2.register.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.repository.Fee2Repository;
import uk.gov.hmcts.fees2.register.data.service.FeeService;

/**
 * Created by tarun on 30/10/2017.
 */

@Service
public class FeeServiceImpl implements FeeService {

    @Autowired
    private Fee2Repository fee2Repository;

    public Fee save(Fee fee) {
        return fee2Repository.save(fee);
    }
}
