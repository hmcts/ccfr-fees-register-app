package uk.gov.hmcts.fees2.register.data.service.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees2.register.data.model.*;
import uk.gov.hmcts.fees2.register.data.repository.ApplicantTypeRepository;
import uk.gov.hmcts.fees2.register.data.repository.ChannelTypeRepository;
import uk.gov.hmcts.fees2.register.data.repository.Fee2Repository;
import uk.gov.hmcts.fees2.register.data.service.validator.validators.IFeeVersionValidator;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Component
public class FeeValidator {

    private static final Logger LOG = LoggerFactory.getLogger(FeeValidator.class);

    protected ApplicationContext context;

    private ChannelTypeRepository channelTypeRepository;

    private ApplicantTypeRepository applicantTypeRepository;

    private List<IFeeVersionValidator> versionValidators;

    private Fee2Repository feeRepository;

    private static final Predicate[] REF = new Predicate[0];

    @Autowired
    public FeeValidator(ApplicationContext context, ChannelTypeRepository channelTypeRepository, List<IFeeVersionValidator> versionValidators, Fee2Repository feeRepository) {
        this.context = context;
        this.channelTypeRepository = channelTypeRepository;
        this.versionValidators = versionValidators;
        this.feeRepository = feeRepository;
    }

    public void validateAndDefaultNewFee(Fee fee) {

        /* Specific Fee Type Validator */
        if (fee.getValidators() != null) {
            fee.getValidators().forEach(
                validatorClass ->
                    context.getBean(validatorClass).validateFee(fee)
            );
        }

        /* Fee Version Validators */
        fee.getFeeVersions().forEach(v -> {
            for (IFeeVersionValidator validator : versionValidators) {
                validator.onCreate(fee, v);
            }
        });

        setDefaultValues(fee);

    }

    private void setDefaultValues(Fee fee) {

        if (fee.getChannelType() == null) {
            fee.setChannelType(channelTypeRepository.getOne(ChannelType.DEFAULT));
        }

        if (fee.getApplicantType() == null) {
            fee.setApplicantType(ApplicantType.applicantWith().name(ApplicantType.ALL).build());
        }

        /* If no status was specified, set it to draft */
        fee.getFeeVersions().stream().filter(v -> v.getStatus() == null).forEach(v -> v.setStatus(FeeVersionStatus.draft));

        /* If no version number was specified, and its only one, set it to 1 */
        if (fee.getFeeVersions().size() == 1) {
            FeeVersion v = fee.getFeeVersions().get(0);
            if (v.getVersion() == null) {
                v.setVersion(1);
            }
        }
    }

    public boolean isFeeExists(Fee fee) {
         List<Fee> fees = feeRepository.findAll(findFeeByReferenceDataAndKeyword(fee));

         if (fees.size() > 0) {
             LOG.info("No. of fees found for the matching reference data: {}", fees.size());


             switch (fee.getFeeType()) {
                 case "FixedFee":
                     return fees.stream().filter(f -> f.getFeeType().equals("FixedFee")).findAny().isPresent();
                 default:
                     return false;
             }
         }

         return false;
    }

    private static Specification findFeeByReferenceDataAndKeyword(Fee fee) {
        return ((root, query, cb) -> getPredicate(root, cb, fee));
    }

    private static Predicate getPredicate(Root<Fee> root, CriteriaBuilder cb, Fee fee) {
        List<Predicate> predicates = new ArrayList<>();

        if (fee.getChannelType() != null) {
            predicates.add(cb.equal(root.get("channelType"), fee.getChannelType()));
        }

        if (fee.getEventType() != null) {
            predicates.add(cb.equal(root.get("eventType"), fee.getEventType()));
        }

        if (fee.getJurisdiction1() != null) {
            predicates.add(cb.equal(root.get("jurisdiction1"), fee.getJurisdiction1()));
        }

        if (fee.getJurisdiction2() != null) {
            predicates.add(cb.equal(root.get("jurisdiction2"), fee.getJurisdiction2()));
        }

        if (fee.getService() != null) {
            predicates.add(cb.equal(root.get("service"), fee.getService()));
        }

        if (fee.getKeyword() != null) {
            predicates.add(cb.equal(root.get("keyword"), fee.getKeyword()));
        }

        return cb.and(predicates.toArray(REF));
    }

}
