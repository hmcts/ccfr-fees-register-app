package uk.gov.hmcts.register.fees.loader;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ClaimCategory {

	private String claimCategoryId;

	private List<ClaimRange> claimRanges;

	public ClaimRange getClaimRange(BigDecimal claimAmount) {

		ClaimRange requiredClaimRange =null;
		for(ClaimRange claimRange : claimRanges){
			
			if ((claimAmount.compareTo(claimRange.getUptoAmount()) == -1) || (claimAmount.compareTo(claimRange.getUptoAmount()) == 0)){
				requiredClaimRange = claimRange;
				break;
			}
		
							 
		 }
	     return requiredClaimRange;		
		

		
		//return claimRanges.stream().filter(y -> claimAmount.floatValue() <= y.getUptoAmount().floatValue()).findAny().orElse(null);
		
	

	}

}
