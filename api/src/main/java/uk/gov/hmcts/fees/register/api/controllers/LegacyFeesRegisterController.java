package uk.gov.hmcts.fees.register.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.fees.register.api.repositories.FeesRegisterRepository;
import uk.gov.hmcts.fees.register.legacymodel.Category;
import uk.gov.hmcts.fees.register.legacymodel.EntityNotFoundException;
import uk.gov.hmcts.fees.register.legacymodel.Fee;
import uk.gov.hmcts.fees.register.legacymodel.FeesRegister;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController()
@RequestMapping("/fees-register")
public class LegacyFeesRegisterController {

    private FeesRegisterRepository feesRegisterRepository;

    @Autowired
    public LegacyFeesRegisterController(FeesRegisterRepository feesRegisterRepository) {
        this.feesRegisterRepository = feesRegisterRepository;
    }

    @GetMapping()
    public FeesRegister getAllFees() {
        return getFeesRegister();
    }


    @GetMapping("/categories")
    public List<Category> getCategories() {
        return getFeesRegister().getCategories();
    }

    @GetMapping("/categories/{id}")
    @ResponseBody
    public Category getCategory(@PathVariable(value = "id") String categoryId) {

        return getFeesRegister()
            .getClaimCategory(categoryId)
            .orElseThrow(() -> new EntityNotFoundException("Category not found, id: " + categoryId));
    }

    @Operation(summary = "Find appropriate fees amount for given claim. This endpoint returns appropriate fee for given category(e.g. onlinefees or hearingfees). All input and output amounts are in pence.  ")
    @GetMapping("/categories/{id}/ranges/{amount}/fees")
    public ChargeableFeeWrapperDto getCategoryRange(
        @Parameter(description = "This is fee category. potential values can be onlinefees or hearingfees", required = true) @PathVariable(value = "id") String id,
        @Parameter(description = "This is claim amount in pence", required = true) @PathVariable(value = "amount") int amount) {

        Fee fee = getCategory(id)
            .findRange(amount)
            .orElseThrow(() -> new EntityNotFoundException("Range not found, amount: " + amount))
            .getFee();

        return new ChargeableFeeWrapperDto(fee, fee.calculate(amount));
    }


    @Operation(summary = "Find appropriate flat fees for given fee id. This endpoint returns appropriate fee for given category(e.g. onlinefees or hearingfees) and flat fee id. ")
    @GetMapping("/categories/{id}/flat/{feeId}")
    public Fee getFlatFeeInACategory(
        @Parameter(description = "This is fee category. potential values can be onlinefees or hearingfees", required = true) @PathVariable(value = "id") String id,
        @Parameter(description = "This is flat fee in a category", required = true) @PathVariable(value = "feeId") String feeId) {

        return getCategory(id)
            .findFlatFee(feeId)
            .orElseThrow(() -> new EntityNotFoundException("Flat fees not found, feeId: " + feeId));
    }

    @Operation(summary = "Find all flat fees for given category. This endpoint returns all flat fees for given category(e.g. onlinefees or hearingfees). ")
    @GetMapping("/categories/{id}/flat")
    public List<Fee> getAllFlatFeesInACategory(
        @Parameter(description = "This is fee category. potential values can be onlinefees or hearingfees", required = true) @PathVariable(value = "id") String id) {

        List<Fee> flatFees = getCategory(id).getFlatFees();

        if (flatFees.isEmpty()) {
            throw new EntityNotFoundException("Flat fees not found, category: " + id);
        }

        return flatFees;

    }

    private FeesRegister getFeesRegister() {
        return feesRegisterRepository.getFeesRegister();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity entityNotFoundException() {
        return new ResponseEntity<>(NOT_FOUND);
    }
}
