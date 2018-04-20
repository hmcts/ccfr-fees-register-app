package uk.gov.hmcts.fees2.register.data.service.impl;

import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees2.register.data.repository.FeeCodeHistoryRepository;
import uk.gov.hmcts.fees2.register.data.service.FeeCodeHistoryService;

@Service
public class FeeCodeHistoryServiceImpl implements FeeCodeHistoryService {

    private final FeeCodeHistoryRepository feeCodeHistoryRepository;

    public FeeCodeHistoryServiceImpl(FeeCodeHistoryRepository feeCodeHistoryRepository) {
        this.feeCodeHistoryRepository = feeCodeHistoryRepository;
    }

}
