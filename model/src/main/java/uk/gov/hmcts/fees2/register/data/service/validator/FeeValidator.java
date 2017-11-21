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

        if(fee instanceof RangedFee) {
            validateRangedFee((RangedFee) fee);
        }

        if(fee.isUnspecifiedClaimAmount()) {
            fee.getFeeVersions().forEach( v ->this.validateVersion(fee, v));
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

    private void validateVersion(Fee fee, FeeVersion v){
        if(fee.isUnspecifiedClaimAmount() && !v.getAmount().acceptsUnspecifiedFees()) {
            throw new BadRequestException(
                "Amount type " + v.getAmount().getClass().getSimpleName()
                    + " is not allowed with unspecified amount fees");
        }

        if(v.getValidFrom() != null && v.getValidTo() != null && v.getValidFrom().compareTo(v.getValidTo()) >= 0) {
            throw new BadRequestException("Fee version valid from must be lower than valid to");
        }
    }

    private void validateRangedFee(RangedFee fee){

        if(fee.isUnspecifiedClaimAmount()) {
            throw new BadRequestException("Ranged fees can not have unspecified claim amounts");
        }

        if(fee.getMinRange() != null && fee.getMaxRange() != null && fee.getMinRange().compareTo(fee.getMaxRange()) >= 0) {
            throw new BadRequestException("Ranged fee min range can not be greater or equal than the max range");
        }

    }

}
