package uk.gov.hmcts.fees2.register.data.service;

import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.model.Fee;

import java.util.List;

public interface FeeService {

    Fee save(Fee fee);

    void save(List<Fee> fees);

    void delete(String code);

    boolean safeDelete(String code);

    FeeLookupResponseDto lookup(LookupFeeDto dto);

    void prevalidate(Fee fee);

    List<Fee> search(LookupFeeDto dto);

    Fee getFee(String code);

    void updateLoaderFee(Fee fee, String code);

    void saveLoaderFee(Fee fee);

}
