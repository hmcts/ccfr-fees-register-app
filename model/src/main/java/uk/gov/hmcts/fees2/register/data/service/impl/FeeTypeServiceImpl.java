package uk.gov.hmcts.fees2.register.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees2.register.data.model.FeeType;
import uk.gov.hmcts.fees2.register.data.repository.FeeTypeRepository;
import uk.gov.hmcts.fees2.register.data.service.FeeTypeService;
import uk.gov.hmcts.fees2.register.data.service.impl.AbstractServiceImpl;

/**
 *
 *
 * @author Tarun Palisetty
 *
 */

@Service
public class FeeTypeServiceImpl extends AbstractServiceImpl<FeeType> implements FeeTypeService {

    @Autowired
    public FeeTypeServiceImpl(FeeTypeRepository feeTypeRepository) {
        super(feeTypeRepository);
    }
}
