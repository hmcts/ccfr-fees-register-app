package uk.gov.hmcts.register.fees.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import uk.gov.hmcts.register.fees.loader.ClaimCategory;
import uk.gov.hmcts.register.fees.loader.Fee;
import uk.gov.hmcts.register.fees.loader.FeesRegister;
import uk.gov.hmcts.register.fees.service.FeesNotFoundException;
import uk.gov.hmcts.register.fees.service.FeesRegisterService;

@RestController()
@RequestMapping("/fees-register")
public class FeesRegisterController {

	private static final Logger LOG = LoggerFactory.getLogger(FeesRegisterController.class);

	@Autowired
	private FeesRegisterService feeService;

	@GetMapping("/cmc")
	public ResponseEntity<FeesRegister> getFeesRegister() {
		return ResponseEntity.ok(feeService.getFeesRegister());
	}

	// ranges
	@GetMapping("/cmc/claimCategories")
	public ResponseEntity<List<ClaimCategory>> getAllCategories() {
		return ResponseEntity.ok(feeService.getAllCategories());
	}

	@GetMapping("/cmc/fees/{eventId}")
	public ResponseEntity<Fee> getFeeDetails(@PathVariable(value = "eventId") String eventId,
			@RequestParam(value = "claimAmount", required = false) BigDecimal claimAmount) {
		Fee fee = null;
		if (claimAmount != null) {
			fee = feeService.getFeeDetails(eventId, claimAmount);
		}

		else {
			fee = feeService.getFeeDetails(eventId);
		}

		return ResponseEntity.ok(fee);

	}

	@GetMapping("/cmc/{claimCategoryId}/fees")
	public ResponseEntity<Fee> getFeeDetailsForClaimAmountAndCategory(
			@PathVariable(value = "claimCategoryId") String claimCategoryId,
			@RequestParam(value = "claimAmount", required = true) BigDecimal claimAmount) {
		return ResponseEntity.ok(feeService.getFeeDetailsForClaimAmountAndCategory(claimAmount, claimCategoryId));
	}

	

	@ExceptionHandler(FeesNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<HttpStatus> handleFeesNotFound(FeesNotFoundException ex) {
		return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
	}

}
