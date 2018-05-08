package uk.gov.hmcts.fees2.register.data.service;

import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees2.register.data.dto.SearchFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.SearchFeeVersionDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;

import java.util.List;

public interface FeeSearchService {
    List<FeeVersion> search(SearchFeeDto feeCriteria, SearchFeeVersionDto versionCriteria);
}

