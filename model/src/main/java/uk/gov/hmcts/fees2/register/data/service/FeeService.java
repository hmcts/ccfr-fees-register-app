package uk.gov.hmcts.fees2.register.data.service;

import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.model.Fee;

import java.util.List;

/**
 * Created by tarun on 30/10/2017.
 */

public interface FeeService  {

    public Fee save(Fee fee);

    public List<Fee> lookup(LookupFeeDto dto);

}
