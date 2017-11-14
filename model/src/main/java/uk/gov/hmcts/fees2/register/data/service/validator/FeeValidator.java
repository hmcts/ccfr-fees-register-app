package uk.gov.hmcts.fees2.register.data.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.model.*;
import uk.gov.hmcts.fees2.register.data.repository.ChannelTypeRepository;

@Component
public class FeeValidator {

    @Autowired
    private ChannelTypeRepository channelTypeRepository;

    public void validateAndDefaultNewFee(Fee fee) {

        /* - VALIDATIONS - */

        if(fee instanceof RangedFee && fee.isUnspecifiedClaimAmount()) {
            throw new BadRequestException("Ranged fees can not have unspecified claim amounts");
        }

        if(fee.isUnspecifiedClaimAmount()) {
            fee.getFeeVersions().forEach( v -> {
                if( !v.getAmount().acceptsUnspecifiedFees()) {
                    throw new BadRequestException(
                        "Amount type " + v.getAmount().getClass().getSimpleName()
                            + " is not allowed with unspecified amount fees");
                }
            });
        }

        /* - DEFAULTS - */

        if (fee.getChannelType() == null) {
            fee.setChannelType(channelTypeRepository.findOne(ChannelType.DEFAULT));
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
