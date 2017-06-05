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
import uk.gov.hmcts.register.fees.service.FeesDto;
import uk.gov.hmcts.register.fees.service.FeesDtoFactory;
import uk.gov.hmcts.register.fees.service.FeesRegisterService;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController()
@RequestMapping("/fees-register")
public class FeesRegisterController {

    private static final Logger LOG = LoggerFactory.getLogger(FeesRegisterController.class);


    private FeesRegisterService feesService;

    private FeesRegisterRepository feesRegisterRepository;

    private final FeesDtoFactory feesDtoFactory;


    @Autowired
    public FeesRegisterController(FeesRegisterService feesService, FeesDtoFactory feesDtoFactory,FeesRegisterRepository feesRegisterRepository ) {
        this.feesService = feesService;
        this.feesDtoFactory = feesDtoFactory;
        this.feesRegisterRepository= feesRegisterRepository;

    }
    @GetMapping("/cmc")
    public ResponseEntity<FeesRegister> getFeesRegister() {
        return ResponseEntity.ok(feesService.getFeesRegister());
    }

    // ranges
    @GetMapping("/cmc/claimCategories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(feesService.getAllCategories());
    }

    @GetMapping("/cmc/fees/{id}")
    public ResponseEntity<FeesDto> getFeeDetails(@PathVariable(value = "id") String id,
                                                 @RequestParam(value = "claimAmount", required = false) Integer claimAmount) {
        Fee fee = null;
        if (claimAmount != null) {
            fee = feesService.getFeeDetails(id, claimAmount);
        } else {
            fee = feesService.getFeeDetails(id);
        }

        return ResponseEntity.ok(feesDtoFactory.toFeeDto(fee,claimAmount));

    }

    @GetMapping("/cmc/{categoryId}/fees")
    public ResponseEntity<FeesDto> getFeeDetailsForClaimAmountAndCategory(@PathVariable(value = "categoryId") String categoryId,
                                                                      @RequestParam(value = "claimAmount") int claimAmount) {
        FeesRegister feesRegister = feesRegisterRepository.getFeesRegister();

        Fee fee = feesRegister.getClaimCategory(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found, id: " + categoryId))
                .findRange(claimAmount)
                .orElseThrow(() -> new EntityNotFoundException("Range not found, claim amount: " + claimAmount))
                .getFee();

        return ResponseEntity.ok(feesDtoFactory.toFeeDto(fee,claimAmount));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<HttpStatus> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(NOT_FOUND);
    }

}
