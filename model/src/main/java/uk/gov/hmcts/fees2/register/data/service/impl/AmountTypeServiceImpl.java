package uk.gov.hmcts.fees2.register.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees2.register.data.model.AmountType;
import uk.gov.hmcts.fees2.register.data.repository.AmountTypeRepository;
import uk.gov.hmcts.fees2.register.data.service.AmountTypeService;
import uk.gov.hmcts.fees2.register.data.service.impl.AbstractServiceImpl;

/**
 *
 *  Service layer methods used to obtain and modify AmountType related information
 *
 *  @author Tarun Palisetty
 *
 */

@Service
public class AmountTypeServiceImpl extends AbstractServiceImpl<AmountType> implements AmountTypeService {

    @Autowired
    public AmountTypeServiceImpl(AmountTypeRepository amountTypeRepository) {
        super(amountTypeRepository);
    }

}
