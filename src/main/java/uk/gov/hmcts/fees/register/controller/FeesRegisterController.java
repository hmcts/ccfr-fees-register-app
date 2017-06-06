package uk.gov.hmcts.fees.register.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.fees.register.model.Category;
import uk.gov.hmcts.fees.register.model.Fee;
import uk.gov.hmcts.fees.register.model.FeesRegister;
import uk.gov.hmcts.fees.register.repository.FeesRegisterRepository;
import uk.gov.hmcts.fees.register.service.EntityNotFoundException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController()
@RequestMapping("/fees-register")
public class FeesRegisterController {

    private FeesRegisterRepository feesRegisterRepository;

    @Autowired
    public FeesRegisterController(FeesRegisterRepository feesRegisterRepository) {
        this.feesRegisterRepository = feesRegisterRepository;
    }

    @GetMapping("/cmc")
    public FeesRegister getAllFees() {
        return getFeesRegister();
    }

    @GetMapping("/cmc/flat")
    public List<Fee> getFlatFees() {
        return getFeesRegister().getFlatFees();
    }

    @GetMapping("/cmc/flat/{id}")
    public Fee getFlatFee(@PathVariable(value = "id") String id) {
        return getFeesRegister()
                .getFeeDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Flat fee not found, id: " + id));
    }

    @GetMapping("/cmc/categories")
    public List<Category> getCategories() {
        return getFeesRegister().getCategories();
    }

    @GetMapping("/cmc/categories/{id}")
    @ResponseBody
    public Category getCategory(@PathVariable(value = "id") String categoryId) {

        return getFeesRegister()
                .getClaimCategory(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found, id: " + categoryId));
    }

    @GetMapping("/cmc/categories/{id}/ranges/{amount}/fees")
    public Fee getCategoryRange(@PathVariable(value = "id") String id,
                                @PathVariable(value = "amount") int amount) {

        return getCategory(id)
                .findRange(amount)
                .orElseThrow(() -> new EntityNotFoundException("Range not found, amount: " + amount))
                .getFee();
    }

    private FeesRegister getFeesRegister() {
        return feesRegisterRepository.getFeesRegister();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<HttpStatus> handleEntityNotFoundException() {
        return new ResponseEntity<>(NOT_FOUND);
    }
}
