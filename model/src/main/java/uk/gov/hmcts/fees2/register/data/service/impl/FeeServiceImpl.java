package uk.gov.hmcts.fees2.register.data.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeVersionNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.ChannelType;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.repository.Fee2Repository;
import uk.gov.hmcts.fees2.register.data.repository.FeeVersionRepository;
import uk.gov.hmcts.fees2.register.data.service.FeeService;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import java.util.List;

@Service
public class FeeServiceImpl implements FeeService {

    @Autowired
    private Fee2Repository fee2Repository;

    @Autowired
    private FeeVersionRepository feeVersionRepository;

    public Fee save(Fee fee) {
        return fee2Repository.save(fee);
    }

    @Override
    public Fee get(String code) {
        return fee2Repository.findByCodeOrThrow(code);
    }

    @Override
    @Transactional
    public boolean approve(String code, Integer version) {

        Fee fee = fee2Repository.findByCodeOrThrow(code);

        FeeVersion ver = feeVersionRepository.findByFeeAndVersion(fee, version);

        if(ver == null) {
            throw new FeeVersionNotFoundException(code);
        }

        ver.setStatus(FeeVersionStatus.approved);

        return true;
    }

    @Override
    /** Magic method that "googles" fees */
    public List<Fee> lookup(LookupFeeDto dto) {

        return fee2Repository.findAll(
            (rootFee, criteriaQuery, criteriaBuilder) ->
                buildFirstLevelPredicate(rootFee)
        );

    }

    private void defaults(LookupFeeDto dto) {
        if(dto.getChannel() == null){
            dto.setChannel(ChannelType.DEFAULT);
        }
    }

    private Predicate buildFirstLevelPredicate(Root<Fee> feeRoot) {

        EntityType<Fee> Fee_ = feeRoot.getModel();

       // Predicate p = feeRoot.
        return null;
    }

}
