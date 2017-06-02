package uk.gov.hmcts.register.fees.loader;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ClaimRange {
	
	private BigDecimal startAmount;
	private BigDecimal uptoAmount;
	private Fee  fee;
	

	
}
