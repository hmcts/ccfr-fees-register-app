package uk.gov.hmcts.register.fees.loader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import lombok.Data;

@Data
@JsonInclude(NON_NULL)
public class Fee {
	
	private String eventId;
	private float feeAmount;
	private Date startDate;
	private Date endDate;
	private Currency currency = Currency.getInstance(Locale.UK);
	private String description; 
	private Float feePercentage;

	public void calculateFeeAmount(BigDecimal claimAmount) {

		BigDecimal feePercentage = new BigDecimal(this.feePercentage);
		BigDecimal calculatedFee = claimAmount.setScale(2, RoundingMode.HALF_EVEN).multiply(feePercentage)
				.divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);

		this.setFeeAmount(calculatedFee.floatValue());

	}
	
	
	

}
