package uk.gov.hmcts.fees2.register.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees2.register.data.model.ApplicantType;
import uk.gov.hmcts.fees2.register.data.repository.ApplicantTypeRepository;
import uk.gov.hmcts.fees2.register.data.service.ApplicantTypeService;


@Service
public class ApplicantServiceImpl extends AbstractServiceImpl<ApplicantType> implements ApplicantTypeService {

    @Autowired
    public ApplicantServiceImpl(ApplicantTypeRepository applicantTypeRepository) {
        super(applicantTypeRepository);
    }
}
