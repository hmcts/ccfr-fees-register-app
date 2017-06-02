package uk.gov.hmcts.register.fees.controller;

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
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.register.fees.model.Category;
import uk.gov.hmcts.register.fees.model.Fee;
import uk.gov.hmcts.register.fees.model.FeesRegister;
import uk.gov.hmcts.register.fees.repository.FeesRegisterRepository;
import uk.gov.hmcts.register.fees.service.EntityNotFoundException;
import uk.gov.hmcts.register.fees.service.FeesRegisterService;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController()
@RequestMapping("/fees-register")
public class FeesRegisterController {

    private static final Logger LOG = LoggerFactory.getLogger(FeesRegisterController.class);

    @Autowired
    private FeesRegisterService feeService;

    @Autowired
    private FeesRegisterRepository feesRegisterRepository;


    @GetMapping("/cmc")
    public ResponseEntity<FeesRegister> getFeesRegister() {
        return ResponseEntity.ok(feeService.getFeesRegister());
    }

    // ranges
    @GetMapping("/cmc/claimCategories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(feeService.getAllCategories());
    }

    @GetMapping("/cmc/fees/{id}")
    public ResponseEntity<Fee> getFeeDetails(@PathVariable(value = "id") String id,
                                             @RequestParam(value = "claimAmount", required = false) Integer claimAmount) {
        Fee fee = null;
        if (claimAmount != null) {
            fee = feeService.getFeeDetails(id, claimAmount);
        } else {
            fee = feeService.getFeeDetails(id);
        }

        return ResponseEntity.ok(fee);

    }

    @GetMapping("/cmc/{categoryId}/fees")
    public ResponseEntity<Fee> getFeeDetailsForClaimAmountAndCategory(@PathVariable(value = "categoryId") String categoryId,
                                                                      @RequestParam(value = "amount") int amount) {
        FeesRegister feesRegister = feesRegisterRepository.getFeesRegister();

        Fee fee = feesRegister.getClaimCategory(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found, id: " + categoryId))
                .findRange(amount)
                .orElseThrow(() -> new EntityNotFoundException("Range not found, amount: " + amount))
                .getFee();

        return ResponseEntity.ok(fee);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<HttpStatus> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(NOT_FOUND);
    }

}
