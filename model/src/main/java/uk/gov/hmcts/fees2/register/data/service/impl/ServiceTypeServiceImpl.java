package uk.gov.hmcts.fees2.register.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees2.register.data.model.ServiceType;
import uk.gov.hmcts.fees2.register.data.repository.ServiceTypeRepository;
import uk.gov.hmcts.fees2.register.data.service.ServiceTypeService;
import uk.gov.hmcts.fees2.register.data.service.impl.AbstractServiceImpl;

/**
 *
 *
 * @author Tarun Palisetty
 */

@Service
public class ServiceTypeServiceImpl extends AbstractServiceImpl<ServiceType> implements ServiceTypeService {

    @Autowired
    public ServiceTypeServiceImpl(ServiceTypeRepository serviceTypeRepository) {
        super(serviceTypeRepository);
    }
}
