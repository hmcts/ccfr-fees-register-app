package uk.gov.hmcts.fees2.register.data.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeNotFoundException;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeVersionNotFoundException;
import uk.gov.hmcts.fees2.register.data.exceptions.TooManyResultsException;
import uk.gov.hmcts.fees2.register.data.model.ChannelType;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.repository.*;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.data.service.validator.FeeValidator;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeeServiceImpl implements FeeService {
    
    private static final Logger LOG = LoggerFactory.getLogger(FeeServiceImpl.class);

    private static final Predicate[] REF = new Predicate[0];

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

    @Autowired
    private Fee2Repository fee2Repository;

    @Autowired
    private FeeValidator feeValidator;


    /* --- */

    public Fee save(Fee fee) {

        feeValidator.validateAndDefaultNewFee(fee);

        return fee2Repository.save(fee);

    }

    @Transactional
    public void delete(String code) {
        fee2Repository.deleteFeeByCode(code);
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

    public FeeLookupResponseDto lookup(LookupFeeDto dto) {

        defaults(dto);

        List<Fee> fees = search(dto);

        if (fees.isEmpty()) {
            throw new FeeNotFoundException(dto);
        }

        if (fees.size() > 1) {
            throw new TooManyResultsException();
        }

        Fee fee = fees.get(0);

        FeeVersion version = fee.getCurrentVersion(true);

        if (version == null) {
            throw new FeeNotFoundException(dto);
        }

        return new FeeLookupResponseDto(
            fee.getCode(),
            version.getDescription(),
            version.getVersion(),
            version.calculateFee(dto.getAmount()));

    }

    @Override
    /** Magic method that "googles" fees */
    public List<Fee> search(LookupFeeDto dto) {

        return fee2Repository
            .findAll(
                (rootFee, criteriaQuery, criteriaBuilder) -> buildFirstLevelPredicate(rootFee, criteriaBuilder, dto)
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

    private Predicate buildFirstLevelPredicate(Root<Fee> fee, CriteriaBuilder builder, LookupFeeDto dto) {

        List<Predicate> predicates = new ArrayList<>();

        if (dto.getChannel() != null) {
            predicates.add(
                builder.equal(
                    fee.get(fee.getModel().getSingularAttribute("channelType")),
                    channelTypeRepository.findByNameOrThrow(dto.getChannel())
                )
            );
        }

        if (dto.getJurisdiction1() != null) {
            predicates.add(
                builder.equal(
                    fee.get(fee.getModel().getSingularAttribute("jurisdiction1")),
                    jurisdiction1Repository.findByNameOrThrow(dto.getJurisdiction1())
                )
            );
        }

        if (dto.getJurisdiction2() != null) {
            predicates.add(
                builder.equal(
                    fee.get(fee.getModel().getSingularAttribute("jurisdiction2")),
                    jurisdiction2Repository.findByNameOrThrow(dto.getJurisdiction2())
                )
            );
        }

        if (dto.getService() != null) {
            predicates.add(
                builder.equal(
                    fee.get(fee.getModel().getSingularAttribute("service")),
                    serviceTypeRepository.findByNameOrThrow(dto.getService())
                )
            );
        }

        if (dto.getDirection() != null) {
            predicates.add(
                builder.equal(
                    fee.get(fee.getModel().getSingularAttribute("directionType")),
                    directionTypeRepository.findByNameOrThrow(dto.getDirection())
                )
            );
        }

        if (dto.getEvent() != null) {
            predicates.add(
                builder.equal(
                    fee.get(fee.getModel().getSingularAttribute("eventType")),
                    eventTypeRepository.findByNameOrThrow(dto.getEvent())
                )
            );
        }

        if (dto.getUnspecifiedClaimAmount() != null) {
            predicates.add(
                builder.equal(
                    fee.get(fee.getModel().getSingularAttribute("unspecifiedClaimAmount")), dto.getUnspecifiedClaimAmount()
                )
            );
        }

        return builder.and(predicates.toArray(REF));

    }

}
