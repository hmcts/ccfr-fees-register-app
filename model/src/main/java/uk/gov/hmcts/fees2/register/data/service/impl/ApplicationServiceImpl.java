package uk.gov.hmcts.fees2.register.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees2.register.data.model.ApplicationType;
import uk.gov.hmcts.fees2.register.data.repository.ApplicationTypeRepository;
import uk.gov.hmcts.fees2.register.data.service.ApplicationTypeService;


@Service
public class ApplicationServiceImpl extends AbstractServiceImpl<ApplicationType> implements ApplicationTypeService {

    @Autowired
    public ApplicationServiceImpl(ApplicationTypeRepository applicationTypeRepository) {
        super(applicationTypeRepository);
    }
}
