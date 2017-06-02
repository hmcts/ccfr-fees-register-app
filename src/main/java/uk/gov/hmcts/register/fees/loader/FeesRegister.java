package uk.gov.hmcts.register.fees.loader;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class FeesRegister {

	private String serviceName;
	private List<Category> claimCategories;
	private List<Fee> flatFeeList;

	public Category getClaimCategory(String claimCategoryId) {

		Category category = this.claimCategories.stream()
				.filter(x -> claimCategoryId.equals(x.getClaimCategoryId())).findFirst().orElse(null);
		
		return category;

	}
	

	public Fee getFeeDetails(String eventId) {
		return flatFeeList.stream().filter(x -> eventId.equals(x.getId())).findAny().orElse(null);
	}

	public Fee getFeeDetailsForClaimAmountAndCategory(BigDecimal amount, String claimCategoryId) {

		Category category = getClaimCategory(claimCategoryId);
		Range range = category.getClaimRange(amount);
		return range.getFee();

	}


}
