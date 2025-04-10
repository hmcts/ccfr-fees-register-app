package uk.gov.hmcts.fees2.register.data.service.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.exceptions.ConflictException;
import uk.gov.hmcts.fees2.register.data.exceptions.FeeNotFoundException;
import uk.gov.hmcts.fees2.register.data.exceptions.TooManyResultsException;
import uk.gov.hmcts.fees2.register.data.model.ChannelType;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeCodeHistory;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.repository.ApplicantTypeRepository;
import uk.gov.hmcts.fees2.register.data.repository.ChannelTypeRepository;
import uk.gov.hmcts.fees2.register.data.repository.EventTypeRepository;
import uk.gov.hmcts.fees2.register.data.repository.Fee2Repository;
import uk.gov.hmcts.fees2.register.data.repository.FeeCodeHistoryRepository;
import uk.gov.hmcts.fees2.register.data.repository.FeeVersionRepository;
import uk.gov.hmcts.fees2.register.data.repository.Jurisdiction1Repository;
import uk.gov.hmcts.fees2.register.data.repository.Jurisdiction2Repository;
import uk.gov.hmcts.fees2.register.data.repository.ServiceTypeRepository;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.data.service.validator.FeeValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FeeServiceImpl implements FeeService {

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
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private ApplicantTypeRepository applicantTypeRepository;

    @Autowired
    private Fee2Repository fee2Repository;

    @Autowired
    private FeeCodeHistoryRepository feeCodeHistoryRepository;

    @Autowired
    private FeeValidator feeValidator;

    private final Pattern pattern = Pattern.compile("^(.*)[^\\d](\\d+)(.*?)$");

    @Override
    public Fee save(Fee fee) {
        feeValidator.validateAndDefaultNewFee(fee);

        if (feeValidator.isExistingFee(fee)) {
            throw new ConflictException("Fee with the given reference data/overlapping range already exists");
        }

        if (null == fee.getCode() && null == fee.getFeeNumber()) {
            Integer nextFeeNumber = fee2Repository.getMaxFeeNumber() + 1;
            fee.setFeeNumber(nextFeeNumber);
            fee.setCode("FEE" + StringUtils.leftPad(nextFeeNumber.toString(), 4, "0"));
        }

        return fee2Repository.save(fee);
    }

    @Override
    public void saveLoaderFee(Fee fee) {

        if (fee.getCode() != null && !fee2Repository.findByCode(fee.getCode()).isPresent()) {
            feeValidator.validateAndDefaultNewFee(fee);

            Matcher matcher = pattern.matcher(fee.getCode());
            fee.setFeeNumber(matcher.find() ? Integer.parseInt(matcher.group(2)) : fee2Repository.getMaxFeeNumber() + 1);
            fee2Repository.save(fee);
        }
    }

    @Override
    @Transactional
    public void updateLoaderFee(Fee updateFee, String newCode) {
        Fee fee = getFee(updateFee.getCode());

        if (newCode != null) {  // If the new feeCode is provided in the request.
            FeeCodeHistory feeCodeHistory = FeeCodeHistory.FeeCodeHistoryWith()
                .fee(fee)
                .old_code(fee.getCode())
                .new_code(newCode)
                .build();
            fee.setFeeCodeHistories(Arrays.asList(feeCodeHistory));
            feeCodeHistoryRepository.save(feeCodeHistory);

            fee.setCode(newCode);

            Matcher matcher = pattern.matcher(newCode);
            fee.setFeeNumber(matcher.find() ? Integer.parseInt(matcher.group(2)) : fee2Repository.getMaxFeeNumber() + 1);
        } else { // If the new feeCode is not present in the request, then auto generate the code.
            Integer nextFeeNumber = fee2Repository.getMaxFeeNumber() + 1;
            fee.setFeeNumber(nextFeeNumber);
            fee.setCode("FEE" + StringUtils.leftPad(nextFeeNumber.toString(), 4, "0"));
        }
    }

    /***
     *
     * @param fees list
     */
    @Transactional
    public void save(List<Fee> fees) {

        fees.forEach(fee -> feeValidator.validateAndDefaultNewFee(fee));

        fee2Repository.saveAll(fees);
    }

    @Transactional
    public void delete(String code) {
        fee2Repository.deleteFeeByCode(code);
    }

    @Transactional
    public boolean safeDelete(String code) {
        Optional<Fee> optFeeToDelete = fee2Repository.findByCode(code);
        if (optFeeToDelete.isPresent() &&
            feeVersionRepository.findByFee_CodeAndStatus(code, FeeVersionStatus.approved).isEmpty()) {
            delete(code);
            return true;

        }
        return false;
    }

    // rename method name
    @Override
    public Fee getFee(String code) {
        return fee2Repository.findByCodeOrThrow(code);
    }

    public FeeLookupResponseDto lookup(LookupFeeDto dto) {

        defaults(dto);

        dto.setVersionStatus(FeeVersionStatus.approved);

        List<Fee> fees = search(dto).stream().filter(fee -> fee.getCurrentVersion(true) != null)
            .collect(Collectors.toList());

        if (fees.isEmpty()) {
            throw new FeeNotFoundException(dto);
        }

        if (fees.size() > 1) {
            throw new TooManyResultsException();
        }

        Fee fee = fees.get(0);

        FeeVersion version = fee.getCurrentVersion(true);

        return new FeeLookupResponseDto(
            fee.getCode(),
            version.getDescription(),
            version.getVersion(),
            version.calculateFee(dto.getAmountOrVolume()));

    }

    @Override
    /* Magic method that "googles" fees */
    public List<Fee> search(LookupFeeDto dto) {

        return fee2Repository
            .findAll(
                (rootFee, criteriaQuery, criteriaBuilder) -> buildFirstLevelPredicate(rootFee, criteriaBuilder, dto)
            )
            .stream()
            .filter(fee -> secondLevelFilter(fee, dto))
            .peek(fee -> {
                if (fee.getFeeVersions() != null) {
                    fee.getFeeVersions().sort(
                        Comparator.comparing(FeeVersion::getVersion).reversed()
                    );
                }
            })
            .collect(Collectors.toList());
    }

    private void defaults(LookupFeeDto dto) {
        if (dto.getChannel() == null) {
            dto.setChannel(ChannelType.DEFAULT);
        }
    }

    private boolean secondLevelFilter(Fee fee, LookupFeeDto dto) {
        return
            (dto.getAmountOrVolume() == null
                || fee.isInRange(dto.getAmountOrVolume()
            ) &&
                (dto.getAuthor() == null
                    || dto.getAuthor().equals(
                    fee.getCurrentVersion(dto.getVersionStatus() == FeeVersionStatus.approved).getAuthor())));
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

        if (dto.getEvent() != null) {
            predicates.add(
                builder.equal(
                    fee.get(fee.getModel().getSingularAttribute("eventType")),
                    eventTypeRepository.findByNameOrThrow(dto.getEvent())
                )
            );
        }

        if (dto.getApplicantType() != null) {
            predicates.add(
                builder.equal(
                    fee.get(fee.getModel().getSingularAttribute("applicantType")),
                    applicantTypeRepository.findByNameOrThrow(dto.getApplicantType())
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

        predicates.add(getKeywordPredicate(fee, builder, dto));

        return builder.and(predicates.toArray(REF));

    }

    private Predicate getKeywordPredicate(Root<Fee> fee, CriteriaBuilder builder, LookupFeeDto dto) {
        if (dto.getKeyword() != null) {
            return builder.equal(fee.get(fee.getModel().getSingularAttribute("keyword")), dto.getKeyword());
        }
        return builder.isNull(fee.get(fee.getModel().getSingularAttribute("keyword")));
    }

    @Override
    /* Validation for pre-save fees */
    public void prevalidate(Fee fee) {

        if (feeValidator.isExistingFee(fee)) {
            throw new ConflictException("One or more fees exist already with the same metadata");
        }

    }

}
