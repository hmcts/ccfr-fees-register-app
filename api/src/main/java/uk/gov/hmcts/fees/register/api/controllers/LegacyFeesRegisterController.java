package uk.gov.hmcts.fees.register.api.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.fees.register.api.repositories.FeesRegisterRepository;
import uk.gov.hmcts.fees.register.legacymodel.Category;
import uk.gov.hmcts.fees.register.legacymodel.EntityNotFoundException;
import uk.gov.hmcts.fees.register.legacymodel.Fee;
import uk.gov.hmcts.fees.register.legacymodel.FeesRegister;

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

    @ApiOperation(value = "Find appropriate fees amount for given claim.",
        notes = "This endpoint returns appropriate fee for given category(e.g. onlinefees or hearingfees). All input and output amounts are in pence.  ", response = Fee.class)
    @GetMapping("/categories/{id}/ranges/{amount}/fees")
    public ChargeableFeeWrapperDto getCategoryRange(
        @ApiParam(value = "This is fee category. potential values can be onlinefees or hearingfees", required = true) @PathVariable(value = "id") String id,
        @ApiParam(value = "This is claim amount in pence", required = true) @PathVariable(value = "amount") int amount) {

        Fee fee = getCategory(id)
            .findRange(amount)
            .orElseThrow(() -> new EntityNotFoundException("Range not found, amount: " + amount))
            .getFee();

        return new ChargeableFeeWrapperDto(fee, fee.calculate(amount));
    }


    @ApiOperation(value = "Find appropriate flat fees for given fee id.",
        notes = "This endpoint returns appropriate fee for given category(e.g. onlinefees or hearingfees) and flat fee id. ", response = Fee.class)
    @GetMapping("/categories/{id}/flat/{feeId}")
    public Fee getFlatFeeInACategory(
        @ApiParam(value = "This is fee category. potential values can be onlinefees or hearingfees", required = true) @PathVariable(value = "id") String id,
        @ApiParam(value = "This is flat fee in a category", required = true) @PathVariable(value = "feeId") String feeId) {

        return getCategory(id)
            .findFlatFee(feeId)
            .orElseThrow(() -> new EntityNotFoundException("Flat fees not found, feeId: " + feeId));
    }

    @ApiOperation(value = "Find all flat fees for given category.",
        notes = "This endpoint returns all flat fees for given category(e.g. onlinefees or hearingfees). ", response = Fee.class)
    @GetMapping("/categories/{id}/flat")
    public List<Fee> getAllFlatFeesInACategory(
        @ApiParam(value = "This is fee category. potential values can be onlinefees or hearingfees", required = true) @PathVariable(value = "id") String id) {

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
