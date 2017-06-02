package uk.gov.hmcts.register.fees.service;

import java.math.BigDecimal;
import java.util.List;

import uk.gov.hmcts.register.fees.loader.ClaimCategory;
import uk.gov.hmcts.register.fees.loader.Fee;
import uk.gov.hmcts.register.fees.loader.FeesRegister;

public interface FeesRegisterService {
	
	 FeesRegister getFeesRegister() ;
	 List<ClaimCategory> getAllCategories();
	 Fee getFeeDetails(String eventId);
	 Fee getFeeDetails(String eventId, BigDecimal claimAmount);
	 Fee getFeeDetailsForClaimAmountAndCategory(BigDecimal claimAmount, String categoryId);

}
