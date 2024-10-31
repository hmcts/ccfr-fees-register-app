package uk.gov.hmcts.fees2.register.data.service.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees2.register.data.dto.SearchFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.SearchFeeVersionDto;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.repository.ApplicantTypeRepository;
import uk.gov.hmcts.fees2.register.data.repository.ChannelTypeRepository;
import uk.gov.hmcts.fees2.register.data.repository.EventTypeRepository;
import uk.gov.hmcts.fees2.register.data.repository.Fee2Repository;
import uk.gov.hmcts.fees2.register.data.repository.Jurisdiction1Repository;
import uk.gov.hmcts.fees2.register.data.repository.Jurisdiction2Repository;
import uk.gov.hmcts.fees2.register.data.repository.ServiceTypeRepository;
import uk.gov.hmcts.fees2.register.data.service.FeeSearchService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeeSearchServiceImpl implements FeeSearchService {
    private static final Predicate[] REF = new Predicate[0];

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private ChannelTypeRepository channelTypeRepository;

    @Autowired
    private Jurisdiction1Repository jurisdiction1Repository;

    @Autowired
    private Jurisdiction2Repository jurisdiction2Repository;

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private ApplicantTypeRepository applicantTypeRepository;

    @Autowired
    private Fee2Repository fee2Repository;

    @Override
    public List<FeeVersion> search(SearchFeeDto feeCriteria, SearchFeeVersionDto versionCriteria) {
        List<Fee> fees = searchFees(feeCriteria);

        if (isExternalFeeSearch(versionCriteria)) {
            return externalFeeSearchFilter(fees, versionCriteria);
        } else {
            return searchForVersionsInFees(fees, versionCriteria);
        }
    }

    @Override
    public List<Fee> search(SearchFeeDto feeCriteria) {
        return searchFees(feeCriteria);
    }

    private List<Fee> searchFees(SearchFeeDto criteria) {
        return fee2Repository.findAll((rootFee, criteriaQuery, criteriaBuilder) -> getFeePredicate(rootFee, criteriaBuilder, criteria))
            .stream()
            .filter(fee -> criteria.getIsDraft() == null
                ||
                fee.isDraft() == criteria.getIsDraft()
            )
            .filter(fee -> criteria.getAmountOrVolume() == null
                ||
                fee.isInRange(criteria.getAmountOrVolume())
            )
            .collect(Collectors.toList());
    }

    private Predicate getFeePredicate(Root<Fee> fee, CriteriaBuilder builder, SearchFeeDto feeCriteria) {
        List<Predicate> predicates = new ArrayList<>();

        if (feeCriteria.getService() != null) {
            predicates.add(
                builder.equal(
                    fee.get(fee.getModel().getSingularAttribute("service")),
                    serviceTypeRepository.findByNameOrThrow(feeCriteria.getService())
                )
            );
        }

        if (feeCriteria.getJurisdiction1() != null) {
            predicates.add(
                builder.equal(
                    fee.get(fee.getModel().getSingularAttribute("jurisdiction1")),
                    jurisdiction1Repository.findByNameOrThrow(feeCriteria.getJurisdiction1())
                )
            );
        }

        if (feeCriteria.getJurisdiction2() != null) {
            predicates.add(
                builder.equal(
                    fee.get(fee.getModel().getSingularAttribute("jurisdiction2")),
                    jurisdiction2Repository.findByNameOrThrow(feeCriteria.getJurisdiction2())
                )
            );
        }

        if (feeCriteria.getChannel() != null) {
            predicates.add(
                builder.equal(
                    fee.get(fee.getModel().getSingularAttribute("channelType")),
                    channelTypeRepository.findByNameOrThrow(feeCriteria.getChannel())
                )
            );
        }

        if (feeCriteria.getEvent() != null) {
            predicates.add(
                builder.equal(
                    fee.get(fee.getModel().getSingularAttribute("eventType")),
                    eventTypeRepository.findByNameOrThrow(feeCriteria.getEvent())
                )
            );
        }

        if (feeCriteria.getApplicantType() != null) {
            predicates.add(
                builder.equal(
                    fee.get(fee.getModel().getSingularAttribute("applicantType")),
                    applicantTypeRepository.findByNameOrThrow(feeCriteria.getApplicantType())
                )
            );
        }

        if (feeCriteria.getUnspecifiedClaimAmount() != null) {
            predicates.add(
                builder.equal(
                    fee.get(fee.getModel().getSingularAttribute("unspecifiedClaimAmount")),
                    feeCriteria.getUnspecifiedClaimAmount()
                )
            );
        }

        return builder.and(predicates.toArray(REF));
    }

    private List<FeeVersion> searchForVersionsInFees(List<Fee> fees, SearchFeeVersionDto versionCriteria) {
        return fees
            .stream()
            .flatMap(f -> f.getFeeVersions().stream())
            .filter(v -> versionCriteria.getVersionStatus() == null || v.getStatus().equals(versionCriteria.getVersionStatus()))
            .filter(v -> versionCriteria.getApprovedBy() == null || versionCriteria.getApprovedBy().equals(v.getApprovedBy()))
            .filter(v -> versionCriteria.getAuthor() == null || versionCriteria.getAuthor().equals(v.getAuthor()))
            .filter(v -> versionCriteria.getIsActive() == null ||
                versionCriteria.getIsActive() && v.equals(v.getFee().getCurrentVersion(true))
                ||
                !versionCriteria.getIsActive() && !v.equals(v.getFee().getCurrentVersion(true))
            )
            .filter(v -> versionCriteria.getIsExpired() == null || v.getValidTo() == null ||
                versionCriteria.getIsExpired() && v.getValidTo().before(new Date())
                ||
                !versionCriteria.getIsExpired() && v.getValidTo().after(new Date())
            )
            .filter(v -> versionCriteria.getDiscontinued() == null || (v.getValidTo() != null && v.getValidTo().before(new Date())))
            .collect(Collectors.toList());
    }

    private List<FeeVersion> externalFeeSearchFilter(List<Fee> fees, SearchFeeVersionDto sfvDto) {

        return fees.stream().flatMap(f -> f.getFeeVersions().stream()).filter(fee -> {
            if (!fee.equals(fee.getFee().getCurrentVersion(true))) {
                return false;
            } else if (sfvDto.getDescription() != null && StringUtils.containsIgnoreCase(fee.getDescription(), sfvDto.getDescription())
                || sfvDto.getSiRefId() != null && StringUtils.containsIgnoreCase(fee.getSiRefId(), sfvDto.getSiRefId())
                || sfvDto.getFeeVersionAmount() != null && fee.calculateFee(sfvDto.getFeeVersionAmount()).floatValue() == sfvDto
                .getFeeVersionAmount().floatValue()) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
    }

    private boolean isExternalFeeSearch(SearchFeeVersionDto versionCriteria) {
        return versionCriteria.getDescription() != null || versionCriteria.getSiRefId() != null
            || versionCriteria.getFeeVersionAmount() != null;
    }
}
