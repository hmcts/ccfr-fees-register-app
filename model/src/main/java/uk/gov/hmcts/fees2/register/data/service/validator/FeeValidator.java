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

    protected ApplicationContext context;

    private List<IFeeVersionValidator> versionValidators;

    private Fee2Repository feeRepository;

    @Autowired
    public FeeValidator(ApplicationContext context, List<IFeeVersionValidator> versionValidators, Fee2Repository feeRepository) {
        this.context = context;
        this.versionValidators = versionValidators;
        this.feeRepository = feeRepository;
    }

    public void validateAndDefaultNewFee(Fee fee) {

        fee.getValidators().forEach(
            validatorClass ->
                context.getBean(validatorClass).validateFee(fee)
        );


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
            fee.setChannelType(new ChannelType(ChannelType.DEFAULT, null, null));
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

    public boolean isExistingFee(Fee newFee) {
        return feeRepository.
            findByChannelTypeAndEventTypeAndJurisdiction1AndJurisdiction2AndService(
                newFee.getChannelType(),
                newFee.getEventType(),
                newFee.getJurisdiction1(),
                newFee.getJurisdiction2(),
                newFee.getService()
            )
            .stream()
            .anyMatch(f -> f.isADuplicateOf(newFee));
    }


}
