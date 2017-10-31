package uk.gov.hmcts.fees2.register.data.service;

import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees2.register.data.model.Fee;

/**
 * Created by tarun on 30/10/2017.
 */

public interface FeeService  {

    public Fee save(Fee fee);

}
