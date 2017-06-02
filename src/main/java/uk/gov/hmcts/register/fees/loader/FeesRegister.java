package uk.gov.hmcts.register.fees.loader;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import lombok.Data;

@Data
public class FeesRegister {

	private String serviceName;
	private List<ClaimCategory> claimCategories;
	private List<Fee> flatFeeList;

	public ClaimCategory getClaimCategory(String claimCategoryId) {

		ClaimCategory claimCategory = this.claimCategories.stream()
				.filter(x -> claimCategoryId.equals(x.getClaimCategoryId())).findFirst().orElse(null);
		
		return claimCategory;

	}
	

	public Fee getFeeDetails(String eventId) {
		return flatFeeList.stream().filter(x -> eventId.equals(x.getId())).findAny().orElse(null);
	}

	public Fee getFeeDetailsForClaimAmountAndCategory(BigDecimal amount, String claimCategoryId) {

		ClaimCategory claimCategory = getClaimCategory(claimCategoryId);
		ClaimRange claimRange = claimCategory.getClaimRange(amount);
		return claimRange.getFee();

	}


}
