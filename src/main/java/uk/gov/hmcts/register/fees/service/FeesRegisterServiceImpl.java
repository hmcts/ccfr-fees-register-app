package uk.gov.hmcts.register.fees.service;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.register.fees.model.Category;
import uk.gov.hmcts.register.fees.model.Fee;
import uk.gov.hmcts.register.fees.model.FeesRegister;
import uk.gov.hmcts.register.fees.repository.FeesRegisterRepository;

@Service
public class FeesRegisterServiceImpl implements FeesRegisterService {


    private final FeesRegisterRepository feesRegisterRepository;

    @Autowired
    public FeesRegisterServiceImpl(FeesRegisterRepository feesRegisterRepository) {
        this.feesRegisterRepository = feesRegisterRepository;
    }

    public FeesRegister getFeesRegister() {
        return feesRegisterRepository.getFeesRegister();

    }

    // Ranges
    public List<Category> getAllCategories() {
        List<Category> categories = feesRegisterRepository.getAllCategories();

        if (null == categories) {
            throw new ClaimCategoriesNotFoundException("Claim categories not found for the service : " + getFeesRegister().getServiceName());
        }

        return categories;

    }

    public Fee getFeeDetails(String eventId) {

        Fee fee = feesRegisterRepository.getFeeDetails(eventId);

        if (null == fee) {
            throw new FeesNotFoundException("Fees not found for the eventid : " + eventId);
        }
        return fee;
    }

    public Fee getFeeDetails(String eventId, int claimAmount) {
        // Get fee details from fee register
        Fee fee = feesRegisterRepository.getFeeDetails(eventId);
        if (null == fee) {
            throw new FeesNotFoundException("Fees not found for the eventid : " + eventId + " and claim amount : " + claimAmount);
        }

        // calculate percentage fees
//        if (fee.getFeePercentage() != 0 && !claimAmount.equals(BigDecimal.ZERO)) {
//            fee.calculateFeeAmount(claimAmount);
//        }
        return fee;

    }

    public Fee getFeeDetailsForClaimAmountAndCategory(int claimAmount, String categoryId) {
        // Get fee details from fee register
        Fee fee = feesRegisterRepository.getFeeDetailsForClaimAmountAndCategory(claimAmount, categoryId);

        if (null == fee) {
            throw new FeesNotFoundException("Fees not found for the categoryId : " + categoryId + " and claim amount : " + claimAmount);
        }
        // calculate percentage fees

        return fee;
    }

}
