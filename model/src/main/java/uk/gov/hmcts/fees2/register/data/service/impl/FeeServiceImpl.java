package uk.gov.hmcts.fees2.register.data.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeNotFoundException;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeVersionNotFoundException;
import uk.gov.hmcts.fees2.register.data.exceptions.TooManyResultsException;
import uk.gov.hmcts.fees2.register.data.model.ChannelType;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.repository.*;
import uk.gov.hmcts.fees2.register.data.service.FeeService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeeServiceImpl implements FeeService {

    @Autowired
    private Fee2Repository fee2Repository;

    public Fee save(Fee fee) {

        if (fee.getChannelType() == null) {
            fee.setChannelType(channelTypeRepository.findOne(ChannelType.DEFAULT));
        }

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

        if (ver == null) {
            throw new FeeVersionNotFoundException(code);
        }

        ver.setStatus(FeeVersionStatus.approved);

        return true;
    }

    public Fee lookup(LookupFeeDto dto) {

        List<Fee> fees = search(dto);

        if(fees.isEmpty()) {
            throw new FeeNotFoundException(dto);
        }

        if(fees.size() > 1) {
            throw new TooManyResultsException();
        }

        return fees.get(0);

    }

    @Override
    /** Magic method that "googles" fees */
    public List<Fee> search(LookupFeeDto dto) {

        defaults(dto);

        return fee2Repository
            .findAll(
                (rootFee, criteriaQuery, criteriaBuilder) -> buildFirstLevelPredicate(rootFee, criteriaQuery, criteriaBuilder, dto)
            )
            .stream()
            .filter(fee -> dto.getAmount() == null || fee.isInRange(dto.getAmount()))
            .collect(Collectors.toList());

    }

    private void defaults(LookupFeeDto dto) {
        if (dto.getChannel() == null) {
            dto.setChannel(ChannelType.DEFAULT);
        }
    }

    private Predicate buildFirstLevelPredicate(Root<Fee> fee, CriteriaQuery<?> cq, CriteriaBuilder builder, LookupFeeDto dto) {

        EntityType<Fee> Fee_ = fee.getModel();

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(
            builder.equal(
                fee.get(Fee_.getSingularAttribute("channelType")),
                channelTypeRepository.findByNameOrThrow(dto.getChannel())
            )
        );

        if (dto.getJurisdiction1() != null) {
            predicates.add(
                builder.equal(
                    fee.get(Fee_.getSingularAttribute("jurisdiction1")),
                    jurisdiction1Repository.findByNameOrThrow(dto.getJurisdiction1())
                )
            );
        }

        if (dto.getJurisdiction2() != null) {
            predicates.add(
                builder.equal(
                    fee.get(Fee_.getSingularAttribute("jurisdiction2")),
                    jurisdiction2Repository.findByNameOrThrow(dto.getJurisdiction2())
                )
            );
        }

        if (dto.getService() != null) {
            predicates.add(
                builder.equal(
                    fee.get(Fee_.getSingularAttribute("service")),
                    serviceTypeRepository.findByNameOrThrow(dto.getService())
                )
            );
        }

        if (dto.getDirection() != null) {
            predicates.add(
                builder.equal(
                    fee.get(Fee_.getSingularAttribute("direction")),
                    directionTypeRepository.findByNameOrThrow(dto.getDirection())
                )
            );
        }

        if (dto.getEvent() != null) {
            predicates.add(
                builder.equal(
                    fee.get(Fee_.getSingularAttribute("event")),
                    eventTypeRepository.findByNameOrThrow(dto.getEvent())
                )
            );
        }

        return builder.and(predicates.toArray(REF));

    }

    private final static Predicate[] REF = new Predicate[0];

    /* --- */

    @Autowired
    private FeeVersionRepository feeVersionRepository;

    @Autowired
    private ChannelTypeRepository channelTypeRepository;

    @Autowired
    private Jurisdiction1Repository jurisdiction1Repository;

    @Autowired
    private Jurisdiction2Repository jurisdiction2Repository;

    @Autowired
    private DirectionTypeRepository directionTypeRepository;

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;
}
