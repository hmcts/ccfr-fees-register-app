package uk.gov.hmcts.fees.register.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees.register.model.FeesRegister;
import uk.gov.hmcts.fees.register.repository.FeesRegisterRepository;

@Service
public class FeesRegisterServiceImpl implements FeesRegisterService {


    private final FeesRegisterRepository feesRegisterRepository;

    @Autowired
    public FeesRegisterServiceImpl(FeesRegisterRepository feesRegisterRepository) {
        this.feesRegisterRepository = feesRegisterRepository;
    }

    public FeesRegister getFeesRegister() {
        return feesRegisterRepository.getFeesRegister();

    }
}
