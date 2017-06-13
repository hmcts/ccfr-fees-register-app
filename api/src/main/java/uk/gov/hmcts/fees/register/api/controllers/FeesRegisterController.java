package uk.gov.hmcts.fees.register.api.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.fees.register.api.repositories.FeesRegisterRepository;
import uk.gov.hmcts.fees.register.model.Category;
import uk.gov.hmcts.fees.register.model.EntityNotFoundException;
import uk.gov.hmcts.fees.register.model.Fee;
import uk.gov.hmcts.fees.register.model.FeesRegister;

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

    @ApiOperation(value = "Find flat fee",
        notes = "Find flat fee objects e.g. fees for fast track and multi track cases.", response = Fee.class)

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

    @ApiOperation(value = "Find appropriate fees amount for given claim.",
        notes = "This endpoint returns appropriate fee for given category(e.g. onlinefees or hearingfees). All input and output amounts are in pence.  ", response = Fee.class)
    @GetMapping("/cmc/categories/{id}/ranges/{amount}/fees")
    public Fee getCategoryRange(
        @ApiParam(value = "This is fee category. potential values can be onlinefees or hearingfees", required = true) @PathVariable(value = "id") String id,
        @ApiParam(value = "This is claim amount in pence", required = true) @PathVariable(value = "amount") int amount) {

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
