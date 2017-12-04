package uk.gov.hmcts.fees2.register.data.service;

import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.model.Fee;

import java.util.List;

public interface FeeService  {

    Fee save(Fee fee);

    void save(List<Fee> fees);

    void delete(String code);

    FeeLookupResponseDto lookup(LookupFeeDto dto);

    List<Fee> search(LookupFeeDto dto);

    Fee get(String code);

    boolean approve(String code, Integer version);

}
