package uk.gov.hmcts.fees2.register.data.service;

import uk.gov.hmcts.fees2.register.data.dto.SearchFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.SearchFeeVersionDto;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;

import java.util.List;

public interface FeeSearchService {

    List<FeeVersion> search(SearchFeeDto feeCriteria, SearchFeeVersionDto versionCriteria);

    List<Fee> search(SearchFeeDto feeCriteria);

}

