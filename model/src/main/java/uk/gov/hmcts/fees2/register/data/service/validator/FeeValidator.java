package uk.gov.hmcts.fees2.register.data.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees2.register.data.model.*;
import uk.gov.hmcts.fees2.register.data.repository.ApplicantTypeRepository;
import uk.gov.hmcts.fees2.register.data.repository.ChannelTypeRepository;
import uk.gov.hmcts.fees2.register.data.service.validator.validators.IFeeVersionValidator;

import java.util.List;

@Component
public class FeeValidator {

    protected ApplicationContext context;

    private ChannelTypeRepository channelTypeRepository;

    private ApplicantTypeRepository applicantTypeRepository;

    private List<IFeeVersionValidator> versionValidators;

    @Autowired
    public FeeValidator(ApplicationContext context, ChannelTypeRepository channelTypeRepository, List<IFeeVersionValidator> versionValidators) {
        this.context = context;
        this.channelTypeRepository = channelTypeRepository;
        this.versionValidators = versionValidators;
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
            fee.setChannelType(channelTypeRepository.findOne(ChannelType.DEFAULT));
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

}
